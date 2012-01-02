/*
 *  Messy.java
 *
 *  Copyright (C) 2010-2011  Francisco Gómez Carrasco
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
package com.softenido.cafecore.security;

import java.security.SecureRandom;

/**
 * 
 * @author franci
 */
public abstract class Messy<E>
{
    private static final SecureRandom rand = new SecureRandom();

    protected final E x;
    protected final E y;

    Messy(E x, E y)
    {
        this.x = x;
        this.y = y;
    }
    public abstract E get();

    public static Messy<Boolean> build(boolean val)
    {
        boolean X = rand.nextBoolean();
        boolean Y = X ^ val;
        return new Messy<Boolean>(X, Y)
        {
            public Boolean get()
            {
                return (x ^ y);
            }
        };
    }

    public static Messy<Boolean>[] build(boolean val[])
    {
        @SuppressWarnings("unchecked")
        Messy<Boolean>[] items = new Messy[val.length];
        for (int i = 0; i < val.length; i++)
        {
            items[i] = build(val[i]);
        }
        return items;
    }

    public static Messy<Byte> build(byte val)
    {
        byte X = (byte) rand.nextInt();
        byte Y = (byte) (X ^ val);
        return new Messy<Byte>(X, Y)
        {
            public Byte get()
            {
                return (byte)(x ^ y);
            }
        };
    }

    public static Messy<Byte>[] build(byte val[])
    {
        @SuppressWarnings("unchecked")
        Messy<Byte> items[] = new Messy[val.length];
        for (int i = 0; i < val.length; i++)
        {
            items[i] = build(val[i]);
        }
        return items;
    }

    public static Messy<Character> build(char val)
    {
        char X = (char) rand.nextInt();
        char Y = (char) (X ^ val);
        return new Messy<Character>(X, Y)
        {
            public Character get()
            {
                return (char)(x ^ y);
            }
        };
    }

    public static Messy<Character>[] build(char val[])
    {
        @SuppressWarnings("unchecked")
        Messy<Character> items[] = new Messy[val.length];
        for (int i = 0; i < val.length; i++)
        {
            items[i] = build(val[i]);
        }
        return items;
    }

    public static Messy<Integer> build(int val)
    {
        int x = rand.nextInt();
        int y = (x ^ val);
        return new Messy<Integer>(x, y)
        {
            public Integer get()
            {
                return (x ^ y);
            }
        };
    }

    public static Messy<Integer>[] build(int val[])
    {
        @SuppressWarnings("unchecked")
        Messy<Integer> items[] = new Messy[val.length];
        for (int i = 0; i < val.length; i++)
        {
            items[i] = build(val[i]);
        }
        return items;
    }

    public static Messy<Long> build(long val)
    {
        long x = rand.nextLong();
        long y = (x ^ val);
        return new Messy<Long>(x, y)
        {
            public Long get()
            {
                return (x ^ y);
            }
        };
    }

    public static Messy<Long>[] build(long val[])
    {
        @SuppressWarnings("unchecked")
        Messy<Long> items[] = new Messy[val.length];
        for (int i = 0; i < val.length; i++)
        {
            items[i] = build(val[i]);
        }
        return items;
    }
}
