/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.cafecore.math;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author franci
 */
public class LongMathTest
{
    
    public LongMathTest()
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
     * Test of gcd method, of class LongMath.
     */
    @Test
    public void testGcd_long_long()
    {
        assertEquals(1L, LongMath.gcd(2L, 3L));
        assertEquals(1L, LongMath.gcd(3L, 5L));
        assertEquals(1L, LongMath.gcd(5L, 7L));
        assertEquals(1L, LongMath.gcd(7L, 9L));
        assertEquals(1L, LongMath.gcd(9L, 11L));
        assertEquals(1L, LongMath.gcd(11L, 13L));
        
        assertEquals(2L, LongMath.gcd(2L, 6L));
        assertEquals(3L, LongMath.gcd(6L, 9L));
        assertEquals(4L, LongMath.gcd(8L, 12L));
        assertEquals(5L, LongMath.gcd(5L, 10L));
        assertEquals(6L, LongMath.gcd(6L, 12L));
        assertEquals(7L, LongMath.gcd(7L, 14L));
    }

    /**
     * Test of gcd method, of class LongMath.
     */
    @Test
    public void testGcd_long_longArr()
    {
        assertEquals(1L, LongMath.gcd(2L, 3L, 4L, 5L));
        assertEquals(2L, LongMath.gcd(4L, 6L, 8L, 12L));
        assertEquals(3L, LongMath.gcd(6L, 9L, 12L));
        assertEquals(4L, LongMath.gcd(4L, 8L, 12L));
    }

    /**
     * Test of lcm method, of class LongMath.
     */
    @Test
    public void testLcm_long_long()
    {
        assertEquals(2L, LongMath.lcm(1L, 2L));
        assertEquals(6L, LongMath.lcm(2L, 3L));
        assertEquals(12L, LongMath.lcm(3L, 4L));
        assertEquals(20L, LongMath.lcm(4L, 5L));
        assertEquals(42L, LongMath.lcm(6L, 7L));
        assertEquals(56L, LongMath.lcm(7L, 8L));
        
        assertEquals(6L, LongMath.lcm(2L, 6L));
        assertEquals(6L, LongMath.lcm(3L, 6L));
        assertEquals(24L, LongMath.lcm(6L, 8L));
        assertEquals(12L, LongMath.lcm(3L, 12L));
        assertEquals(10L, LongMath.lcm(5L, 10L));
        assertEquals(12L, LongMath.lcm(6L, 12L));
    }

    /**
     * Test of lcm method, of class LongMath.
     */
    @Test
    public void testLcm_long_longArr()
    {
        assertEquals(60L, LongMath.lcm(2L, 3L, 4L, 5L));
        assertEquals(24L, LongMath.lcm(4L, 6L, 8L, 12L));
        assertEquals(36L, LongMath.lcm(6L, 9L, 12L));
        assertEquals(24L, LongMath.lcm(4L, 8L, 12L));
        assertEquals(1155L, LongMath.lcm(3L, 5L, 7L, 11L));
    }

    static long[] FIBONACCI = {0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181, 6765, 10946, 17711, 28657, 46368, 75025, 121393, 196418, 317811, 514229, 832040, 1346269, 2178309, 3524578, 5702887, 9227465, 14930352, 24157817, 39088169};
    static long[] FIBO13_21 = {13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181, 6765, 10946, 17711, 28657, 46368, 75025, 121393, 196418, 317811, 514229, 832040, 1346269, 2178309, 3524578, 5702887, 9227465, 14930352, 24157817, 39088169};
    
    /**
     * Test of fibonacci method, of class LongMath.
     */
    @Test
    public void testFibonacci_longArr()
    {
        long[] fibonacci = new long[FIBONACCI.length];
        assertEquals(FIBONACCI.length, LongMath.fibonacci(fibonacci));
        assertArrayEquals(FIBONACCI, fibonacci);
    }

    /**
     * Test of fibonacci method, of class LongMath.
     */
    @Test
    public void testFibonacci_3args()
    {
        long[] fibonacci = new long[FIBONACCI.length];
        assertEquals(FIBONACCI.length, LongMath.fibonacci(fibonacci, 0L, 1L));
        assertArrayEquals(FIBONACCI, fibonacci);
        
        fibonacci = new long[FIBO13_21.length];
        assertEquals(FIBO13_21.length, LongMath.fibonacci(fibonacci, 13, 21));
        assertArrayEquals(FIBO13_21, fibonacci);
    }

    /**
     * Test of fibonacci method, of class LongMath.
     */
    @Test
    public void testFibonacci_5args()
    {
        long[] fibonacci = new long[FIBONACCI.length];
        LongMath.fibonacci(fibonacci, 0L, 1L, 0, FIBONACCI.length/2);
        LongMath.fibonacci(fibonacci, FIBONACCI[ FIBONACCI.length/2],  FIBONACCI[ FIBONACCI.length/2+1],  FIBONACCI.length/2,  FIBONACCI.length);
        assertArrayEquals(FIBONACCI, fibonacci);
    }
}
