/*
 *  ParallelMessageDigest.java
 *
 *  Copyright (C) 2009-2011  Francisco Gómez Carrasco
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

import com.softenido.cafecore.util.ArrayUtils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author franci
 */
public class ParallelMessageDigest extends MessageDigest implements Cloneable
{
    protected final MessageDigest[] md;

    protected ParallelMessageDigest(String algorithm,MessageDigest[] md)
    {
        super(algorithm);
        this.md = md;
    }

    public static MessageDigest getInstance(MessageDigest[] md) throws NoSuchAlgorithmException
    {
        String name = "";
        for (MessageDigest item : md)
        {
            name += item.getAlgorithm();
        }
        return new ParallelMessageDigest(name,md);
    }
    public static MessageDigest getInstance(String[] algorithm) throws NoSuchAlgorithmException
    {
        MessageDigest[] md = new MessageDigest[algorithm.length];
        String name = "";
        for(int i=0;i<algorithm.length;i++)
        {
            name += algorithm[i];
            md[i] = MessageDigest.getInstance(algorithm[i]);
        }
        return new ParallelMessageDigest(name,md);
    }
    
    @Override
    protected void engineUpdate(byte input)
    {
        for(MessageDigest item: md)
        {
            item.update(input);
        }
    }

    @Override
    protected void engineUpdate(byte[] input, int offset, int len)
    {
        for(MessageDigest item: md)
        {
            item.update(input, offset, len);
        }
    }

    @Override
    protected byte[] engineDigest()
    {
        byte[][] digest = new byte[md.length][];
        for(int i =0;i<md.length;i++)
        {
            digest[i] = md[i].digest();
        }
        return ArrayUtils.cat(digest);
    }

    @Override
    protected void engineReset()
    {
        for(MessageDigest item: md)
        {
            item.reset();
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        MessageDigest[] md2 = new MessageDigest[md.length];
        for(int i=0;i<md.length;i++)
        {
            md2[i]=(MessageDigest) md[i].clone();
        }
        return new ParallelMessageDigest(this.getAlgorithm(), md2);
    }

}
