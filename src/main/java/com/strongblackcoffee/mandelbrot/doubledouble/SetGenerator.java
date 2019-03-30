package com.strongblackcoffee.mandelbrot.doubledouble;

import com.hellblazer.utils.math.DoubleDouble;

/**
 * 
 */
public class SetGenerator {
    
    public SetGenerator(int viewWidth, int viewHeight, Complex windowPointAtZeroZero, double windowPointDelta, int maxIterations) {
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        this.windowPointAtZeroZero = windowPointAtZeroZero;
        this.windowPointDelta = windowPointDelta;
        this.maxIterations = maxIterations;
    }
    
    private final int viewWidth;
    private final int viewHeight;
    private final Complex windowPointAtZeroZero;
    private final double windowPointDelta;
    private final int maxIterations;
    
    public MJSet generateMJSet() {
        //MJSet = new MJSet(int viewWidth, int viewHeight, Complex windowPointAtZeroZero, double windowPointDelta)
        MJSet mjSet= new MJSet(viewWidth, viewHeight, windowPointAtZeroZero, windowPointDelta);
        DoubleDouble d = new DoubleDouble(windowPointDelta);
        
        DoubleDouble wy = windowPointAtZeroZero.getImag();
        for (int y=0; y < viewHeight; ++y) {
            DoubleDouble wx = windowPointAtZeroZero.getReal();
            for (int x=0; x < viewWidth; ++x) {
                Complex c = new Complex(wx,wy);
                mjSet.setDistanceFromSet(x, y, computeDistanceFromSet(c,c,maxIterations));
                wx = wx.add(d);
            }
            wy.add(d);
        }
        return mjSet;
    }
    
    /**
     * Calculates z(i+1) = z(i) + c until z's magnitude is greater than 2 or maxIterations
     * have been performed.  Returns the number of iterations required for z to become
     * greater than 2 or returns 0 if z never became greater than 2.
     */
    public int computeDistanceFromSet(Complex z0, Complex c, int maxIterations) {
        Complex z = z0;
        for (int i=0; i < maxIterations; ++i) {
            z = z.nextz(c);
            if (hasEscaped(z)) return i+1;
        }
        return 0;
    }
    
    /**
     * Once a candidate's distance from 0 is larger than 2, that candidate has escaped
     */
    static boolean hasEscaped(Complex c) {
        double distance_from_0_squared = c.magnitudeSquared();
        if (distance_from_0_squared > 4.0) {
          return false;  
        }
        return true;
    }
    
}
