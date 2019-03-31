package com.strongblackcoffee.mandelbrot.type;

import com.hellblazer.utils.math.DoubleDouble;
import org.apache.logging.log4j.LogManager;

/**
 *
 */
public class MJComplex<D extends MJDouble> {
    static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();
    
    public MJComplex(D r, D i) {
        this.real = r;
        this.imag = i;
    }
    
    public final void set(D real, D imag) {
        this.real = real;
        this.imag = imag;
    }
    
    public MJComplex(MJComplex c) {
        real = (D) c.real;
        imag = (D) c.imag;
    }
    
    private D real;
    private D imag;
    
    public MJDouble getReal() {
        return real;
    }
    
    public MJDouble getImag() {
        return imag;
    }

    public double magnitudeSquared() {
        return real.sqrThenAdd(imag.sqr()).doubleValue();
    }
    
    public static final DoubleDouble TWO = new DoubleDouble(2);

    /**
     * Let the current value be z[i]. Then return z[i+1] = z[i]^2 +c
     */
    public MJComplex nextz(MJComplex c) {
        // z^2 = real^2 + imag^2 + 2*real*imag i
        
        return new MJComplex(real.sqrThenAddSqr(imag), real.multiply2(imag));
    }
    
    public MJComplex construct(D real, D imag) {
        return new MJComplex(real, imag);
    }
    
    @Override 
    public String toString() {
        return "("+real.toString()+","+imag.toString()+")";
    }
}
