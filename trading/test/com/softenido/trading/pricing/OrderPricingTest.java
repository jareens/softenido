/*
 * OrderPricingTest.java
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
import static org.junit.Assert.assertEquals;
import org.junit.*;

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
        OrderPricing zero = new OrderPricing("SetFlatCommisionCost");
        Pricing flat = zero.setFlatCommisionCost(4.95);

        assertEquals(0,zero.getCost(TransactionType.BUY, OrderType.STOP_LIMIT, 185.33, 100),0.00);
        assertEquals(4.95,flat.getCost(TransactionType.BUY, OrderType.STOP, 10,1),0.00);
        assertEquals(4.95,flat.getCost(TransactionType.BUY, OrderType.STOP, 10,100),0.00);
        assertEquals(4.95,flat.getCost(TransactionType.BUY, OrderType.STOP, 10,1000),0.00);
        assertEquals(4.95,flat.getCost(TransactionType.BUY, OrderType.STOP, 1,100),0.00);
        assertEquals(4.95,flat.getCost(TransactionType.BUY, OrderType.STOP, 10,100),0.00);
        assertEquals(4.95,flat.getCost(TransactionType.BUY, OrderType.STOP, 100,100),0.00);
        assertEquals(4.95,flat.getCost(TransactionType.SELL, OrderType.STOP, 10,100),0.00);
        assertEquals(4.95,flat.getCost(TransactionType.SHORT, OrderType.STOP, 10,100),0.00);
        assertEquals(4.95, flat.getCost(TransactionType.COVER, OrderType.STOP, 10, 100), 0.00);
    }

    /**
     * Test of setSellAndShortFlatFeeCost method, of class OrderPricing.
     */
    @Test
    public void testSetSellAndShortFlatFeeCost()
    {
        OrderPricing zero = new OrderPricing("SetFlatCommisionCost");
        OrderPricing buy = zero.setFlatCommisionCost(4.95);
        Pricing sell = buy.setFlatCommisionCost(5.95);

        assertEquals(0,zero.getCost(TransactionType.BUY, OrderType.MARKET, 185.33, 100),0.00);
        assertEquals(4.95,buy.getCost(TransactionType.BUY, OrderType.MARKET, 10,10),0.00);
        assertEquals(4.95,buy.getCost(TransactionType.COVER, OrderType.MARKET, 10,100),0.00);
        assertEquals(5.95,sell.getCost(TransactionType.SELL, OrderType.MARKET, 10,100),0.00);
        assertEquals(5.95, sell.getCost(TransactionType.SHORT, OrderType.MARKET, 10, 100), 0.00);
    }
}
