/*
 *  BucketMap.java
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
package com.softenido.findrepe;

import com.softenido.core.equals.EqualsBuilder;
import com.softenido.core.equals.EqualsHashMap;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author franci
 */
public class _BucketMap<E>
{
    private E[] empty;
    private final Map<E, List<E>> map;

    public _BucketMap(Comparator<E> cmp)
    {
        map = (cmp==null)?new LinkedHashMap<E, List<E>>() : new TreeMap<E, List<E>>(cmp);
    }
    public _BucketMap(EqualsBuilder<E> cmp)
    {
        map = (cmp==null)?new HashMap<E, List<E>>() : new EqualsHashMap<E, List<E>>(cmp);
    }
    public _BucketMap()
    {
        map = new HashMap<E, List<E>>();
    }
    public void add(E item)
    {
        List<E> list = map.get(item);
        if (list == null)
        {
            list = new ArrayList<E>();
            map.put(item, list);
            if(empty==null)
            {
                empty = (E[]) Array.newInstance(item.getClass(), 0);
            }
        }
        list.add(item);
    }
    public E[][] toArray()
    {
        if(empty==null)
            return null;
        E[][] dst = (E[][]) Array.newInstance(empty.getClass(),map.size());
        
        List<E>[] values = map.values().toArray(new ArrayList[0]);

        for (int i = 0; i < dst.length; i++)
        {
            dst[i] = values[i].toArray(empty);
        }
        return dst;
    }
}
