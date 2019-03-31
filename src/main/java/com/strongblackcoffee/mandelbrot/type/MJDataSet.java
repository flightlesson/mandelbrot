package com.strongblackcoffee.mandelbrot.type;

import com.hellblazer.utils.math.DoubleDouble;
import com.strongblackcoffee.mandelbrot.Mandelbrot;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexFormat;
import org.apache.commons.math3.exception.MathParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents view into a Mandelbrot or Julia set.
 */
public class MJDataSet<C extends MJComplex> {
    
    
    
    private final int viewWidth;
    private final int viewHeight;
    private final C windowPointAtZeroZero;
    private final double windowPointDelta;
    private final int[][] distanceFromSet;
    
    public MJDataSet(int viewWidth, int viewHeight, C windowPointAtZeroZero, double windowPointDelta) {
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
