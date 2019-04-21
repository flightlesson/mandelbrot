package com.strongblackcoffee.mandelbrot.generator;

/**
 * Contract for implementations that compute rows in Mandelbrot or Julia sets.
 */
public interface MJCalc extends Runnable {
    public interface Callback {
        public void setColumns(int row, int[] columns);
    }
    public void run();
}
