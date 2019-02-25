package com.strongblackcoffee.mandelbrot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import org.apache.commons.math3.complex.Complex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This Pane's bounds define the pixel coordinates where the upper left corner is (0,0)
 * and the lower right corner is (width, height).  I.e., the more positive the Y coordinate,
 * the closer it is to the bottom of the screen.
 * 
 */
class MandelbrotPane extends JPanel implements ComponentListener, MouseListener, MouseMotionListener {
    static final Logger LOGGER = LogManager.getLogger();
    
    MandelbrotPane(ColorMap colorMap) {
        // setBounds(100,100,800,600);
        LOGGER.info("MandelbrotPane constructor");
        this.addComponentListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        
        this.setBounds(0,0,800,600);
        mandelbrotSet = new MandelbrotSet(colorMap);
        //img = image.updateImg(this.getWidth(), this.getHeight()); 
        this.setVisible(true);
        resetClipping();
    }
    
    MandelbrotSet mandelbrotSet;
    private BufferedImage img;
    boolean resized = false;
    Point clipBoxStart = null;
    Point clipBoxEnd = null;
    Complex clipTopLeft;
    Complex clipBottomRight;
    int maxIterations;
    
    public void resetClipping() {
        clipTopLeft = new Complex(-2.4, 1.4);
        clipBottomRight = new Complex(1.2, -1.4);
        maxIterations = 100;
        resized = true;
        this.repaint();
    }
    
    
    /**
     * Params upperLeft and size are the clipping region in pixel space;
     * Adjust upperLeft and lowerRight to the corresponding region in mandelbrot space.
     */
    public void clipTo(Point upperLeft, Point bottomRight) {
        
        int widthInPixels = this.getWidth();
        int heightInPixels = this.getHeight();
        
        double currentClippingRegionWidth = clipBottomRight.getReal() - clipTopLeft.getReal();
        double deltaX = currentClippingRegionWidth / (widthInPixels - 1);
        double currentClippingRegionHeight = clipTopLeft.getImaginary() - clipBottomRight.getImaginary();
        double deltaY = currentClippingRegionHeight / (heightInPixels - 1);
        
        double newClipLeftX = clipTopLeft.getReal() + upperLeft.x * deltaX;
        double newClipRightX = clipTopLeft.getReal() + bottomRight.x * deltaX;
        
        double newClipTopY = clipBottomRight.getImaginary() + bottomRight.y * deltaY;
        double newClipBottomY = clipBottomRight.getImaginary() + upperLeft.y * deltaY;
        
        Complex newClipTopLeft = new Complex(newClipLeftX, newClipTopY);
        Complex newClipBottomRight = new Complex(newClipRightX, newClipBottomY);
        
        LOGGER.info("clipTo(upperLeft="+upperLeft+",bottomRight="+bottomRight+") deltaX="+ deltaX+", deltaY="+deltaY
                + ", clipTopLeft :: " + clipTopLeft + " => " + newClipTopLeft 
                + ", clipBottomRight :: " + clipBottomRight + " => " + newClipBottomRight);
        
        clipTopLeft = newClipTopLeft;
        clipBottomRight = newClipBottomRight;
        resized = true;
        this.repaint();
    }
    
    @Override
    public void paint(Graphics g) {
        //LOGGER.info("paint");
        if (resized) {
            img = getImage();
            resized = false;
        }
        g.drawImage(img, 0, 0, this);
        if (clipBoxStart != null && clipBoxEnd != null) {
            // LOGGER.info("Drawing rectangle at");
            g.setColor(Color.CYAN);
            g.drawRect(clipBoxStart.x, clipBoxStart.y, clipBoxEnd.x - clipBoxStart.x, clipBoxEnd.y - clipBoxStart.y);
        }
    }
    
    BufferedImage getImage() {
        //if (img == null) 
        img = mandelbrotSet.asBufferedImage(this.getWidth(),this.getHeight(), clipTopLeft, clipBottomRight, maxIterations);
        //img = mandelbrotSet.asBufferedImage(this.getWidth(),this.getHeight(), new Complex(-2.4,-1.4), new Complex(1.2,1.4), 100);
        return img;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        //LOGGER.debug("Resized: currentSize="+this.getBounds()+", newSize="+e.getComponent().getBounds());
        resized = true;
        
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        LOGGER.debug("Moved: " + e);
    }

    @Override
    public void componentShown(ComponentEvent e) {
        LOGGER.debug("Shown: " + e);
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        LOGGER.debug("Hidden: " + e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        LOGGER.debug("mouseClicked: " + e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        clipBoxStart = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // clip image to the rectangle from clipBoxStart to e.getPoint() and redraw;
        this.clipTo(clipBoxStart,clipBoxEnd);
        clipBoxStart = null;
        clipBoxEnd = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // do nothing        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        clipBoxEnd = e.getPoint();
        this.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // do nothing
    }
    
}
