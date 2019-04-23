package com.strongblackcoffee.mandelbrot.generator;

import com.hellblazer.utils.math.DoubleDouble;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import org.apache.commons.math3.complex.Complex;
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
    
    /**
     * 
     * @param centerX  real part of point at (width/2, height/2)
     * @param centerY  imaginary part of point at (width/2, height/2)
     * @param cX
     * @param cY
     * @param delta
     * @param width
     * @param height
     * @param maxIterations
     * @param callback 
     */
    MJCalcFactory(BigDecimal centerX, BigDecimal centerY, BigDecimal cX, BigDecimal cY,
                  double delta, int width, int height, int maxIterations, MJCalc.Callback callback) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.cX = cX;
        this.cY = cY;
        this.delta = delta;
        this.width = width;
        this.height = height;
        this.maxIterations = maxIterations;
        this.callback = callback;
    }
    
    private final BigDecimal centerX;
    private final BigDecimal centerY;
    private final BigDecimal cX;
    private final BigDecimal cY;
    private final double delta;
    private final int width;
    private final int height;
    private final int maxIterations;
    private final MJCalc.Callback callback;
    
    /**
     * Returns the most appropriate implementation.
     */
    public MJCalc getCalc(int row) {
        if (delta > 1.0e-12) {
            double topLeftX = centerX.doubleValue() - width/2 * delta;
            double topLeftY = centerY.doubleValue() - height/2 * delta;
            Complex c = null;
            if (cX != null && cY != null) {
                c = new Complex(cX.doubleValue(), cY.doubleValue());
            }
            return new MJCalcDouble(width, row, 
                    topLeftX, topLeftY + row * delta, 
                    c,
                    delta, maxIterations, callback);
        } else if (delta > 1.0e-26) {
            DoubleDouble z0X = DoubleDouble.valueOf(centerX.toString()).subtract(new DoubleDouble(width/2 * delta));
            DoubleDouble z0Y = DoubleDouble.valueOf(centerY.toString()).subtract(new DoubleDouble(height/2 * delta));
            DoubleDouble cX = null;
            DoubleDouble cY = null;
            
            return new MJCalcDoubleDouble(width, row, 
                    z0X, z0Y.add(new DoubleDouble(row * delta)), 
                    cX, cY, 
                    delta, maxIterations, callback);
        }
        
        // FIXME
        
        DoubleDouble z0X = DoubleDouble.valueOf(centerX.toString()).subtract(new DoubleDouble(width/2 * delta));
        DoubleDouble z0Y = DoubleDouble.valueOf(centerY.toString()).subtract(new DoubleDouble(height/2 * delta));
        DoubleDouble cX = null;
        DoubleDouble cY = null;
            
        return new MJCalcDoubleDouble(width, row, 
                z0X, z0Y.add(new DoubleDouble(row * delta)), 
                cX, cY, 
                delta, maxIterations, callback);
    }
}
