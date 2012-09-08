/*
 *  OSNameTest.java
 *
 *  Copyright (C) 2012 Francisco Gómez Carrasco
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  Report bugs or new features to: flikxxi@gmail.com
 *
 */
package com.softenido.cafecore.os;

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
public class OSNameTest
{
    
    public OSNameTest()
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
     * Test of getName method, of class OSName.
     */
    @Test
    public void testGetName()
    {
        OSName os = new OSName("name","version","arch");
        assertEquals("name", os.getName());
    }

    /**
     * Test of getVersion method, of class OSName.
     */
    @Test
    public void testGetVersion()
    {
        OSName os = new OSName("name","version","arch");
        assertEquals("version", os.getVersion());
    }

    /**
     * Test of getArch method, of class OSName.
     */
    @Test
    public void testGetArch()
    {
        OSName os = new OSName("name","version","arch");
        assertEquals("arch", os.getArch());
    }

    /**
     * Test of toString method, of class OSName.
     */
    @Test
    public void testToString()
    {
     
        OSName name = new OSName("name");
        assertEquals("name", name.toString());
        
        OSName name_version = new OSName("name","version");
        assertEquals("name version", name_version.toString());

        OSName name_version_arch = new OSName("name","version","arch");
        assertEquals("name version arch", name_version_arch.toString());
    }
}
