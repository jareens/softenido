/*
 *  HumanMillisFormat.java
 *
 *  Copyright (C) 2007-2011  Francisco Gómez Carrasco
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  Report bugs or new features to: flikxxi@gmail.com
 *
 */
package com.softenido.cafecore.text;

import java.text.NumberFormat;

/**
 *
 * @author franci
 */
public class HumanMillisFormat
{
    public static final int DAY = 86400000;
    public static final int HOUR = 3600000;
    public static final int MINUTE = 60000;
    public static final int SECOND = 1000;

    private static final int[] MILLIS = {DAY, HOUR, MINUTE, SECOND, 0};
    private static final String[] UNITS = {"d", "h", "m", "s", "ms"};
    private static final NumberFormat FMT1 = NumberFormat.getIntegerInstance();
    private static final NumberFormat FMT2 = NumberFormat.getIntegerInstance();
    static
    {
        FMT1.setMinimumIntegerDigits(1);
        FMT2.setMinimumIntegerDigits(2);
    }
    private final NumberFormat fmt;
    private final int minItems;
    private final int maxItems;
    private final int round;

    public HumanMillisFormat(int min,int max,boolean padding, int round)
    {
        this.minItems = min;
        this.maxItems = max;
        this.fmt = padding?FMT2:FMT1;
        this.round=round;
    }
    public HumanMillisFormat(int min,int max,boolean padding)
    {
        this(min,max,padding,0);
    }
    public HumanMillisFormat(int min,int max)
    {
        this(min,max,false,0);
    }

    public HumanMillisFormat(int min)
    {
        this(min,4);
    }
    public HumanMillisFormat()
    {
        this(1);
    }

    public int getMaxItems()
    {
        return maxItems;
    }

    public int getMinItems()
    {
        return minItems;
    }
    public String toString(long millis)
    {
        int first = MILLIS.length-1;
        int last  = 0;
                
        if(round>0)
        {
            millis = millis - (millis % round);
        }
        
        long tokens[] = new long[MILLIS.length];
        
        for(int i=0;i<MILLIS.length && round<=MILLIS[i];i++)
        {
            long item=millis;
            if(MILLIS[i]>0)
            {
                item   = (millis / MILLIS[i]);
                millis = (millis % MILLIS[i]);
            }
            
            tokens[i] = item;
            if(item>0)
            {
                first = Math.min(first, i);
                last  = Math.max(last, i);
            }
        }
        while( last-first+1 > maxItems && last>first)
            last--;
        while( last-first+1 < minItems && last<MILLIS.length-1 && round<MILLIS[last])
            last++;
        while( last-first+1 < minItems && first>0)
            first--;
            
        StringBuilder txt = new StringBuilder();
        
        for(int i=0;i<MILLIS.length;i++)
        {
            if(i>=first && i<=last)
            {
                txt.append(fmt.format(tokens[i])).append(UNITS[i]);
            }
        }
        return txt.toString();
    }
    public String toString2(long millis)
    {
        StringBuilder txt = new StringBuilder();
        int items = 0;
        int num;

        for (int i = 0; i < MILLIS.length && items < maxItems; i++)
        {
            num = (int) (millis / MILLIS[i]);
            millis = millis % MILLIS[i];

            if (num > 0 || items>0 || i>=MILLIS.length-minItems)
            {
                if (items >= maxItems && millis * 2 > MILLIS[i])
                {
                    num++;
                }
                txt.append(fmt.format(num)).append(UNITS[i]);
                items++;
            }
        }
        return txt.toString();
    }
}
