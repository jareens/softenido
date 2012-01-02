/*
 *  EqualsData.java
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

/**
 *
 * @author franci
 */
public class EqualsData<E,D> extends Equals<E>
{
    protected final D data;
    public EqualsData(E object, D data)
    {
        super(object);
        this.data = data;
    }

    public D getData()
    {
        return data;
    }
    @Override
    public boolean equals(Object obj)
    {
        if( obj==null || data==null )
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        @SuppressWarnings("unchecked")
        final EqualsData<E,D> other = (EqualsData<E,D>) obj;
        return data.equals(other.data);
    }
    @Override
    public int hashCode()
    {
        return (data!=null)?data.hashCode():0;
    }
}
