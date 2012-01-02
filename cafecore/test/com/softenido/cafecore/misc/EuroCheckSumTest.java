/*
 *  EuroCheckSumTest.java
 *
 *  Copyright (C) 2007-2011  Francisco Gómez Carrasco
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
package com.softenido.cafecore.misc;

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
public class EuroCheckSumTest
{
    
    public EuroCheckSumTest()
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
     * Test of verify method, of class EuroCheckSum.
     */
    @Test
    public void testVerify()
    {
        final String serial5[] ={"P06905651959","V21591393322","V21017971894"};
        final String serial10[] ={"X39539326223","X54522698627","X30914928713","X29272356371",""};
        final String serial20[] ={"S08879592301"};
        final String serial50[] ={"V10529505391","V41687986621"};
        final String serial100[] ={};
        final String serial200[] ={};
        final String serial500[] ={};
        
        final String serials[][] ={serial5,serial10,serial20,serial50,serial100,serial200,serial500};
        
        boolean expResult = true;
        
        for(int i=0;i<serials.length;i++)
        {
            for(int j=0;j<serials[i].length;i++)
            {
                boolean result = EuroCheckSum.verify(serials[i][j]);
                assertEquals(expResult, result);
            }
        }
    }
    
}
