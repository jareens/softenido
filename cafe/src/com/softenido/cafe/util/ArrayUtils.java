/*
 *  ArrayUtils.java
 *
 *  Copyright (C) 2007-2009  Francisco Gómez Carrasco
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
package com.softenido.cafe.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author franci
 */
public final class ArrayUtils
{

    public static byte[] getByteArray(int[] intArray)
    {
        byte[] byteArray = new byte[intArray.length * 4];
        for (int i = 0, b = 0; i < intArray.length; i++)
        {
            byteArray[b++] = (byte) ((intArray[i] >> 24) & 0xff);
            byteArray[b++] = (byte) ((intArray[i] >> 16) & 0xff);
            byteArray[b++] = (byte) ((intArray[i] >> 8) & 0xff);
            byteArray[b++] = (byte) (intArray[i] & 0xff);
        }
        return byteArray;
    }

    public static int[] getIntArray(byte[] byteArray)
    {
        int[] intArray = new int[byteArray.length / 4];
        for (int i = 0, b = 0; i < intArray.length; i++)
        {
            intArray[i] = (byteArray[i] << 24) & (byteArray[i] << 16) & (byteArray[i] << 8) & intArray[i];
        }
        return intArray;
    }

    public static <T> T[] reverseCopyOf(T[] original)
    {
        T[] reverse = Arrays.copyOf(original, original.length);
        for (int i = 0, j = original.length - 1; i < reverse.length; i++, j--)
        {
            reverse[i] = original[j];
        }
        return reverse;
    }

    /**
     * Copies the specified array, eliminating duplicated objects and compacting
     * the arrary if necesary
     *
     * @param <T>
     * @param original
     * @return a copy of the original array, compated eliminating duplicated objects
     */
    public static <T> T[] uniqueCopyOf(T[] src)
    {
        ArrayList<T> list = new ArrayList<T>(src.length);
        for (T item : src)
        {
            if (!list.contains(item))
            {
                list.add(item);
            }
        }
        return list.toArray(Arrays.copyOf(src, 0));
    }

    public static <T> T[][] splitEquals(T[] src,Comparator<T> cmp)
    {
        //verificar que funciona cuando se usa un comparador
                
        Map<T, List<T>> map = (cmp==null)?new LinkedHashMap<T, List<T>>() : new TreeMap<T, List<T>>(cmp);

        for (int i = 0; i < src.length; i++)
        {
            List<T> list = map.get(src[i]);
            if (list == null)
            {
                list = new ArrayList<T>();
                map.put(src[i], list);
            }
            list.add(src[i]);
        }
        T[] empty = (T[]) Arrays.copyOf(src, 0);
        T[][] dst = (T[][]) Array.newInstance(src.getClass(), map.size());

        List<T>[] values = map.values().toArray(new ArrayList[0]);

        for (int i = 0; i < dst.length; i++)
        {
            dst[i] = values[i].toArray(empty);
        }
        return dst;
    }
    public static <T> T[][] splitEquals(T[] src)
    {
        return splitEquals(src, null);
    }
    public static <T> T[][] splitAgainEquals(T[][] src,Comparator<T> cmp)
    {
        //verificar que funciona cuando se usa un comparador

        List<T[]> list = new ArrayList<T[]>(src.length);

        for (int i = 0; i < src.length; i++)
        {
            T[][] split = splitEquals(src[i], cmp);
            for(int j=0;j<split.length;j++)
            {
                list.add(split[j]);
            }
        }
        return list.toArray(Arrays.copyOf(src, 0));
    }
    public static <T> T[][] splitAgainEquals(T[][] src)
    {
        return splitAgainEquals(src, null);
    }

}
