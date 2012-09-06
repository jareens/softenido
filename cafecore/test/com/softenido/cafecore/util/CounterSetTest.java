/*
 * CounterSetTest.java
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

import java.util.Collection;
import java.util.Iterator;
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
public class CounterSetTest
{
    
    public CounterSetTest()
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
     * Test of count method, of class CounterSet.
     */
    @Test
    public void testCount()
    {
        CounterSet<String> set = new CounterSet<String>();
        for(int i=0;i<10;i++)
            for(int j=0;j<i;j++)
                set.add(""+i);

        for(int i=0;i<10;i++)
            assertEquals(i, set.count(""+i));
    }

    /**
     * Test of size method, of class CounterSet.
     */
    @Test
    public void testSize()
    {
        CounterSet<String> set = new CounterSet<String>();
        for(int i=0;i<10;i++)
        {
            set.add(""+i);
        }        
        assertEquals(10, set.size());
    }

    /**
     * Test of isEmpty method, of class CounterSet.
     */
    @Test
    public void testIsEmpty()
    {
        CounterSet<String> set = new CounterSet<String>();
        assertTrue(set.isEmpty());
        set.add("1");
        assertFalse(set.isEmpty());
    }

    /**
     * Test of contains method, of class CounterSet.
     */
    @Test
    public void testContains()
    {
        CounterSet<String> set = new CounterSet<String>();
        set.add("1");
        assertTrue(set.contains("1"));
        assertFalse(set.contains("2"));
    }

}
