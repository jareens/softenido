/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.formulas;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author franci
 */
public class TradingFormulasTest
{

    public TradingFormulasTest()
    {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test of tradeRatio method, of class TradingFormulas.
     */
    @Test
    public void testTradeRatio()
    {
        assertEquals(1, TradingFormulas.tradeRatio(11, 10, 9), 0.001);
        assertEquals(2, TradingFormulas.tradeRatio(12, 10, 9), 0.001);
        assertEquals(3, TradingFormulas.tradeRatio(13, 10, 9), 0.001);
        assertEquals(4, TradingFormulas.tradeRatio(14, 10, 9), 0.001);
        assertEquals(0.5, TradingFormulas.tradeRatio(10.5, 10, 9), 0.001);
        assertEquals(3.61, TradingFormulas.tradeRatio(636.22, 576.51, 559.97), 0.001);

    }

    /**
     * Test of tradeLimit method, of class TradingFormulas.
     */
    @Test
    public void testTradeLimit()
    {
        assertEquals(10, TradingFormulas.tradeLimit(11, 9, 1), 0.001);
        assertEquals(10, TradingFormulas.tradeLimit(12, 9, 2), 0.001);
        assertEquals(9.5, TradingFormulas.tradeLimit(11, 9, 3), 0.001);
        assertEquals(9.4, TradingFormulas.tradeLimit(11, 9, 4), 0.001);
        assertEquals(12, TradingFormulas.tradeLimit(13, 10, 0.5), 0.001);
        assertEquals(585.3867, TradingFormulas.tradeLimit(636.22, 559.97, 2), 0.001);
    }

    /**
     * Test of tradeSize method, of class TradingFormulas.
     */
    @Test
    public void testTradeSize_6args()
    {
        assertEquals(29, TradingFormulas.tradeSize(4000, 40, 10, 9, 10, 0.01), 0.001);
        assertEquals(9, TradingFormulas.tradeSize(4000, 40, 10, 7, 10, 0.01), 0.001);
        assertEquals(37, TradingFormulas.tradeSize(4000, 40, 22.72, 23.51, 10, 0.01), 0.001);
        assertEquals(1, TradingFormulas.tradeSize(4000, 40, 585.38, 559.97, 9.90, 0.02), 0.001);
        assertEquals(1, TradingFormulas.tradeSize(4000, 40, 559.97, 585.38, 9.90, 0.02), 0.001);
    }

    /**
     * Test of tradeSize method, of class TradingFormulas.
     */
    @Test
    public void testTradeSize_4args()
    {
        assertEquals(30, TradingFormulas.tradeSize(30.00, 1, 0, 0), 0.001);
        assertEquals(10, TradingFormulas.tradeSize(30.00, 3, 0, 0), 0.001);
        assertEquals(1, TradingFormulas.tradeSize(30.00, 16.54, 0, 0), 0.001);
        assertEquals(1, TradingFormulas.tradeSize(30.00, 25.41, 0, 0), 0.001);
        assertEquals(2, TradingFormulas.tradeSize(30.00, 10.01, 0, 0), 0.001);
    }

    /**
     * Test of tradeType method, of class TradingFormulas.
     */
    @Test
    public void testTradeType()
    {
        assertEquals(TradingFormulas.BUY, TradingFormulas.tradeType(11, 10, 9));
        assertEquals(TradingFormulas.SHORT, TradingFormulas.tradeType(9, 10, 11));
        assertEquals(TradingFormulas.NULL, TradingFormulas.tradeType(9, 10, 9));
    }

    /**
     * Test of tradeRisk method, of class TradingFormulas.
     */
    @Test
    public void testTradeRisk()
    {
        assertEquals(2, TradingFormulas.tradeRisk(10, 8, 7), 0.0);
        assertEquals(2, TradingFormulas.tradeRisk(7, 9, 10), 0.0);
        assertEquals(1, TradingFormulas.tradeRisk(5, 10, 15), 0.0);
        assertEquals(2, TradingFormulas.tradeRisk(150, 100, 75), 0.0);
        assertEquals(2.15, TradingFormulas.tradeRisk(7.25, 8.11, 8.51), 0.005);
        
    }
}
