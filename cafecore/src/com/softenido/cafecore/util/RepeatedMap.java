/*
 * RepeatedMap.java
 *
 * Copyright (c) 2012 Francisco Gómez Carrasco
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
package com.softenido.cafecore.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author franci
 */
public class RepeatedMap<K,V> implements Map<K,V>
{
    final HashMap<K,List<V>> map = new HashMap<K,List<V>>();
    
    public int size()
    {
        return this.map.size();
    }

    public boolean isEmpty()
    {
        return this.map.isEmpty();
    }

    public boolean containsKey(Object key)
    {
        return this.map.containsKey(key);
    }
    
    public boolean containsValue(Object value)
    {
        for( List<V> item : map.values() )
        {
            if(item.contains(value))
                return true;
        }
        return false;
    }

    public V get(Object key)
    {
        List<V> list = map.get(key);
        if(list==null || list.isEmpty())
        {
            return null;
        }
        return list.get(0);
    }
    
    public V[] get(V[] buf, Object key)
    {
        List<V> list = map.get(key);
        return list!=null?list.toArray(buf):null;
    }

    public V put(K key, V value)
    {
        List<V> list = map.get(key);
        if(list == null)
        {
            list = new ArrayList<V>();
            map.put(key, list);
        }
        list.add(value);
        return list.get(0);
    }

    public V remove(Object key)
    {
        V value = null;
        List<V> list = map.get(key);
        if(list!=null)
        {
            if(list.isEmpty())
            {
                map.remove(key);
            }
            else
            {
                value = list.get(0);
            }
        }
        return value;
    }

    public void putAll(Map<? extends K, ? extends V> t)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void clear()
    {
        map.clear();
    }

    public Set<K> keySet()
    {
        return map.keySet();
    }

    public Collection<V> values()
    {
        ArrayList<V> list = new ArrayList<V>();
        for( Entry<K, List<V>> entry : map.entrySet() )
        {
            List<V> lv = entry.getValue();
            for( V v : lv)
            {
                list.add(v);
            }
        }
        return list;
    }

    public Set<Entry<K, V>> entrySet()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString()
    {
        return "RepeatedMap{" + "map=" + map + '}';
    }

    
}
