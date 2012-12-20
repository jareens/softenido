/*
 * Sorts.java
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

import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author franci
 */
public class Sorts
{
    public static void reverse(int[] a)
    {
        for(int h=0,t=a.length-1;h<t;h++,t--)
        {
            int tmp = a[h];
            a[h]=a[t];
            a[t]=tmp;
        }
    }
    public static void reverse(double[] a)
    {
        for(int h=0,t=a.length-1;h<t;h++,t--)
        {
            double tmp = a[h];
            a[h]=a[t];
            a[t]=tmp;
        }
    }
    public static <T> void reverse(T[] a)
    {
        for(int h=0,t=a.length-1;h<t;h++,t--)
        {
            T tmp = a[h];
            a[h]=a[t];
            a[t]=tmp;
        }
    }
    
    public static void sort(int[] key, int[] value, int fromIndex, int toIndex, boolean reverse)
    {
        class Entry
        {
            final int key;
            final int value;
            public Entry(int key, int value)
            {
                this.key = key;
                this.value = value;
            }            
        }
        class EntryComparator implements Comparator<Entry>
        {
            public int compare(Entry o1, Entry o2)
            {
                if(o1.key<o2.key)
                    return -1;
                if(o1.key>o2.key)
                    return +1;
                if(o1.value<o2.value)
                    return -1;
                if(o1.value>o2.value)
                    return +1;
                return 0;
            }
        }
        class ReverseEntryComparator extends EntryComparator
        {
            @Override
            public int compare(Entry o1, Entry o2)
            {
                return super.compare(o2, o1);
            }
        }

        Entry[] entries = new Entry[toIndex-fromIndex];
        for(int i=0,j=fromIndex;i<entries.length;i++,j++)
        {
            entries[i]=new Entry(key[j], value[j]);
        }
        EntryComparator cmp = reverse?new ReverseEntryComparator():new EntryComparator();
        Arrays.sort(entries,cmp);
        
        for(int i=0,j=fromIndex;i<entries.length;i++,j++)
        {
            key[j]  =entries[i].key;
            value[j]=entries[i].value;
        }
    }
    public static void sort(int[] key, int[] value)
    {
        sort(key, value, 0, key.length, false);
    }
    public static void sort(int[] key, int[] value, boolean reverse)
    {
        sort(key, value, 0, key.length, reverse);
    }
    public static void sort(int[] key, int[] value, int fromIndex, int toIndex)
    {
        sort(key, value, fromIndex, toIndex, false);
    }
    
    public static void sort(double[] key, double[] value, int fromIndex, int toIndex, boolean reverse)
    {
        class Entry
        {
            final double key;
            final double value;
            public Entry(double key, double value)
            {
                this.key = key;
                this.value = value;
            }            
        }
        class EntryComparator implements Comparator<Entry>
        {
            public int compare(Entry o1, Entry o2)
            {
                if(o1.key<o2.key)
                    return -1;
                if(o1.key>o2.key)
                    return +1;
                if(o1.value<o2.value)
                    return -1;
                if(o1.value>o2.value)
                    return +1;
                return 0;
            }
        }
        class ReverseEntryComparator extends EntryComparator
        {
            @Override
            public int compare(Entry o1, Entry o2)
            {
                return super.compare(o2, o1);
            }
        }

        Entry[] entries = new Entry[toIndex-fromIndex];
        for(int i=0,j=fromIndex;i<entries.length;i++,j++)
        {
            entries[i]=new Entry(key[j], value[j]);
        }
        EntryComparator cmp = reverse?new ReverseEntryComparator():new EntryComparator();
        Arrays.sort(entries,cmp);
        
        for(int i=0,j=fromIndex;i<entries.length;i++,j++)
        {
            key[j]  =entries[i].key;
            value[j]=entries[i].value;
        }
    }
    public static void sort(double[] key, double[] value)
    {
        sort(key, value, 0, key.length, false);
    }
    public static void sort(double[] key, double[] value, boolean reverse)
    {
        sort(key, value, 0, key.length, reverse);
    }
    public static void sort(double[] key, double[] value, int fromIndex, int toIndex)
    {
        sort(key, value, fromIndex, toIndex, false);
    }
    
