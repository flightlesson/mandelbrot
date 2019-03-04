package com.strongblackcoffee.mandelbrot;

/**
 *
 */
public interface MandelbrotStatisticsCollector {
    public void recordImageSize(int width, int height);
    public void recordDeltas(double deltaX, double deltaY);
    public void recordCurrentPoint(MandelbrotSet.Cell cell);
    public void recordMaxIterations(int maxIterations);
}
