/*
 * HumanMillisFormatTest.java
 *
 * Copyright (c) 2011-2012  Francisco Gómez Carrasco
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

package com.softenido.cafecore.text;

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
public class HumanMillisFormatTest
{
    static final HumanMillisFormat hmf12 = new HumanMillisFormat(1,2,false);
    static final HumanMillisFormat hmf13 = new HumanMillisFormat(1,3,true);
    static final HumanMillisFormat hmf24s = new HumanMillisFormat(2,4,false,HumanMillisFormat.SECOND);
    static final HumanMillisFormat hmf14m = new HumanMillisFormat(1,4,false,HumanMillisFormat.MINUTE);
    
    static final int  h = 3600000;
    static final int  m =   60000;
    static final int  s =    1000;
            
    static final long millis_0_0_3 = 0*h + 0*m + 3*s;
    static final long millis_0_2_0 = 0*h + 2*m + 0*s;
    static final long millis_0_2_3 = 0*h + 2*m + 3*s;
    static final long millis_1_0_0 = 1*h + 0*m + 0*s;
    static final long millis_1_0_3 = 1*h + 0*m + 3*s;
    static final long millis_1_2_0 = 1*h + 2*m + 0*s;
    static final long millis_1_2_3 = 1*h + 2*m + 3*s;
    
    public HumanMillisFormatTest()
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
     * Test of getMaxItems method, of class HumanMillisFormat.
     */
    @Test
    public void testGetMaxItems()
    {
        assertEquals(2, hmf12.getMaxItems());
        assertEquals(3, hmf13.getMaxItems());
        assertEquals(4, hmf24s.getMaxItems());
        assertEquals(4, hmf14m.getMaxItems());
    }

    /**
     * Test of getMinItems method, of class HumanMillisFormat.
     */
    @Test
    public void testGetMinItems()
    {
        assertEquals(1, hmf12.getMinItems());
        assertEquals(1, hmf13.getMinItems());
        assertEquals(2, hmf24s.getMinItems());
        assertEquals(1, hmf14m.getMinItems());
    }

    /**
     * Test of toString method, of class HumanMillisFormat.
     */
    @Test
    public void testToString()
    {
        assertEquals("3s",hmf12.toString(millis_0_0_3));
        assertEquals("2m",hmf12.toString(millis_0_2_0)); 
        assertEquals("2m3s",hmf12.toString(millis_0_2_3)); 
        assertEquals("1h",hmf12.toString(millis_1_0_0)); 
        assertEquals("1h0m",hmf12.toString(millis_1_0_3)); 
        assertEquals("1h2m",hmf12.toString(millis_1_2_0)); 
        assertEquals("1h2m",hmf12.toString(millis_1_2_3)); 

        assertEquals("03s",hmf13.toString(millis_0_0_3));
        assertEquals("02m",hmf13.toString(millis_0_2_0)); 
        assertEquals("02m03s",hmf13.toString(millis_0_2_3)); 
        assertEquals("01h",hmf13.toString(millis_1_0_0)); 
        assertEquals("01h00m03s",hmf13.toString(millis_1_0_3)); 
        assertEquals("01h02m",hmf13.toString(millis_1_2_0)); 
        assertEquals("01h02m03s",hmf13.toString(millis_1_2_3)); 

        assertEquals("0m3s",hmf24s.toString(millis_0_0_3));
        assertEquals("2m0s",hmf24s.toString(millis_0_2_0)); 
        assertEquals("2m3s",hmf24s.toString(millis_0_2_3)); 
        assertEquals("1h0m",hmf24s.toString(millis_1_0_0)); 
        assertEquals("1h0m3s",hmf24s.toString(millis_1_0_3)); 
        assertEquals("1h2m",hmf24s.toString(millis_1_2_0)); 
        assertEquals("1h2m3s",hmf24s.toString(millis_1_2_3)); 

        assertEquals("0m",hmf14m.toString(millis_0_0_3));
        assertEquals("2m",hmf14m.toString(millis_0_2_0)); 
        assertEquals("2m",hmf14m.toString(millis_0_2_3)); 
        assertEquals("1h",hmf14m.toString(millis_1_0_0)); 
        assertEquals("1h",hmf14m.toString(millis_1_0_3)); 
        assertEquals("1h2m",hmf14m.toString(millis_1_2_0)); 
        assertEquals("1h2m",hmf14m.toString(millis_1_2_3)); 
    }
}
