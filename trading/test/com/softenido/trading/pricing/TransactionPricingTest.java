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
public class TransactionPricingTest
{
    
    public TransactionPricingTest()
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
     * Test of getCost method, of class TransactionPricing.
     */
    @Test
    public void testGetCost()
    {
        System.out.println("getCost");
        TransactionType tt = null;
        OrderType ot = null;
        double price = 0.0;
        int shares = 0;
        TransactionPricing instance = new TransactionPricing("");
        double expResult = 0.0;
        double result = instance.getCost(tt, ot, price, shares);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setFlatCost method, of class TransactionPricing.
     */
    @Test
    public void testSetFlatCost()
    {
        System.out.println("setFlatCost");
        double val = 0.0;
        TransactionPricing instance = new TransactionPricing("");
        TransactionPricing expResult = null;
        TransactionPricing result = instance.setFlatCost(val);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setSellFlatCost method, of class TransactionPricing.
     */
    @Test
    public void testSetSellFlatCost()
    {
        System.out.println("setSellFlatCost");
        double val = 0.0;
        TransactionPricing instance = new TransactionPricing("");
        TransactionPricing expResult = null;
        TransactionPricing result = instance.setSellFlatCost(val);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setSellAndShortFlatCost method, of class TransactionPricing.
     */
    @Test
    public void testSetSellAndShortFlatCost()
    {
        System.out.println("setSellAndShortFlatCost");
        double val = 0.0;
        TransactionPricing instance = new TransactionPricing("");
        TransactionPricing expResult = null;
        TransactionPricing result = instance.setSellAndShortFlatCost(val);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
