/*
 *  IterableBuilder.java
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

import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author franci
 */
public class IterableBuilder
{
    public static <T> ProducerConsumer<T> build(T[] items)
    {
        return new ArrayProducerConsumer<T>(items);
    }
    public static <T> ProducerConsumer<T> build(List<T> list)
    {
        return new ListProducerConsumer<T>(list);
    }

    public static <T> ProducerConsumer<T> build(BlockingQueue<T> queue,T poison)
    {
        return new QueueProducerConsumer<T>(queue,poison);
    }
}
