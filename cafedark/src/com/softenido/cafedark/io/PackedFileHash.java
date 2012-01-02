/*
 *  PackedFileHash.java
 *
 *  Copyright (C) 2007-2010  Francisco Gómez Carrasco
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
package com.softenido.cafedark.io;

import com.softenido.cafedark.io.virtual.VirtualFile;
import com.softenido.cafecore.security.ParallelMessageDigest;
import com.softenido.cafedark.util.FileDigest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.compress.archivers.ArchiveException;

/**
 *
 * @author franci
 */
public class PackedFileHash
{
    private static final String MD5 = "MD5";
    private static final String SHA1 = "SHA-1";
    private static final String[] DEF_ALG =
    {
        MD5, SHA1
    };
    private static int bufSize = 64 * 1024;
    private final VirtualFile file;
    private final long size;
    private byte[] fastMD5 = null;
    private byte[] fastSHA1 = null;
    private byte[] fullMD5 = null;
    private byte[] fullSHA1 = null;
    private boolean exception = false;
    private FileDigest digest = null;
    private final Object lock = new Object();
    private static boolean pow2 = true;

    /**
     * Creates a new PackedFileHash instance from a File object.
     * @param file
     */
    public PackedFileHash(VirtualFile file)
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
        final PackedFileHash other = (PackedFileHash) obj;
        if (file.equals(other.file))
        {
            return true;
        }
        if (this.size != other.size)
        {
            return false;
        }
        //two files of size 0 are always equal
        if (this.size == 0)
        {
            return true;
        }
        try
        {
            // exception
            if (this.exception || other.exception)
            {
                return false;
            }

            //to determine if both points to the same taget
            if (this.file.getCanonicalPath().equals(other.file.getCanonicalPath()))
            {
                return true;
            }
            if (pow2)
            {
                return equalsHash(other);
            }
            else
            {
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
        }


        catch (ArchiveException ex)
        {
            Logger.getLogger(PackedFileHash.class.getName()).log(Level.SEVERE, null, ex);
        }        catch (FileNotFoundException ex)
        {
            Logger.getLogger(PackedFileHash.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }        catch (IOException ex)
        {
            Logger.getLogger(PackedFileHash.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        return (int) (file.length() % Integer.MAX_VALUE);
    }

    private synchronized void buildFastHash() throws FileNotFoundException, IOException, ArchiveException
    {
        if (fastMD5 != null && fastSHA1 != null)
        {
            return;
        }
        boolean error = true;
        InputStream fis = null;
        try
        {
            MessageDigest md5 = MessageDigest.getInstance(MD5);
            MessageDigest sha1 = MessageDigest.getInstance(SHA1);
            if (size > 0)
            {
                byte[] buf = new byte[1024];
                fis = file.getInputStream();
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
            Logger.getLogger(PackedFileHash.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(PackedFileHash.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void buildFullHash() throws FileNotFoundException, IOException, ArchiveException
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
        InputStream fis = null;
        try
        {
            MessageDigest md5 = MessageDigest.getInstance(MD5);
            MessageDigest sha1 = MessageDigest.getInstance(SHA1);
            byte[] buf = new byte[bufSize];
            fis = file.getInputStream();
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
            Logger.getLogger(PackedFileHash.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(PackedFileHash.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public long getSize()
    {
        return size;
    }

    public VirtualFile getFile()
    {
        return file;
    }

    public byte[] getFastMD5() throws FileNotFoundException, IOException, ArchiveException
    {
        if (fastMD5 == null)
        {
            buildFastHash();
        }
        return fastMD5;
    }

    public byte[] getFastSHA1() throws FileNotFoundException, IOException, ArchiveException
    {
        if (fastSHA1 == null)
        {
            buildFastHash();
        }
        return fastSHA1;
    }

    public byte[] getFullMD5() throws FileNotFoundException, IOException, ArchiveException
    {
        if (fullMD5 == null)
        {
            buildFullHash();
        }
        return fullMD5;
    }

    public byte[] getFullSHA1() throws FileNotFoundException, IOException, ArchiveException
    {
        if (fullSHA1 == null)
        {
            buildFullHash();
        }
        return fullSHA1;
    }
    private static final long[] SIZES = FileDigest.buildSizes();

    private boolean equalsHash(PackedFileHash other)
    {
        VirtualFile cause = null;
        try
        {
            final FileDigest digestA = this.getDigest();
            final FileDigest digestB = other.getDigest();
            digestA.keepOn();
            try
            {
                digestB.keepOn();
                try
                {
                    for (int i = 0; i < SIZES.length - 1 && SIZES[i]<size ; i++)
                    {
                        // avoid reading just few bytes in the next iteration
                        if( size<(SIZES[i]+SIZES[i+1])/2 )
                        {
                            break;
                        }
                        final long s = SIZES[i];

                        cause = this.file;
                        byte[] digA = digestA.getHash(s);
                        cause = other.file;
                        byte[] digB = digestB.getHash(s);
                        cause = null;
                        if (digA != null && digB != null)
                        {
                            if (!Arrays.equals(digA, digB))
                            {
                                return false;
                            }
                        }
                    }
                    cause = this.file;
                    byte[] digA = digestA.getHash();
                    cause = other.file;
                    byte[] digB = digestB.getHash();
                    cause = null;
                    if (digA != null && digB != null)
                    {
                        return Arrays.equals(digA, digB);
                    }
                    return false;

                }
                finally
                {
                    digestB.keepOff();
                }
            }
            finally
            {
                digestA.keepOff();
            }
        }




        catch (ArchiveException ex)
        {
            Logger.getLogger(PackedFileHash.class.getName()).log(Level.WARNING, cause == null ? null : cause.getPath(), ex);
        }        catch (FileNotFoundException ex)
        {
            Logger.getLogger(PackedFileHash.class.getName()).log(Level.WARNING, cause == null ? null : cause.getPath(), ex);
        }        catch (IOException ex)
        {
            Logger.getLogger(PackedFileHash.class.getName()).log(Level.SEVERE, cause == null ? null : cause.getPath(), ex);
        }        catch (CloneNotSupportedException ex)
        {
            Logger.getLogger(PackedFileHash.class.getName()).log(Level.SEVERE, cause == null ? null : cause.getPath(), ex);
        }        catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(PackedFileHash.class.getName()).log(Level.SEVERE, cause == null ? null : cause.getPath(), ex);
        }
        exception = true;
        return false;
    }

    private FileDigest getDigest() throws NoSuchAlgorithmException
    {
        synchronized (lock)
        {
            if (digest == null)
            {
                MessageDigest md = ParallelMessageDigest.getInstance(DEF_ALG);
                digest = new FileDigest(file, md);
            }
            return digest;
        }
    }

    @Override
    public String toString()
    {
        return file.toString();
    }

}
