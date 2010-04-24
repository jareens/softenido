/*
 *  FileDigest.java
 *
 *  Copyright (C) 2009  Francisco Gómez Carrasco
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
package com.softenido.cafe.util;

import com.softenido.cafe.io.packed.PackedFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author franci
 */
public class FileDigest
{

    private final Object lock = new Object();
    private final File file;
    private final PackedFile pf;
    private final long length;
    private final byte[][] hashes = new byte[64][];
    private final long[] sizes = new long[64];
    private byte[] hash = null;
    private int hashCount = 0;
    private long count = 0;
    private final MessageDigest md;
    //resources to free in keepOff
    private int keep = 0;
    private InputStream data = null;
    private byte[] buf = null;

    public FileDigest(File file, MessageDigest md) throws NoSuchAlgorithmException
    {
        this.file = file;
        this.pf =null;
        this.length = file.length();
        this.md = md;
    }
    public FileDigest(PackedFile file, MessageDigest md) throws NoSuchAlgorithmException
    {
        this.file = null;
        this.pf =file;
        this.length = file.length();
        this.md = md;
    }

    public void keepOn()
    {
        synchronized (lock)
        {
            keep++;
        }
    }

    public void keepOff() throws IOException
    {
        synchronized (lock)
        {
            keep--;
            assert (keep>=0);
            if (keep <= 0)
            {
                if (buf != null)
                {
                    buf = null;
                }
                if (data != null)
                {
                    data.close();
                    data = null;
                }
            }
        }
    }

    public byte[] getHash() throws FileNotFoundException, IOException, CloneNotSupportedException
    {
        synchronized (lock)
        {
            if(hash==null)
            {
                hash = getHash(length);
            }
            return hash;
        }
    }

    public byte[] getHash(long size) throws FileNotFoundException, IOException, CloneNotSupportedException
    {
        synchronized (lock)
        {
            size = Math.min(size, length);
            for (int i = 0; i < hashCount && size <= count; i++)
            {
                if (hashes[i] != null && sizes[i]==size)
                {
                    return hashes[i];
                }
            }
            if(size<count)
            {
                return null;
            }
            hashes[hashCount] = buildHash(size);
            sizes[hashCount] = size;
            return hashes[hashCount++];
        }
    }

    private byte[] buildHash(long size) throws FileNotFoundException, IOException, CloneNotSupportedException
    {
        if( size > 0 )
        {
            if (data == null)
            {
                data = getInputStream();
                data.skip(count);
            }
            if (buf == null)
            {
                buf = new byte[(int)Math.min(64*1024,length)];
            }
            size = Math.min(size,length);

            while (count < size)
            {
                int r = Math.min(buf.length, (int) (size - count));
                r = data.read(buf, 0, r);
                md.update(buf, 0, r);
                count += r;
            }
        }

        MessageDigest md2 = (count < length) ? (MessageDigest) md.clone() : md;
        return md2.digest();
    }

    public static long[] buildSizes()
    {
        int i;
        long pow2  = 0;
        long[] sizes = new long[64];
        sizes[0] = 1024;
        sizes[1] = 4096;
        pow2=3;
        //fibonacci like computed values
        for(i=2;i<10;i++)
        {
            sizes[i] = sizes[i-1]*2;
            pow2= (pow2<<1)+1;
        }
        //fibonacci like computed values
        for(;i<sizes.length && (sizes[i-1]>pow2) ;i++)
        {
            sizes[i] = sizes[i-1]+sizes[i-2];
            pow2= (pow2<<1)+1;
        }
        //pow2 computed values
        for(;i<sizes.length;i++)
        {
            sizes[i] = pow2;
            pow2= (pow2<<1)+1;
        }
        assert(sizes[63]==Long.MAX_VALUE);
        return sizes;
    }

    private InputStream getInputStream() throws FileNotFoundException, IOException
    {
        if(file!=null)
        {
            return new FileInputStream(file);
        }
        if(pf!=null)
        {
            return pf.getInputStream();
        }
        return null;
    }

}
