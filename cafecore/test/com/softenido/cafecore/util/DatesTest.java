/*
 * DatesTest.java
 *
 * Copyright (c) 2011-2012  Francisco Gómez Carrasco
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

import org.junit.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author franci
 */
public class DatesTest
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
    public DatesTest()
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
     * Test of isToday method, of class Dates.
     */
    @Test
    public void testIsToday_Calendar_Calendar()
    {
        assertEquals(true, Dates.isToday(todayCalendar1, todayCalendar2));
        assertEquals(false, Dates.isToday(todayCalendar1, yesterdayCalendar2));
        assertEquals(false, Dates.isToday(todayCalendar2, yesterdayCalendar1));
    }

    /**
     * Test of isToday method, of class Dates.
     */
    @Test
    public void testIsToday_Date_Date()
    {
        assertEquals(true, Dates.isToday(today1, today2));
        assertEquals(false, Dates.isToday(today1, yesterday2));
        assertEquals(false, Dates.isToday(today2, yesterday1));
    }

    /**
     * Test of isYesterday method, of class Dates.
     */
    @Test
    public void testIsYesterday_Calendar_Calendar()
    {
        assertEquals(true, Dates.isYesterday(todayCalendar1, yesterdayCalendar2));
        assertEquals(false, Dates.isYesterday(todayCalendar1, todayCalendar2));
        assertEquals(false, Dates.isYesterday(todayCalendar2, tomorrowCalendar1));
    }

    /**
     * Test of isYesterday method, of class Dates.
     */
    @Test
    public void testIsYesterday_Date_Date()
    {
        assertEquals(true, Dates.isYesterday(today1, yesterday2));
        assertEquals(false, Dates.isYesterday(today1, today2));
        assertEquals(false, Dates.isYesterday(today2, tomorrow1));
    }

    /**
     * Test of isTomorrow method, of class Dates.
     */
    @Test
    public void testIsTomorrow_Calendar_Calendar()
    {
        assertEquals(true, Dates.isTomorrow(todayCalendar1, tomorrowCalendar2));
        assertEquals(false, Dates.isTomorrow(todayCalendar1, todayCalendar2));
        assertEquals(false, Dates.isTomorrow(todayCalendar2, yesterdayCalendar1));
    }

    /**
     * Test of isTomorrow method, of class Dates.
     */
    @Test
    public void testIsTomorrow_Date_Date()
    {
        assertEquals(true, Dates.isYesterday(today1, yesterday2));
        assertEquals(false, Dates.isYesterday(today1, today2));
        assertEquals(false, Dates.isYesterday(today2, today1));
    }

    /**
     * Test of isSameYear method, of class Dates.
     */
    @Test
    public void testIsSameYear()
    {
        Calendar c21 = new GregorianCalendar(1972, 1, 1, 1, 1, 1);
        Calendar c22 = new GregorianCalendar(1972, 2, 2, 2, 2, 2);
        Calendar c33 = new GregorianCalendar(1973, 3, 3, 3, 3, 3);
        Date d21 = c21.getTime();
        Date d22 = c22.getTime();
        Date d33 = c33.getTime();

        assertEquals(true,  Dates.isSameYear(c22, c21));
        assertEquals(false, Dates.isSameYear(c22, c33));
    }
}
