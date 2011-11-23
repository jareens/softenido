/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.trading.pricing;

import com.softenido.trading.TransactionType;
import com.softenido.trading.OrderType;
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
public class PricingBuilderTest
{
    static double DELTA = 0.000001;
    public PricingBuilderTest()
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
     * Test of setFlatCommisionCost method, of class PricingBuilder.
     */
    @Test
    public void testSetFlatCommisionCost()
    {
        Pricing zero = new PricingBuilder("flat.zero").build();
        Pricing four = new PricingBuilder("flat.four").setFlatCommisionCost(4.00).build();

        assertEquals(0,zero.getCost(TransactionType.BUY, OrderType.STOP_LIMIT, 185.33, 100),DELTA);
        assertEquals(0,zero.getCost(TransactionType.SELL, OrderType.STOP, 184.55, 100),DELTA);
        assertEquals(0,zero.getCost(TransactionType.SHORT, OrderType.STOP_LIMIT, 185.33, 100),DELTA);
        assertEquals(0,zero.getCost(TransactionType.COVER, OrderType.STOP, 184.55, 100),DELTA);
        
        assertEquals(4.0,four.getCost(TransactionType.BUY, OrderType.STOP_LIMIT, 185.33, 100),DELTA);
        assertEquals(4.0,four.getCost(TransactionType.SELL, OrderType.STOP, 184.55, 100),DELTA);
        assertEquals(4.0,four.getCost(TransactionType.SHORT, OrderType.STOP_LIMIT, 185.33, 100),DELTA);
        assertEquals(4.0,four.getCost(TransactionType.COVER, OrderType.STOP, 184.55, 100),DELTA);
    }

    /**
     * Test of setSellAndShortFlatFeeCost method, of class PricingBuilder.
     */
    @Test
    public void testSetSellAndShortFlatFeeCost()
    {
        PricingBuilder builder     = new PricingBuilder("");
        
        Pricing zero = builder.build();
        Pricing four = builder.setSellAndShortFlatFeeCost(0.04).build();

        assertEquals(0,zero.getCost(TransactionType.BUY, OrderType.STOP_LIMIT, 185.33, 100),DELTA);
        assertEquals(0,zero.getCost(TransactionType.SELL, OrderType.STOP, 184.55, 100),DELTA);
        assertEquals(0,zero.getCost(TransactionType.SHORT, OrderType.STOP_LIMIT, 185.33, 100),DELTA);
        assertEquals(0,zero.getCost(TransactionType.COVER, OrderType.STOP, 184.55, 100),DELTA);
        
        assertEquals(0,four.getCost(TransactionType.BUY, OrderType.STOP_LIMIT, 185.33, 100),DELTA);
        assertEquals(0.04,four.getCost(TransactionType.SELL, OrderType.STOP, 184.55, 100),DELTA);
        assertEquals(0.04,four.getCost(TransactionType.SHORT, OrderType.STOP_LIMIT, 185.33, 100),DELTA);
        assertEquals(0,four.getCost(TransactionType.COVER, OrderType.STOP, 184.55, 100),DELTA);
    }

    /**
     * Test of build method, of class PricingBuilder.
     */
    @Test
    public void testBuild()
    {
        Pricing zecco     = new PricingBuilder("zecco").setFlatCommisionCost(4.95).setSellAndShortFlatFeeCost(0.04).build();
        Pricing sogotrade = new PricingBuilder("sogotrade").setFlatCommisionCost(3.00).setSellAndShortFlatFeeCost(0.04).build();

        assertEquals(4.95,zecco.getCost(TransactionType.BUY, OrderType.STOP_LIMIT, 185.33, 100),DELTA);
        assertEquals(4.99,zecco.getCost(TransactionType.SELL, OrderType.STOP, 184.55, 100),DELTA);
        assertEquals(4.99,zecco.getCost(TransactionType.SHORT, OrderType.STOP_LIMIT, 185.33, 100),DELTA);
        assertEquals(4.95,zecco.getCost(TransactionType.COVER, OrderType.STOP, 184.55, 100),DELTA);
        
        assertEquals(3.00,sogotrade.getCost(TransactionType.BUY, OrderType.STOP_LIMIT, 185.33, 100),DELTA);
        assertEquals(3.04,sogotrade.getCost(TransactionType.SELL, OrderType.STOP, 184.55, 100),DELTA);
        assertEquals(3.04,sogotrade.getCost(TransactionType.SHORT, OrderType.STOP_LIMIT, 185.33, 100),DELTA);
        assertEquals(3.00,sogotrade.getCost(TransactionType.COVER, OrderType.STOP, 184.55, 100),DELTA);
    }
}
