package com.strongblackcoffee.mandelbrot.generator;

/**
 * Contract for implementations that compute rows in Mandelbrot or Julia sets using
 * <tt>Z[i+1] = Z[i] + C</tt> where Z[0] is the set coordinates of the pixel and C
 * is a constant.  The difference between the Mandelbrot set and a Julia set is that 
 * C is Z[0] for the Mandelbrot set (i.e., each point's C is different) but C is 
 * some constant for a Julia set (each Julia set has a differnent C).
 *
 * <p>Each instance computes one row in the set.
 *
 * <p>The implementation's constructor should be similar to
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
    
    /**
     * Provides the setColumns() method that the MJCalc implementation uses to 
     * communicate results back to the application.
     */
    public interface Callback {
        
        /**
         * The MJCalc implementation's run() method calls this to communicate its results
         * back to the application.
         * @param row     identifies which row these results are for
         * @param columns one int for each pixel in the row; 0 means the pixel is in the set,
         *                otherwise the number of iterations before the pixel left the set.
         */
        public void setColumns(int row, int[] columns);
    }
    
    /**
     * The instance (presumably via its constructor) contains a description of
     * the row to be computed.  The run() method computes that row and uses
     * MJCalc.Callback's setColumns() method to communicate the results back to
     * the calling application.
     */
    public void run();
}
