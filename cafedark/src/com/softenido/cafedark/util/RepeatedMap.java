/*
 *  RepeatedMap.java
 *
 *  Copyright (C) 2009-2011 Francisco Gómez Carrasco
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
package com.softenido.cafedark.util;

import com.softenido.cafecore.equals.EqualsBuilder;
import com.softenido.cafecore.equals.EqualsHashMap;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author franci
 */
public class RepeatedMap<K,V> implements Map<K,V>
{
    private V[] empty;
    private final Map<K, List<V>> map;

    public RepeatedMap(Comparator<K> cmp)
    {
        map = (cmp==null)?new HashMap<K, List<V>>() : new TreeMap<K, List<V>>(cmp);
    }
    public RepeatedMap(EqualsBuilder<K> cmp)
    {
        map = (cmp==null)?new HashMap<K, List<V>>() : new EqualsHashMap<K, List<V>>(cmp);
    }
    public RepeatedMap()
    {
        map = new HashMap<K, List<V>>();
    }

    public int size()
    {
        return map.size();
    }

    public boolean isEmpty()
    {
        return map.isEmpty();
    }

    @SuppressWarnings("element-type-mismatch")
    public boolean containsKey(Object key)
    {
        return map.containsKey(key);
    }

    public boolean containsValue(Object value)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public V get(Object key)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    @SuppressWarnings("unchecked")
    public V put(K key, V value)
    {
        List<V> list = map.get(key);
        if (list == null)
        {
            list = new ArrayList<V>();
            map.put(key, list);
            if(empty==null)
            {
                empty = (V[]) Array.newInstance(value.getClass(), 0);
            }
        }
        list.add(value);
        return list.get(0);
    }

    public V remove(Object key)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void putAll(Map<? extends K, ? extends V> m)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void clear()
    {
        map.clear();
    }

    public Set<K> keySet()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<V> values()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Entry<K, V>> entrySet()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    @SuppressWarnings("unchecked")
    public V[][] toArray()
    {
        if(empty==null)
            return null;
        V[][] dst = (V[][]) Array.newInstance(empty.getClass(),map.size());

        List<V>[] values = map.values().toArray(new ArrayList[0]);

        for (int i = 0; i < dst.length; i++)
        {
            dst[i] = values[i].toArray(empty);
        }
        return dst;
    }

}
