/*
 * StringsTest.java
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

import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.*;

/**
 *
 * @author franci
 */
public class StringsTest
{
    
    public StringsTest()
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
     * Test of repeat method, of class Strings.
     */
    @Test
    public void testRepeat_char_int()
    {
        assertEquals("", Strings.repeat('0', 0));
        assertEquals("1", Strings.repeat('1', 1));
        assertEquals("22", Strings.repeat('2', 2));
        assertEquals("333", Strings.repeat('3', 3));
        assertEquals("4444", Strings.repeat('4', 4));
        assertEquals("55555", Strings.repeat('5', 5));
        assertEquals("666666", Strings.repeat('6', 6));
        assertEquals("7777777", Strings.repeat('7', 7));
    }

    /**
     * Test of repeat method, of class Strings.
     */
    @Test
    public void testRepeat_String_int()
    {
        assertEquals("", Strings.repeat("0", 0));
        assertEquals("1", Strings.repeat("1", 1));
        assertEquals("1212", Strings.repeat("12", 2));
        assertEquals("123123123", Strings.repeat("123", 3));
        assertEquals("1234123412341234", Strings.repeat("1234", 4));
        assertEquals("1234512345123451234512345", Strings.repeat("12345", 5));
        assertEquals("123456123456123456123456123456123456", Strings.repeat("123456", 6));
        assertEquals("1234567123456712345671234567123456712345671234567", Strings.repeat("1234567", 7));
    }

    /**
     * Test of fill method, of class Strings.
     */
    @Test
    public void testFill_4args_1()
    {
        StringBuilder builder = new StringBuilder();
        assertEquals("", Strings.fill(builder, '0', 0, false).toString());
        assertEquals("1", Strings.fill(builder, '1', 1, false).toString());
        assertEquals("12", Strings.fill(builder, '2', 2, false).toString());
        assertEquals("123", Strings.fill(builder, '3', 3, false).toString());
        assertEquals("1234", Strings.fill(builder, '4', 4, false).toString());
        assertEquals("12345", Strings.fill(builder, '5', 5, false).toString());
        assertEquals("123456", Strings.fill(builder, '6', 6, false).toString());
        builder = new StringBuilder();
        assertEquals("", Strings.fill(builder, '0', 0, true).toString());
        assertEquals("1", Strings.fill(builder, '1', 1, true).toString());
        assertEquals("21", Strings.fill(builder, '2', 2, true).toString());
        assertEquals("321", Strings.fill(builder, '3', 3, true).toString());
        assertEquals("4321", Strings.fill(builder, '4', 4, true).toString());
        assertEquals("54321", Strings.fill(builder, '5', 5, true).toString());
        assertEquals("654321", Strings.fill(builder, '6', 6, true).toString());
    }

    /**
     * Test of fill method, of class Strings.
     */
    @Test
    public void testFill_4args_2()
    {
        assertEquals("", Strings.fill("", '0', 0, false).toString());
        assertEquals("1", Strings.fill("", '1', 1, false).toString());
        assertEquals("22", Strings.fill("", '2', 2, false).toString());
        assertEquals("033", Strings.fill("0", '3', 3, false).toString());
        assertEquals("0044", Strings.fill("00", '4', 4, false).toString());
        assertEquals("00055", Strings.fill("000", '5', 5, false).toString());
        assertEquals("000066", Strings.fill("0000", '6', 6, false).toString());

        assertEquals("", Strings.fill("", '0', 0, true).toString());
        assertEquals("1", Strings.fill("", '1', 1, true).toString());
        assertEquals("22", Strings.fill("", '2', 2, true).toString());
        assertEquals("330", Strings.fill("0", '3', 3, true).toString());
        assertEquals("4400", Strings.fill("00", '4', 4, true).toString());
        assertEquals("55000", Strings.fill("000", '5', 5, true).toString());
        assertEquals("660000", Strings.fill("0000", '6', 6, true).toString());
    }

    /**
     * Test of fill method, of class Strings.
     */
    @Test
    public void testFill_3args_1()
    {
        StringBuilder builder = new StringBuilder();
        assertEquals("", Strings.fill(builder, '0', 0).toString());
        assertEquals("1", Strings.fill(builder, '1', 1).toString());
        assertEquals("12", Strings.fill(builder, '2', 2).toString());
        assertEquals("123", Strings.fill(builder, '3', 3).toString());
        assertEquals("1234", Strings.fill(builder, '4', 4).toString());
        assertEquals("12345", Strings.fill(builder, '5', 5).toString());
        assertEquals("123456", Strings.fill(builder, '6', 6).toString());
    }

