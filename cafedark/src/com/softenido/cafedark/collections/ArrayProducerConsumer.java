/*
 *  ArrayProducerConsumer.java
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

/**
 *
 * @author franci
 */
public class ArrayProducerConsumer<E> implements ProducerConsumer<E>, Iterator<E>
{

    final E[] items;
    int consumerIndex = 0;
    int producerIndex = 0;

    public ArrayProducerConsumer(E[] items, boolean empty)
    {
        this.items = items;
        this.producerIndex = (empty ? 0 : items.length);
    }

    public ArrayProducerConsumer(E[] items)
    {
        this(items, false);
    }

    public void add(E val)
    {
        items[producerIndex++] = val;
    }

    public Iterator<E> iterator()
    {
        return this;
    }

    public boolean hasNext()
    {
        return (consumerIndex < producerIndex);
    }

    public E next()
    {
        return items[consumerIndex++];
    }

    public void remove()
    {
        throw new UnsupportedOperationException();
    }
}
