package com.strongblackcoffee.mandelbrot.doubledouble;

import com.hellblazer.utils.math.DoubleDouble;

/**
 * Represents view into a Mandelbrot or Julia set.
 */
public class MJSet {
    private final int viewWidth;
    private final int viewHeight;
    private final Complex windowPointAtZeroZero;
    private final double windowPointDelta;
    private final int[][] distanceFromSet;
    
    public MJSet(int viewWidth, int viewHeight, Complex windowPointAtZeroZero, double windowPointDelta) {
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        this.windowPointAtZeroZero = windowPointAtZeroZero;
        this.windowPointDelta = windowPointDelta;
        this.distanceFromSet = new int[viewWidth][viewHeight];
    }
    
    public void setDistanceFromSet(int x, int y, int distance) {
        this.distanceFromSet[x][y] = distance;
    }
    
    public int getDistanceFromSet(int x, int y) {
        return this.distanceFromSet[x][y];
    }
}
