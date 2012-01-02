/*
 *  NumberOption.java
 *
 *  Copyright (C) 2009-2010 Francisco Gómez Carrasco
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
package com.softenido.cafedark.util.options;

import com.softenido.cafecore.util.SizeUnits;

/**
 *
 * @author franci
 */
public class SizeOption extends StringOption
{
    private static final SizeUnits sizeParser = new SizeUnits();
    
    public SizeOption(String longName)
    {
        super(longName);
    }
    public SizeOption(char shortName, String longName)
    {
        super(shortName, longName);
    }

    public long longValue() throws MissingOptionParameterException, InvalidOptionParameterException
    {
        String value = getValue();
        try
        {
            return sizeParser.parse(value);
        }
        catch (NumberFormatException ex)
        {
            throw new InvalidOptionParameterException(getUsedName(),value);
        }
    }
    public long longValue(long def) throws InvalidOptionParameterException
    {
        try
        {
            return isUsed() ? longValue() : def;
        }
        catch (MissingOptionParameterException ex)
        {
            return def;
        }
    }     
}
