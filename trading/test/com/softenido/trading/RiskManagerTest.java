/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.trading;

import com.softenido.trading.pricing.Pricing;
import com.softenido.trading.pricing.PricingBuilder;
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
public class RiskManagerTest
{
    
    public RiskManagerTest()
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
     * Test of buildRisk method, of class RiskManager.
     */
    @Test
    public void testBuildRisk()
    {
        Pricing pricing = new PricingBuilder("zecco").setFlatCommisionCost(4.95).setSellAndShortFlatFeeCost(0.04).build();
        Account account = new Account("zecco", 100000, 2000, 6000, pricing, 50000);
        Trade trade = new Trade("IBM", 180.00, 175.00, 190.00, 1.);
        RiskManager manager = new RiskManager(account);

        Trade result1 = manager.buildRisk(trade, 1000);
        assertEquals(165, result1.shares);
        assertEquals(2.0, result1.ratio,0.0000005);
        assertEquals(999.94, result1.risk ,0.0000005);

        Trade result2 = manager.buildRisk(trade, 2000);
        assertEquals(277, result2.shares);
        assertEquals(2.0, result2.ratio,0.0000005);
        assertEquals(1671.94, result2.risk ,0.0000005);
        
    }
}
