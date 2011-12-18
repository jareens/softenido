/*
 *  SplitEquals.java
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
package com.softenido.cafedark.util;

import com.softenido.cafedark.collections.Consumer;
import com.softenido.cafedark.collections.IterableBuilder;
import com.softenido.cafedark.collections.ProducerConsumer;
import com.softenido.cafedark.util.concurrent.ParallelTask;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author franci
 */
public class SplitEquals<E>
{

    public static <T> Consumer<T[]> split(Consumer<T> src, ProducerConsumer<T[]> dst, Comparator<T> cmp, T[] empty, final int min, final int max)
    {

        Map<T, List<T>> map = (cmp == null) ? new LinkedHashMap<T, List<T>>() : new TreeMap<T, List<T>>(cmp);

        for (T item : src)
        {
            List<T> list = map.get(item);
            if (list == null)
            {
                list = new ArrayList<T>();
                map.put(item, list);
            }
            list.add(item);
        }

        for (List<T> item : map.values())
        {
            int size = item.size();
            if (size >= min && size <= max)
            {
                dst.add(item.toArray(empty));
            }
        }
        return dst;
    }

    public static <T> Consumer<T[]> splitAgain(Consumer<T[]> src, ProducerConsumer<T[]> dst, Comparator<T> cmp, T[] empty, final int min, final int max)
    {
        for (T[] srcItem : src)
        {
            Consumer<T> wrapSrc = IterableBuilder.build(srcItem);
            split(wrapSrc, dst, cmp, empty, min, max);
        }
        return dst;
    }

    public static <T> Runnable buildSplit(final Consumer<T> src, final ProducerConsumer<T[]> dst, final Comparator<T> cmp, final T[] empty, final T[] poison, final int min, final int max)
    {
        return new Runnable()
        {

            public void run()
            {
                split(src, dst, cmp, empty, min, max);
                if (poison != null)
                {
                    dst.add(poison);
                }
            }
        };
    }

    public static <T> Runnable buildSplitAgain(final Consumer<T[]> src, final ProducerConsumer<T[]> dst, final Comparator<T> cmp, final T[] empty, final T[] poison, final int min, final int max)
    {
        return new Runnable()
        {

            public void run()
            {
                splitAgain(src, dst, cmp, empty, min, max);
                if (poison != null)
                {
                    dst.add(poison);
                }
            }
        };
    }

    public static <T> Runnable buildSplitParallel(final Consumer<T> src, final ProducerConsumer<T[]> dst, final Comparator<T> cmp, final T[] empty, final T[] poison, final int min, final int max)
    {
        return new ParallelTask(buildSplit(src, dst, cmp, empty, null, min, max))
        {
            @Override
            public void tail()
            {
                if (poison != null)
                {
                    dst.add(poison);
                }
            }
        };
    }
    
    public static <T> Runnable buildSplitAgainParallel(final Consumer<T[]> src, final ProducerConsumer<T[]> dst, final Comparator<T> cmp, final T[] empty, final T[] poison, final int min, final int max)
    {
        return new ParallelTask(buildSplitAgain(src, dst, cmp, empty, null, min, max))
        {
            @Override
            public void tail()
            {
                if (poison != null)
                {
                    dst.add(poison);
                }
            }
        };
    }
}
