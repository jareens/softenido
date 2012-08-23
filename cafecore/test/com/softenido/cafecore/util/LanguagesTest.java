/*
 * LanguagesTest.java
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
package com.softenido.cafecore.util;

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
public class LanguagesTest
{
    
    public LanguagesTest()
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
     * Test of iso2 method, of class Languages.
     */
    @Test
    public void testIso2()
    {
        assertNull(Languages.iso2(null));
        assertNull(Languages.iso2("garbaje not a language"));
        
        assertEquals("es", Languages.iso2("es"));
        assertEquals("es", Languages.iso2("spa"));
        assertEquals("es", Languages.iso2("spanish"));
        assertEquals("es", Languages.iso2("Spanish"));

        assertEquals("bo", Languages.iso2("bo"));
        assertEquals("bo", Languages.iso2("tib"));
        assertEquals("bo", Languages.iso2("bod"));
        assertEquals("bo", Languages.iso2("Tibetan"));
        assertEquals("bo", Languages.iso2("tibetan"));
    }

    /**
     * Test of iso3 method, of class Languages.
     */
    @Test
    public void testIso3()
    {
        assertNull(Languages.iso3(null));
        assertNull(Languages.iso3("garbaje not a language"));
        
        assertEquals("spa", Languages.iso3("es"));
        assertEquals("spa", Languages.iso3("spa"));
        assertEquals("spa", Languages.iso3("spanish"));
        assertEquals("spa", Languages.iso3("Spanish"));

        assertEquals("bod", Languages.iso3("bo"));
        assertEquals("bod", Languages.iso3("tib"));
        assertEquals("bod", Languages.iso3("bod"));
        assertEquals("bod", Languages.iso3("Tibetan"));
        assertEquals("bod", Languages.iso3("tibetan"));
    }

    /**
     * Test of alias method, of class Languages.
     */
    @Test
    public void testAlias()
    {
        assertNull(Languages.alias(null));
        assertNull(Languages.alias("garbaje not a language"));
        
        assertEquals("spa", Languages.alias("es"));
        assertEquals("spa", Languages.alias("spa"));
        assertEquals("spa", Languages.alias("spanish"));
        assertEquals("spa", Languages.alias("Spanish"));

        assertEquals("tib", Languages.alias("bo"));
        assertEquals("tib", Languages.alias("tib"));
        assertEquals("tib", Languages.alias("bod"));
        assertEquals("tib", Languages.alias("Tibetan"));
        assertEquals("tib", Languages.alias("tibetan"));
    }

    /**
     * Test of name method, of class Languages.
     */
    @Test
    public void testName()
    {
        assertNull(Languages.name(null));
        assertNull(Languages.name("garbaje not a language"));
        
        assertEquals("Spanish", Languages.name("es"));
        assertEquals("Spanish", Languages.name("spa"));
        assertEquals("Spanish", Languages.name("spanish"));
        assertEquals("Spanish", Languages.name("Spanish"));

        assertEquals("Tibetan", Languages.name("bo"));
        assertEquals("Tibetan", Languages.name("tib"));
        assertEquals("Tibetan", Languages.name("bod"));
        assertEquals("Tibetan", Languages.name("Tibetan"));
        assertEquals("Tibetan", Languages.name("tibetan"));
    }
}
