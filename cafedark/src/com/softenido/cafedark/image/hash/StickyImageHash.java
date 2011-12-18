/*
 *  StickyImageHash.java
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
package com.softenido.cafedark.image.hash;

import com.softenido.cafedark.io.Hash;
import java.util.ArrayList;

/**
 *
 * @author franci
 */
public class StickyImageHash implements Hash
{
    static private final Object lock = new Object();
    private final int w;
    private final int h;
    private final int hc;
    private final int colorThreshold;
    private final int countThreshold;
    private ArrayList<byte[]> hash;


    public StickyImageHash(int w, int h, int hc, byte[] hash, float colorThresold, float countThresold)
    {
        this.w    = w;
        this.h    = h;
        this.hc   = hc;
        this.colorThreshold = (int) (colorThresold * 256);
        this.countThreshold = (int) (countThresold * hash.length);
        this.hash = new ArrayList<byte[]>();
        this.hash.add(hash);
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
        final StickyImageHash other = (StickyImageHash) obj;
        if (this.w != other.w)
        {
            return false;
        }
        if (this.h != other.h)
        {
            return false;
        }
        synchronized(lock)
        {
            if (this.hash.equals(other.hash))
            {
                return true;
            }
            
            for(byte[] thisItem : this.hash)
            {
                for(byte[] otherItem : other.hash)
                {
                    if(thisItem == otherItem)
                    {
                        return true;
                    }
                    int count = 0;
                    for(int i=0;i<thisItem.length && count<=countThreshold;i++)
                    {
                        int thisColor = thisItem[i]>=0?thisItem[i]:thisItem[i]+256;
                        int otherColor= otherItem[i]>=0?otherItem[i]:otherItem[i]+256;
                        int colorDiff = thisColor-otherColor;
                        colorDiff = Math.max(colorDiff,-colorDiff);
                        if(colorDiff>colorThreshold)
                        {
                            count++;
                        }
                    }
                    if(count<countThreshold)
                    {
                        this.hash = merge(this.hash,other.hash);
                        other.hash=this.hash;
                        return true;
                    }
                }
            }
        }
        return false;
    }
    @Override
    public int hashCode()
    {
        return hc;
    }

    private static ArrayList<byte[]> merge(ArrayList<byte[]>... lists)
    {
        ArrayList<byte[]> hash = new ArrayList<byte[]>();
        for(int i=0;i<lists.length;i++)
        {
            hash.addAll(lists[i]);
        }
        return hash;
    }
}
