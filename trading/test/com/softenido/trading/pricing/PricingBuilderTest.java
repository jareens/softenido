/*
 * PricingBuilderTest.java
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
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import static org.junit.Assert.assertEquals;
import org.junit.*;

/**
 *
 * @author franci
 */
public class PricingBuilderTest
{
    TransactionType BUY = TransactionType.BUY;
    OrderType LIMIT = OrderType.LIMIT;
    
    final static BigDecimal _0_00  = BigDecimal.ZERO;
    final static BigDecimal _1_10  = BigDecimal.valueOf(1.10).setScale(2);
    final static BigDecimal _2_25  = BigDecimal.valueOf(2.25).setScale(2);
    final static BigDecimal _2_59  = BigDecimal.valueOf(2.59).setScale(2);
    final static BigDecimal _4_50  = BigDecimal.valueOf(4.50).setScale(2);
    final static BigDecimal _5_37  = BigDecimal.valueOf(5.37).setScale(2);
    final static BigDecimal _10_60 = BigDecimal.valueOf(10.60).setScale(2);
    final static BigDecimal _12_80 = BigDecimal.valueOf(12.80).setScale(2);
    final static BigDecimal _13_40 = BigDecimal.valueOf(13.40).setScale(2);
    
    final static BigDecimal _7_00 = BigDecimal.valueOf(7.00).setScale(2);
    final static BigDecimal _4_95 = BigDecimal.valueOf(4.95).setScale(2);
    final static BigDecimal _3_00 = BigDecimal.valueOf(3.00).setScale(2);
    final static BigDecimal _2_95 = BigDecimal.valueOf(2.95).setScale(2);
    final static BigDecimal _1_00 = BigDecimal.valueOf(1.00).setScale(2);
        
    final static double[] MC_M = new double[]{0,    300.01,      3000.01,     35000.01,    75000.01,    140000.01};
    final static double[] MC_F = new double[]{1.10, 2.45,        4.65,        6.40,        9.20,        13.40};
    final static double[] MC_R = new double[]{0.0,  0.024/100.0, 0.012/100.0, 0.007/100.0, 0.003/100.0, 0.0 };
    
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
     * Test of zeroCost method, of class PricingBuilder.
     */
    @Test
    public void testZeroCost()
    {
        PricingBuilder builder = new PricingBuilder("testZeroCost", 6, 2);
        
        assertEquals(_0_00, builder.zeroCost().getCost(TransactionType.BUY, OrderType.LIMIT, 1.0, 1));
        assertEquals(_0_00, builder.zeroCost().getCost(TransactionType.SELL, OrderType.STOP, 1.0, 1));
        assertEquals(_0_00, builder.zeroCost().getCost(TransactionType.SHORT, OrderType.STOP_LIMIT, 1.0, 1));
        assertEquals(_0_00, builder.zeroCost().getCost(TransactionType.COVER, OrderType.MARKET, 1.0, 1));
    }

    /**
     * Test of flatCost method, of class PricingBuilder.
     */
    @Test
    public void testFlatCost()
    {
        PricingBuilder builder = new PricingBuilder("testFlatCost", 6, 2);
        assertEquals(_4_95, builder.flatCost(4.95).getCost(BUY, OrderType.LIMIT, 1.0, 1));
        assertEquals(_3_00, builder.flatCost(3.00).getCost(BUY, OrderType.STOP, 2.0, 3));
        assertEquals(_2_95, builder.flatCost(2.95).getCost(BUY, OrderType.STOP_LIMIT, 4.0, 5));
        assertEquals(_1_00, builder.flatCost(1.00).getCost(BUY, OrderType.MARKET, 6.0, 7));
    }

    /**
     * Test of shareCost method, of class PricingBuilder.
     */
    @Test
    public void testShareCost_3args()
    {
        PricingBuilder builder = new PricingBuilder("testShareCost_3args", 6, 2);
        // LightSpeed
        final int[] shares = new int[] {0};
        final double[] flat = new double[] {0};
        final double[] price = new double[] {0.0045};
        Pricing lightSpeed = builder.shareCost(shares, flat, price, 1.00, 4.50);
        assertEquals(_1_00, lightSpeed.getCost(BUY, LIMIT, 5, 50));
        assertEquals(_2_25, lightSpeed.getCost(BUY, LIMIT, 5, 500));
        assertEquals(_4_50, lightSpeed.getCost(BUY, LIMIT, 5, 5000));
    }

    /**
     * Test of shareCost method, of class PricingBuilder.
     */
    @Test
    public void testRatioCost()
    {
        PricingBuilder builder = new PricingBuilder("testRatioCost", 6, 2);
        Pricing pricing = builder.ratioCost(MC_M,MC_F,MC_R,0, Double.MAX_VALUE);
        
        assertEquals(_1_10, pricing.getCost(BUY, LIMIT, 6, 10));
        assertEquals(_2_59, pricing.getCost(BUY, LIMIT, 6, 100));
        assertEquals(_5_37, pricing.getCost(BUY, LIMIT, 6, 1000));
        assertEquals(_10_60, pricing.getCost(BUY, LIMIT, 6, 10000));
        assertEquals(_12_80, pricing.getCost(BUY, LIMIT, 1.2, 100000));
        assertEquals(_13_40, pricing.getCost(BUY, LIMIT, 6,   100000));
    }

}
