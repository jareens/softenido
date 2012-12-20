/*
 * ShufflesTest.java
 *
 * Copyright (c) 2012 Francisco Gómez Carrasco
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

import java.util.Arrays;
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
public class ShufflesTest
{
    private static final int SIZE = 128;
    public ShufflesTest()
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
     * Test of sequence method, of class Shuffles.
     */
    @Test
    public void testSequence_intArr_long()
    {
        int[][] seed = new int[SIZE][];
        
        for(int i=0;i<seed.length;i++)
        {
            seed[i] = Shuffles.sequence(new int[SIZE],i);
        }
        for(int i=0;i<seed.length;i++)
        {
            for(int j=i+1;j<seed.length;j++)
            {
                boolean equal = Arrays.equals(seed[i], seed[j]);
                assertEquals(false, equal);
            }
        }
    }

    /**
     * Test of sequence method, of class Shuffles.
     */
    @Test
    public void testSequence_intArr()
    {
        int count = 0;
        int[] src = Sorts.sequence(new int[SIZE]);
        int[][] rand = new int[SIZE][];
        
        for(int i=0;i<rand.length;i++)
        {
            rand[i] = Shuffles.sequence(Arrays6.copyOf(src, src.length));
        }
        for(int i=0;i<rand.length;i++)
        {
            for(int j=i+1;j<rand.length;j++)
            {
                if(Arrays.equals(rand[i], rand[j]))
                {
                    count++;
                }
            }
        }
        assertEquals(true, count < (SIZE/100+1));
    }

    /**
     * Test of shuffles method, of class Shuffles.
     */
    @Test
    public void testShuffles_intArr_long()
    {
        int[] src = Sorts.sequence(new int[SIZE]);
        int[][] seed = new int[SIZE][];
        
        for(int i=0;i<seed.length;i++)
        {
            seed[i] = Shuffles.shuffles(Arrays6.copyOf(src, src.length),i);
        }
        for(int i=0;i<seed.length;i++)
        {
            for(int j=i+1;j<seed.length;j++)
            {
                boolean equal = Arrays.equals(seed[i], seed[j]);
                assertEquals(false, equal);
            }
        }
    }

    /**
     * Test of shuffles method, of class Shuffles.
     */
    @Test
    public void testShuffles_intArr()
    {
        int count = 0;
        int[] src = Sorts.sequence(new int[SIZE]);
        int[][] rand = new int[SIZE][];
        
        for(int i=0;i<rand.length;i++)
        {
            rand[i] = Shuffles.shuffles(Arrays6.copyOf(src, src.length));
        }
        for(int i=0;i<rand.length;i++)
        {
            for(int j=i+1;j<rand.length;j++)
            {
                if(Arrays.equals(rand[i], rand[j]))
                {
                    count++;
                }
            }
        }
        assertEquals(true, count < (SIZE/100+1));
    }

    /**
     * Test of suffle method, of class Shuffles.
     */
    @Test
    public void testSuffle_GenericType_long()
    {        
        String[] src = Sorts.sequence(new String[SIZE]);
        String[][] seed = new String[SIZE][];
        
        for(int i=0;i<seed.length;i++)
        {
            seed[i] = Shuffles.shuffles(Arrays6.copyOf(src, src.length),i);
        }
        for(int i=0;i<seed.length;i++)
        {
            for(int j=i+1;j<seed.length;j++)
            {
                boolean equal = Arrays.equals(seed[i], seed[j]);
                assertEquals(false, equal);
            }
        }
    }
    /**
     * Test of suffle method, of class Shuffles.
     */
    @Test
    public void testSuffle_GenericType()
    {
        //verificar
        int count = 0;
        int[] src = Sorts.sequence(new int[SIZE]);
        int[][] rand = new int[SIZE][];
        
        for(int i=0;i<rand.length;i++)
        {
            rand[i] = Shuffles.shuffles(Arrays6.copyOf(src, src.length));
        }
        for(int i=0;i<rand.length;i++)
        {
            for(int j=i+1;j<rand.length;j++)
            {
                if(Arrays.equals(rand[i], rand[j]))
                {
                    count++;
                }
            }
        }
        assertEquals(true, count < (SIZE/100+1));
    }

    /**
     * Test of sequence method, of class Shuffles.
     */
    @Test
    public void testSequence_longArr_long()
    {
        long[][] seed = new long[SIZE][];
        
        for(int i=0;i<seed.length;i++)
        {
            seed[i] = Shuffles.sequence(new long[SIZE],i);
        }
        for(int i=0;i<seed.length;i++)
        {
            for(int j=i+1;j<seed.length;j++)
            {
                boolean equal = Arrays.equals(seed[i], seed[j]);
                assertEquals(false, equal);
            }
        }
    }

    /**
     * Test of sequence method, of class Shuffles.
     */
    @Test
    public void testSequence_longArr()
    {
        int count = 0;
        long[] src = Sorts.sequence(new long[SIZE]);
        long[][] rand = new long[SIZE][];
        
        for(int i=0;i<rand.length;i++)
        {
            rand[i] = Shuffles.sequence(Arrays6.copyOf(src, src.length));
        }
        for(int i=0;i<rand.length;i++)
        {
            for(int j=i+1;j<rand.length;j++)
            {
                if(Arrays.equals(rand[i], rand[j]))
                {
                    count++;
                }
            }
        }
        assertEquals(true, count < (SIZE/100+1));
    }
}
