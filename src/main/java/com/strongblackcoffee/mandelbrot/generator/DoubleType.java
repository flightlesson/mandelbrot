package com.strongblackcoffee.mandelbrot.generator;

/**
 * Wraps a more primitive Double type; intended as a hook to make it easier
 * to convert to extended precision float types.
 */
public class DoubleType {
    
    private final double d;
    
    public DoubleType(double d) {
        this.d = d;
    }
    
    public DoubleType plus(DoubleType that) {
        return new DoubleType(d + that.d);
    }
    
    public DoubleType minus(DoubleType that) {
        return new DoubleType(d - that.d);
    }
    
    public DoubleType dividedBy(int that) {
        return new DoubleType(d / that);
    }
    
    public double getDouble() {
        return d;
    }
}
