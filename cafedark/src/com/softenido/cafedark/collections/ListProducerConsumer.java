/*
 *  ListProducerConsumer.java
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
package com.softenido.cafedark.collections;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @author franci
 */
public class ListProducerConsumer<E> implements ProducerConsumer<E>, Iterator<E>
{
    final List<E> list;
    int index = 0;

    public ListProducerConsumer(List<E> list)
    {
        this.list = list;
    }

    public void add(E o)
    {
        list.add(o);
    }

    public Iterator<E> iterator()
    {
        return this;
    }

    public boolean hasNext()
    {
        return (index<list.size());
    }

    public E next()
    {
        return list.get(index++);
    }

    public void remove()
    {
        throw new UnsupportedOperationException();
    }
}
