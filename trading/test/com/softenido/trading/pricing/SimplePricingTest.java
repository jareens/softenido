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
public class SimplePricingTest
{
    
    public SimplePricingTest()
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
     * Test of getCost method, of class SimplePricing.
     */
    @Test
    public void testGetCost()
    {
        System.out.println("getCost");
        TransactionType tt = null;
        OrderType ot = null;
        double price = 0.0;
        int shares = 0;
        SimplePricing instance = new SimplePricing("");
        double expResult = 0.0;
        double result = instance.getCost(tt, ot, price, shares);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setFlatCost method, of class SimplePricing.
     */
    @Test
    public void testSetFlatCost()
    {
        System.out.println("setFlatCost");
        double val = 0.0;
        SimplePricing instance = new SimplePricing("");
        SimplePricing expResult = null;
        SimplePricing result = instance.setFlatCost(val);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setRatioCost method, of class SimplePricing.
     */
    @Test
    public void testSetRatioCost()
    {
        System.out.println("setRatioCost");
        double val = 0.0;
        SimplePricing instance = new SimplePricing("");
        SimplePricing expResult = null;
        SimplePricing result = instance.setRatioCost(val);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setShareCost method, of class SimplePricing.
     */
    @Test
    public void testSetShareCost()
    {
        System.out.println("setShareCost");
        double val = 0.0;
        SimplePricing instance = new SimplePricing("");
        SimplePricing expResult = null;
        SimplePricing result = instance.setShareCost(val);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setMinCost method, of class SimplePricing.
     */
    @Test
    public void testSetMinCost()
    {
        System.out.println("setMinCost");
        double val = 0.0;
        SimplePricing instance = new SimplePricing("testSetMinCost");
        SimplePricing expResult = null;
        SimplePricing result = instance.setMinCost(val);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setMaxCost method, of class SimplePricing.
     */
    @Test
    public void testSetMaxCost()
    {
        System.out.println("setMaxCost");
        double val = 0.0;
        SimplePricing instance = new SimplePricing("");
        SimplePricing expResult = null;
        SimplePricing result = instance.setMaxCost(val);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
