/*
 *  ArrayUtils.java
 *
 *  Copyright (C) 2007  Francisco Gómez Carrasco
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
package org.fjtk.ce;

/**
 *
 * @author franci
 */
public final class ArrayUtils 
{
    public static byte[] getByteArray(int[] intArray)
    {
        byte[] byteArray = new byte[intArray.length*4];
        for(int i=0,b=0;i<intArray.length;i++)
        {
            byteArray[b++] = (byte)((intArray[i] >> 24) & 0xff);
            byteArray[b++] = (byte)((intArray[i] >> 16) & 0xff);
            byteArray[b++] = (byte)((intArray[i] >> 8) & 0xff);
            byteArray[b++] = (byte)(intArray[i] & 0xff);
        }
        return byteArray;
    }
    public static int[] getIntArray(byte[] byteArray)
    {
        int[] intArray = new int[byteArray.length/4];
        for(int i=0,b=0;i<intArray.length;i++)
        {
            intArray[i] = (byteArray[i] << 24) & (byteArray[i] << 16) & (byteArray[i] << 8) & intArray[i];
        }
        return intArray;
    }
}
