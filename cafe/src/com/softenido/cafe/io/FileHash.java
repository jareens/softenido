/*
 *  FileHash.java
 *
 *  Copyright (C) 2007-2009  Francisco Gómez Carrasco
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
package com.softenido.cafe.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author franci
 */
public class FileHash
{
    private static final String MD5 = "MD5";
    private static final String SHA1 = "SHA-1";
    
    private File file;
    private final long size;
    private byte[] fastHash1 = null;
    private byte[] fastHash2 = null;
    private byte[] fullHash1 = null;
    private byte[] fullHash2 = null;
    private static int bufSize = 64*1024;

    /**
     * Creates a new FileHash instance from a File object.
     * @param file
     */
    public FileHash(File file)
    {
        this.file = file;
        this.size = file.length();
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
        final FileHash other = (FileHash) obj;
        if (this.file.length() != other.file.length())
        {
            return false;
        }
        this.buildFastHash();
        other.buildFastHash();
        if (this.fastHash1 != other.fastHash1 && (this.fastHash1 == null || !Arrays.equals(this.fastHash1, other.fastHash1)))
        {
            return false;
        }
        if (this.fastHash2 != other.fastHash2 && (this.fastHash2 == null || !Arrays.equals(this.fastHash2, other.fastHash2)))
        {
            return false;
        }
        this.buildFullHash();
        other.buildFullHash();
        if (this.fullHash1 != other.fullHash1 && (this.fullHash1 == null || !Arrays.equals(this.fullHash1, other.fullHash1)))
        {
            return false;
        }
        if (this.fullHash2 != other.fullHash2 && (this.fullHash2 == null || !Arrays.equals(this.fullHash2, other.fullHash2)))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        return (int) (file.length() % Integer.MAX_VALUE);
    }

    private synchronized void buildFastHash()
    {
        if (fastHash1 != null && fastHash2 != null)
        {
            return;
        }
        FileInputStream fis = null;
        try
        {
            MessageDigest md5 = MessageDigest.getInstance(MD5);
            MessageDigest sha1 = MessageDigest.getInstance(SHA1);
            if (file.length() > 0)
            {
                byte[] buf = new byte[1024];
                fis = new FileInputStream(file);
                int r = fis.read(buf);
                fis.close();
                md5.update(buf, 0, r);
                sha1.update(buf, 0, r);
            }

            fastHash1 = md5.digest();
            fastHash2 = sha1.digest();
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(FileHash.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(FileHash.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(FileHash.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                if (fis != null)
                {
                    fis.close();
                }
            }
            catch (IOException ex)
            {
                Logger.getLogger(FileHash.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void buildFullHash()
    {
        if (fullHash1 != null && fullHash2 != null)
        {
            return;
        }

        if (file.length() <= 1024)
        {
            fullHash1 = fastHash1;
            fullHash2 = fastHash2;
            return;
        }

        FileInputStream fis = null;
        try
        {
            MessageDigest md5 = MessageDigest.getInstance(MD5);
            MessageDigest sha1 = MessageDigest.getInstance(SHA1);
            byte[] buf = new byte[bufSize];
            fis = new FileInputStream(file);
            int r;

            while ((r = fis.read(buf)) > 0)
            {
                md5.update(buf, 0, r);
                sha1.update(buf, 0, r);
            }

            fullHash1 = md5.digest();
            fullHash2 = sha1.digest();
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(FileHash.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(FileHash.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (NoSuchAlgorithmException e)
        {
        }
        finally
        {
            try
            {
                if (fis != null)
                {
                    fis.close();
                }
            }
            catch (IOException ex)
            {
                Logger.getLogger(FileHash.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public long getSize()
    {
        return size;
    }

    public File getFile()
    {
        return file;
    }
    
}
