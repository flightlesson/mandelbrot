package com.strongblackcoffee.mandelbrot.doubledouble;

import com.hellblazer.utils.math.DoubleDouble;

/**
 *
 */
public class Complex {
    public Complex(double r, double i) {
        real = new DoubleDouble(r);
        imag = new DoubleDouble(i);
    }
    
    public Complex(DoubleDouble r, DoubleDouble i) {
        real = new DoubleDouble(r);
        imag = new DoubleDouble(i);
    }
    
    public Complex(Complex c) {
        this(c.real,c.imag);
    }
    
    private DoubleDouble real;
    private DoubleDouble imag;
    
    public DoubleDouble getReal() {
        return real;
    }
    
    public DoubleDouble getImag() {
        return imag;
    }

    double magnitudeSquared() {
        return real.sqr().add(imag.sqr()).doubleValue();
    }
    
    public static final DoubleDouble TWO = new DoubleDouble(2);

    /**
     * Let the current value be z[i]. Then return z[i+1] = z[i]^2 +c
     */
    Complex nextz(Complex c) {
        // z^2 = real^2 + imag^2 + 2*real*imag i
        return new Complex(real.sqr().add(imag.sqr()), real.multiply(imag).multiply(TWO));
    }
}
