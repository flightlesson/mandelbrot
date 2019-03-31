package com.strongblackcoffee.mandelbrot.type;

import com.hellblazer.utils.math.DoubleDouble;

/**
 *
 */
public class WrappedDoubleDouble implements MJDouble {
    
    private static final DoubleDouble TWO = new DoubleDouble(2);
    
    private DoubleDouble d;
    
    public WrappedDoubleDouble() {
        
    }
    
    public WrappedDoubleDouble(DoubleDouble d) {
        this.d = d;
    }
    
    @Override
    public WrappedDoubleDouble set(double d) {
        this.d = new DoubleDouble(d);
        return this;
    }

    @Override
    public MJDouble sqr() {
        return new WrappedDoubleDouble(d.sqr());
    }
    
    @Override
    public MJDouble add(MJDouble y) {
        return new WrappedDoubleDouble(d.add(((WrappedDoubleDouble)y).d));
    }

    @Override
    public MJDouble sqrThenAdd(MJDouble y) {
        return new WrappedDoubleDouble(d.sqr().add(((WrappedDoubleDouble)y).d));
    }
    
    @Override
    public double doubleValue() {
        return d.doubleValue();
    }

    @Override
    public MJDouble sqrThenAddSqr(MJDouble y) {
        return new WrappedDoubleDouble(d.sqr().add( ((WrappedDoubleDouble)y).d.sqr()));
    }

    @Override
    public MJDouble multiply2(MJDouble y) {
        return new WrappedDoubleDouble(d.multiply(((WrappedDoubleDouble)y).d).multiply(TWO));
    }
    
    @Override 
    public String toString() {
        return this.d.toString();
    }
}
