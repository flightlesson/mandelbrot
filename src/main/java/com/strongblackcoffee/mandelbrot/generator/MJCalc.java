package com.strongblackcoffee.mandelbrot.generator;

/**
 * Contract for implementations that compute rows in Mandelbrot or Julia sets
 * using <tt>Z[i+1] = Z[i] + C</tt> where Z[0] is the set coordinates of the 
 * pixel and C is a constant.  The differenence between the Mandelbrot set and 
 * a Julia set is the C is Z[0] for the Mandelbrot set (i.e., each point's C is different)
 * but C is some constant for a Julia set (each Julia set has a differnent C).
 *
 * Each instance 
 *
 * The implementation's constructor should be similar to
 * <pre>
 * class MJCalcExtendedDouble implements MJCalc {
 *   MJCalcExtendedDouble(int width, // number of pixels in the row
 *           int row,   // row number, needed when calling MJCalc.Callback's setColumns()
 *           ? leftmost_z0, // Z[0] of the leftmost pixel in the row; the type is implementation dependant
 *           ? c, // null means compute the Mandelbrot set; otherwise the Julia set's C
 *           double delta, // distance, in set coordinates, between each pixel
 *           int maxIterations, // if Z[maxIteration]'s absolute value is less than 2 then Z[0] is assumed to be in the set.
 *           MJCalc.Callback callback) // provides the setColumns() method
 * </pre>
 */
public interface MJCalc extends Runnable {
    public interface Callback {
        public void setColumns(int row, int[] columns);
    }
    
    /**
     * The implementation's constructor described the row that this will compute;
     * This will call MJCalc.Callback's setColumns() method to communicate the results 
     * back to the calling application.
     */
    public void run();
}
