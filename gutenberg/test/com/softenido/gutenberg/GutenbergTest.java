/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.gutenberg;

import java.io.IOException;
import java.io.InputStream;
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
public class GutenbergTest
{
    
    public GutenbergTest()
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
     * Test of getISO3Languages method, of class Gutenberg.
     */
    @Test
    public void testGetISOLanguages()
    {
        String[] iso3 = Gutenberg.getISO3Languages();
        assertNotNull(iso3);

        for(int i=0;i<iso3.length;i++)
        {
            String item = iso3[i];
            assertNotNull(item);
            assertEquals(3, item.length());
        }
    }

    /**
     * Test of getLanguageData method, of class Gutenberg.
     */
    @Test
    public void testGetLanguageData() throws IOException
    {
        String[] iso3 = Gutenberg.getISO3Languages();
        for(int i=0;i<iso3.length;i++)
        {
            InputStream data = Gutenberg.getLanguageDataStream(iso3[i]);
            assertNotNull(data);
            data.close();
        }
        assertNull(Gutenberg.getLanguageDataStream(null));
        assertNull(Gutenberg.getLanguageDataStream(""));
        assertNull(Gutenberg.getLanguageDataStream("a file that not exists"));
    }
}
