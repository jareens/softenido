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
    private static int bufSize = 64 * 1024;
    private File file;
    private final long size;
    private byte[] fastMD5 = null;
    private byte[] fastSHA1 = null;
    private byte[] fullMD5 = null;
    private byte[] fullSHA1 = null;
    private boolean exception = false;

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
        if(file.equals(other.file))
        {
            return true;
        }
        if (this.file.length() != other.file.length())
        {
            return false;
        }
        try
        {
            // exception
            if(this.exception || other.exception)
            {
                return false;
            }

            //to determine if both points to the same taget
            if (this.file.getCanonicalPath().equals(other.file.getCanonicalPath()))
            {
                return true;
            }
            if (!Arrays.equals(this.getFastMD5(), other.getFastMD5()))
            {
                return false;
            }
            if (!Arrays.equals(this.getFastSHA1(), other.getFastSHA1()))
            {
                return false;
            }
            if (!Arrays.equals(this.getFullMD5(), other.getFullMD5()))
            {
                return false;
            }
            if (!Arrays.equals(this.getFullSHA1(), other.getFullSHA1()))
            {
                return false;
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(FileHash.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        return (int) (file.length() % Integer.MAX_VALUE);
    }

    private synchronized void buildFastHash() throws FileNotFoundException, IOException
    {
        if (fastMD5 != null && fastSHA1 != null)
        {
            return;
        }
        boolean error = true;
        FileInputStream fis = null;
        try
        {
            MessageDigest md5 = MessageDigest.getInstance(MD5);
            MessageDigest sha1 = MessageDigest.getInstance(SHA1);
            if (size > 0)
            {
                byte[] buf = new byte[1024];
                fis = new FileInputStream(file);
                int r = fis.read(buf);
                fis.close();
                md5.update(buf, 0, r);
                sha1.update(buf, 0, r);
            }

            fastMD5 = md5.digest();
            fastSHA1 = sha1.digest();
            error = false;
        }
        catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(FileHash.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            if (error)
            {
                this.exception = true;
            }
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

    private void buildFullHash() throws FileNotFoundException, IOException
    {
        if (fullMD5 != null && fullSHA1 != null)
        {
            return;
        }

        if (size <= 1024)
        {
            fullMD5 = fastMD5;
            fullSHA1 = fastSHA1;
            return;
        }
        boolean error = true;
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

            fullMD5 = md5.digest();
            fullSHA1 = sha1.digest();
            error = false;
        }
        catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(FileHash.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            if (error)
            {
                this.exception = true;
            }
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

    public byte[] getFastMD5() throws FileNotFoundException, IOException
    {
        if (fastMD5 == null)
        {
            buildFastHash();
        }
        return fastMD5;
    }

    public byte[] getFastSHA1() throws FileNotFoundException, IOException
    {
        if (fastSHA1 == null)
        {
            buildFastHash();
        }
        return fastSHA1;
    }

    public byte[] getFullMD5() throws FileNotFoundException, IOException
    {
        if (fullMD5 == null)
        {
            buildFullHash();
        }
        return fullMD5;
    }

    public byte[] getFullSHA1() throws FileNotFoundException, IOException
    {
        if (fullSHA1 == null)
        {
            buildFullHash();
        }
        return fullSHA1;
    }
}
