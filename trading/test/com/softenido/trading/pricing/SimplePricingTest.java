/*
 * SimplePricingTest.java
 *
 * Copyright (c) 2012  Francisco Gómez Carrasco
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Report bugs or new features to: flikxxi@gmail.com
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.trading.pricing;

import com.softenido.trading.OrderType;
import com.softenido.trading.TransactionType;
import static org.junit.Assert.*;
import org.junit.*;

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
        SimplePricing sp = new SimplePricing("GetCost", 10, 0.05, 0.0005, 20, 30);
        assertEquals(20, sp.getCost(TransactionType.BUY, OrderType.STOP, 10, 10), 0.0);
        assertEquals(30, sp.getCost(TransactionType.BUY, OrderType.STOP, 10, 100), 0.0);
        assertEquals(20.0100, sp.getCost(TransactionType.BUY, OrderType.STOP, 10, 20), 0.0);
        assertEquals(25.0150, sp.getCost(TransactionType.BUY, OrderType.STOP, 10, 30), 0.0);
    }

    /**
     * Test of setFlatCost method, of class SimplePricing.
     */
    @Test
    public void testSetFlatCost()
    {
        SimplePricing sp = new SimplePricing("SetFlatCost").setFlatCost(4.95);
        assertEquals(4.95, sp.getCost(TransactionType.BUY, OrderType.STOP, 10, 10), 0.0);
        assertEquals(4.95, sp.getCost(TransactionType.BUY, OrderType.STOP, 10, 100), 0.0);
        assertEquals(4.95, sp.getCost(TransactionType.BUY, OrderType.STOP, 10, 20), 0.0);
        assertEquals(4.95, sp.getCost(TransactionType.BUY, OrderType.STOP, 10, 30), 0.0);
    }

    /**
     * Test of setRatioCost method, of class SimplePricing.
     */
    @Test
    public void testSetRatioCost()
    {
        SimplePricing sp = new SimplePricing("SetRatioCost").setRatioCost(0.01);
        assertEquals(1.00, sp.getCost(TransactionType.BUY, OrderType.STOP, 10, 10), 0.0);
        assertEquals(10.00, sp.getCost(TransactionType.BUY, OrderType.STOP, 10, 100), 0.0);
        assertEquals(2.00, sp.getCost(TransactionType.BUY, OrderType.STOP, 10, 20), 0.0);
        assertEquals(3.00, sp.getCost(TransactionType.BUY, OrderType.STOP, 10, 30), 0.0);
    }

    /**
     * Test of setShareCost method, of class SimplePricing.
     */
    @Test
    public void testSetShareCost()
    {
        SimplePricing sp = new SimplePricing("SetShareCost").setShareCost(0.01);
        assertEquals(0.10, sp.getCost(TransactionType.BUY, OrderType.STOP, 10, 10), 0.0);
        assertEquals(1.00, sp.getCost(TransactionType.BUY, OrderType.STOP, 10, 100), 0.0);
        assertEquals(0.20, sp.getCost(TransactionType.BUY, OrderType.STOP, 10, 20), 0.0);
        assertEquals(0.30, sp.getCost(TransactionType.BUY, OrderType.STOP, 10, 30), 0.0);
    }

    /**
     * Test of setMinCost method, of class SimplePricing.
     */
    @Test
    public void testSetMinCost()
    {
        SimplePricing sp = new SimplePricing("SetMinCost").setShareCost(0.01).setMinCost(0.25);
        assertEquals(0.25, sp.getCost(TransactionType.BUY, OrderType.STOP, 10, 10), 0.0);
        assertEquals(1.00, sp.getCost(TransactionType.BUY, OrderType.STOP, 10, 100), 0.0);
        assertEquals(0.25, sp.getCost(TransactionType.BUY, OrderType.STOP, 10, 20), 0.0);
        assertEquals(0.30, sp.getCost(TransactionType.BUY, OrderType.STOP, 10, 30), 0.0);
    }

    /**
     * Test of setMaxCost method, of class SimplePricing.
     */
    @Test
    public void testSetMaxCost()
    {
        SimplePricing sp = new SimplePricing("SetShareCost").setShareCost(0.01).setMaxCost(0.25);
        assertEquals(0.10, sp.getCost(TransactionType.BUY, OrderType.STOP, 10, 10), 0.0);
        assertEquals(0.25, sp.getCost(TransactionType.BUY, OrderType.STOP, 10, 100), 0.0);
        assertEquals(0.20, sp.getCost(TransactionType.BUY, OrderType.STOP, 10, 20), 0.0);
        assertEquals(0.25, sp.getCost(TransactionType.BUY, OrderType.STOP, 10, 30), 0.0);
    }
}
