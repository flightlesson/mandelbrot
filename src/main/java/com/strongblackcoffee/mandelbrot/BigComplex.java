package com.strongblackcoffee.mandelbrot;

import java.math.BigDecimal;

/**
 *
 */
public class BigComplex {
    
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
