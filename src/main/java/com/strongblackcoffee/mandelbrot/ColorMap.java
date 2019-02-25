package com.strongblackcoffee.mandelbrot;

import java.awt.Color;

/**
 *
 */
public class ColorMap {
    
    public static void main(String[] args) {
        ColorMap colorMap = new ColorMap();
        
    }

    ColorMap() {
    }
    
    
    
    int getColor(int n) {
        if (n == 100) return 0;
        if (n > 64) return Color.PINK.getRGB();
        if (n > 42) return Color.BLUE.getRGB();
        if (n > 24) return Color.GREEN.getRGB();
        if (n > 16)  return Color.RED.darker().getRGB();
        if (n > 10)  return Color.RED.getRGB();
        if (n > 8)  return Color.RED.brighter().getRGB();
        if (n > 6)  return Color.ORANGE.darker().getRGB();
        if (n > 5)   return Color.ORANGE.getRGB();
        if (n > 4)   return Color.YELLOW.getRGB();
        if (n > 3)   return 0xCCCCCC;
        if (n > 2)  return 0xDDDDDD;
        if (n > 1)  return 0xEEEEEE;
        return 0xFFFFFF;
    }
}
