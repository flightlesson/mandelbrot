package com.strongblackcoffee.mandelbrot;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 */
public class BigComplex {
    static final String SIMPLE_NAME = MethodHandles.lookup().lookupClass().getSimpleName();
    static final Logger LOGGER = LogManager.getLogger(SIMPLE_NAME);
    
    public static BigComplex parse(String s) {
        if (s == null) return null;
        try {
            String[] a = s.split(",");
            BigDecimal real = new BigDecimal(a[0]);
            BigDecimal imag = new BigDecimal(a[1]);
            return new BigComplex(real, imag);
        } catch (Exception ex) {
            LOGGER.warn(SIMPLE_NAME + ": " + ex.getMessage());
        }
        return null;
    }
    
    public BigComplex(BigDecimal real, BigDecimal imag) {
        this.real = real;
        this.imag = imag;
    }
    
    public final BigDecimal real;
    public final BigDecimal imag;
    
    @Override
    public String toString() {
        return real.toString() + (imag.compareTo(BigDecimal.ZERO)<0?"":"+") + imag.toString() + "i";
    }
    
}
