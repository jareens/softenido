/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.hardcore.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
}
