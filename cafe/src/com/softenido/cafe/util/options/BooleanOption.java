/*
 *  BooleanOption.java
 *
 *  Copyright (C) 2009  Francisco Gómez Carrasco
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
package com.softenido.cafe.util.options;

/**
 *
 * @author franci
 */
public class BooleanOption implements Option
{

    private final char shortName;
    private final String longName;
    private final String _longName;
    private final String __longName;
    private int count = 0;
    private boolean oneHyphen = true;
    private boolean twoHyphen = true;

    public BooleanOption(char shortName, String longName)
    {
        this.shortName = shortName;
        this.longName = longName;
        this._longName = "-"+longName;
        this.__longName = "--"+longName;
    }
    public BooleanOption(String longName)
    {
        this((char)0,longName);
    }
    public String getLongName()
    {
        return longName;
    }

    public char getShortName()
    {
        return shortName;
    }

    public int getCount()
    {
        return count;
    }
    public void addCount()
    {
        count++;
    }
    public boolean isUsed()
    {
        return (count>0);
    }
    public int parseLong(int index,String[] args)
    {
        String option = args[index];
        if(twoHyphen && option.equals(__longName))
        {
            count++;
            return 1;
        }
        if(oneHyphen && option.equals(_longName))
        {
            count++;
            return 1;
        }
        return 0;
    }
}
