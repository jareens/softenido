/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.hardcore.text;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;
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
