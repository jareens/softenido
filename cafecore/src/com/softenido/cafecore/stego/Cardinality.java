/*
 *  Cardinality.java
 *
 *  Copyright (C) 2007-2010 Francisco Gómez Carrasco
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
package com.softenido.cafecore.stego;

/**
 *
 * @author franci
 */
public class Cardinality
{

    public Cardinality()
    {
    }

    public static int getByteCardinality(byte b)
    {
        int n = b >= 0 ? b : 256 + b;
        int num=0;
        for (int i = 0; i<8 && n>0; i++,n>>=1)
        {
            if((n&1)==1)
            {
                num++;
            }
        }
        return num;
    }

    public static int getByteCardinality(byte[] b, int ini, int length)
    {
        int num = 0;
        for (int i = ini; i < length; i++)
        {
            num += getByteCardinality(b[i]);
        }
        return num;
    }

    public static int getByteCardinality(byte[] b)
    {
        return getByteCardinality(b, 0, b.length);
    }

    public static int getCharCardinality(char c)
    {
        int n = c;
        int num=0;
        for (int i = 0; i<16 && n>0; i++,n>>=1)
        {
            if((n&1)==1)
            {
                num++;
            }
        }
        return num;
    }
    public static int getCharCardinality(char[] c, int ini, int length)
    {
        int num = 0;
        for (int i = ini; i < length; i++)
        {
            num += getCharCardinality(c[i]);
        }
        return num;
    }

    public static int getCharCardinality(char[] c)
    {
        return getCharCardinality(c, 0, c.length);
    }

    public static int getIntCardinality(int n)
    {
        int num=0;
        for (int i = 0; i<32 && n!=0; i++,n>>>=1)
        {
            if((n&1)==1)
            {
                num++;
            }
        }
        return num;
    }
    public static int getIntCardinality(int[] n, int ini, int length)
    {
        int num = 0;
        for (int i = ini; i < length; i++)
        {
            num += getIntCardinality(n[i]);
        }
        return num;
    }

    public static int getIntCardinality(int[] n)
    {
        return getIntCardinality(n, 0, n.length);
    }

    public static int getLongCardinality(long n)
    {
        int num=0;
        for (int i = 0; i<64 && n!=0; i++,n>>>=1)
        {
            if((n&1)==1)
            {
                num++;
            }
        }
        return num;
    }
    public static int getLongCardinality(int[] n, int ini, int length)
    {
        int num = 0;
        for (int i = ini; i < length; i++)
        {
            num += getLongCardinality(n[i]);
        }
        return num;
    }

    public static int getLongCardinality(int[] n)
    {
        return getLongCardinality(n, 0, n.length);
    }
}
