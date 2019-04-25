package com.strongblackcoffee.mandelbrot.generator;

import com.strongblackcoffee.mandelbrot.BigComplex;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
public class SetGenerator {
    static final String SIMPLE_NAME = MethodHandles.lookup().lookupClass().getSimpleName();
    static final Logger LOGGER = LogManager.getLogger(SIMPLE_NAME);
    
    static final String DELTA_DEFAULT = "0.002";
    
    static private final String USAGE = "$app [options] {x-coordinate-of-center} {y-coordinate-of-center}";
    static private final String HEADER = "Mandlebrot/Julia XML frame generator";
    static private final String FOOTER = "\n"
            +"\nGenerates a series of Mandlebrot or Julia set frames that can be used to"
            +"\nconstruct an animation."
            +"\n";
    static private final Options OPTIONS;
    static {
        OPTIONS = new Options();
        OPTIONS.addOption("h","help",false,"Print this message.");
        OPTIONS.addOption("t","threads",true,"'-t 8' uses 8 threads instead of the default, [4]");
        OPTIONS.addOption("z","zoomFactor",true,"[0.95]");
        OPTIONS.addOption("d","delta",true,"initial delta value ["+DELTA_DEFAULT+"]");
        OPTIONS.addOption("D","minDelta",true,"stop when delta zooms smaller than this [1.0e-26]");
        OPTIONS.addOption("W","width",true,"width in pixels [1920]");
        OPTIONS.addOption("H","height",true,"height in pixels [1080]");
        OPTIONS.addOption("M","maxFrames",true,"maximum number of frames to generate [0 (unlimited)]");
        OPTIONS.addOption("o","outFilePattern",true,"where output files go [frames/frame-{%06d}.xml]");
        OPTIONS.addOption("j","julia",true,"'-j 1.2,3.4' generates a Julia set with c=1.2+3.4i");
        OPTIONS.addOption("i","maxIterations",true,"maxIterations [1000]");
    }
    
    static public void main(String[] args) {
        try {
            CommandLine cmdline = (new DefaultParser()).parse(OPTIONS,args);
            if (cmdline.hasOption("help") || cmdline.getArgs().length != 2) {
                (new HelpFormatter()).printHelp(USAGE,HEADER,OPTIONS,FOOTER,false);
                System.exit(1);
            }
            
            SetGenerator setGenerator = new SetGenerator(
                    Integer.parseInt(  cmdline.getOptionValue("width","1920")),
                    Integer.parseInt(  cmdline.getOptionValue("height","1080")),
                    new BigComplex(new BigDecimal(cmdline.getArgs()[0]),new BigDecimal(cmdline.getArgs()[1])),
                    BigComplex.parse(  cmdline.getOptionValue("julia", null)),
                    Double.parseDouble(cmdline.getOptionValue("delta",DELTA_DEFAULT)), 
                    Double.parseDouble(cmdline.getOptionValue("minDelta","1e-26")),
                    Double.parseDouble(cmdline.getOptionValue("zoomFactor","0.95")),
                    Integer.parseInt(  cmdline.getOptionValue("maxIterations","100000")),
                    Integer.parseInt(  cmdline.getOptionValue("maxFrames","0")), 
                    cmdline.getOptionValue("outFilePattern","frames/frame-{%06d}.xml"),
                    Integer.parseInt(  cmdline.getOptionValue("threads","4")));
            
            setGenerator.run();
            
        } catch (ParseException ex) {
            // can't use logger; it's not configured
            System.err.println(ex.getMessage());
            (new HelpFormatter()).printHelp(USAGE,HEADER,OPTIONS,FOOTER,false);
        }
    }
    
    public SetGenerator(int widthInPixels, int heightInPixels, 
                        BigComplex center, BigComplex julia, 
                        double initialDelta, 
                        double minimumDelta,
                        double zoomFactor,
                        int maxIterations,
                        int maxFrames,
                        String outFilePattern,
                        int nThreads) {
        this.widthInPixels = widthInPixels;
        this.heightInPixels = heightInPixels;
        this.center = center;
        this.julia = julia;
        this.initialDelta = initialDelta;
        this.minimumDelta = minimumDelta;
        this.zoomFactor = zoomFactor;
        this.maxIterations = maxIterations;
        this.maxFrames = maxFrames;
        this.outFilePattern = outFilePattern;
        this.nThreads = nThreads;
    }
    
    private final int widthInPixels;
    private final int heightInPixels;
    private final BigComplex center;
    private final BigComplex julia;
    private final double initialDelta;
    private final double minimumDelta;
    private final double zoomFactor;
    private final int maxIterations;
    private final int maxFrames;
    private final String outFilePattern;
    private final int nThreads;
    
    public void run() {
        double delta = initialDelta;
        int counter = 0;
        while ((maxFrames == 0 || counter < maxFrames)
                && delta < 1.0 && delta > minimumDelta) {
            ++counter;
            try (PrintWriter xmlWriter = new PrintWriter(getXMLFilename(counter))) {
                Instant startedAt = Instant.now();
                generateFrame(delta,xmlWriter);
                xmlWriter.close();
                LOGGER.info("frame " + counter + " took " + Duration.between(startedAt,Instant.now()).toString());
            } catch (IOException ex) {
                LOGGER.error(ex.getLocalizedMessage());
                System.exit(-1);
            }
            delta = zoomFactor * delta;
        }
    }
    
    String getXMLFilename(int n) {
        return outFilePattern.replace("{%06d}", String.format("%06d", n));
    }
    
    void generateFrame(double delta, PrintWriter xmlWriter) {
        MJSet mjset = new MJSet(widthInPixels, heightInPixels, 
                                center, julia,
                                delta, maxIterations);
        ExecutorService pool = Executors.newFixedThreadPool(nThreads);
        mjset.generate(pool);
        mjset.toXML(xmlWriter);
    }
}
