package com.strongblackcoffee.mandelbrot.generator;

/**
 * A point in a Mandelbrot or Julia set.
 */
public class Point {
    public Point(ComplexType coordinates, int distanceFromSet) {
        this.coordinates = coordinates;
        this.distanceFromSet = distanceFromSet;
    }
    
    /**
     * The real part is the coordinate on the X axis, 
     * the imaginary part is the coordinate on the Y axis.
     */
    public final ComplexType coordinates;
    
    /**
     * 0 means this point is in the set, otherwise the number of iterations
     * before the point escaped the set.
     */
    public final int distanceFromSet;
    
    @Override public String toString() {
        return coordinates.toString() + ":" + distanceFromSet;
        
    }
}
