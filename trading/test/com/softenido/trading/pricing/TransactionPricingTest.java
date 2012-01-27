/*
 * TransactionPricingTest.java
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
        SimplePricing buy = new SimplePricing("Buy", 1, 0.01, 0.0001, 0, Double.MAX_VALUE);
        SimplePricing sell = new SimplePricing("Sell", 2, 0.02, 0.0002, 0, Double.MAX_VALUE);
        SimplePricing sshort = new SimplePricing("Short", 3, 0.03, 0.0003, 0, Double.MAX_VALUE);
        SimplePricing cover = new SimplePricing("Cover", 4, 0.04, 0.0004, 0, Double.MAX_VALUE);
        
        TransactionPricing sp = new TransactionPricing("GetCost", buy, sell, sshort, cover);
        
        assertEquals(1.2020, sp.getCost(TransactionType.BUY, OrderType.STOP, 1, 20), 0.0);
        assertEquals(2.4040, sp.getCost(TransactionType.SELL, OrderType.STOP, 1, 20), 0.0);
        assertEquals(3.6060, sp.getCost(TransactionType.SHORT, OrderType.STOP, 1, 20), 0.0);
        assertEquals(4.8080, sp.getCost(TransactionType.COVER, OrderType.STOP, 1, 20), 0.0);
    }

    /**
     * Test of setFlatCost method, of class TransactionPricing.
     */
    @Test
    public void testSetFlatCost()
    {
        SimplePricing buy = new SimplePricing("Buy", 1, 0.01, 0.0001, 0, Double.MAX_VALUE);
        SimplePricing sell = new SimplePricing("Sell", 2, 0.02, 0.0002, 0, Double.MAX_VALUE);
        SimplePricing sshort = new SimplePricing("Short", 3, 0.03, 0.0003, 0, Double.MAX_VALUE);
        SimplePricing cover = new SimplePricing("Cover", 4, 0.04, 0.0004, 0, Double.MAX_VALUE);
        
        TransactionPricing sp = new TransactionPricing("SetFlatCost", buy, sell, sshort, cover).setFlatCost(5);
        
        assertEquals(5.2020, sp.getCost(TransactionType.BUY, OrderType.STOP, 1, 20), 0.0);
        assertEquals(5.4040, sp.getCost(TransactionType.SELL, OrderType.STOP, 1, 20), 0.0);
        assertEquals(5.6060, sp.getCost(TransactionType.SHORT, OrderType.STOP, 1, 20), 0.0);
        assertEquals(5.8080, sp.getCost(TransactionType.COVER, OrderType.STOP, 1, 20), 0.0);
    }

    /**
     * Test of setSellFlatCost method, of class TransactionPricing.
     */
    @Test
    public void testSetSellFlatCost()
    {
        SimplePricing buy = new SimplePricing("Buy", 1, 0.01, 0.0001, 0, Double.MAX_VALUE);
        SimplePricing sell = new SimplePricing("Sell", 2, 0.02, 0.0002, 0, Double.MAX_VALUE);
        SimplePricing sshort = new SimplePricing("Short", 3, 0.03, 0.0003, 0, Double.MAX_VALUE);
        SimplePricing cover = new SimplePricing("Cover", 4, 0.04, 0.0004, 0, Double.MAX_VALUE);
        
        TransactionPricing sp = new TransactionPricing("GetCost", buy, sell, sshort, cover).setSellFlatCost(5);
        
        assertEquals(1.2020, sp.getCost(TransactionType.BUY, OrderType.STOP, 1, 20), 0.0);
        assertEquals(5.4040, sp.getCost(TransactionType.SELL, OrderType.STOP, 1, 20), 0.0);
        assertEquals(3.6060, sp.getCost(TransactionType.SHORT, OrderType.STOP, 1, 20), 0.0);
        assertEquals(4.8080, sp.getCost(TransactionType.COVER, OrderType.STOP, 1, 20), 0.0);
    }

    /**
     * Test of setSellAndShortFlatCost method, of class TransactionPricing.
     */
    @Test
    public void testSetSellAndShortFlatCost()
    {
        SimplePricing buy = new SimplePricing("Buy", 1, 0.01, 0.0001, 0, Double.MAX_VALUE);
        SimplePricing sell = new SimplePricing("Sell", 2, 0.02, 0.0002, 0, Double.MAX_VALUE);
        SimplePricing sshort = new SimplePricing("Short", 3, 0.03, 0.0003, 0, Double.MAX_VALUE);
        SimplePricing cover = new SimplePricing("Cover", 4, 0.04, 0.0004, 0, Double.MAX_VALUE);
        
        TransactionPricing sp = new TransactionPricing("GetCost", buy, sell, sshort, cover).setSellAndShortFlatCost(5);
        
        assertEquals(1.2020, sp.getCost(TransactionType.BUY, OrderType.STOP, 1, 20), 0.0);
        assertEquals(5.4040, sp.getCost(TransactionType.SELL, OrderType.STOP, 1, 20), 0.0);
        assertEquals(5.6060, sp.getCost(TransactionType.SHORT, OrderType.STOP, 1, 20), 0.0);
        assertEquals(4.8080, sp.getCost(TransactionType.COVER, OrderType.STOP, 1, 20), 0.0);
    }
}
