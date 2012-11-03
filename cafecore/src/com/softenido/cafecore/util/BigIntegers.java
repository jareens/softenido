/*
*  BigIntegers.java
*
*  Copyright (C) 2012 Francisco Gómez Carrasco
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
*  Copied parts from android and jdk are distributed in their original license.
*
*/
package com.softenido.cafecore.util;

import java.math.BigInteger;

/**
 *
 * @author franci
 */
public class BigIntegers
{
    public static BigInteger[] valueOf(byte[] values, int start, int end)
    {
        BigInteger[] bi = new BigInteger[end-start];
        for(int i=0,j=start;j<end;i++,j++)
        {
            bi[i] = BigInteger.valueOf(values[j]);
        }
        return bi;
    }
    public static BigInteger[] valueOf(byte[] values)
    {
        return valueOf(values, 0, values.length);
    }
    public static BigInteger[] valueOf(char[] values, int start, int end)
    {
        BigInteger[] bi = new BigInteger[end-start];
        for(int i=0,j=start;j<end;i++,j++)
        {
            bi[i] = BigInteger.valueOf(values[j]);
        }
        return bi;
    }
    public static BigInteger[] valueOf(char[] values)
    {
        return valueOf(values, 0, values.length);
    }
    public static BigInteger[] valueOf(int[] values, int start, int end)
    {
        BigInteger[] bi = new BigInteger[end-start];
        for(int i=0,j=start;j<end;i++,j++)
        {
            bi[i] = BigInteger.valueOf(values[j]);
        }
        return bi;
    }
    public static BigInteger[] valueOf(int[] values)
    {
        return valueOf(values, 0, values.length);
    }
    public static BigInteger[] valueOf(long[] values, int start, int end)
    {
        BigInteger[] bi = new BigInteger[end-start];
        for(int i=0,j=start;j<end;i++,j++)
        {
            bi[i] = BigInteger.valueOf(values[j]);
        }
        return bi;
    }
    public static BigInteger[] valueOf(long[] values)
    {
        return valueOf(values, 0, values.length);
    }
            
}
