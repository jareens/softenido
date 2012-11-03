/*
*  BigIntegersTest.java
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
*  Copied parts from android and jdk are distributed in their original license.
*
*/
package com.softenido.cafecore.util;

import java.math.BigInteger;
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
public class BigIntegersTest
{
    
    public BigIntegersTest()
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
    BigInteger[] VALUES = {BigInteger.ZERO, BigInteger.ONE, BigInteger.TEN};
    /**
     * Test of valueOf method, of class BigIntegers.
     */
    @Test
    public void testValueOf_byteArr()
    {
        byte[] values = {0, 1, 10};
        assertArrayEquals(VALUES, BigIntegers.valueOf(values));
    }

    /**
     * Test of valueOf method, of class BigIntegers.
     */
    @Test
    public void testValueOf_charArr()
    {
        char[] values = {0, 1, 10};
        assertArrayEquals(VALUES, BigIntegers.valueOf(values));
    }

    /**
     * Test of valueOf method, of class BigIntegers.
     */
    @Test
    public void testValueOf_intArr()
    {
        int[] values = {0, 1, 10};
        assertArrayEquals(VALUES, BigIntegers.valueOf(values));
    }

    /**
     * Test of valueOf method, of class BigIntegers.
     */
    @Test
    public void testValueOf_longArr()
    {
        long[] values = {0L, 1L, 10L};
        assertArrayEquals(VALUES, BigIntegers.valueOf(values));
    }

    /**
     * Test of valueOf method, of class BigIntegers.
     */
    @Test
    public void testValueOf_3args_1()
    {
        byte[] values = {0, 0, 1, 10, 20};
        assertArrayEquals(VALUES, BigIntegers.valueOf(values, 1, 4));
    }

    /**
     * Test of valueOf method, of class BigIntegers.
     */
    @Test
    public void testValueOf_3args_2()
    {
        char[] values = {0, 0, 1, 10, 20};
        assertArrayEquals(VALUES, BigIntegers.valueOf(values, 1, 4));
    }

    /**
     * Test of valueOf method, of class BigIntegers.
     */
    @Test
    public void testValueOf_3args_3()
    {
        int[] values = {0, 0, 1, 10, 20};
        assertArrayEquals(VALUES, BigIntegers.valueOf(values, 1, 4));
    }

    /**
     * Test of valueOf method, of class BigIntegers.
     */
    @Test
    public void testValueOf_3args_4()
    {
        long[] values = {0, 0, 1, 10, 20};
        assertArrayEquals(VALUES, BigIntegers.valueOf(values, 1, 4));
    }
}
