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
public class OrderPricingTest
{
    
    public OrderPricingTest()
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
     * Test of getCost method, of class OrderPricing.
     */
    @Test
    public void testGetCost()
    {
        Pricing zecco = new OrderPricing("zecco").setFlatCommisionCost(4.95).setSellAndShortFlatFeeCost(0.04);
        Pricing sogotrade = new OrderPricing("sogotrade").setFlatCommisionCost(3.00).setSellAndShortFlatFeeCost(0.04);

        assertEquals(4.95,zecco.getCost(TransactionType.BUY, OrderType.STOP_LIMIT, 185.33, 100),0.00);
        assertEquals(4.99,zecco.getCost(TransactionType.SELL, OrderType.STOP, 184.55, 100),0.00);
        
        assertEquals(3.00,sogotrade.getCost(TransactionType.BUY, OrderType.STOP_LIMIT, 185.33, 100),0.00);
        assertEquals(3.04,sogotrade.getCost(TransactionType.SELL, OrderType.STOP, 184.55, 100),0.00);
    }

    /**
     * Test of setFlatCommisionCost method, of class OrderPricing.
     */
    @Test
    public void testSetFlatCommisionCost()
    {
        System.out.println("setFlatCommisionCost");
        double val = 0.0;
        OrderPricing instance = new OrderPricing("flatComision");
        OrderPricing expResult = null;
        OrderPricing result = instance.setFlatCommisionCost(val);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setSellAndShortFlatFeeCost method, of class OrderPricing.
     */
    @Test
    public void testSetSellAndShortFlatFeeCost()
    {
        System.out.println("setSellAndShortFlatFeeCost");
        double val = 0.0;
        OrderPricing instance = new OrderPricing("fee");
        OrderPricing expResult = null;
        OrderPricing result = instance.setSellAndShortFlatFeeCost(val);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