    /**
     * Test of fill method, of class Strings.
     */
    @Test
    public void testFill_3args_2()
    {
        assertEquals("", Strings.fill("", '0', 0).toString());
        assertEquals("1", Strings.fill("", '1', 1).toString());
        assertEquals("22", Strings.fill("", '2', 2).toString());
        assertEquals("033", Strings.fill("0", '3', 3).toString());
        assertEquals("0044", Strings.fill("00", '4', 4).toString());
        assertEquals("00055", Strings.fill("000", '5', 5).toString());
        assertEquals("000066", Strings.fill("0000", '6', 6).toString());
    }

    /**
     * Test of commaSeparatedValues method, of class Strings.
     */
    @Test
    public void testDelimiterSeparatedValues_List_String()
    {
        List<Integer> list = Arrays6.asList(new Integer[]{1,2,3,4,5,6,7,8,9,0});
        List<Integer> empty = Arrays6.asList(new Integer[]{});
        List<Integer> one = Arrays6.asList(new Integer[]{1});
        assertEquals("1|2|3|4|5|6|7|8|9|0", Strings.delimiterSeparatedValues(list, "|"));
        assertEquals("", Strings.delimiterSeparatedValues(empty, "|"));
        assertEquals("1", Strings.delimiterSeparatedValues(one, "|"));
    }

    /**
     * Test of delimiterSeparatedValues method, of class Strings.
     */
    @Test
    public void testDelimiterSeparatedValues_GenericType_String()
    {
        Integer[] list = new Integer[]{1,2,3,4,5,6,7,8,9,0};
        Integer[] empty = new Integer[]{};
        Integer[] one = new Integer[]{1};
        assertEquals("1|2|3|4|5|6|7|8|9|0", Strings.delimiterSeparatedValues(list, "|"));
        assertEquals("", Strings.delimiterSeparatedValues(empty, "|"));
        assertEquals("1", Strings.delimiterSeparatedValues(one, "|"));
    }

    /**
     * Test of commaSeparatedValues method, of class Strings.
     */
    @Test
    public void testCommaSeparatedValues_List()
    {
        List<Integer> list = Arrays6.asList(new Integer[]{1,2,3,4,5,6,7,8,9,0});
        List<Integer> empty = Arrays6.asList(new Integer[]{});
        List<Integer> one = Arrays6.asList(new Integer[]{1});
        assertEquals("1,2,3,4,5,6,7,8,9,0", Strings.commaSeparatedValues(list));
        assertEquals("", Strings.commaSeparatedValues(empty));
        assertEquals("1", Strings.commaSeparatedValues(one));
    }

    /**
     * Test of commaSeparatedValues method, of class Strings.
     */
    @Test
    public void testCommaSeparatedValues_GenericType()
    {
        Integer[] list = new Integer[]{1,2,3,4,5,6,7,8,9,0};
        Integer[] empty = new Integer[]{};
        Integer[] one = new Integer[]{1};
        assertEquals("1,2,3,4,5,6,7,8,9,0", Strings.commaSeparatedValues(list));
        assertEquals("", Strings.commaSeparatedValues(empty));
        assertEquals("1", Strings.commaSeparatedValues(one));
    }

    /**
     * Test of isnull method, of class Strings.
     */
    @Test
    public void testIsnull()
    {
        assertEquals(null, Strings.isnull(null,null));
        assertEquals("s1", Strings.isnull("s1",null));
        assertEquals("s2", Strings.isnull(null, "s2"));
        assertEquals("s1", Strings.isnull("s1", "s2"));
        
        assertEquals(null, Strings.isnull(null,null, (String)null));
        assertEquals("s3", Strings.isnull(null,null, "s3"));
        assertEquals("s2", Strings.isnull(null,"s2", (String)null));
        assertEquals("s2", Strings.isnull(null,"s2", "s3"));
        assertEquals("s1", Strings.isnull("s1",null, (String)null));
        assertEquals("s1", Strings.isnull("s1",null, "s3"));
        assertEquals("s1", Strings.isnull("s1","s2", (String)null));
        assertEquals("s1", Strings.isnull("s1","s2", "s3"));
    }

}
