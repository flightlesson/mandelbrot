package com.strongblackcoffee.mandelbrot.generator;

import com.hellblazer.utils.math.DoubleDouble;
import java.lang.invoke.MethodHandles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * {@link MJCalc} implementation that uses DoubleDouble to give up to about 
 * 30 digits precision.
 */
public class MJCalcDoubleDouble implements MJCalc, Runnable {
    static final String SIMPLE_NAME = MethodHandles.lookup().lookupClass().getSimpleName();
    static final Logger LOGGER = LogManager.getLogger(SIMPLE_NAME);
    
    static final DoubleDouble DOUBLEDOUBLE_2 = new DoubleDouble(2);

    /**
     *
     * @param width
     * @param row
     * @param z0_real
     * @param z0_imag
     * @param delta
     * @param maxIterations
     * @param callback
     */
    public MJCalcDoubleDouble(int width, int row, 
                        DoubleDouble z0_real, DoubleDouble z0_imag, 
                        DoubleDouble c_real, DoubleDouble c_imag,
                        double delta, int maxIterations, MJCalc.Callback callback) {
        LOGGER.info(SIMPLE_NAME+"(width="+width+",row="+row+",z0_0_real="+z0_real+",z0_imag="+z0_imag
                +",delta="+delta+",maxIterations="+maxIterations+",callback)");
        this.width = width;
        this.row = row;
        this.z0_real = z0_real;
        this.z0_imag = z0_imag;
        this.c0_real = c_real;
        this.c0_imag = c_imag;
        this.delta = delta;
        this.maxIterations = maxIterations;
        this.callback = callback;
    }
    
    private final int width;
    private final int row;
    private final DoubleDouble z0_real;
    private final DoubleDouble z0_imag;
    private final DoubleDouble c0_real;
    private final DoubleDouble c0_imag;
    private final double delta;
    private final int maxIterations;
    private final MJCalc.Callback callback;
    
    static double distanceFrom0Squared(DoubleDouble real, DoubleDouble imag) {
        //return c.getReal() * c.getReal() + c.getImaginary() * c.getImaginary();
        return real.sqr().add(imag.sqr()).doubleValue();
    }

    @Override
    public void run() {
        //LOGGER.info(thisSimpleName + ": run() row="+row+", width="+width);
        try {
            int[] columns = new int[this.width];
            for (int col=0; col < this.width; ++col) {
                columns[col] = 0;
                DoubleDouble z_real = z0_real.add(new DoubleDouble(col * delta));
                DoubleDouble z_imag = z0_imag;
                DoubleDouble c_real = c0_real==null ? z_real : c0_real;
                DoubleDouble c_imag = c0_real==null ? z_imag : c0_imag;
                for (int i = 0; i < maxIterations; ++i) {
                    DoubleDouble t = z_real.sqr().subtract(z_imag.sqr()).add(c_real);
                    z_imag = z_real.multiply(DOUBLEDOUBLE_2).multiply(z_imag).add(c_imag);
                    z_real = t;
                    
                    //LOGGER.debug(thisSimpleName+": row="+row+", col="+col+", i="+i+", z[i+1]="+z+", |z|^2="+distanceFrom0Squared(z));
                    if (distanceFrom0Squared(z_real,z_imag) > 4.0) {
                        columns[col] = i+1;
                        break;
                    }
                }
            }
            //LOGGER.info(thisSimpleName+": row="+row+" setting columns="+Arrays.toString(columns));
            this.callback.setColumns(this.row, columns);
        } catch (Exception ex) {
            LOGGER.warn(SIMPLE_NAME + ": row="+row+", width="+width+": "+ex.getLocalizedMessage());
        }
    }
    
}
