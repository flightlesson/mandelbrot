package com.strongblackcoffee.mandelbrot.type;

/**
 *
 */
public class WrappedDouble implements MJDouble {
    
    
    private double d;
    
    public WrappedDouble() {
    }
    
    public WrappedDouble(double d) {
        this.d = d;
    }
    
    public WrappedDouble set(double d) {
        this.d = d;
        return this;
    }

    @Override
    public MJDouble sqr() {
        return new WrappedDouble(d * d);
    }
    
    @Override
    public MJDouble add(MJDouble y) {
        return new WrappedDouble(d + ((WrappedDouble) y).d);
    }

    @Override
    public MJDouble sqrThenAdd(MJDouble y) {
        return new WrappedDouble(d * d + ((WrappedDouble)y).d);
    }
    
    @Override
    public double doubleValue() {
        return d;
    }

    @Override
    public MJDouble sqrThenAddSqr(MJDouble y) {
        double yd = ((WrappedDouble)y).d;
        return new WrappedDouble(d * d + yd * yd);
    }

    @Override
    public MJDouble multiply2(MJDouble y) {
        return new WrappedDouble(d * ((WrappedDouble)y).d * 2.0);
    }
    
    @Override 
    public String toString() {
        return String.valueOf(this.d);
    }
}
