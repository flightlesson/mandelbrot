package com.strongblackcoffee.mandelbrot.generator;

import org.apache.commons.math3.complex.Complex;

/**
 *
 */
public abstract class SetGenerator {
    
    protected ComplexType juliaSetC;
    protected int maxIterations;
    
    /**
     * 
     */
    public Point computePoint(ComplexType coordinates) {
        ComplexType z, c;
        if (juliaSetC == null) {
            z = new ComplexType(0.0,0.0);
            c = new ComplexType(coordinates);
        } else {
            z = new ComplexType(coordinates);
            c = juliaSetC;
        }
        return new Point(coordinates, computeDistanceFromSet(z,c,maxIterations));
    }
    
    public Grid computeGrid(int width, int height, ComplexType lowerLeftCorner, ComplexType upperRightCorner) {
        Grid grid = new Grid(width, height);
        DoubleType deltax = upperRightCorner.getReal().minus(lowerLeftCorner.getReal()).dividedBy(width);
        DoubleType deltay = upperRightCorner.getImaginary().minus(lowerLeftCorner.getImaginary()).dividedBy(height);
        DoubleType xx = lowerLeftCorner.getReal();
        for (int x=0; x < width; ++x) {
            DoubleType yy = lowerLeftCorner.getImaginary();
            for (int y=0; y < height; ++y) {
                grid.setPointAt(x,y,computePoint(new ComplexType(xx,yy)));
                yy = yy.plus(deltay);
            }
            xx = xx.plus(deltax);
        }
        return grid;
    }
    
    /**
     * Iterate <tt>z[i] = z[i-1]^2 + c</tt> until i gt maxIterations 
     * or z[i] is more than 2 units from 0+0i, returning i or 0 if i gt maxIterations.
     */
    int computeDistanceFromSet(ComplexType z0, ComplexType c, int maxIterations) {
        ComplexType z = new ComplexType(z0);
        for (int i = 0; i < maxIterations; ++i) {
            if (z.square().add(c).getDistanceFrom0Squared() > 4.0) {
                return i+1;
            }
        }
        return 0;
    }
}
