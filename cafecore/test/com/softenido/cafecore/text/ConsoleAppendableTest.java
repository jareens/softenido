/*
 *  ConsoleAppendableTest.java
 *
 *  Copyright (C) 2012  Francisco Gómez Carrasco
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
package com.softenido.cafecore.text;

import static org.junit.Assert.assertEquals;
import org.junit.*;

/**
 *
 * @author franci
 */
public class ConsoleAppendableTest
{
    
    public ConsoleAppendableTest()
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
     * Test of append method, of class ConsoleAppendable.
     */
    @Test
    public void testAppend_char() throws Exception
    {
        StringBuilder sb = new StringBuilder();
        ConsoleAppendable ca = new ConsoleAppendable(sb);
        ca.append('1');
        assertEquals("1",ca.toString());
        ca.append('2');
        assertEquals("12",ca.toString());
        ca.append('3');
        assertEquals("123",ca.toString());

        sb = new StringBuilder();
        ca = new ConsoleAppendable(sb,new ConsoleAppendable.Options(2,false));
        assertEquals("",ca.toString());
        ca.append('1').append('2');
        assertEquals("12",ca.toString());
        ca.append('1');
        assertEquals("12\n1",ca.toString());
        ca.append(' ').append('1');
        assertEquals("12\n1 \n1",ca.toString());
}

    /**
     * Test of append method, of class ConsoleAppendable.
     */
    @Test
    public void testAppend_3args() throws Exception
    {
        StringBuilder sb = new StringBuilder();
        ConsoleAppendable ca = new ConsoleAppendable(sb);
        ca.append("aeiou",0,5);
        assertEquals("aeiou",ca.toString());
        ca.append("0123456789",1,3);
        assertEquals("aeiou12",ca.toString());
        ca.append("aeiou",2,4);
        assertEquals("aeiou12io",ca.toString());
        
        sb = new StringBuilder();
        ca = new ConsoleAppendable(sb,new ConsoleAppendable.Options(2,false));
        ca.append("0").append("1").append("2").append(" ").append("3").append("4").append(" ").append("5").append(" ").append("6").append(" ").append("7").append("8").append("9");
        assertEquals("01\n2 \n34\n5 \n6 \n78\n9",ca.toString());
    }

    /**
     * Test of append method, of class ConsoleAppendable.
     */
    @Test
    public void testAppend_CharSequence() throws Exception
    {
        StringBuilder sb = new StringBuilder();
        ConsoleAppendable instance = new ConsoleAppendable(sb);
        instance.append("aeiou");
        assertEquals("aeiou",instance.toString());
        instance.append("0123456789");
        assertEquals("aeiou0123456789",instance.toString());
        instance.append("aeiou");
        assertEquals("aeiou0123456789aeiou",instance.toString());
    }
}
