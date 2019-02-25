package com.strongblackcoffee.mandelbrot;

import java.awt.image.BufferedImage;
import java.awt.Color;
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
 * function <tt>z[0] = 0, z[i+1] = z[i]^2 + c</tt> does not diverge when iterated.
 * 
 * <p>For example, consider <tt>c = 0 + 0i</tt>. Then 
 * <pre>
 * z[1] = 0 + 0i
 * z[2] = 0 + 0i
 * </pre>
 * This obviously doesn't diverge and is in the Mandelbrot set.
 * 
 * Or consider <tt>c = 3 + 3i</tt>:
 * <pre>
 * z[1] = 3 + 3i
 * z[2] = 3 + 21i
 * z[3] = 
 */
public class MandelbrotSet {
    static final Logger LOGGER = LogManager.getLogger();
    
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
     * @return the number of iterations before candidate escaped the Mandelbrot set.
     */
    static int isInMandelbrotSet(int maxIterations, Complex candidate) {
        //LOGGER.debug("isInMandelbrotSet("+maxIterations+","+candidate+")");
        Complex f = new Complex(0,0);
        for (int i = 0; i < maxIterations; ++i) {
            f = f.multiply(f).add(candidate);
            //LOGGER.debug("f["+(i+1)+"] is " + f);
            if (hasEscaped(f)) {
                //LOGGER.debug(candidate + " is not in the Mandelbrot set");
                return i;
            }
        }
        //LOGGER.debug(candidate + " is in the Mandelbrot set");
        return maxIterations;
    }
    
    private int[] stats;
    private ColorMap colorMap;
    
    MandelbrotSet(ColorMap colorMap) {
        this.colorMap = colorMap;
    }
    
    /**
     * 
     * @param widthInPixels   width as number of pixels
     * @param heightInPixels  height as number of pixels
     * @param upperLeftCorner    upper left corner of the rectangle to be drawn
     * @param lowerRightCorner   lower right corner of the rectangle to be drawn
     * @return 
     */
    BufferedImage asBufferedImage(int widthInPixels, int heightInPixels, Complex upperLeftCorner, Complex lowerRightCorner, int maxIterations) {
        if (widthInPixels < 2 || heightInPixels < 2) {
            LOGGER.fatal("widthInPixels="+widthInPixels+" and heightInPixels="+heightInPixels+" must both be greater than 2!");
            System.exit(1);
        }
        
        int[][] pixels = new int[widthInPixels][heightInPixels];
        
        stats = new int[maxIterations+1];
        
        double widthIncrement = (lowerRightCorner.getReal() - upperLeftCorner.getReal()) / (widthInPixels - 1);
        double heightIncrement = (upperLeftCorner.getImaginary() - lowerRightCorner.getImaginary()) / (heightInPixels - 1);
        
        LOGGER.info("width from " + upperLeftCorner.getReal() + " to " + lowerRightCorner.getReal() 
                    + " in " + widthInPixels + " increments of " + widthIncrement + " is "
                    + ( upperLeftCorner.getReal() + (widthInPixels * widthIncrement) ));
        LOGGER.info("height from " + lowerRightCorner.getImaginary() + " to " + upperLeftCorner.getImaginary() 
                    + " in " + heightInPixels + " increments of " + heightIncrement + " is "
                    + ( lowerRightCorner.getImaginary() + (heightInPixels * heightIncrement) ));

        BufferedImage img = new BufferedImage(widthInPixels,heightInPixels,BufferedImage.TYPE_INT_RGB);
        
        double x = upperLeftCorner.getReal();
        for (int ix=0; ix < widthInPixels; ++ix) {
            double y = lowerRightCorner.getImaginary();
            for (int iy=0; iy < heightInPixels; ++iy) {
                int n = isInMandelbrotSet(maxIterations, new Complex(x,y));
                ++stats[n];
                //int color = getColor(n); // (n == maxIterations ? 0 : 0xFFFFFF);
                //img.setRGB(ix,iy,color);
                pixels[ix][iy] = n;
                y += heightIncrement;
            }
            x += widthIncrement;
        }
        
        for (int ix=0; ix < widthInPixels; ++ix) {
            for (int iy=0; iy < heightInPixels; ++iy) {
                int color = colorMap.getColor(pixels[ix][iy]);
                img.setRGB(ix,iy,color);
            }
        }
        
        return img;
    }
}
