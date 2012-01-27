/*
 * PositionType.java
 *
 * Copyright (c) 2011-2012  Francisco Gómez Carrasco
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

package com.softenido.trading;

/**
 *
 * @author franci
 */
public enum PositionType
{
    LONG("Long"), SHORT("Short");

    final String name;

    private PositionType(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    static public PositionType get(double limit, double stop, double target)
    {
        if(stop < limit && target > limit)
        {
            return LONG;
        }
        if(stop > limit && target < limit)
        {
            return SHORT;
        }
        return null;
    }
}
