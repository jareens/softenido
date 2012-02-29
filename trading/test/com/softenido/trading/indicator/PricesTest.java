/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.trading.indicator;

import com.softenido.trading.stock.Stock;
import java.util.Date;
import java.util.GregorianCalendar;
import org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author franci
 */
public class PricesTest
{
    static class Day
    {
        final Date date;
        final double values[];

        public Day(Date date, double open, double high, double low, double close, double volume, double adjClose)
        {
            this.date = date;
            this.values = new double[]{open, high, low, close, volume, adjClose};
        }
    }

    static private Day day(int year, int month, int day, double open, double high, double low, double close, double volume, double adjClose)
    {
        Date date = new GregorianCalendar(year, month, day).getTime();
        return new Day(date,open, high, low, close, volume, adjClose);
    }
    static final Day[] DATA=
    {
        day(2012,02, 9,193.03,194.46,192.55,193.13, 3876400,193.13),
        day(2012,02, 8,192.78,193.58,191.73,192.95, 3803800,192.95),
        day(2012,02, 7,192.45,194.14,191.97,193.35, 3433000,192.60),
        day(2012,02, 6,192.48,193.76,192.00,192.82, 3639800,192.07),
        day(2012,02, 3,192.93,194.13,192.54,193.64, 4521700,192.89),
        day(2012,02, 2,192.72,193.33,191.33,191.53, 3907300,190.79),
        day(2012,02, 1,193.21,194.81,192.41,192.62, 5088800,191.87),
        day(2012,01,31,193.09,193.10,191.00,192.60, 4826800,191.85),
        day(2012,01,30,189.39,192.73,188.22,192.50, 4359000,191.75),
        day(2012,01,27,190.01,191.77,189.81,190.46, 3360400,189.72),
        day(2012,01,26,191.79,192.79,190.47,190.98, 4004700,190.24),
        day(2012,01,25,191.33,192.24,189.61,191.73, 4359700,190.99),
        day(2012,01,24,188.63,192.30,188.52,191.93, 5345700,191.19),
        day(2012,01,23,187.91,190.52,187.67,189.98, 5751700,189.24),
        day(2012,01,20,185.77,188.97,184.75,188.52,12849700,187.79),
        day(2012,01,19,181.79,182.36,180.35,180.52, 8567200,179.82),
        day(2012,01,18,179.83,181.60,179.50,181.07, 4600600,180.37),
        day(2012,01,17,180.36,182.00,179.32,180.00, 6003400,179.30),
        day(2012,01,13,179.48,179.61,177.35,179.16, 5279200,178.47),
        day(2012,01,12,181.86,181.91,178.38,180.55, 6881000,179.85),
        day(2012,01,11,180.73,182.81,180.50,182.32, 4110800,181.61),
        day(2012,01,10,183.23,183.72,181.20,181.31, 5161000,180.61),
        day(2012,01, 9,182.20,182.27,180.27,181.59, 5201200,180.89),
        day(2012,01, 6,184.39,184.48,182.31,182.54, 4897100,181.83),
        day(2012,01, 5,184.81,185.03,183.10,184.66, 4463100,183.94),
        day(2012,01, 4,185.57,186.33,184.94,185.54, 4346700,184.82),
        day(2012,01, 3,186.73,188.71,186.00,186.30, 5646000,185.58)
    };
    public PricesTest()
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

    @Test
    public void testSomeMethod()
    {
        Stock stock = new Stock("Cocacola", "KO", "NYSE", "USD");
        
        String[] fields = new String[]{"Date","Open","High","Low","Close","Volume","Adj Close"};
        
        Date[] dates = new Date[DATA.length];
        double[][] values = new double[DATA.length][];

        for(int i=0;i<DATA.length;i++)
        {
            dates[i]=DATA[i].date;
            values[i]=DATA[i].values;
        }
        
        Prices prices = new Prices(stock,fields, dates, values);
        
        Date startDate = DATA[0].date;
        Date endDate = DATA[DATA.length-1].date;
        
        // dates
        Assert.assertArrayEquals(dates,prices.getDates());
        //values
        Assert.assertArrayEquals(values,prices.getValues());
    }
}
