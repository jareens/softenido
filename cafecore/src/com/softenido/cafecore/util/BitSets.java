/*
 * BitSets.java
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

import java.util.BitSet;

/**
 *
 * @author franci
 */
public class BitSets
{
    public static BitSet copy(BitSet dst,byte[] src)
    {
        if(dst ==null)
        {
            dst = new BitSet(src.length*8);
        }
        for (int i = 0,bit=0; i < src.length; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                boolean val = ((src[i]>>>j)&1)==1;
                dst.set(bit++, val);
            }
        }
        return dst;
    }
    public static byte[] copy(byte[] dst,BitSet src)
    {
        for (int i = 0,bit=0; i < dst.length; i++)
        {
            byte val = 0;
            byte pow = -128;
            for (int j = 0; j < 8; j++, pow>>>=1)
            {
                if(src.get(bit++))
                {
                    val |= pow;
                }
            }
            dst[i]=val;
        }
        return dst;
    }
    public static BitSet asBitSet(byte[] src)
    {
        return copy(null,src);
    }
    public static byte[] asBytes(BitSet src)
    {
        return copy(null,src);
    }

}
