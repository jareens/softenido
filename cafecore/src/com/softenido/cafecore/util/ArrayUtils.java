/*
 * ArrayUtils.java
 *
 * Copyright (c) 2012  Francisco Gómez Carrasco
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

import com.softenido.cafecore.equals.EqualsBuilder;
import com.softenido.cafecore.security.SecureBitSet;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 *
 * @author franci
 */
public final class ArrayUtils
{
    public static String toHexString(byte[] byteArray)
    {
        StringBuilder hex = new StringBuilder();
        for(int i=0;i<byteArray.length;i++)
        {
            byte c = byteArray[i];
            String l = Integer.toHexString(c&15);
            c = (byte) (c >> 4);
            String h = Integer.toHexString(c&15);
            hex.append(h).append(l);
        }
        return hex.toString();
    }
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
     * @param src
     * @return a copy of the src array, compated eliminating duplicated objects
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
        T[] empty = Arrays.copyOf(src, 0);
        T[][] dst = (T[][]) Array.newInstance(src.getClass(), map.size());

        List<T>[] values = map.values().toArray(new ArrayList[map.values().size()]);

        for (int i = 0; i < dst.length; i++)
        {
            dst[i] = values[i].toArray(empty);
        }
        return dst;
    }
    public static <T> T[][] splitEquals(T[] src,EqualsBuilder<T> cmp)
    {
        RepeatedSet<T> set = new RepeatedSet<T>(cmp);

        Collections.addAll(set, src);
        T[][] dst = (T[][]) Array.newInstance(src.getClass(), set.size());
        return set.toArray(dst);
    }

    public static <T> T[][] splitEquals(T[] src)
    {
        return splitEquals(src, (Comparator<T>) null);
    }
    public static <T> T[][] splitAgainEquals(T[][] src,Comparator<T> cmp)
    {
        //verificar que funciona cuando se usa un comparador

        List<T[]> list = new ArrayList<T[]>(src.length);

        for (int i = 0; i < src.length; i++)
        {
            T[][] split = splitEquals(src[i], cmp);
            Collections.addAll(list, split);
        }
        return list.toArray(Arrays.copyOf(src, 0));
    }
    public static <T> T[][] splitAgainEquals(T[][] src)
    {
        return splitAgainEquals(src, null);
    }

    public static byte[] cat(byte[][] src)
    {
        int size = 0;
        if (src == null)
        {
            return new byte[0];
        }
        for (int i = 0; i < src.length; i++)
        {
            if (src[i] != null)
            {
                size += src[i].length;
            }
        }
        byte[] dst = new byte[size];
        for (int i = 0, w = 0; i < src.length; i++)
        {
            if (src[i] != null)
            {
                for (int j = 0; j < src[i].length; j++)
                {
                    dst[w++] = src[i][j];
                }
            }
        }
        return dst;
    }
    public static <T> T[] cat(T[] ... src)
    {
        if (src == null)
        {
            return null;
        }
        int size = 0;
        T[] dst = null;
        for (int i = 0; i < src.length; i++)
        {
            if (src[i] != null)
            {
                size += src[i].length;
                if(dst==null)
                {
                    dst = Arrays.copyOf(src[i],0);
                }
            }
        }
        if(dst ==null)
        {
            return null;
        }
        dst = Arrays.copyOf(dst,size);
        for (int i = 0, w = 0; i < src.length; i++)
        {
            if (src[i] != null)
            {
                for (int j = 0; j < src[i].length; j++)
                {
                    dst[w++] = src[i][j];
                }
            }
        }
        return dst;
    }
    public static BitSet asBitSet(byte[] src)
    {
        return asBitSet(src, false);
    }
    public static BitSet asBitSet(byte[] src,boolean secure)
    {
        BitSet bits = (secure? new SecureBitSet(src.length*8):new BitSet(src.length*8));

        for(int i=0;i<src.length;i++)
            for(int j=0;j<8;j++)
                bits.set(i*8+j, ((src[i]>>>j) & 1) != 0);
        return bits;
    }
    public static BitSet asAsBitSet(short[] src,boolean secure)
    {
        BitSet bits = (secure? new SecureBitSet(src.length*16):new BitSet(src.length*16));

        for(int i=0;i<src.length;i++)
            for(int j=0;j<16;j++)
                bits.set(i*16+j, ((src[i]>>>j) & 1) != 0);
        return bits;
    }

    public byte[] toByteArray(BitSet bits,int nbits)
    {
        byte[] dst = new byte[(nbits*8+1)/8];
        for(int i=0;i<dst.length;i++)
        {
            byte b=0;
            for(int j=0;j<16;j++)
                b |= bits.get(i*16+j)?(1<<j):0;
            dst[i]=b;
        }
        return dst;
    }
    // reordena aleatoriamente
    public <T> void rand(T[] data, Random rand)
    {
        List<T> list = Arrays.asList(data);
        Collections.shuffle(list, rand);
    }
    public static int compare(byte[] b1,byte[] b2)
    {
        int size = Math.min(b1.length, b2.length);
        for(int i =0;i<size;i++)
        {
            int cmp = b1[i] - b2[i];
            if(cmp!=0)
            {
                return cmp;
            }
        }
        return (b1.length<b2.length ? -1 : (b1.length==b2.length ? 0 : 1));
    }
    public static int compare(byte[][] b1,byte[][] b2)
    {
        int size = Math.min(b1.length, b2.length);
        for(int i =0;i<size;i++)
        {
            int cmp = compare(b1[i],b2[i]);
            if(cmp!=0)
            {
                return cmp;
            }
        }
        return (b1.length<b2.length ? -1 : (b1.length==b2.length ? 0 : 1));
    }   
}
