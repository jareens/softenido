/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.trading.pricing;

import com.softenido.trading.OrderType;
import com.softenido.trading.TransactionType;
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
public class NullPricingTest
{
    
    public NullPricingTest()
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
     * Test of getCost method, of class NullPricing.
     */
    @Test
    public void testGetCost()
    {
        System.out.println("getCost");
        Pricing instance = new NullPricing();
        assertEquals(0, instance.getCost(TransactionType.BUY, OrderType.LIMIT, 0, 0), 0.0);
        assertEquals(0, instance.getCost(TransactionType.SELL, OrderType.MARKET, 0, 0), 0.0);
        assertEquals(0, instance.getCost(TransactionType.SHORT, OrderType.STOP, 0, 0), 0.0);
        assertEquals(0, instance.getCost(TransactionType.COVER, OrderType.STOP_LIMIT, 0, 0), 0.0);
    }
}
