package com.strongblackcoffee.mandelbrot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
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
class MandelbrotPane extends JPanel implements ComponentListener, MouseListener, 
        MouseMotionListener, MandelbrotStatisticsPanel.Callback {
    static final Logger LOGGER = LogManager.getLogger();
    
    MandelbrotPane(ColorProvider colorMap) {
        LOGGER.info("MandelbrotPane constructor");
        this.statisticsPanel = new MandelbrotStatisticsPanel(this);
        this.addComponentListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        
        this.setMinimumSize(new Dimension(600,600));
        mandelbrotSet = new MandelbrotSet(colorMap, statisticsPanel);
        //img = image.updateImg(this.getWidth(), this.getHeight()); 
        this.setVisible(true);
        reset();
        LOGGER.info("MandelbrotPane constructor completed");
    }
    
    private final MandelbrotStatisticsPanel statisticsPanel;
    private MandelbrotSet mandelbrotSet;
    private BufferedImage img;
    int maxIterations;
    Complex centerOfWindow = null;
    double delta;
    double zoomFactor;
    
    
    boolean viewChanged = false;
    
    public void reset() {
        // viewport is pw pixels wide and ph pixels high
        // window is centerpoint and zoom, where  zoom is a number > 1.
        // zoom = 1 means draw X  = -2.4, +2 
        
        setCenterOfWindow(new Complex(-0.2,0.0),false);
        setDelta(0.009,false);
        setMaxIterations(100,false);
        zoomFactor = 0.8;
        viewChanged = true;
        this.repaint();
    }
    
    public void setMaxIterations(int newMaxIterations, boolean repaint) {
        maxIterations = newMaxIterations;
        this.statisticsPanel.setMaxIterations(maxIterations);
        if (repaint) {
            viewChanged = true;
            this.repaint();
        }
    }
    
    public void setCenterOfWindow(Complex center, boolean repaint) {
        this.centerOfWindow = center;
        this.statisticsPanel.setCenter(center);
        if (repaint) {
            viewChanged = true;
            this.repaint();
        }
    }
    
    public void setDelta(double delta, boolean repaint) {
        this.delta = delta;
        this.statisticsPanel.setDelta(delta);
        if (repaint) {
            viewChanged = true;
            this.repaint();
        }
    }
    
    public void zoomIn() {
        this.setDelta(this.delta * this.zoomFactor,true);
    }
    
    public void zoomOut() {
        this.setDelta(this.delta / this.zoomFactor,true);
    }
    
    public MandelbrotStatisticsPanel getStatisticsPane() {
        return this.statisticsPanel;
    }
    
    
//    /**
//     * Params upperLeft and size are the clipping region in pixel space;
//     * Adjust upperLeft and lowerRight to the corresponding region in mandelbrot space.
//     */
//    public void clipTo(Point upperLeft, Point bottomRight) {
//        
//        int widthInPixels = this.getWidth();
//        int heightInPixels = this.getHeight();
//        
//        double currentClippingRegionWidth = clipBottomRight.getReal() - clipTopLeft.getReal();
//        double deltaX = currentClippingRegionWidth / (widthInPixels - 1);
//        double currentClippingRegionHeight = clipTopLeft.getImaginary() - clipBottomRight.getImaginary();
//        double deltaY = currentClippingRegionHeight / (heightInPixels - 1);
//        
//        double newClipLeftX = clipTopLeft.getReal() + upperLeft.x * deltaX;
//        double newClipRightX = clipTopLeft.getReal() + bottomRight.x * deltaX;
//        
//        double newClipTopY = clipBottomRight.getImaginary() + bottomRight.y * deltaY;
//        double newClipBottomY = clipBottomRight.getImaginary() + upperLeft.y * deltaY;
//        
//        Complex newClipTopLeft = new Complex(newClipLeftX, newClipTopY);
//        Complex newClipBottomRight = new Complex(newClipRightX, newClipBottomY);
//        
//        LOGGER.info("clipTo(upperLeft="+upperLeft+",bottomRight="+bottomRight+") deltaX="+ deltaX+", deltaY="+deltaY
//                + ", clipTopLeft :: " + clipTopLeft + " => " + newClipTopLeft 
//                + ", clipBottomRight :: " + clipBottomRight + " => " + newClipBottomRight);
//        
//        clipTopLeft = newClipTopLeft;
//        clipBottomRight = newClipBottomRight;
//        resized = true;
//        this.repaint();
//    }
    
    @Override
    public void paint(Graphics g) {
        //LOGGER.info("paint");
        if (viewChanged) {
            img = getImage();
            viewChanged = false;
        }
        g.drawImage(img, 0, 0, this);
        //if (clipBoxStart != null && clipBoxEnd != null) {
        //    // LOGGER.info("Drawing rectangle at");
        //    g.setColor(Color.CYAN);
        //    g.drawRect(clipBoxStart.x, clipBoxStart.y, clipBoxEnd.x - clipBoxStart.x, clipBoxEnd.y - clipBoxStart.y);
        //}
    }
    
    /**
     * Returns the window (i.e., the Mandelbrot set point) coordinates corresponding to a pixel in the view.
     */
    Complex getWindowCoordinates(Point p) {
        
//        this.getX();
//        this.getY();
//        this.getHeight();
//        this.getWidth();
//        this.centerOfWindow.getReal();
//        this.centerOfWindow.getImaginary();
//        this.delta;
//        
//        int viewCenterX;
//        int viewCenterY;
        
        LOGGER.info("getWindowCoordinates: view's X is ["+this.getX()+","+(this.getX()+getWidth()-1)+"], p.x="+p.x);
        int viewCenterX = getWidth()/2;
        int viewCenterXMovedBy = p.x - viewCenterX;
        double windowCenterX = this.centerOfWindow.getReal() + viewCenterXMovedBy * delta;
        double windowCenterY = this.centerOfWindow.getImaginary() + (p.y - getHeight()/2) * delta;
        return new Complex(windowCenterX,windowCenterY);
        
    }
    
    BufferedImage getImage() {
        //if (img == null) 
        LOGGER.info("getImage()");
        //img = mandelbrotSet.asBufferedImage(this.getWidth(),this.getHeight(), clipTopLeft, clipBottomRight, maxIterations);
        img = mandelbrotSet.asBufferedImage(this.getWidth(),this.getHeight(), this.centerOfWindow, this.delta, this.maxIterations);
        return img;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        LOGGER.debug("Resized: currentSize="+this.getBounds()+", newSize="+e.getComponent().getBounds());
        viewChanged = true;
        //this.statisticsPanel.recordImageSize(e.getComponent().getWidth(),e.getComponent().getHeight());
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        LOGGER.debug("componentMoved: " + e);
    }

    @Override
    public void componentShown(ComponentEvent e) {
        LOGGER.debug("componentShown: " + e);
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        LOGGER.debug("componentHidden: " + e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        LOGGER.debug("mouseClicked: clickCount=" + e.getClickCount() + ", point=" + e.getLocationOnScreen());
        if (e.getClickCount() >= 2) {
            this.setCenterOfWindow(getWindowCoordinates(e.getPoint()),true);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //clipBoxStart = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //// clip image to the rectangle from clipBoxStart to e.getPoint() and redraw;
        //this.clipTo(clipBoxStart,clipBoxEnd);
        //clipBoxStart = null;
        //clipBoxEnd = null;
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
        //clipBoxEnd = e.getPoint();
        //this.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //LOGGER.debug("mouseMoved");
        //this.statisticsPanel.recordCurrentPoint(this.mandelbrotSet.getCell(e.getPoint())); 
    }

    @Override
    public void maxIterationsChanged(int newMaxIterations) {
        this.setMaxIterations(newMaxIterations, true);
    }
}
