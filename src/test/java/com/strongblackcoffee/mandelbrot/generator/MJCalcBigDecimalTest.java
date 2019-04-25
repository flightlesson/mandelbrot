package com.strongblackcoffee.mandelbrot.generator;

import com.strongblackcoffee.mandelbrot.BigComplex;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class MJCalcBigDecimalTest {
    
    public MJCalcBigDecimalTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of distanceFrom0Squared method, of class MJCalcBigDecimal.
     */
    @Test
    public void testDistanceFrom0Squared() {
        System.out.println("distanceFrom0Squared");
        BigDecimal real = new BigDecimal("1.23");
        BigDecimal imag = new BigDecimal("0.789");
        double expResult = (new BigDecimal("2.135421")).doubleValue();
        double result = MJCalcBigDecimal.distanceFrom0Squared(real, imag);
        assertEquals(expResult,result,0.0);
    }

    /**
     * Test of run method, of class MJCalcBigDecimal.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        MJCalcBigDecimal instance = new MJCalcBigDecimal(1, 1, 
                        new BigDecimal("-0.08900836",new MathContext(60)), 
                        new BigDecimal("-0.962533",new MathContext(60)), 
                        null, null, 0.0000001, 2, 
                        new MJCalc.Callback() {
                            @Override public void setColumns(int row, int[] columns) {
                                System.out.println("row="+row+", columns="+Arrays.toString(columns));
                            }
                        });
        instance.run();
    }
    
}
