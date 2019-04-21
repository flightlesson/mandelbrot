package com.strongblackcoffee.mandelbrot.generator;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import org.apache.commons.math3.complex.Complex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * {@link MJCalc} implementation that uses doubles.  
 * Fast, but only about 14 digits of precision.
 */
public class MJCalcDouble implements MJCalc, Runnable {
    static final String thisSimpleName = MethodHandles.lookup().lookupClass().getSimpleName();
    static final Logger LOGGER = LogManager.getLogger(thisSimpleName);

    /**
     *
     * @param width
     * @param row
     * @param z0_0_real
     * @param z0_imag
     * @param delta
     * @param maxIterations
     * @param callback
     */
    public MJCalcDouble(int width, int row, double z0_0_real, double z0_imag, double delta, int maxIterations, MJCalc.Callback callback) {
        //LOGGER.info(thisSimpleName+"(width="+width+",row="+row+",z0_0_real="+z0_0_real+",z0_imag="+z0_imag
        //        +",delta="+delta+",maxIterations="+maxIterations+",callback)");
        this.width = width;
        this.row = row;
        this.z0_0_real = z0_0_real;
        this.z0_imag = z0_imag;
        this.delta = delta;
        this.maxIterations = maxIterations;
        this.callback = callback;
    }
    
    private final int width;
    private final int row;
    private final double z0_0_real;
    private final double z0_imag;
    private final double delta;
    private final int maxIterations;
    private final MJCalc.Callback callback;
    
    static double distanceFrom0Squared(Complex c) {
        return c.getReal() * c.getReal() + c.getImaginary() * c.getImaginary();
    }

    @Override
    public void run() {
        //LOGGER.info(thisSimpleName + ": run() row="+row+", width="+width);
        try {
            int[] columns = new int[this.width];
            for (int col=0; col < this.width; ++col) {
                columns[col] = 0;
                Complex z0 = new Complex(z0_0_real + col * delta, z0_imag);
                Complex z = z0;
                Complex c = z0;
                for (int i = 0; i < maxIterations; ++i) {
                    z = z.multiply(z).add(c);
                    //LOGGER.debug(thisSimpleName+": row="+row+", col="+col+", i="+i+", z[i+1]="+z+", |z|^2="+distanceFrom0Squared(z));
                    if (distanceFrom0Squared(z) > 4.0) {
                        columns[col] = i+1;
                        break;
                    }
                }
            }
            //LOGGER.info(thisSimpleName+": row="+row+" setting columns="+Arrays.toString(columns));
            this.callback.setColumns(this.row, columns);
        } catch (Exception ex) {
            LOGGER.warn(thisSimpleName + ": row="+row+", width="+width+": "+ex.getLocalizedMessage());
        }
    }
    
}
