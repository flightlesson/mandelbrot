package com.strongblackcoffee.mandelbrot.generator;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The idea is that there can be several implementations if {@link MjCalc}; 
 * the {@link #getCalc(int) getCalc} method returns the most appropriate of these.
 * 
 * <p>For example, if delta is smaller that 10^-14 then an implementation that 
 * uses doubles won't have enough precision.  But when delta is larger, a higher
 * precision implementation might run too slowly.
 */
public class MJCalcFactory {
    static final String thisSimpleName = MethodHandles.lookup().lookupClass().getSimpleName();
    static final Logger LOGGER = LogManager.getLogger(thisSimpleName);
    
    MJCalcFactory(BigDecimal centerX, BigDecimal centerY, double delta, int width, int height, int maxIterations, MJCalc.Callback callback) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.delta = delta;
        this.width = width;
        this.height = height;
        this.maxIterations = maxIterations;
        this.callback = callback;
    }
    
    private final BigDecimal centerX;
    private final BigDecimal centerY;
    private final double delta;
    private final int width;
    private final int height;
    private final int maxIterations;
    private final MJCalc.Callback callback;
    
    /**
     * Returns the most appropriate implementation.
     */
    public MJCalc getCalc(int row) {
        double topLeftX = centerX.doubleValue() - width/2 * delta;
        double topLeftY = centerY.doubleValue() - height/2 * delta;
        return new MJCalcDouble(width, row, 
                topLeftX, topLeftY + row * delta,
                delta, maxIterations, callback);
    }
}
