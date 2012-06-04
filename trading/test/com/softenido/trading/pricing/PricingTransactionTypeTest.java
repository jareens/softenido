/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.trading.pricing;

import com.softenido.trading.OrderType;
import com.softenido.trading.TransactionType;
import java.math.BigDecimal;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author franci
 */
public class PricingTransactionTypeTest
{
    static final TransactionType BUY = TransactionType.BUY;
    static final OrderType LIMIT = OrderType.LIMIT;
    static final double DELTA = 0.000001;
    
    public PricingTransactionTypeTest()
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
     * Test of getCost method, of class PricingTransactionType.
     */
    @Test
    public void testGetCost()
    {
        PricingBuilder builder = new PricingBuilder("testGetCost", 6, 2);
        Pricing pbuy = builder.flatCost(1);
        Pricing psell = builder.flatCost(2);
        Pricing pshort = builder.flatCost(3);
        Pricing pcover = builder.flatCost(4);
        
        Pricing bssc = builder.transaction(pbuy, psell, pshort, pcover);
        
        assertEquals(1, bssc.getCost(BUY, LIMIT, 6, 10).doubleValue(),DELTA);
        assertEquals(2, bssc.getCost(TransactionType.SELL, LIMIT, 6, 10).doubleValue(),DELTA);
        assertEquals(3, bssc.getCost(TransactionType.SHORT, LIMIT, 6, 10).doubleValue(),DELTA);
        assertEquals(4, bssc.getCost(TransactionType.COVER, LIMIT, 6, 10).doubleValue(),DELTA);        
    }
}