    public static <V> void sort(int[] key, V[] value, int fromIndex, int toIndex, boolean reverse)
    {
        class Entry
        {
            final int key;
            final V value;
            public Entry(int key, V value)
            {
                this.key = key;
                this.value = value;
            }            
        }
        class EntryComparator implements Comparator<Entry>
        {
            public int compare(Entry o1, Entry o2)
            {
                if(o1.key<o2.key)
                    return -1;
                if(o1.key>o2.key)
                    return +1;
                if(o1.value instanceof Comparable)
                {
                    return ((Comparable)o1.value).compareTo(o2.value);
                }
                return 0;
            }
        }
        class ReverseEntryComparator extends EntryComparator
        {
            @Override
            public int compare(Entry o1, Entry o2)
            {
                return super.compare(o2, o1);
            }
        }

        Entry[] entries = new Entry[toIndex-fromIndex];
        for(int i=0,j=fromIndex;i<entries.length;i++,j++)
        {
            entries[i]=new Entry(key[j], value[j]);
        }
        EntryComparator cmp = reverse?new ReverseEntryComparator():new EntryComparator();
        Arrays.sort(entries,cmp);
        
        for(int i=0,j=fromIndex;i<entries.length;i++,j++)
        {
            key[j]  =entries[i].key;
            value[j]=entries[i].value;
        }
    }
    public static <V> void sort(int[] key, V[] value)
    {
        sort(key, value, 0, key.length, false);
    }
    public static <V> void sort(int[] key, V[] value, boolean reverse)
    {
        sort(key, value, 0, key.length, reverse);
    }
    public static <V> void sort(int[] key, V[] value, int fromIndex, int toIndex)
    {
        sort(key, value, fromIndex, toIndex, false);
    }

    public static <K,V> void sort(K[] key, V[] value, int fromIndex, int toIndex, boolean reverse)
    {
        class Entry
        {
            final K key;
            final V value;
            public Entry(K key, V value)
            {
                this.key = key;
                this.value = value;
            }            
        }
        class EntryComparator implements Comparator<Entry>
        {
            public int compare(Entry o1, Entry o2)
            {
                if(o1.key instanceof Comparable)
                {
                    return ((Comparable)o1.key).compareTo(o2.key);
                }
                if(o1.value instanceof Comparable)
                {
                    return ((Comparable)o1.value).compareTo(o2.value);
                }
                return 0;
            }
        }
        class ReverseEntryComparator extends EntryComparator
        {
            @Override
            public int compare(Entry o1, Entry o2)
            {
                return super.compare(o2, o1);
            }
        }

        Entry[] entries = new Entry[toIndex-fromIndex];
        for(int i=0,j=fromIndex;i<entries.length;i++,j++)
        {
            entries[i]=new Entry(key[j], value[j]);
        }
        EntryComparator cmp = reverse?new ReverseEntryComparator():new EntryComparator();
        Arrays.sort(entries,cmp);
        
        for(int i=0,j=fromIndex;i<entries.length;i++,j++)
        {
            key[j]  =entries[i].key;
            value[j]=entries[i].value;
        }
    }
    public static <K,V> void sort(K[] key, V[] value)
    {
        sort(key, value, 0, key.length, false);
    }
    public static <K,V> void sort(K[] key, V[] value, boolean reverse)
    {
        sort(key, value, 0, key.length, reverse);
    }
    public static <K,V> void sort(K[] key, V[] value, int fromIndex, int toIndex)
    {
        sort(key, value, fromIndex, toIndex, false);
    }

    public static <T> Comparator<T> asStringComparator()
    {
        return new Comparator<T>()
        {
            public int compare(T t1, T t2)
            {
                String s1 = t1.toString();
                String s2 = t2.toString();
                return s1.compareTo(s2);
            }
        };
    }
    public static <T> Comparator<T> asStringComparatorIgnoreCase()
    {
        return new Comparator<T>()
        {
            public int compare(T t1, T t2)
            {
                String s1 = t1.toString();
                String s2 = t2.toString();
                return s1.compareToIgnoreCase(s2);
            }
        };
    }
    public static <T> Comparator<T> asStringComparatorReverse()
    {
        return new Comparator<T>()
        {
            public int compare(T t1, T t2)
            {
                String s1 = t1.toString();
                String s2 = t2.toString();
                return s2.compareTo(s1);
            }
        };
    }
    public static <T> Comparator<T> asStringComparatorIgnoreCaseReverse()
    {
        return new Comparator<T>()
        {
            public int compare(T t1, T t2)
            {
                String s1 = t1.toString();
                String s2 = t2.toString();
                return s2.compareToIgnoreCase(s1);
            }
        };
    }
    
    public static int[] sequence(int[] values, int from)
    {
        for(int i=0;i<values.length;i++, from++)
        {
            values[i] = from;
        }
        return values;
    }
    public static int[] sequence(int[] values)
    {
        for(int i=0;i<values.length;i++)
        {
            values[i] = i;
        }
        return values;
    }
    public static long[] sequence(long[] values, long from)
    {
        for(int i=0;i<values.length;i++, from++)
        {
            values[i] = from;
        }
        return values;
    }
    public static long[] sequence(long[] values)
    {
        for(int i=0;i<values.length;i++)
        {
            values[i] = i;
        }
        return values;
    }
    public static String[] sequence(String[] values, int from)
    {
        for(int i=0;i<values.length;i++, from++)
        {
            values[i] = Integer.toString(from);
        }
        return values;
    }
    public static String[] sequence(String[] values)
    {
        for(int i=0;i<values.length;i++)
        {
            values[i] = Integer.toString(i);
        }
        return values;
    }
}
