/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.hardcore.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author franci
 */
public class Dates
{
    /**
     * 
     * @param today
     * @param other
     * @return
     */
    static public boolean isToday(Calendar today, Calendar other)
    {
        return today.get(Calendar.YEAR) == other.get(Calendar.YEAR) &&
               today.get(Calendar.DAY_OF_YEAR) == other.get(Calendar.DAY_OF_YEAR);
    }
    /**
     * 
     * @param today
     * @param other
     * @return
     */
    static public boolean isToday(Date today, Date other)
    {
        Calendar todayCalendar = new GregorianCalendar();
        Calendar otherCalendar = new GregorianCalendar();
        todayCalendar.setTime(today);
        otherCalendar.setTime(other);
        return isToday(todayCalendar, otherCalendar);
    }
    /**
     * 
     * @param today
     * @param other
     * @return
     */
    static public boolean isYesterday(Calendar today, Calendar other)
    {
        other = (Calendar) other.clone();
        other.add(Calendar.DAY_OF_MONTH, 1);
        return isToday(today,other);
    }
    /**
     * 
     * @param today
     * @param other
     * @return
     */
    static public boolean isYesterday(Date today, Date other)
    {
        Calendar todayCalendar = new GregorianCalendar();
        Calendar otherCalendar = new GregorianCalendar();
        todayCalendar.setTime(today);
        otherCalendar.setTime(other);
        return isYesterday(todayCalendar, otherCalendar);
    }
    /**
     * 
     * @param today
     * @param other
     * @return
     */
    static public boolean isTomorrow(Calendar today, Calendar other)
    {
        other = (Calendar) other.clone();
        other.add(Calendar.DAY_OF_MONTH, -1);
        return isToday(today,other);
    }
    /**
     * 
     * @param today
     * @param other
     * @return
     */
    static public boolean isTomorrow(Date today, Date other)
    {
        Calendar todayCalendar = new GregorianCalendar();
        Calendar otherCalendar = new GregorianCalendar();
        todayCalendar.setTime(today);
        otherCalendar.setTime(other);
        return isTomorrow(todayCalendar, otherCalendar);
    }
}
