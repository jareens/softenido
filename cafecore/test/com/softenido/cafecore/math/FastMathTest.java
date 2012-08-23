/*
 * FastMathTest.java
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
package com.softenido.cafecore.math;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author franci
 */
public class FastMathTest
{
    
    public FastMathTest()
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
     * Test of log2 method, of class FastMath.
     */
    @Test
    public void testLog2_long()
    {
        assertEquals(0, FastMath.log2(1));
        assertEquals(1, FastMath.log2(2));
        assertEquals(2, FastMath.log2(4));
        assertEquals(3, FastMath.log2(8));
        assertEquals(4, FastMath.log2(16));
        assertEquals(5, FastMath.log2(32));
        assertEquals(6, FastMath.log2(64));
        assertEquals(7, FastMath.log2(128));

        assertEquals(1, FastMath.log2(3));
        assertEquals(2, FastMath.log2(7));
        assertEquals(3, FastMath.log2(15));
        assertEquals(4, FastMath.log2(31));
        assertEquals(5, FastMath.log2(63));
        assertEquals(6, FastMath.log2(127));
    }

    /**
     * Test of log2 method, of class FastMath.
     */
    @Test
    public void testLog2_long_boolean()
    {
        assertEquals(0, FastMath.log2(1,true));
        assertEquals(1, FastMath.log2(2,true));
        assertEquals(2, FastMath.log2(4,true));
        assertEquals(3, FastMath.log2(8,true));
        assertEquals(4, FastMath.log2(16,true));
        assertEquals(5, FastMath.log2(32,true));
        assertEquals(6, FastMath.log2(64,true));
        assertEquals(7, FastMath.log2(128,true));

        assertEquals(2, FastMath.log2(3,true));
        assertEquals(3, FastMath.log2(7,true));
        assertEquals(4, FastMath.log2(15,true));
        assertEquals(5, FastMath.log2(31,true));
        assertEquals(6, FastMath.log2(63,true));
        assertEquals(7, FastMath.log2(127,true));
    }

    /**
     * Test of pow2 method, of class FastMath.
     */
    @Test
    public void testPow2()
    {
        assertEquals(1, FastMath.pow2(0));
        assertEquals(2, FastMath.pow2(1));
        assertEquals(4, FastMath.pow2(2));
        assertEquals(8, FastMath.pow2(3));
        assertEquals(16, FastMath.pow2(4));
        assertEquals(32, FastMath.pow2(5));
        assertEquals(64, FastMath.pow2(6));
        assertEquals(128, FastMath.pow2(7));
        assertEquals(256, FastMath.pow2(8));
    }

    /**
     * Test of gcd method, of class FastMath.
     */
    @Test
    public void testGcd_int_int()
    {
        assertEquals(1, FastMath.gcd(2,3));
        assertEquals(1, FastMath.gcd(3,5));
        assertEquals(1, FastMath.gcd(5,7));
        assertEquals(1, FastMath.gcd(7,9));
        assertEquals(1, FastMath.gcd(9,11));
        assertEquals(1, FastMath.gcd(11,13));
        
        assertEquals(2, FastMath.gcd(2,6));
        assertEquals(3, FastMath.gcd(6,9));
        assertEquals(4, FastMath.gcd(8,12));
        assertEquals(5, FastMath.gcd(5,10));
        assertEquals(6, FastMath.gcd(6,12));
        assertEquals(7, FastMath.gcd(7,14));
    }

    /**
     * Test of gcd method, of class FastMath.
     */
    @Test
    public void testGcd_int_intArr()
    {
        assertEquals(1, FastMath.gcd(2,3,4,5));
        assertEquals(2, FastMath.gcd(4,6,8,12));
        assertEquals(3, FastMath.gcd(6,9,12));
        assertEquals(4, FastMath.gcd(4,8,12));
    }

    /**
     * Test of lcm method, of class FastMath.
     */
    @Test
    public void testLcm_int_int()
    {
        assertEquals(2, FastMath.lcm(1,2));
        assertEquals(6, FastMath.lcm(2,3));
        assertEquals(12, FastMath.lcm(3,4));
        assertEquals(20, FastMath.lcm(4,5));
        assertEquals(42, FastMath.lcm(6,7));
        assertEquals(56, FastMath.lcm(7,8));
        
        assertEquals(6, FastMath.lcm(2,6));
        assertEquals(6, FastMath.lcm(3,6));
        assertEquals(24, FastMath.lcm(6,8));
        assertEquals(12, FastMath.lcm(3,12));
        assertEquals(10, FastMath.lcm(5,10));
        assertEquals(12, FastMath.lcm(6,12));
    }

    /**
     * Test of lcm method, of class FastMath.
     */
    @Test
    public void testLcmint_intArr()
    {
        assertEquals(60, FastMath.lcm(2,3,4,5));
        assertEquals(24, FastMath.lcm(4,6,8,12));
        assertEquals(36, FastMath.lcm(6,9,12));
        assertEquals(24, FastMath.lcm(4,8,12));
    }

    /**
     * Test of log method, of class FastMath.
     */
    @Test
    public void testLog()
    {
        for(int i=0;i<20000000;i++)
        {
            assertEquals(Math.log(i%300), FastMath.log(i%300), 0.0);
        }
    }

}
