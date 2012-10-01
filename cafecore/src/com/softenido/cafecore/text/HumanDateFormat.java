/*
 * HumanDateFormat.java
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

package com.softenido.cafecore.text;

import com.softenido.cafecore.util.Dates;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author franci
 */
public class HumanDateFormat extends DateFormat
{
    private final int style;
    private final GregorianCalendar today;
    private final DateFormat todayTime;
    private final DateFormat sameYearDate;
    private final DateFormat sameYearTime;
    private final DateFormat absoluteDate;
    private final DateFormat absoluteTime;
    

    private HumanDateFormat(Date today, int style, Locale locale)
    {
        this.today = new GregorianCalendar();
        this.today.setTime(today);
        this.style=style;
        switch(style)
        {
            default:
            case DateFormat.SHORT:
                this.todayTime = SimpleDateFormat.getTimeInstance(DateFormat.SHORT, locale);
                this.sameYearDate = new SimpleDateFormat("MMM d", locale);
                this.sameYearTime = null;
                this.absoluteDate = new SimpleDateFormat("yyyy-MM-dd", locale);
                this.absoluteTime = null;
                break;
            case DateFormat.MEDIUM:
                this.todayTime = SimpleDateFormat.getTimeInstance(DateFormat.MEDIUM, locale);
                this.sameYearDate = new SimpleDateFormat("MMM d", locale);
                this.sameYearTime = SimpleDateFormat.getTimeInstance(DateFormat.SHORT, locale);
                this.absoluteDate = new SimpleDateFormat("yyyy-MM-dd", locale);
                this.absoluteTime = this.sameYearTime;
                break;
            case DateFormat.LONG:
                this.todayTime = SimpleDateFormat.getTimeInstance(DateFormat.MEDIUM, locale);
                this.sameYearDate = new SimpleDateFormat("MMM d", locale);
                this.sameYearTime = this.todayTime;
                this.absoluteDate = new SimpleDateFormat("yyyy-MM-dd", locale);
                this.absoluteTime = this.sameYearTime;
                break;
            case DateFormat.FULL:
                this.todayTime = null;
                this.sameYearDate = null;
                this.sameYearTime = null;
                this.absoluteDate = new SimpleDateFormat("yyyy-MM-dd", locale);
                this.absoluteTime = SimpleDateFormat.getTimeInstance(DateFormat.MEDIUM, locale);
                // cuando sepa como hay que poner 9:19:29  como 09:19:29
                break;
        }
        
    }
    public static HumanDateFormat getFullInstance(Date today, Locale locale)
    {
        return new HumanDateFormat(today, DateFormat.FULL, locale);
    }
    public static HumanDateFormat getFullInstance(Date today)
    {
        return new HumanDateFormat(today,DateFormat.FULL, Locale.getDefault());
    }
    public static HumanDateFormat getLongInstance(Date today, Locale locale)
    {
        return new HumanDateFormat(today,DateFormat.LONG, locale);
    }
    public static HumanDateFormat getLongInstance(Date today)
    {
        return new HumanDateFormat(today,DateFormat.LONG, Locale.getDefault());
    }
    public static HumanDateFormat getMediumInstance(Date today, Locale locale)
    {
        return new HumanDateFormat(today,DateFormat.MEDIUM, locale);
    }
    public static HumanDateFormat getMediumInstance(Date today)
    {
        return new HumanDateFormat(today,DateFormat.MEDIUM, Locale.getDefault());
    }
    public static HumanDateFormat getShortInstance(Date today, Locale locale)
    {
        return new HumanDateFormat(today,DateFormat.SHORT, locale);
    }
    public static HumanDateFormat getShortInstance(Date today)
    {
        return new HumanDateFormat(today,DateFormat.SHORT, Locale.getDefault());
    }
    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition pos)
    {
        DateFormat dfDate;
        DateFormat dfTime;
        if(style==DateFormat.FULL)
        {
            dfDate = absoluteDate;
            dfTime = absoluteTime;
        }
        else
        {
            Calendar other = new GregorianCalendar();
            other.setTime(date);

            if(todayTime!= null && Dates.isToday(this.today,other))
            {
                return todayTime.format(date, toAppendTo, pos);
            }
            if(Dates.isSameYear(this.today,other) )
            {
                dfDate = sameYearDate;
                dfTime = sameYearTime;
            }
            else
            {
                dfDate = absoluteDate;
                dfTime = absoluteTime;
            }
        }
        toAppendTo = dfDate.format(date, toAppendTo, pos);
        if(dfTime!=null)
        {
            toAppendTo.append(' ');
            pos.setEndIndex(pos.getEndIndex()+1);
            toAppendTo = dfTime.format(date, toAppendTo, pos);
        }
        return toAppendTo;
    }

    @Override
    public Date parse(String source, ParsePosition pos)
    {
        return absoluteDate.parse(source, pos);
    }
    static ResourceBundle getResourceBundle(Locale locale)
    {
        return ResourceBundle.getBundle(HumanDateFormat.class.getName(), locale);
    }    
}
