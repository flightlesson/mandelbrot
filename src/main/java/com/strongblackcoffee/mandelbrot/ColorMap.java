package com.strongblackcoffee.mandelbrot;

import java.awt.Color;

/**
 *
 */
public class ColorMap {
    
    public static void main(String[] args) {
        ColorMap colorMap = new ColorMap(1000);
        
    }

    ColorMap(int n) {
        colorMap = new int[n];
        colorMap[0] = 0xFFFFFF;
        colorMap[1] = 0xAAFF99; // light green
        colorMap[2] = 0x00FF00; // green
        colorMap[3] = 0x00B31E; // darker green
        colorMap[4] = 0x99CCFF; // lighter blue
        colorMap[5] = 0x3377FF; // light blue
        colorMap[6] = 0x0000FF; // blue
        colorMap[7] = 0x0000AA; // dark blue
        colorMap[8] = 0x665500; // golden
        colorMap[9] = 0xCCCC00; 
        colorMap[10] = 0xFFFF00; // yellow
        colorMap[11] = 0xFFFF99; // pastel yellow
        colorMap[12] = 0xFFF7CC;
        
    }
    
    private int[] colorMap;
    
}
