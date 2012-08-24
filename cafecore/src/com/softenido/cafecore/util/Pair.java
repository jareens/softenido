/*
 *  Pair.java
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

/**
 *
 * @author franci
 */
public class Pair<K,V>
{
    private final K key;
    private final V val;

    public Pair(K key, V val)
    {
        this.key = key;
        this.val = val;
    }

    public K getKey()
    {
        return key;
    }

    public V getVal()
    {
        return val;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        @SuppressWarnings("unchecked")
        final Pair<K, V> other = (Pair<K, V>) obj;
        if (this.key != other.key && (this.key == null || !this.key.equals(other.key)))
        {
            return false;
        }
        if (this.val != other.val && (this.val == null || !this.val.equals(other.val)))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 67 * hash + (this.key != null ? this.key.hashCode() : 0);
        hash = 67 * hash + (this.val != null ? this.val.hashCode() : 0);
        return hash;
    }
    
}
