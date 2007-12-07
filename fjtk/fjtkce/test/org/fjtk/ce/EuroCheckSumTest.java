/*
 *  ExtracIcons.java
 *
 *  Copyright (C) 2007  Francisco Gómez Carrasco
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
package org.fjtk.ce;

import junit.framework.*;

/**
 *
 * @author franci
 */
public class EuroCheckSumTest extends TestCase
{
    
    public EuroCheckSumTest(String testName)
    {
        super(testName);
    }
    
    protected void setUp() throws Exception
    {
    }
    
    protected void tearDown() throws Exception
    {
    }
    
    /**
     * Test of verify method, of class org.fjtk.ce.EuroCheckSum.
     */
    public void testVerifySerial()
    {
        System.out.println("verify");
        
        final String serial5[] ={};
        final String serial10[] ={"x39539326223"};
        final String serial20[] ={"S08879592301"};
        final String serial50[] ={"V10529505391"};
        final String serial100[] ={};
        final String serial200[] ={};
        final String serial500[] ={};
        
        final String serials[][] ={serial5,serial10,serial20,serial50,serial100,serial200,serial500};
        
        boolean expResult = true;
        
        for(int i=0;i<serials.length;i++)
        {
            for(int j=0;j<serials[i].length;i++)
            {
                boolean result = EuroCheckSum.verifySerial(serials[i][j]);
                assertEquals(expResult, result);
            }
        }        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
