/*
 *  FileComparatorByName.java
 *
 *  Copyright (C) 2010  Francisco Gómez Carrasco
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

import com.softenido.cafecore.equals.EqualsFilterBuilder;
import com.softenido.cafedark.io.virtual.VirtualFile;
import java.util.Comparator;

/**
 *
 * @author franci
 */
public class FileComparatorByName extends EqualsFilterBuilder<VirtualFile> implements Comparator<VirtualFile>
{
    private final boolean half;
    private final boolean ignoreCase;

    public FileComparatorByName(boolean half,boolean ignoreCase)
    {
        this.half = half;
        this.ignoreCase = ignoreCase;
    }

    @Override
    public int compare(VirtualFile pf1, VirtualFile pf2)
    {
        if(pf1==pf2)
        {
            return 0;
        }
        String name1 = pf1.getName();
        String name2 = pf2.getName();
        if(half)
        {
            int size1 = name1.length();
            int size2 = name2.length();
            return (size1<size2 ? -1 : (size1==size2 ? 0 : 1));
        }
        return ignoreCase?name1.compareToIgnoreCase(name2):name1.compareTo(name2);
    }

    @Override
    public int filterCode(VirtualFile e)
    {
        return e.getName().length();
    }

    @Override
    public boolean filterEquals(VirtualFile pf1, VirtualFile pf2)
    {
        if(pf1==pf2)
        {
            return false;
        }
        String name1 = pf1.getName();
        String name2 = pf2.getName();
        if(half)
        {
            int size1 = name1.length();
            int size2 = name2.length();
            return (size1==size2);
        }
        return ignoreCase?name1.equalsIgnoreCase(name2):name1.equals(name2);
    }
   
}
