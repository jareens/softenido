/*
 * RepeatedMapTest.java
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
import java.util.Map;
import java.util.Set;
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
public class RepeatedMapTest
{
    
    public RepeatedMapTest()
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
     * Test of size method, of class RepeatedMap.
     */
    @Test
    public void testSize()
    {
        RepeatedMap<String,String> map = new RepeatedMap();
        assertEquals(0, map.size());

        map.put("1", "uno");
        assertEquals(1, map.size());
        map.put("1", "one");
        assertEquals(1, map.size());
                
        map.put("2", "dos");
        assertEquals(2, map.size());
        map.put("2", "two");
        assertEquals(2, map.size());
        
        map.clear();
        assertEquals(0, map.size());
    }

    /**
     * Test of isEmpty method, of class RepeatedMap.
     */
    @Test
    public void testIsEmpty()
    {
        RepeatedMap<String,String> map = new RepeatedMap();
        assertTrue(map.isEmpty());

        map.put("1", "uno");
        assertFalse(map.isEmpty());
        
        map.clear();
        assertTrue(map.isEmpty());
    }

    /**
     * Test of containsKey method, of class RepeatedMap.
     */
    @Test
    public void testContainsKey()
    {
        RepeatedMap<String,String> map = new RepeatedMap();
        assertFalse(map.containsKey("1"));

        map.put("1", "uno");
        assertTrue(map.containsKey("1"));
        map.put("1", "one");
        assertTrue(map.containsKey("1"));
        
        assertFalse(map.containsKey("2"));
        
        map.put("2", "dos");
        assertTrue(map.containsKey("2"));
        map.put("2", "two");
        assertTrue(map.containsKey("2"));

        map.clear();
        assertFalse(map.containsKey("1"));
    }

    /**
     * Test of containsValue method, of class RepeatedMap.
     */
    @Test
    public void testContainsValue()
    {
        RepeatedMap<String,String> map = new RepeatedMap();
        assertFalse(map.containsValue("uno"));

        map.put("1", "uno");
        assertTrue(map.containsValue("uno"));
        map.put("1", "one");
        assertTrue(map.containsValue("one"));
        
        assertFalse(map.containsValue("two"));
        
        map.put("2", "dos");
        assertTrue(map.containsValue("dos"));
        map.put("2", "two");
        assertTrue(map.containsValue("two"));

        map.clear();
        assertFalse(map.containsKey("one"));
    }

    /**
     * Test of get method, of class RepeatedMap.
     */
    @Test
    public void testGet_Object()
    {
        RepeatedMap<String,String> map = new RepeatedMap();
        assertNull(map.get("uno"));

        map.put("1", "uno");
        assertEquals("uno",map.get("1"));
        map.put("1", "one");
        assertEquals("uno",map.get("1"));

        map.clear();
        assertNull(map.get("uno"));
    }
}
