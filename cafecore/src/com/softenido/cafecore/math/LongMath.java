/*
 *  LongMath.java
 *
 *  Copyright (C) 1998-2012  Francisco Gómez Carrasco
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
package com.softenido.cafecore.math;

public class LongMath
{
    public static long gcd(long a, long b)
    {
       return (b==0)? a : gcd(b, a % b);
    }
    public static long gcd(long a, long ... b)
    {
        return gcd(a,gcd(b,0,b.length));
    }
    private static long gcd(long[] n,int from, int to)
    {
        if (from == to)
        {
            return 0;
        }
        if(from+1 == to)
        {
            return n[from];
        }
        if(from+2 == to)
        {
            return gcd(n[from],n[from+1]);
        }
        int mid = (from+to)/2;
        return gcd(gcd(n,from,mid),gcd(n,mid,to));
    }

    public static long lcm(long a, long b)
    {
        return (a*b)/gcd(a,b);
    }
    public static long lcm(long a, long ... b)
    {
        return lcm(a,lcm(b,0,b.length));
    }

    private static long lcm(long[] n,int from, int to)
    {
        if (from == to)
        {
            return 0;
        }
        if(from+1 == to)
        {
            return n[from];
        }
        if(from+2 == to)
        {
            return lcm(n[from],n[from+1]);
        }
        int mid = (from+to)/2;
        return lcm(lcm(n,from,mid),lcm(n,mid,to));
    }
    public static int fibonacci(long[] seq)
    {
        return fibonacci(seq, 0, 1, 0, seq.length);
    }
    public static int fibonacci(long[] seq, long f0, long f1)
    {
        return fibonacci(seq, f0, f1, 0, seq.length);
    }
    public static int fibonacci(long[] seq, long f0, long f1, int start, int end)
    {
        int count=0;
        if(start<end)
        {
            seq[start] = f0;
            count++;
            if(start+1<end)
            {
                seq[start+1]=f1;
                count++;
            }
            for(int i=start+2;i<end;i++)
            {
                long f2 = f0 + f1;
                seq[i] = f2;
                f0=f1;
                f1=f2;
                count++;
            }
        }            
        return count;
    }
}
