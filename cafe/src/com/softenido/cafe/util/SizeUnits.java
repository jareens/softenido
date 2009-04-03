/*
 *  SizeUnits.java
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
package com.softenido.cafe.util;

/**
 *
 * @author franci
 */
public class SizeUnits extends AbstractUnits
{

    final static long[] VALUES = new long[]
    {
        1, 1024, 1024 * 1024, 1024 * 1024 * 1024, 1024 * 1024 * 1024 * 1024
    };
    final static String[] SHORT_NAMES = new String[]
    {
        "b", "k", "m", "g", "t"
    };
    final static String[] LONG_NAMES = new String[]
    {
        "", "kilo", "mega", "giga", "tera"
    };

    public SizeUnits()
    {
        super(VALUES, SHORT_NAMES, LONG_NAMES);
    }
}
