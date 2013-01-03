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

import com.softenido.cafecore.util.Arrays6;
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
    public void testLcm_int_intArr()
    {
        assertEquals(60, FastMath.lcm(2,3,4,5));
        assertEquals(24, FastMath.lcm(4,6,8,12));
        assertEquals(36, FastMath.lcm(6,9,12));
        assertEquals(24, FastMath.lcm(4,8,12));
        assertEquals(1155, FastMath.lcm(3, 5, 7, 11));
    }

    /**
     * Test of log method, of class FastMath.
     */
    @Test
    public void testLog()
    {
        for(int i=0;i<1500;i++)
        {
            assertEquals(Math.log(i%300), FastMath.log(i%300), 0.0);
        }
    }
    static int[] FIBONACCI = {0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181, 6765, 10946, 17711, 28657, 46368, 75025, 121393, 196418, 317811, 514229, 832040, 1346269, 2178309, 3524578, 5702887, 9227465, 14930352, 24157817, 39088169};
    static int[] FIBO13_21 = {13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181, 6765, 10946, 17711, 28657, 46368, 75025, 121393, 196418, 317811, 514229, 832040, 1346269, 2178309, 3524578, 5702887, 9227465, 14930352, 24157817, 39088169};

    /**
     * Test of fibonacci method, of class FastMath.
     */
    @Test
    public void testFibonacci_intArr()
    {
        int[] fibonacci = new int[FIBONACCI.length];
        assertEquals(FIBONACCI.length, FastMath.fibonacci(fibonacci));
        assertArrayEquals(FIBONACCI, fibonacci);
    }

    /**
     * Test of fibonacci method, of class FastMath.
     */
    @Test
    public void testFibonacci_3args()
    {
        int[] fibonacci = new int[FIBONACCI.length];
        assertEquals(FIBONACCI.length, FastMath.fibonacci(fibonacci, 0, 1));
        assertArrayEquals(FIBONACCI, fibonacci);
        
        fibonacci = new int[FIBO13_21.length];
        assertEquals(FIBO13_21.length, FastMath.fibonacci(fibonacci, 13, 21));
        assertArrayEquals(FIBO13_21, fibonacci);
    }

    /**
     * Test of fibonacci method, of class FastMath.
     */
    @Test
    public void testFibonacci_5args()
    {
        int[] fibonacci = new int[FIBONACCI.length];
        FastMath.fibonacci(fibonacci, 0, 1, 0, FIBONACCI.length/2);
        FastMath.fibonacci(fibonacci, FIBONACCI[ FIBONACCI.length/2],  FIBONACCI[ FIBONACCI.length/2+1],  FIBONACCI.length/2,  FIBONACCI.length);
        assertArrayEquals(FIBONACCI, fibonacci);
    }

    /**
     * Test of maxDouble method, of class FastMath.
     */
    @Test
    public void testMaxDouble()
    {
        assertEquals(2d, FastMath.maxDouble(1d, 2d), 0.0d);
        assertEquals(3d, FastMath.maxDouble(1d, 2d, 3d), 0.0d);
        assertEquals(3d, FastMath.maxDouble(3d, 2d, 1d), 0.0d);
        assertEquals(9d, FastMath.maxDouble(9d, 8d, 7d, 6d, 5d), 0.0d);
    }

    /**
     * Test of maxFloat method, of class FastMath.
     */
    @Test
    public void testMaxFloat()
    {
        assertEquals(2f, FastMath.maxFloat(1f, 2f), 0.0f);
        assertEquals(3f, FastMath.maxFloat(1f, 2f, 3f), 0.0f);
        assertEquals(3f, FastMath.maxFloat(3f, 2f, 1f), 0.0f);
        assertEquals(9f, FastMath.maxFloat(9f, 8f, 7f, 6f, 5f), 0.0f);
    }

    /**
     * Test of maxInteger method, of class FastMath.
     */
    @Test
    public void testMaxInteger()
    {
        assertEquals(2, FastMath.maxInteger(1, 2));
        assertEquals(3, FastMath.maxInteger(1, 2, 3));
        assertEquals(3, FastMath.maxInteger(3, 2, 1));
        assertEquals(9, FastMath.maxInteger(9, 8, 7, 6, 5));
    }

    /**
     * Test of maxLong method, of class FastMath.
     */
    @Test
    public void testMaxLong()
    {
        assertEquals(2L, FastMath.maxLong(1L, 2L));
        assertEquals(3L, FastMath.maxLong(1L, 2L, 3L));
        assertEquals(3L, FastMath.maxLong(3L, 2L, 1L));
        assertEquals(9L, FastMath.maxLong(9L, 8L, 7L, 6L, 5L));
    }

    /**
     * Test of minDouble method, of class FastMath.
     */
    @Test
    public void testMinDouble()
    {
        assertEquals(1d, FastMath.minDouble(1d, 2d), 0.0d);
        assertEquals(1d, FastMath.minDouble(1d, 2d, 3d), 0.0d);
        assertEquals(1d, FastMath.minDouble(3d, 2d, 1d), 0.0d);
        assertEquals(5d, FastMath.minDouble(9d, 8d, 7d, 6d, 5d), 0.0d);
    }

    /**
     * Test of minFloat method, of class FastMath.
     */
    @Test
    public void testMinFloat()
    {
        assertEquals(1f, FastMath.minFloat(1f, 2f), 0.0f);
        assertEquals(1f, FastMath.minFloat(1f, 2f, 3f), 0.0f);
        assertEquals(1f, FastMath.minFloat(3f, 2f, 1f), 0.0f);
        assertEquals(5f, FastMath.minFloat(9f, 8f, 7f, 6f, 5f), 0.0f);
    }

    /**
     * Test of minInt method, of class FastMath.
     */
    @Test
    public void testMinInteger()
    {
        assertEquals(1, FastMath.minInteger(1, 2));
        assertEquals(1, FastMath.minInteger(1, 2, 3));
        assertEquals(1, FastMath.minInteger(3, 2, 1));
        assertEquals(5, FastMath.minInteger(9, 8, 7, 6, 5));
    }

    /**
     * Test of minLong method, of class FastMath.
     */
    @Test
    public void testMinLong()
    {
        assertEquals(1L, FastMath.minLong(1L, 2L));
        assertEquals(1L, FastMath.minLong(1L, 2L, 3L));
        assertEquals(1L, FastMath.minLong(3L, 2L, 1L));
        assertEquals(5L, FastMath.minLong(9L, 8L, 7L, 6L, 5L));
    }

}
