/*
 * StockPrice.java
 *
 * Copyright (c) 2012  Francisco Gómez Carrasco
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.trading.stock;

import java.util.Date;

/**
 *
 * @author franci
 */
public class StockPrice
{
    static public final int OPEN =1 ;
    static public final int HIGH =2;
    static public final int LOW=3;
    static public final int CLOSE=4;
    static public final int ADJUSTED_CLOSE=5;
    static public final int VOLUME=6;
    
    final Date date;
    final double open;
    final double high;
    final double low;
    final double close;
    final double adjustedClose;
    final long volume;
   
    public StockPrice(Date date, double open, double high, double low, double close, double adjustedClose, long volume)
    {
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.adjustedClose = adjustedClose;
        this.volume = volume;
    }
    public StockPrice(Date date, double open, double high, double low, double close)
    {
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.adjustedClose = close;
        this.volume = 0;
    }

    public double getAdjustedClose()
    {
        return adjustedClose;
    }

    public double getClose()
    {
        return close;
    }

    public Date getDate()
    {
        return date;
    }

    public double getHigh()
    {
        return high;
    }

    public double getLow()
    {
        return low;
    }

    public double getOpen()
    {
        return open;
    }

    public long getVolume()
    {
        return volume;
    }
    
    public double getField(int field)
    {
        switch(field)
        {
            case OPEN:
                return open;
            case HIGH:
                return high;
            case LOW:
                return low;
            case CLOSE:
                return close;
            case ADJUSTED_CLOSE:
                return adjustedClose;
            case VOLUME:
                return volume;
            default:
                throw new IllegalArgumentException("illegal argument "+field);
        }
    }   
}
