/*
*  Arrays.java
*
*  Copyright (C) 2011  Francisco Gómez Carrasco
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

import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.List;

/**
 * Just helps java5 classes to use java6 Arrays funtions
 * 
 * @author franci
 */
public class Arrays
{
    static private final java.util.Arrays array=null;
    
    private Arrays()
    {
        // empty, this is just to avoid class instantiation
    }
    
    public static <T> List<T> asList(T... a)
    {
        return java.util.Arrays.asList(a);
    }
    
    public static <T> void sort(T[] a, Comparator<? super T> c)
    {
        java.util.Arrays.sort(a, c);
    }
    
    /**
     * Copies {@code newLength} elements from {@code original} into a new array.
     * If {@code newLength} is greater than {@code original.length}, the result is padded
     * with the value {@code null}.
     * 
     * copied from android source code
     *
     * @param original the original array
     * @param newLength the length of the new array
     * @return the new array
     * @throws NegativeArraySizeException if {@code newLength < 0}
     * @throws NullPointerException if {@code original == null}
     * @since 1.6
     */
    public static <T> T[] copyOf(T[] original, int newLength) 
    {
        if (original == null) 
        {
            throw new NullPointerException();
        }
        if (newLength < 0) 
        {
            throw new NegativeArraySizeException();
        }
        return copyOfRange(original, 0, newLength);
    }
    /**
     * Copies elements from {@code original} into a new array, from indexes start (inclusive) to
     * end (exclusive). The original order of elements is preserved.
     * If {@code end} is greater than {@code original.length}, the result is padded
     * with the value {@code null}.
     *
     * copied from android source code
     * 
     * @param original the original array
     * @param start the start index, inclusive
     * @param end the end index, exclusive
     * @return the new array
     * @throws ArrayIndexOutOfBoundsException if {@code start < 0 || start > original.length}
     * @throws IllegalArgumentException if {@code start > end}
     * @throws NullPointerException if {@code original == null}
     * @since 1.6
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] copyOfRange(T[] original, int start, int end) 
    {
        int originalLength = original.length; // For exception priority compatibility.
        if (start > end) 
        {
            throw new IllegalArgumentException();
        }
        if (start < 0 || start > originalLength) 
        {
            throw new ArrayIndexOutOfBoundsException();
        }
        int resultLength = end - start;
        int copyLength = Math.min(resultLength, originalLength - start);
        T[] result = (T[]) Array.newInstance(original.getClass().getComponentType(), resultLength);
        System.arraycopy(original, start, result, 0, copyLength);
        return result;
    }
    
}
