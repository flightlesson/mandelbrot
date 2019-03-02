package com.strongblackcoffee.mandelbrot;

import java.awt.Color;

/**
 *
 */
public interface ColorProvider {
    
    /**
     * 
     * @param n   -1 &lt;= n &lt; maxColors 
     * @param maxColors 
     * @return 
     */
    int getColor(int n, int maxColors);
}
