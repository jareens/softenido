/*
 *  FindRepeOptions.java
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

package com.softenido.findrepe;

import com.softenido.cafe.io.ForEachFileOptions;

/**
 *
 * @author franci
 */
public class FindRepeOptions extends ForEachFileOptions
{
    int minCount;
    int maxCount;
    public FindRepeOptions()
    {
        super();
        minCount = 0;
        maxCount = Integer.MAX_VALUE;
    }

    public FindRepeOptions(ForEachFileOptions val)
    {
        super(val);
        minCount = 0;
        maxCount = Integer.MAX_VALUE;
    }

    public FindRepeOptions(FindRepeOptions val)
    {
        super(val);
        minCount = val.minCount;
        maxCount = val.maxCount;
    }

    public int getMaxCount()
    {
        return maxCount;
    }

    public void setMaxCount(int maxCount)
    {
        this.maxCount = maxCount;
    }

    public int getMinCount()
    {
        return minCount;
    }

    public void setMinCount(int minCount)
    {
        this.minCount = minCount;
    }
}
