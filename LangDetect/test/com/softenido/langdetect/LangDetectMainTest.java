/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.langdetect;

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
public class LangDetectMainTest
{
    
    public LangDetectMainTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
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
     * Test of main method, of class LangDetectMain.
     */
    @Test
    public void testMain() throws Exception
    {
        String[] args = {"load",LangDetectMainTest.class.getResource("dictionary.txt.gz").toString()};
        LangDetectMain.main(args);
    }
}
