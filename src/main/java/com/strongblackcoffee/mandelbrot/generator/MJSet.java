package com.strongblackcoffee.mandelbrot.generator;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 */
public class MJSet {
    static final String thisSimpleName = MethodHandles.lookup().lookupClass().getSimpleName();
    static final Logger LOGGER = LogManager.getLogger(thisSimpleName);
    
    public MJSet(int widthInPixels, int heightInPixels, 
                 BigDecimal centerX, BigDecimal centerY, 
                 double delta, int maxIterations) {
        LOGGER.debug("MJSet(widthInPixels="+widthInPixels+", heightInPixels="+heightInPixels
            + ", centerX="+centerX+", centerY="+centerY
            + ", delta="+delta+", maxIterations="+maxIterations);
        this.widthInPixels = widthInPixels;
        this.heightInPixels = heightInPixels;
        this.centerX = centerX;
        this.centerY = centerY;
        this.delta = delta;
        this.maxIterations = maxIterations;
        
        this.pointFactory = new MJCalcFactory(centerX, centerY, delta, widthInPixels, heightInPixels, maxIterations, new MJCalc.Callback() {
            @Override public void setColumns(int row, int[] columns) {
                MJSet.this.setColumns(row, columns);
            }
        });
    }
    
    private final int widthInPixels;
    private final int heightInPixels;
    private final BigDecimal centerX;
    private final BigDecimal centerY;
    private final double delta;
    private final int maxIterations;
    private final MJCalcFactory pointFactory;
    
    private int[][] points = null;
    
    public int getWidth() {
        return widthInPixels;
    }
    
    public int getHeight() {
        return heightInPixels;
    }
    
    public BigDecimal getCenterX() {
        return centerX;
    }
    
    public BigDecimal getCenterY() {
        return centerY;
    }
    
    public double getDelta() {
        return delta;
    }
    
    public int getMaxIterations() {
        return maxIterations;
    }
    
    public int[][] getPoints() {
        return points;
    }
    
    synchronized public void setColumns(int row, int[] columns) {
        LOGGER.debug(thisSimpleName+": setColumns row="+row+", columns="+Arrays.toString(columns));
        points[row] = columns;
    }
    
    
    
    /**
     * Compute the distances.
     */
    public void generate(ExecutorService pool) {
        if (this.points == null) {
            this.points = new int[heightInPixels][];
        
            for (int row = 0; row < this.heightInPixels; ++row) {
                LOGGER.debug(thisSimpleName+": scheduling calculation of row="+row);
                pool.execute(pointFactory.getCalc(row));
            }
        }
        LOGGER.info(thisSimpleName+": waiting for pool to complete");
        pool.shutdown();
        try {
            pool.awaitTermination(2, TimeUnit.HOURS);
        } catch (InterruptedException ex) {
            LOGGER.info(thisSimpleName+": generate: " + ex.getLocalizedMessage());
        }
    }
    
    public void toXML(PrintWriter out) {
        out.print("<MJSet widthInPixels=\""+widthInPixels+"\""
                +" heightInPixels=\""+heightInPixels+"\""
                +" centerX=\""+centerX+"\""
                +" centerY=\""+centerY+"\""
                +" delta=\""+delta+"\""
                +" maxIterations=\""+maxIterations+"\"");
        int[][] p = getPoints();
        if (p == null) {
            out.println("/>");
        } else {
            out.println(">");
            for (int i=0; i < p.length; ++i) {
                out.print("  <row y=\""+i+"\">");
                String comma="";
                for (int j=0; j < p[i].length; ++j) {
                    out.print(comma + p[i][j]);
                    comma = ",";
                }
                out.println("</row>");
            }
            out.println("</MJSet>");
        }
    }
    
    @Override
    public String toString() {
        StringWriter sw = new StringWriter();
        toXML(new PrintWriter(sw));
        try {
            sw.close();
            return sw.toString();
        } catch (IOException ex) {
            return ex.getLocalizedMessage();
        }
    }
    
    public static class Main {
        
        static private final String USAGE = "$app [options] centerX centerY delta width height maxIterations";
        static private final String HEADER = "Mandlebrot/Julia set generator:";
        static private final String FOOTER = "\n"
                +"\nGenerates a Mandlebrot or Julia set."
                +"\n";
        static private final Options OPTIONS;
        static {
            OPTIONS = new Options();
            OPTIONS.addOption("h","help",false,"Print this message.");
            OPTIONS.addOption("t","threads",true,"'-t 8' uses 8 threads instead of the default, 4.");
            OPTIONS.addOption("j","julia",true,"'-j 1.2,3.4' generates a Julia set with c=1.2+3.4i");
        }
    
    static public void main(String[] args) {
        try {
            CommandLine cmdline = (new DefaultParser()).parse(OPTIONS,args);
            if (cmdline.hasOption("help") || cmdline.getArgs().length != 6) {
                (new HelpFormatter()).printHelp(USAGE,HEADER,OPTIONS,FOOTER,false);
                System.exit(1);
            }
            
            BigDecimal centerX = new BigDecimal(cmdline.getArgs()[0]);
            BigDecimal centerY = new BigDecimal(cmdline.getArgs()[1]);
            double delta = Double.parseDouble(cmdline.getArgs()[2]);
            int width = Integer.parseInt(cmdline.getArgs()[3]);
            int height = Integer.parseInt(cmdline.getArgs()[4]);
            int maxIterations = Integer.parseInt(cmdline.getArgs()[5]);
            
            int nthreads = Integer.parseInt(cmdline.getOptionValue("threads","4"));
            MJSet mjSet;
            if (cmdline.hasOption("julia")) {
                mjSet = null; // FIXME
            } else {
                mjSet = new MJSet(width,height,centerX,centerY,delta,maxIterations);
            }
            LOGGER.info("Generating ("+nthreads+" threads) ..." );
            ExecutorService pool = Executors.newFixedThreadPool(nthreads);
            mjSet.generate(pool);
            LOGGER.info("... generation complete.");
            PrintWriter out = new PrintWriter(System.out);
            mjSet.toXML(out);
            out.close();
            
        } catch (ParseException ex) {
            // can't use logger; it's not configured
            System.err.println(ex.getMessage());
            (new HelpFormatter()).printHelp(USAGE,HEADER,OPTIONS,FOOTER,false);
        }
    }
    }
    
}
