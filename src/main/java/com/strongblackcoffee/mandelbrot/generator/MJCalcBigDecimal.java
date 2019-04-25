package com.strongblackcoffee.mandelbrot.generator;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * {@link MJCalc} implementation that uses BigDecimal to
 * provide unlimited precision.
 */
public class MJCalcBigDecimal implements MJCalc, Runnable {
    static final String SIMPLE_NAME = MethodHandles.lookup().lookupClass().getSimpleName();
    static final Logger LOGGER = LogManager.getLogger(SIMPLE_NAME);
    
    static final BigDecimal BIGDECIMAL_2 = new BigDecimal(2);

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
    public MJCalcBigDecimal(int width, int row, 
                        BigDecimal z0_real, BigDecimal z0_imag, 
                        BigDecimal c_real, BigDecimal c_imag,
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
    private final BigDecimal z0_real;
    private final BigDecimal z0_imag;
    private final BigDecimal c0_real;
    private final BigDecimal c0_imag;
    private final double delta;
    private final int maxIterations;
    private final MJCalc.Callback callback;
    
    static double distanceFrom0Squared(BigDecimal real, BigDecimal imag) {
        return real.pow(2).add(imag.pow(2)).doubleValue();
    }

    @Override
    public void run() {
        LOGGER.info(SIMPLE_NAME + ": run() row="+row+", width="+width);
        try {
            int[] columns = new int[this.width];
            for (int col=0; col < this.width; ++col) {
                columns[col] = 0;
                BigDecimal z_real = z0_real.add(BigDecimal.valueOf(col * delta));
                BigDecimal z_imag = z0_imag;
                BigDecimal c_real = c0_real==null ? z_real : c0_real;
                BigDecimal c_imag = c0_real==null ? z_imag : c0_imag;
                for (int i = 0; i < maxIterations; ++i) {
                    BigDecimal t = z_real.pow(2).subtract(z_imag.pow(2)).add(c_real);
                    z_imag = z_real.multiply(BIGDECIMAL_2).multiply(z_imag).add(c_imag);
                    z_real = t;
                    
                    LOGGER.debug(SIMPLE_NAME+": row="+row+", col="+col+", i="+i+", z[i+1]="+z_real+"+"+z_imag+"i, |z|^2="+distanceFrom0Squared(z_real,z_imag));
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
