/*
 *  EqualsProxyBuilder.java
 *
 *  Copyright (C) 2010  Francisco Gómez Carrasco
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
package com.softenido.cafecore.equals;

import com.softenido.cafecore.equals.Equals;
import com.softenido.cafecore.equals.EqualsBuilder;


/**
 *
 * @author franci
 */
public abstract class EqualsProxyBuilder<E> implements EqualsBuilder<E>
{
    public final Equals<E> build(E e)
    {
        return new Equals<E>(e)
        {
            @SuppressWarnings("unchecked")
            @Override
            public boolean equals(Object obj)
            {
                if(obj==null)
                {
                    return false;
                }
                if(!(obj instanceof Equals))
                {
                    return false;
                }
                return proxyEquals(this.object, ((Equals<E>)obj).object);
            }
            @Override
            public int hashCode()
            {
                return proxyCode(this.object);
            }
        };
    }
    public abstract int proxyCode(E e);
    public abstract boolean proxyEquals(E e,E o);
}

