package com.strongblackcoffee.mandelbrot;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.lang.invoke.MethodHandles;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Mandelbrot set contains those complex numbers <tt>c</tt> for which the
 * function <tt>z[0] = 0, z[i+1] = z[i]<sup>2</sup> + c</tt> does not diverge when iterated.
 * 
 * <p>For example, consider <tt>c = 0 + 0i</tt>. Then 
 * <pre>
 * z[1] = 0 + 0i
 * z[2] = 0 + 0i
 * </pre>
 * obviously doesn't diverge and is in the Mandelbrot set.
 * 
 * <p>But a number like <tt>c = 1 + i</tt>
 * <pre>
 * z[1] = 1 + i
 * z[2] = (1 + i)^2 + (1 + i)
 *      = 2i + (1 + i)
 *      = 1 + 3i  
 * </pre>
 * quickly diverges.
 * 
 */
public class MandelbrotSet {
    static final Logger LOGGER = LogManager.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
    
    static private final String USAGE = "<app> [<options>] <n> <c> [<c> ...]";
    static private final String HEADER = "Tests each <c> to determine if it's still in the Mandelbrot set after <n> iterations.";
    static private final String FOOTER = "\n"
            + "The Mandelbrot set contains those complex numbers $$c$$ for which the function "
            + "\n$$z[0] = 0, z[i+1] = z[i]^2 + c$$ remains bounded as $$i$$ increases."
            +"\n";
    static private final Options OPTIONS;
    static {
        OPTIONS = new Options();
        OPTIONS.addOption("h","help",false,"Print this message.");
        OPTIONS.addOption("v","verbose",false,"Show intermediate values.");
    }
    
    static public void main(String[] args) {
        try {
            CommandLine cmdline = (new DefaultParser()).parse(OPTIONS,args);
            if (cmdline.hasOption("help")) {
                (new HelpFormatter()).printHelp(USAGE,HEADER,OPTIONS,FOOTER,false);
                System.exit(1);
            }
            args = cmdline.getArgs();
            int n = Integer.parseInt(args[0]);
            ComplexFormat cf = new ComplexFormat();
            for (int i=1; i < args.length; ++i) {
                LOGGER.debug("args["+i+"] = \"" + args[i] + "\" => ...");
                Complex c = cf.parse(args[i]);
                LOGGER.debug("... " + c);
                int x = MandelbrotSet.isInMandelbrotSet(n, c);
                if (x == n) {
                    System.out.println(c + " remains in the Mandelbrot set after " + n + " iterations");
                } else {
                    System.out.println(c + " escaped the Mandelbrot set after " + x + " iterations");
                }
            }
            
        } catch (ParseException ex) {
            // can't use logger; it's not configured
            System.err.println(ex.getMessage());
            (new HelpFormatter()).printHelp(USAGE,HEADER,OPTIONS,FOOTER,false);
            System.exit(1);
        }
    }
    
    /**
     * Once a candidate's distance from 0 is larger than 2, that candidate has escaped
     */
    static boolean hasEscaped(Complex c) {
        double distance_from_0_squared = c.getReal() * c.getReal() + c.getImaginary() * c.getImaginary();
        if (distance_from_0_squared < 4) {
          return false;  
        }
        return true;
    }
    
    /**
     * Tests c to determine whether or not candidate escaped the Mandelbrot set.
     * @param candidate
     * @return 0 or the number of iterations before candidate escaped the Mandelbrot set.
     *         I.e., returns <tt>n</tt> where </tt>0 &lt;= n &lt;= maxIterations</tt>.
     */
    static int isInMandelbrotSet(int maxIterations, Complex candidate) {
        //LOGGER.debug("isInMandelbrotSet("+maxIterations+","+candidate+")");
        Complex f = new Complex(0,0);
        for (int i = 0; i < maxIterations; ++i) {
            f = f.multiply(f).add(candidate);
            //LOGGER.debug("f["+(i+1)+"] is " + f);
            if (hasEscaped(f)) {
                //LOGGER.debug(candidate + " is not in the Mandelbrot set");
                return i+1;
            }
        }
        //LOGGER.debug(candidate + " is in the Mandelbrot set");
        return 0;
    }
    
    private MandelbrotStatisticsPanel statisticsCollector;
    private ColorProvider colorProvider;
    
    public MandelbrotSet(ColorProvider colorMap, MandelbrotStatisticsPanel statisticsCollector) {
        LOGGER.info("MandelbrotSet constructor");
        this.statisticsCollector = statisticsCollector;
        this.colorProvider = colorMap;
        this.histogramData = null;
    }
    
    /** 
     * The Mandelbrot set is an array of Cell; each Cell has a <tt>a + bi</tt> 
     * value and a distance (number of iterations)from the Mandelbrot set, 
     * where distance 0 means the Cell is in the set.
     */
    public static class Cell {
        Cell(double a, double b, int distance) {
            this.a = a;
            this.b = b;
            this.distance = distance;
        }
        final double a; 
        final double b;
        final int distance;
        @Override public String toString() {
            return "Cell("+a+(b>=0?"+":"")+b+"i,"+distance+")";
        }
    }
    
    int widthInPixels;
    int heightInPixels;
    int maxIterations;
    
