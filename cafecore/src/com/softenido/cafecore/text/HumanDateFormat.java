/*
 * HumanDateFormat.java
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

package com.softenido.cafecore.text;

import com.softenido.cafecore.util.Dates;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author franci
 */
public class HumanDateFormat extends DateFormat
{
    final GregorianCalendar today;
    final DateFormat dFmt;
    final DateFormat tFmt;
    final DateFormat dtFmt;
    final int style;

    HumanDateFormat(Date today, int style)
    {
        this.today = new GregorianCalendar();
        this.today.setTime(today);
        this.style = style;
        if(style == DateFormat.SHORT)
        {
            this.dFmt = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
            this.tFmt = SimpleDateFormat.getTimeInstance(DateFormat.SHORT);
            this.dtFmt = null;
        }
        else
        {
            this.dFmt = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
            this.tFmt = SimpleDateFormat.getTimeInstance(DateFormat.SHORT);
            this.dtFmt = SimpleDateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
        }
    }
    public static HumanDateFormat getLongInstance(Date today)
    {
        return new HumanDateFormat(today,DateFormat.LONG);
    }
    public static HumanDateFormat getShortInstance(Date today)
    {
        return new HumanDateFormat(today,DateFormat.SHORT);
    }
    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition pos)
    {
        Calendar other = new GregorianCalendar();
        other.setTime(date);
        if(style==DateFormat.SHORT)
        {
            if(Dates.isToday(this.today,other))
            {
                return tFmt.format(date, toAppendTo, pos);
            }
            else if(Dates.isTomorrow(this.today,other))
            {
                return toAppendTo.append("Tomorrow");
            }
            else if(Dates.isYesterday(this.today,other))
            {
                return toAppendTo.append("Yesterday");
            }
            return dFmt.format(date, toAppendTo, pos);
        }
        else
        {
            if(Dates.isToday(this.today,other))
            {
                toAppendTo.append("Today ");
                return tFmt.format(date, toAppendTo, pos);
            }
            else if(Dates.isTomorrow(this.today,other))
            {
                toAppendTo.append("Tomorrow ");
                return tFmt.format(date, toAppendTo, pos);
            }
            else if(Dates.isYesterday(this.today,other))
            {
                toAppendTo.append("Yesterday ");
                return tFmt.format(date, toAppendTo, pos);
            }
            return dtFmt.format(date, toAppendTo, pos);
        }
        
    }

    @Override
    public Date parse(String source, ParsePosition pos)
    {
        return dtFmt.parse(source, pos);
    }
    
}
