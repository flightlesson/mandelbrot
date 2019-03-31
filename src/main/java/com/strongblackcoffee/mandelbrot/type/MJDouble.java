package com.strongblackcoffee.mandelbrot.type;

/**
 *
 */
public interface MJDouble {
    public MJDouble set(double v);
    public MJDouble sqr();
    public MJDouble add(MJDouble y);
    public MJDouble sqrThenAdd(MJDouble y);
    public MJDouble sqrThenAddSqr(MJDouble y);
    public MJDouble multiply2(MJDouble y);
    public double doubleValue();
}
