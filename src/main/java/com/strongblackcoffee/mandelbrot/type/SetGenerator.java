package com.strongblackcoffee.mandelbrot.type;

import org.apache.logging.log4j.LogManager;

/**
 * 
 */
public class SetGenerator {
    static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();
    
    public static <C extends MJComplex> MJSet generateMJSet(int viewWidth, int viewHeight, C windowPointAtZeroZero, 
            Class<? extends MJDouble> mjDoubleCls, double windowPointDelta, int maxIterations) {
        //MJSet = new MJSet(int viewWidth, int viewHeight, Complex windowPointAtZeroZero, double windowPointDelta)
        MJSet mjSet= new MJSet<C>(viewWidth, viewHeight, windowPointAtZeroZero, windowPointDelta);
        MJDouble d = null;
        try {
            d = mjDoubleCls.newInstance().set(windowPointDelta);
        } catch (InstantiationException | IllegalAccessException ex) {
            LOGGER.fatal("newInstance", ex);
            System.exit(1);
        }
        
        MJDouble wy = windowPointAtZeroZero.getImag();
        for (int y=0; y < viewHeight; ++y) {
            MJDouble wx = windowPointAtZeroZero.getReal();
            for (int x=0; x < viewWidth; ++x) {
                MJComplex c = null;
                try {
                    c = windowPointAtZeroZero.getClass().getConstructor(mjDoubleCls,mjDoubleCls).newInstance(mjDoubleCls.cast(wx),mjDoubleCls.cast(wy));
                } catch (Exception ex) {
                    LOGGER.fatal("newInstance", ex);
                    System.exit(1);
                }
                mjSet.setDistanceFromSet(x, y, computeDistanceFromSet(c,c,maxIterations));
                wx = wx.add(d);
            }
            wy.add(d);
        }
        return mjSet;
    }
    
    /**
     * Calculates z(i+1) = z(i)^2 + c until z's magnitude is greater than 2 or maxIterations
     * have been performed.  Returns the number of iterations required for z to become
     * greater than 2 or returns 0 if z never became greater than 2.
     */
    public static int computeDistanceFromSet(MJComplex z0, MJComplex c, int maxIterations) {
        MJComplex z = z0;
        for (int i=0; i < maxIterations; ++i) {
            z = z.nextz(c);
            if (hasEscaped(z)) return i+1;
        }
        return 0;
    }
    
    /**
     * Once a candidate's distance from 0 is larger than 2, that candidate has escaped
     */
    static boolean hasEscaped(MJComplex c) {
        double distance_from_0_squared = c.magnitudeSquared();
        if (distance_from_0_squared > 4.0) {
          return false;  
        }
        return true;
    }
    
}
