/*
 *  FileHash.java
 *
 *  Copyright (C) 2007  Francisco Gómez Carrasco
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
package org.fjtk.se;

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

    private File file;
    private byte[] fastHash = null;
    private byte[] fullHash = null;
    private static int bufSize = 64*1024;

    public FileHash(File file)
    {
        this.file = file;
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
        if (this.fastHash != other.fastHash && (this.fastHash == null || !Arrays.equals(this.fastHash, other.fastHash)))
        {
            return false;
        }
        this.buildFullHash();
        other.buildFullHash();
        if (this.fullHash != other.fullHash && (this.fullHash == null || !Arrays.equals(this.fullHash, other.fullHash)))
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
        if (fastHash != null)
        {
            return;
        }
        FileInputStream fis = null;
        try
        {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            if (file.length() > 0)
            {
                byte[] buf = new byte[1024];
                fis = new FileInputStream(file);
                int r = fis.read(buf);
                fis.close();
                md5.update(buf, 0, r);
            }

            fastHash = md5.digest();
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
        if (fullHash != null)
        {
            return;
        }

        if (file.length() <= 1024)
        {
            fullHash = fastHash;
            return;
        }

        FileInputStream fis = null;
        try
        {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] buf = new byte[bufSize];
            fis = new FileInputStream(file);
            int r;

            while ((r = fis.read(buf)) > 0)
            {
                md5.update(buf, 0, r);
            }

            fullHash = md5.digest();
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
}
