/*
 *  HumanMillisFormat.java
 *
 *  Copyright (C) 2007-2010  Francisco Gómez Carrasco
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
package com.softenido.core.text;

import java.text.NumberFormat;

/**
 *
 * @author franci
 */
public class HumanMillisFormat
{

    private static final int[] MILLIS = {86400000, 3600000, 60000, 1000};
    private static final String[] UNITS = {"d", "h", "m", "s"};
    private static final NumberFormat FMT = NumberFormat.getIntegerInstance();
    private static final NumberFormat FMTG = NumberFormat.getIntegerInstance();
    static
    {
        FMT.setMinimumIntegerDigits(2);
        FMT.setGroupingUsed(false);
        FMTG.setMinimumIntegerDigits(2);
        FMTG.setGroupingUsed(true);
    }
    private final NumberFormat fmt;
    private final int minItems;
    private final int maxItems;

    public HumanMillisFormat(int min,int max,boolean grouping)
    {
        this.minItems = min;
        this.maxItems = max;
        fmt = grouping?FMTG:FMT;
    }

    public HumanMillisFormat(int min,int max)
    {
        this(min,max,false);
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
