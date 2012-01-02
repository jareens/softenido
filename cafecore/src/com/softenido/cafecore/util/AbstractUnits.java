/*
 * AbstractUnits.java
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
package com.softenido.cafecore.util;

/**
 *
 * @author franci
 */
public class AbstractUnits
{

    final long[] values;
    final String[] shortNames;
    final String[] longNames;
    final boolean ignoreCase;

    public AbstractUnits(long[] values, String[] shortNames, String[] longNames, boolean ignoreCase)
    {
        this.values = values;
        this.shortNames = shortNames;
        this.longNames = longNames;
        this.ignoreCase = ignoreCase;
    }

    public AbstractUnits(long[] values, String[] shortNames, String[] longNames)
    {
        this(values, shortNames, longNames, true);
    }

    public long parse(String nm) throws NumberFormatException
    {
        if (ignoreCase)
        {
            nm = nm.toLowerCase();
        }
        for (int i = 0; i < shortNames.length; i++)
        {
            if (nm.endsWith(shortNames[i]))
            {
                String val = nm.substring(0, nm.length() - shortNames[i].length());
                return Long.parseLong(val) * values[i];
            }
        }
        return Long.parseLong(nm);
    }
    public String toString(long val,boolean round)
    {
        for (int i = values.length-1; i >=0; i--)
        {
            long unit = (round ? Math.round((double) val / values[i]) : val / values[i]);
            if (Math.abs(val)>=values[i])
            {
                return ""+unit+shortNames[i];
            }
        }
        return ""+val;
    }
}
