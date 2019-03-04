package com.strongblackcoffee.mandelbrot.generator;

/**
 *
 */
public class Grid {
    public Grid(int width, int height) {
        grid = new Point[width][height];
    }
    
    private final Point grid[][];
    
    public void setPointAt(int x, int y, Point p) {
        grid[x][y] = p;
    }
    
    public Point getPointAt(int x, int y) {
        return grid[x][y];
    }
}
