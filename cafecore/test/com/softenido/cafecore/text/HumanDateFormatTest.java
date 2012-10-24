/*
 * HumanDateFormatTest.java
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
package com.softenido.cafecore.text;

import org.junit.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author franci
 */
public class HumanDateFormatTest
{
    
    Calendar yesterdayCalendar;
    Calendar todayCalendar;
    Calendar nowCalendar;
    Calendar tomorrowCalendar;
    Calendar januaryCalendar;
    Calendar februaryCalendar;
    Date yesterday;
    Date today;
    Date now;
    Date tomorrow;
    Date january;
    Date february;
    
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
        int nov = Calendar.NOVEMBER;
        int jan = Calendar.JANUARY;
        int feb = Calendar.FEBRUARY;
        yesterdayCalendar  = new GregorianCalendar(2011, nov, 6, 9, 19, 29);
        todayCalendar      = new GregorianCalendar(2011, nov, 7, 9, 19, 29);
        nowCalendar        = new GregorianCalendar(2011, nov, 7, 19, 29, 39);
        tomorrowCalendar   = new GregorianCalendar(2011, nov, 8, 9, 19, 29);
        januaryCalendar    = new GregorianCalendar(2011, jan, 11, 9, 19, 29);
        februaryCalendar   = new GregorianCalendar(2012, feb, 22, 9, 19, 29);
        
        yesterday = yesterdayCalendar.getTime();
        today     = todayCalendar.getTime();
        now       = nowCalendar.getTime();
        tomorrow  = tomorrowCalendar.getTime();
        january   = januaryCalendar.getTime();
        february  = februaryCalendar.getTime();
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
//        for(Locale loc : Locale.getAvailableLocales())
//        {
//            //if(loc.getCountry().length()==0)
//            if(!loc.toString().startsWith("es_"))
//                continue;
//            System.out.println(loc.toString());
//            DateFormat tsdf;
//            DateFormat dsdf;
//            for(int i=DateFormat.FULL;i<=DateFormat.SHORT;i++)
//            {
//                dsdf = SimpleDateFormat.getDateInstance(i,loc);
//                tsdf = SimpleDateFormat.getTimeInstance(i,loc);
//                Calendar c = tsdf.getCalendar();
//                System.out.println("( "+dsdf.format(c.getTime())+" )( "+tsdf.format(c.getTime())+" )");
//            }
//        }
        HumanDateFormat hdf;
        hdf= (HumanDateFormat) HumanDateFormat.getShortInstance(now, Locale.US);
        assertEquals("Nov 6", hdf.format(yesterday));
        assertEquals("9:19 AM", hdf.format(today));
        assertEquals("7:29 PM", hdf.format(now));
        assertEquals("Nov 8", hdf.format(tomorrow));
        assertEquals("Jan 11", hdf.format(january));
        assertEquals("2012-02-22", hdf.format(february));
        
        hdf = (HumanDateFormat) HumanDateFormat.getShortInstance(now, new Locale("es_ES"));
        assertEquals("nov 6", hdf.format(yesterday));
        assertEquals("9:19", hdf.format(today));
        assertEquals("19:29", hdf.format(now));
        assertEquals("nov 8", hdf.format(tomorrow));
        assertEquals("ene 11", hdf.format(january));
        assertEquals("2012-02-22", hdf.format(february));
        
        hdf= (HumanDateFormat) HumanDateFormat.getMediumInstance(now, Locale.US);
        assertEquals("Nov 6 9:19 AM", hdf.format(yesterday));
        assertEquals("9:19:29 AM", hdf.format(today));
        assertEquals("7:29:39 PM", hdf.format(now));
        assertEquals("Nov 8 9:19 AM", hdf.format(tomorrow));
        assertEquals("Jan 11 9:19 AM", hdf.format(january));
        assertEquals("2012-02-22 9:19 AM", hdf.format(february));
        
        hdf = (HumanDateFormat) HumanDateFormat.getMediumInstance(now, new Locale("es_ES"));
        assertEquals("nov 6 9:19", hdf.format(yesterday));
        assertEquals("9:19:29", hdf.format(today));
        assertEquals("19:29:39", hdf.format(now));
        assertEquals("nov 8 9:19", hdf.format(tomorrow));
        assertEquals("ene 11 9:19", hdf.format(january));
        assertEquals("2012-02-22 9:19", hdf.format(february));

        hdf= (HumanDateFormat) HumanDateFormat.getLongInstance(now, Locale.US);
        assertEquals("Nov 6 9:19:29 AM", hdf.format(yesterday));
        assertEquals("9:19:29 AM", hdf.format(today));
        assertEquals("7:29:39 PM", hdf.format(now));
        assertEquals("Nov 8 9:19:29 AM", hdf.format(tomorrow));
        assertEquals("Jan 11 9:19:29 AM", hdf.format(january));
        assertEquals("2012-02-22 9:19:29 AM", hdf.format(february));
        
        hdf = (HumanDateFormat) HumanDateFormat.getLongInstance(today, new Locale("es_ES"));
        assertEquals("nov 6 9:19:29", hdf.format(yesterday));
        assertEquals("9:19:29", hdf.format(today));
        assertEquals("19:29:39", hdf.format(now));
        assertEquals("nov 8 9:19:29", hdf.format(tomorrow));
        assertEquals("ene 11 9:19:29", hdf.format(january));
        assertEquals("2012-02-22 9:19:29", hdf.format(february));

        hdf= (HumanDateFormat) HumanDateFormat.getFullInstance(now, Locale.US);
        assertEquals("2011-11-06 09:19:29.000", hdf.format(yesterday));
        assertEquals("2011-11-07 09:19:29.000", hdf.format(today));
        assertEquals("2011-11-07 19:29:39.000", hdf.format(now));
        assertEquals("2011-11-08 09:19:29.000", hdf.format(tomorrow));
        assertEquals("2011-01-11 09:19:29.000", hdf.format(january));
        assertEquals("2012-02-22 09:19:29.000", hdf.format(february));

        hdf = (HumanDateFormat) HumanDateFormat.getFullInstance(now, new Locale("es_ES"));
        assertEquals("2011-11-06 09:19:29.000", hdf.format(yesterday));
        assertEquals("2011-11-07 09:19:29.000", hdf.format(today));
        assertEquals("2011-11-07 19:29:39.000", hdf.format(now));
        assertEquals("2011-11-08 09:19:29.000", hdf.format(tomorrow));
        assertEquals("2011-01-11 09:19:29.000", hdf.format(january));
        assertEquals("2012-02-22 09:19:29.000", hdf.format(february));
        
        hdf = (HumanDateFormat) HumanDateFormat.getRFC822TimeZoneInstance(new Locale("es_ES"));
        assertEquals("2011-11-06 09:19:29.000+0100", hdf.format(yesterday));
        hdf = (HumanDateFormat) HumanDateFormat.getGeneralTimeZoneInstance(new Locale("es_ES"));
        assertEquals("2011-11-06 09:19:29.000 CET", hdf.format(yesterday));
        
    }
}
