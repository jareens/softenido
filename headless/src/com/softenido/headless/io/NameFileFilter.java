/*
 *  NameFileFilter.java
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
package com.softenido.headless.io;

import com.softenido.headless.util.regex.RegExs;
import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

/**
 *
 * @author franci
 */
public class NameFileFilter implements FileFilter
{
    final String filter;
    final boolean ignoreCase;
    final boolean usePath;

    public static NameFileFilter getStringInstance(String filter)
    {
        return new NameFileFilter(filter,false,false);
    }
    public static NameFileFilter getWildCardInstance(String filter)
    {
        return getRegExInstance(RegExs.wildcardToRegex(filter));
    }
    public static NameFileFilter getRegExInstance(String filter)
    {
        class NameFileFilterRegEx extends NameFileFilter
        {
            final Pattern pattern;
            public NameFileFilterRegEx(String filter, boolean ignoreCase, boolean usePath)
            {
                super(filter, ignoreCase, usePath);
                this.pattern = Pattern.compile(filter);
            }           
            @Override
            public boolean accept(File file)
            {
                String name = usePath? file.getAbsolutePath():file.getName();
                return pattern.matcher(name).matches();
            }
        }
        return new NameFileFilterRegEx(filter,false,false);
    }

    protected NameFileFilter(String filter, boolean ignoreCase,boolean usePath)
    {
        this.filter = filter;
        this.ignoreCase = ignoreCase;
        this.usePath = usePath;
    }
  
    public boolean accept(File file)
    {
        String name = usePath? file.getAbsolutePath():file.getName();
        return ignoreCase?filter.equalsIgnoreCase(name):filter.equals(name);
    }

}
