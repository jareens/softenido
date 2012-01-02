/*
 *  SecureBitSet.java
 *
 *  Copyright (C) 2010 Francisco Gómez Carrasco
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
import java.util.BitSet;

public class SecureBitSet extends BitSet
{
    private int count=0;
    private SecureRandom random = null;

    public SecureBitSet(int nbits)
    {
        super(nbits);
    }

    public SecureBitSet()
    {
    }

    private void grow(int bitIndex)
    {
        //benign race condition, no need to synchronize
        if(random==null)
        {
            random = new SecureRandom();
        }
        count = Math.max(count, length());
        for(;count<=bitIndex;count++)
        {
            set(count,random.nextBoolean());
        }
    }

    @Override
    public boolean get(int bitIndex)
    {
        if(count<bitIndex)
        {
            grow(bitIndex);
        }
        return super.get(bitIndex);
    }

    @Override
    public BitSet get(int fromIndex, int toIndex)
    {
        if(count<toIndex)
        {
            grow(toIndex);
        }
        return super.get(fromIndex, toIndex);
    }
}