    /**
     * Cell[0] is the upper left corner of the image.
     * Cell[x + j*width] is the Cell as position x,y.
     */
    private Cell cells[];
    
    private int[] histogramData;
    
    Cell getCell(Point point) {
        int i = point.y * widthInPixels + point.x;
        // LOGGER.info("getCell(point="+point+"), i="+i+", cells.length="+cells.length);
        if (i < cells.length) return cells[i];
        return new Cell(point.x, point.y, 0);
    }
    
    
    public BufferedImage asBufferedImage(int widthInPixels, int heightInPixels, Complex center, double delta, int maxIterations) {
        if (widthInPixels < 2 || heightInPixels < 2) {
            throw new IllegalArgumentException("widthInPixels="+widthInPixels+" and heightInPixels="+heightInPixels+" must both be > 1");
        }
        
        histogramData = new int[maxIterations+1];
        BufferedImage img = new BufferedImage(widthInPixels,heightInPixels,BufferedImage.TYPE_INT_RGB);
        
        double wx0 = center.getReal() - (widthInPixels / 2) * delta;
        double wy0 = center.getImaginary() - (heightInPixels/ 2) * delta;

        double wx = wx0;
        for (int x = 0; x < widthInPixels; ++x) {
            double wy = wy0;
            for (int y = 0; y < heightInPixels; ++y) {
                int distance = isInMandelbrotSet(maxIterations, new Complex(wx,wy));
                ++histogramData[distance];
                img.setRGB(x,y,colorProvider.getColor(distance,maxIterations));
                wy += delta;
            }
            wx += delta;
        }
        
        return img;
    }
    
//    /**
//     * Returns a view into the Mandelbrot set.
//     * @param widthInPixels      width as number of pixels
//     * @param heightInPixels     height as number of pixels
//     * @param upperLeftCorner    upper left corner of the rectangle to be drawn
//     * @param lowerRightCorner   lower right corner of the rectangle to be drawn
//     * @param maxIterations      maximum number of iterations
//     * @return the view into the Mandelbrot set
//     */
//    public BufferedImage asBufferedImage(int widthInPixels, int heightInPixels, Complex upperLeftCorner, Complex lowerRightCorner, int maxIterations) {
//        if (LOGGER.isDebugEnabled()) {
//            LOGGER.debug("asBufferedImage(widthInPixels="+widthInPixels+",heightInPixels="+heightInPixels+",upperLeftCorner,lowerRightCorner,maxIterations="+maxIterations+")");
//        }
//        if (widthInPixels < 2 || heightInPixels < 2) {
//            LOGGER.fatal("widthInPixels="+widthInPixels+" and heightInPixels="+heightInPixels+" must both be greater than 2!");
//            System.exit(1);
//        }
//        
//        this.widthInPixels = widthInPixels;
//        this.heightInPixels = heightInPixels;
//        this.maxIterations = maxIterations;
//        
//        histogramData = new int[maxIterations+1];
//        cells = new Cell[widthInPixels * heightInPixels];
//        
//        double widthIncrement = (lowerRightCorner.getReal() - upperLeftCorner.getReal()) / (widthInPixels - 1);
//        double heightIncrement = (upperLeftCorner.getImaginary() - lowerRightCorner.getImaginary()) / (heightInPixels - 1);
//        
//        if (LOGGER.isDebugEnabled()) {
//            LOGGER.debug("width from " + upperLeftCorner.getReal() + " to " + lowerRightCorner.getReal() 
//                    + " in " + widthInPixels + " increments of " + widthIncrement + " is "
//                    + ( upperLeftCorner.getReal() + (widthInPixels * widthIncrement) ));
//            LOGGER.debug("height from " + lowerRightCorner.getImaginary() + " to " + upperLeftCorner.getImaginary() 
//                    + " in " + heightInPixels + " increments of " + heightIncrement + " is "
//                    + ( lowerRightCorner.getImaginary() + (heightInPixels * heightIncrement) ));
//        }
//
//        BufferedImage img = new BufferedImage(widthInPixels,heightInPixels,BufferedImage.TYPE_INT_RGB);
//        
//        double y = lowerRightCorner.getImaginary();
//        for (int iy=0; iy < heightInPixels; ++iy) {
//            double x = upperLeftCorner.getReal();
//            for (int ix=0; ix < widthInPixels; ++ix) {
//                int n = isInMandelbrotSet(maxIterations, new Complex(x,y));
//                cells[iy*widthInPixels + ix] = new Cell(x,y,n);
//                ++histogramData[n];
//                img.setRGB(ix,iy,colorProvider.getColor(n,maxIterations));
//                x += widthIncrement;
//            }
//            y += heightIncrement;
//        }
//        
//        LOGGER.debug("returning img");
//        return img;
//    }
    
    /**
     * Returns the histogram data generated by the most recent 
     * {@link #asBufferedImage(int,int,Complex,Complex,int) asBufferedImage} call.
     * I.e., the <tt>i</tt><sup>th</sup> entry in the returned array is the number
     * of pixels that escaped the Mandelbrot set after <tt>i</tt> iterations;
     * the <tt>i=0</tt><sup>th</sup> entry is the number of pixels remaining in the Mandelbrot set.
     */
    public int[] getHistogramData() {
        return histogramData;
    }
}
