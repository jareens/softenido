/*
 * HumanDateFormatTest.java
 *
 * Copyright (c) 2011  Francisco Gómez Carrasco
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
package com.softenido.hardcore.text;

import org.junit.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author franci
 */
public class HumanDateFormatTest
{
    Calendar yesterdayCalendar1;
    Calendar yesterdayCalendar2;
    Calendar todayCalendar1;
    Calendar todayCalendar2;
    Calendar tomorrowCalendar1;
    Calendar tomorrowCalendar2;
    Date yesterday1;
    Date yesterday2;
    Date today1;
    Date today2;
    Date tomorrow1;
    Date tomorrow2;
    public HumanDateFormatTest()
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
        yesterdayCalendar1 = new GregorianCalendar(1972, 11, 6, 1, 1, 1);
        yesterdayCalendar2 = new GregorianCalendar(1972, 11, 6, 2, 2, 2);
        todayCalendar1     = new GregorianCalendar(1972, 11, 7, 1, 1, 1);
        todayCalendar2     = new GregorianCalendar(1972, 11, 7, 2, 2, 2);
        tomorrowCalendar1  = new GregorianCalendar(1972, 11, 8, 1, 1, 1);
        tomorrowCalendar2  = new GregorianCalendar(1972, 11, 8, 2, 2, 2);
        yesterday1         = yesterdayCalendar1.getTime();
        yesterday2         = yesterdayCalendar2.getTime();
        today1             = todayCalendar1.getTime();
        today2             = todayCalendar2.getTime();
        tomorrow1          = tomorrowCalendar1.getTime();
        tomorrow2          = tomorrowCalendar2.getTime();
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of format method, of class HumanDateFormat.
     */
    @Test
    public void testFormat()
    {
        HumanDateFormat shdf = (HumanDateFormat) HumanDateFormat.getShortInstance(today1);
        assertEquals("2:02", shdf.format(today2));
        assertEquals("Yesterday", shdf.format(yesterday2));
        assertEquals("Tomorrow", shdf.format(tomorrow2));

        HumanDateFormat lhdf = (HumanDateFormat) HumanDateFormat.getLongInstance(today1);
        assertEquals("Today 2:02", lhdf.format(today2));
        assertEquals("Yesterday 2:02", lhdf.format(yesterday2));
        assertEquals("Tomorrow 2:02", lhdf.format(tomorrow2));
    }
}
