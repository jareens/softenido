/*
 *  RepeatedSet.java
 *
 *  Copyright (C) 2009-2012 Francisco Gómez Carrasco
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
package com.softenido.cafecore.util;

import com.softenido.cafecore.equals.EqualsBuilder;
import com.softenido.cafecore.equals.EqualsHashMap;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author franci
 */
public class RepeatedSet<E> implements Set<E>
{
    private E[] empty;
    private final Map<E, List<E>> map;

    public RepeatedSet(Comparator<E> cmp)
    {
        map = (cmp==null)?new LinkedHashMap<E, List<E>>() : new TreeMap<E, List<E>>(cmp);
    }
    public RepeatedSet(EqualsBuilder<E> cmp)
    {
        map = (cmp==null)?new HashMap<E, List<E>>() : new EqualsHashMap<E, List<E>>(cmp);
    }
    public RepeatedSet()
    {
        map = new HashMap<E, List<E>>();
    }
    
    public int size()
    {
        return map.size();
    }

    public boolean isEmpty()
    {
        return map.isEmpty();
    }

    public boolean contains(Object o)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Iterator<E> iterator()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object[] toArray()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public <E> E[] toArray(E[] a)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    @SuppressWarnings("unchecked")
    public E[][] toArray(E[][] dst)
    {
        List<E>[] values = map.values().toArray(new ArrayList[map.values().size()]);
        
        if(dst.length<values.length)
        {
            dst = Arrays6.copyOf(dst,values.length);
        }

        for (int i = 0; i < values.length; i++)
        {
            dst[i] = values[i].toArray(empty);
        }
        for (int i = values.length; i < dst.length; i++)
        {
            dst[i] = null;
        }
        return dst;
    }

    @SuppressWarnings("unchecked")
    public boolean add(E e)
    {
        List<E> list = map.get(e);
        if (list == null)
        {
            list = new ArrayList<E>();
            map.put(e, list);
            if(empty==null)
            {
                empty = (E[]) Array.newInstance(e.getClass(), 0);
            }
        }
        return list.add(e);
    }
    public boolean remove(Object o)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean containsAll(Collection<?> c)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean addAll(Collection<? extends E> c)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean retainAll(Collection<?> c)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean removeAll(Collection<?> c)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void clear()
    {
        map.clear();
    }
}
