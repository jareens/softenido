/*
 * SimpleInteger.java
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.cafecore.util;

/**
 *
 * @author franci
 */
public class SimpleInteger
{
    int value;

    public SimpleInteger(int value)
    {
        this.value = value;
    }
    public SimpleInteger()
    {
        this(0);
    }
    @Override
    public int hashCode()
    {
        return value;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final SimpleInteger other = (SimpleInteger) obj;
        if (this != other && this.get()!=other.get())
        {
            return false;
        }
        return true;
    }

    public int get()
    {
        return value;
    }
    public void add(int n)
    {
        value += n;
    }
    public int addAndGet(int n)
    {
        value += n;
        return value;
    }
}
