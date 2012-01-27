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
package com.softenido.cafedark.io;

import com.softenido.cafecore.util.regex.RegExs;
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
    final Rule[] rules;
    
    static public class Rule 
    {
        final String fileName;
        final boolean dir;
        final boolean file;
        final boolean required;

        Rule(String name, boolean dir, boolean file, boolean required)
        {
            this.fileName = name;
            this.dir = dir;
            this.file = file;
            this.required = required;
        }
        boolean verify(File file)
        {
            File fd = new File(file.getParentFile(),fileName);
            return fd.exists();
        }
        static boolean verify(File file, Rule[] rules)
        {
            if(rules==null || rules.length==0)
            {
                return true;
            }
            int count=0;
            for(Rule r : rules)
            {
                boolean ok = r.verify(file);
                if(!ok && r.required)
                    return false;
                count++;
            }
            return (count>0);
        }
    }

    public static NameFileFilter getStringInstance(String filter)
    {
        return new NameFileFilter(filter,false,false);
    }
    public static NameFileFilter getStringInstance(String filter,Rule[] rules)
    {
        return new NameFileFilter(filter,false,false,rules);
    }
    public static NameFileFilter getWildCardInstance(String filter)
    {
        return getRegExInstance(RegExs.wildcardToRegex(filter));
    }
    public static NameFileFilter getWildCardInstance(String filter,Rule[] rules)
    {
        return getRegExInstance(RegExs.wildcardToRegex(filter),rules);
    }
    public static NameFileFilter getRegExInstance(String filter)
    {
        return getRegExInstance(filter,null);
    }
    public static NameFileFilter getRegExInstance(String filter,Rule[] rules)
    {
        class NameFileFilterRegEx extends NameFileFilter
        {
            final Pattern pattern;
            public NameFileFilterRegEx(String filter, boolean ignoreCase, boolean usePath, Rule[] rules)
            {
                super(filter, ignoreCase, usePath, rules);
                this.pattern = Pattern.compile(filter);
            }           
            @Override
            public boolean accept(File file)
            {
                String name = usePath? file.getAbsolutePath():file.getName();
                return pattern.matcher(name).matches();
            }
        }
        return new NameFileFilterRegEx(filter,false,false,rules);
    }

    protected NameFileFilter(String filter, boolean ignoreCase,boolean usePath, Rule[] rules)
    {
        this.filter = filter;
        this.ignoreCase = ignoreCase;
        this.usePath = usePath;
        this.rules=rules;
    }
    protected NameFileFilter(String filter, boolean ignoreCase,boolean usePath)
    {
        this(filter, ignoreCase, usePath, null);
    }
  
    public boolean accept(File file)
    {
        String name = usePath? file.getAbsolutePath():file.getName();
        boolean ret = ignoreCase?filter.equalsIgnoreCase(name):filter.equals(name);
        return (ret && Rule.verify(file, rules));
    }
    static public Rule getDir(String name, boolean all)
    {
        return new Rule(name, true, false, all);
    }
    static public Rule getFile(String name, boolean all)
    {
        return new Rule(name, false, true, all);
    }
    @Override
    public String toString()
    {
        return "NameFileFilter{" + "filter=" + filter + ", ignoreCase=" + ignoreCase + ", usePath=" + usePath + ", rules=" + rules + '}';
    }
    
}
