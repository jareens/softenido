/*
 *  BitSetReader.java
 *
 *  Copyright (C) 2011 Francisco Gómez Carrasco
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
package com.softenido.core.util;

import java.util.BitSet;

/**
 *
 * @author franci
 */
public abstract class BitSetReader
{
    private static byte[] POW2 = {1,2,4,8,16,32,64,-128};
    public abstract boolean get();
    public abstract byte get(int bits);
    public abstract int count();

    static public BitSetReader build(final BitSet src)
    {
        return new BitSetReader()
        {
            private final BitSet bs=src;
            private int count=0;
            @Override
            public boolean get()
            {
                return bs.get(count++);
            }
            @Override
            public byte get(int bits)
            {
                byte val = 0;
                
                for (int i = 0; i<bits; i++)
                {
                    if(get())
                    {
                        val |= POW2[i];
                    }
                }
                return val;
            }
            @Override
            public int count()
            {
                return count;
            }
        };
    }
    static public BitSetReader syncronized(final BitSetReader src)
    {
        return new BitSetReader()
        {
            private final BitSetReader bq=src;
            private final Object lock=new Object();
            @Override
            public boolean get()
            {
                synchronized(lock)
                {
                    return bq.get();
                }
            }
            @Override
            public byte get(int bits)
            {
                synchronized(lock)
                {
                    return bq.get(bits);
                }
            }
            @Override
            public int count()
            {
                synchronized(lock)
                {
                    return bq.count();
                }
            }
        };
    }
}
