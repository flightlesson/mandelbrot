package com.strongblackcoffee.mandelbrot.generator;

import org.apache.commons.math3.complex.Complex;

/**
 * Wraps a more primitive Complex type; intended as a hook to make it easier
 * to convert to extended precision float types.
 */
public class ComplexType {
    
    private Complex c;
    
    public ComplexType(Double r, Double i) {
        c = new Complex(r,i);
    }
    
    public ComplexType(DoubleType r, DoubleType i) {
        c = new Complex(r.getDouble(), i.getDouble());
    }
    
    public ComplexType(ComplexType original) {
        c = new Complex(original.c.getReal(), original.c.getImaginary());
    }
    
    protected ComplexType(Complex c) {
        this.c = c;
    }
    
    @Override
    public String toString() {
        return c.toString();
    }
    
    public DoubleType getReal() {
        return new DoubleType(c.getReal());
    }
    
    public DoubleType getImaginary() {
        return new DoubleType(c.getImaginary());
    }
    
    /**
     * Returns a ComplexType whose value is this squared.
     * May change the value of this; may return this.
     */
    public ComplexType square() {
        this.c = this.c.multiply(this.c);
        return this;
    }
    
    /**
     * Returns a ComplexType whose value is this plus c.
     * May change the value of this; may return this.
     */
    public ComplexType add(ComplexType that) {
        this.c = this.c.add(that.c);
        return this;
    }
    
    /**
     * Returns the magnitude of this, squared.
     */    
    public double getDistanceFrom0Squared() {
        return c.getReal() * c.getReal() + c.getImaginary() * c.getImaginary();
    }
}
