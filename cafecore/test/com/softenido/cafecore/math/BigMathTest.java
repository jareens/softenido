/*
 * BigMathTest.java
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

import com.softenido.cafecore.util.BigIntegers;
import java.math.BigDecimal;
import java.math.BigInteger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import org.junit.*;

/**
 *
 * @author franci
 */
public class BigMathTest
{
    final static BigInteger _1 = BigInteger.ONE;
    final static BigInteger _2 = BigInteger.valueOf(2);
    final static BigInteger _3 = BigInteger.valueOf(3);
    final static BigInteger _4 = BigInteger.valueOf(4);
    final static BigInteger _5 = BigInteger.valueOf(5);
    final static BigInteger _6 = BigInteger.valueOf(6);
    final static BigInteger _7 = BigInteger.valueOf(7);
    final static BigInteger _8 = BigInteger.valueOf(8);
    final static BigInteger _9 = BigInteger.valueOf(9);
    final static BigInteger _10 = BigInteger.valueOf(10);
    final static BigInteger _11 = BigInteger.valueOf(11);
    final static BigInteger _12 = BigInteger.valueOf(12);
    final static BigInteger _13 = BigInteger.valueOf(13);
    final static BigInteger _14 = BigInteger.valueOf(14);
    final static BigInteger _15 = BigInteger.valueOf(15);
    final static BigInteger _20 = BigInteger.valueOf(20);
    final static BigInteger _24 = BigInteger.valueOf(24);
    final static BigInteger _35 = BigInteger.valueOf(35);
    final static BigInteger _36 = BigInteger.valueOf(36);
    final static BigInteger _42 = BigInteger.valueOf(42);
    final static BigInteger _56 = BigInteger.valueOf(56);
    final static BigInteger _60 = BigInteger.valueOf(60);
    final static BigInteger _99 = BigInteger.valueOf(99);
    
    public BigMathTest()
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
     * Test of buildE method, of class BigMath.
     */
    @Test
    public void testBuildE()
    {
        BigDecimal e;
        BigDecimal r;
        
        e = new BigDecimal("2.7182818284590452353602874713526624977572470936999595749669676277240766303535475945713821785251664274");
        r = BigMath.buildE(101);
        assertEquals(e, r);
    }
    
    /**
     * Test of gcd method, of class BigMath.
     */
    @Test
    public void testGcd_BigInteger_BigInteger()
    {
        assertEquals(_1, BigMath.gcd(_2,_3));
        assertEquals(_1, BigMath.gcd(_3,_5));
        assertEquals(_1, BigMath.gcd(_5,_7));
        assertEquals(_1, BigMath.gcd(_7,_9));
        assertEquals(_1, BigMath.gcd(_9,_11));
        assertEquals(_1, BigMath.gcd(_11,_13));
        
        assertEquals(_2, BigMath.gcd(_2,_6));
        assertEquals(_3, BigMath.gcd(_6,_9));
        assertEquals(_4, BigMath.gcd(_8,_12));
        assertEquals(_5, BigMath.gcd(_5,_10));
        assertEquals(_6, BigMath.gcd(_6,_12));
        assertEquals(_7, BigMath.gcd(_7,_14));
    }

    /**
     * Test of gcd method, of class BigMath.
     */
    @Test
    public void testGcd_BigInteger_BigIntegerArr()
    {
        assertEquals(_1, BigMath.gcd(_2,_3,_4,_5));
        assertEquals(_2, BigMath.gcd(_4,_6,_8,_12));
        assertEquals(_3, BigMath.gcd(_6,_9,_12));
        assertEquals(_4, BigMath.gcd(_4,_8,_12));
    }

    /**
     * Test of lcm method, of class BigMath.
     */
    @Test
    public void testLcm_BigInteger_BigInteger()
    {
        assertEquals(_2, BigMath.lcm(_1,_2));
        assertEquals(_6, BigMath.lcm(_2,_3));
        assertEquals(_12, BigMath.lcm(_3,_4));
        assertEquals(_20, BigMath.lcm(_4,_5));
        assertEquals(_42, BigMath.lcm(_6,_7));
        assertEquals(_56, BigMath.lcm(_7,_8));
        
        assertEquals(_6, BigMath.lcm(_2,_6));
        assertEquals(_6, BigMath.lcm(_3,_6));
        assertEquals(_24, BigMath.lcm(_6,_8));
        assertEquals(_12, BigMath.lcm(_3,_12));
        assertEquals(_10, BigMath.lcm(_5,_10));
        assertEquals(_12, BigMath.lcm(_6,_12));
    }

    /**
     * Test of lcm method, of class BigMath.
     */
    @Test
    public void testLcm_BigInteger_BigIntegerArr()
    {
        assertEquals(_60, BigMath.lcm(_2,_3,_4,_5));
        assertEquals(_24, BigMath.lcm(_4,_6,_8,_12));
        assertEquals(_36, BigMath.lcm(_6,_9,_12));
        assertEquals(_24, BigMath.lcm(_4,_8,_12));
    }

    static BigInteger[] FIBONACCI = BigIntegers.valueOf(new int[]{0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181, 6765, 10946, 17711, 28657, 46368, 75025, 121393, 196418, 317811, 514229, 832040, 1346269, 2178309, 3524578, 5702887, 9227465, 14930352, 24157817, 39088169});
    static BigInteger[] FIBO13_21 = BigIntegers.valueOf(new int[]{13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181, 6765, 10946, 17711, 28657, 46368, 75025, 121393, 196418, 317811, 514229, 832040, 1346269, 2178309, 3524578, 5702887, 9227465, 14930352, 24157817, 39088169});

    /**
     * Test of fibonacci method, of class BigMath.
     */
    @Test
    public void testFibonacci_BigIntegerArr()
    {
        BigInteger[] fibonacci = new BigInteger[FIBONACCI.length];
        assertEquals(FIBONACCI.length, BigMath.fibonacci(fibonacci));
        assertArrayEquals(FIBONACCI, fibonacci);
    }

    /**
     * Test of fibonacci method, of class BigMath.
     */
    @Test
    public void testFibonacci_3args()
    {
        BigInteger[] fibonacci = new BigInteger[FIBONACCI.length];
        assertEquals(FIBONACCI.length, BigMath.fibonacci(fibonacci, BigInteger.ZERO, BigInteger.ONE));
        assertArrayEquals(FIBONACCI, fibonacci);
        
        fibonacci = new BigInteger[FIBO13_21.length];
        assertEquals(FIBO13_21.length, BigMath.fibonacci(fibonacci, BigInteger.valueOf(13), BigInteger.valueOf(21)));
        assertArrayEquals(FIBO13_21, fibonacci);
    }

    /**
     * Test of fibonacci method, of class BigMath.
     */
    @Test
    public void testFibonacci_5args()
    {
        BigInteger[] fibonacci = new BigInteger[FIBONACCI.length];
        BigMath.fibonacci(fibonacci, BigInteger.ZERO, BigInteger.ONE, 0, FIBONACCI.length/2);
        BigMath.fibonacci(fibonacci, FIBONACCI[ FIBONACCI.length/2],  FIBONACCI[ FIBONACCI.length/2+1],  FIBONACCI.length/2,  FIBONACCI.length);
        assertArrayEquals(FIBONACCI, fibonacci);
    }
    
}
