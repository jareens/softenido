/*
 * GutenbergTest.java
 *
 * Copyright (c) 2012 Francisco Gómez Carrasco
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
