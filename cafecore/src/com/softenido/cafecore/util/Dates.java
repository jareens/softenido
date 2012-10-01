/*
 * Dates.java
 *
 * Copyright (c) 2011-2012 Francisco Gómez Carrasco
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
     * @param today the date to be considered as today
     * @param other the date with is compared
     * @return true if both dates contains the same day
     */
    static public boolean isToday(Calendar today, Calendar other)
    {
        return today.get(Calendar.YEAR) == other.get(Calendar.YEAR) &&
               today.get(Calendar.DAY_OF_YEAR) == other.get(Calendar.DAY_OF_YEAR);
    }
    /**
     *
     * @param today the date to be considered as today
     * @param other the date with is compared
     * @return true if both dates contains the same day
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
     * @param today the date to be considered as today
     * @param other the date with is compared
     * @return true if other is the previous day of today
     */
    static public boolean isYesterday(Calendar today, Calendar other)
    {
        other = (Calendar) other.clone();
        other.add(Calendar.DAY_OF_MONTH, 1);
        return isToday(today,other);
    }
    /**
     *
     * @param today the date to be considered as today
     * @param other the date with is compared
     * @return true if other is the previous day of today
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
     * @param today the date to be considered as today
     * @param other the date with is compared
     * @return true if other is the next day of today
     */
    static public boolean isTomorrow(Calendar today, Calendar other)
    {
        other = (Calendar) other.clone();
        other.add(Calendar.DAY_OF_MONTH, -1);
        return isToday(today,other);
    }
    /**
     *
     * @param today the date to be considered as today
     * @param other the date with is compared
     * @return true if other is the next day of today
     */
    static public boolean isTomorrow(Date today, Date other)
    {
        Calendar todayCalendar = new GregorianCalendar();
        Calendar otherCalendar = new GregorianCalendar();
        todayCalendar.setTime(today);
        otherCalendar.setTime(other);
        return isTomorrow(todayCalendar, otherCalendar);
    }

    /**
     * 
     * @param today the date to be considered as today
     * @param other the date with is compared
     * @return true if both dates contains the same day
     */
    static public boolean isSameYear(Calendar today, Calendar other)
    {
        return today.get(Calendar.YEAR) == other.get(Calendar.YEAR);
    }
    /**
     *
     * @param today the date to be considered as today
     * @param other the date with is compared
     * @return true if both dates contains the same day
     */
    static public boolean isSameYear(Date today, Date other)
    {
        Calendar todayCalendar = new GregorianCalendar();
        Calendar otherCalendar = new GregorianCalendar();
        todayCalendar.setTime(today);
        otherCalendar.setTime(other);
        return isSameYear(todayCalendar, otherCalendar);
    }
}
