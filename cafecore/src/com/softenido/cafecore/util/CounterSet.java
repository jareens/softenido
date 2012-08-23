/*
 * CounterSet.java
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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author franci
 */
public class CounterSet<E> implements Set<E>
{
    int count=0;
    final HashMap<E,SimpleInteger> map;

    public CounterSet()
    {
        this.map = new HashMap<E, SimpleInteger>();
    }

    public CounterSet(int initialCapacity) 
    {
        this.map = new HashMap<E, SimpleInteger>(initialCapacity);
    }

    public CounterSet(int initialCapacity, float loadFactor) 
    {
        this.map = new HashMap<E, SimpleInteger>(initialCapacity, loadFactor);
    }
    
    public int count(E e)
    {
        SimpleInteger counter = map.get(e);
        return (counter!=null)? counter.get() : 0 ;
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
        return map.containsKey((E)o);
    }

    public Iterator<E> iterator()
    {
        return map.keySet().iterator();
    }

    public Object[] toArray()
    {
        return map.keySet().toArray();
    }

    public <T> T[] toArray(T[] a)
    {
        return map.keySet().toArray(a);
    }
    public <T> E[] toArray(E[] a, int[] c)
    {
        int i=0;
        for( Entry<E, SimpleInteger> kv : map.entrySet())
        {
            a[i] = kv.getKey();
            c[i] = kv.getValue().get();
            i++;
        }
        return a;
    }

    public boolean add(E e)
    {
        count++;
        SimpleInteger counter = map.get(e);
        if(counter!=null)
        {
            counter.add(1);
            return false;
        }
        map.put(e,new SimpleInteger(1));
        return true;
    }

    public boolean remove(Object o)
    {
        count--;
        SimpleInteger counter = map.get((E)o);
        if(counter==null)
        {
            return false;
        }
        if(counter.get()==1)
        {
            map.remove((E)o);
        }
        else
        {
            counter.add(-1);
        }
        return true;
    }

    public boolean containsAll(Collection<?> c)
    {
        return map.keySet().containsAll(c);
    }

    public boolean addAll(Collection<? extends E> c)
    {
        for(E e : c)
        {
            this.add(e);
        }
        return true;
    }

    public boolean retainAll(Collection<?> c)
    {
        boolean ret=false;
        for(E k:map.keySet())
        {
            if(!c.contains(k))
            {
                count--;
                map.remove(k);
                ret = true;
            }
        }
        return ret;
    }
    
    public boolean removeAll(Collection<?> c)
    {
        boolean ret=false;
        for(Object o: c)
        {
            count--;
            ret |= (map.remove((E)c)!=null);
        }
        return ret;
    }

    public void clear()
    {
        count=0;
        map.clear();
    }

    public double getSuccess()
    {
        return (double)this.count/(double)this.size();
    }

}
