/*
 *  HumanMillisFormat.java
 *
 *  Copyright (C) 2007  Francisco Gómez Carrasco
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
package org.fjtk.ce;

import java.text.NumberFormat;

/**
 *
 * @author franci
 */
public class HumanMillisFormat
{

    private static final int[] MILLIS = {86400000, 3600000, 60000, 1000};
    private static final String[] UNITS = {"d", "h", "m", "s"};
    static NumberFormat fmt = NumberFormat.getIntegerInstance();
    static
    {
        fmt.setMinimumIntegerDigits(2);
    }
    private int minItems = 1;
    private int maxItems = 4;

    public HumanMillisFormat()
    {
    }

    public HumanMillisFormat(int min)
    {
        this.minItems = min;
    }
    public HumanMillisFormat(int min,int max)
    {
        this.minItems = min;
        this.maxItems = max;
    }

    public int getMaxItems()
    {
        return maxItems;
    }

    public void setMaxItems(int maxItems)
    {
        this.maxItems = maxItems;
    }

    public int getMinItems()
    {
        return minItems;
    }

    public void setMinItems(int minItems)
    {
        this.minItems = minItems;
    }

    public String toString(long millis)
    {
        StringBuffer txt = new StringBuffer();
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
                txt.append(fmt.format(num) + UNITS[i]);
                items++;
            }
        }

        return txt.toString();
    }
}
