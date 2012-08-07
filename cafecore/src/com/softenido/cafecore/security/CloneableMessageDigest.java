/*
 *  CloneableMessageDigest.java
 *
 *  Copyright (C) 2009-2012 Francisco Gómez Carrasco
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

import com.softenido.cafecore.util.Arrays6;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author franci
 */
public class CloneableMessageDigest extends ParallelMessageDigest
{

    public CloneableMessageDigest(String algorithm, MessageDigest[] md)
    {
        super(algorithm, md);
    }
    //Returns a MessageDigest object that implements the specified digest algorithm.
    static MessageDigest getInstance(String algorithm, int cloneCount) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        try
        {
            
            if(cloneCount>1)
            {
                MessageDigest clone = (MessageDigest) md.clone();
            }
            return md;
        }
        catch (CloneNotSupportedException ex)
        {
            MessageDigest[] list = new MessageDigest[cloneCount];
            list[0] = md;
            for(int i=1;i<list.length;i++)
            {
                list[i] = MessageDigest.getInstance(algorithm);
            }
            return new CloneableMessageDigest(algorithm, list);
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        if(md.length>=1)
        {
            return new CloneableMessageDigest(getAlgorithm(), Arrays6.copyOfRange(new CloneableMessageDigest[0], 1, md.length));
        }
        throw new CloneNotSupportedException();
    }


}
