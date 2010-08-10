/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.softenido.cafe.io.packed;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.ArchiveException;

/**
 *
 * @author franci
 */
class ZipPackedFileSystem implements PackedFileSystem
{
    private static PackedPool pool = new PackedPool();
    private final String path;
    
    public ZipPackedFileSystem(String path)
    {
        this.path = path;
    }
    public long length()
    {
        return 0;//getZipEntry().getSize();
    }

    public String getCanonicalPath() throws IOException
    {
        return path;//zFile.getCanonicalPath()+"!"+eName;
    }
    public String getAbsolutePath() throws IOException
    {
        return path;//zFile.getCanonicalPath()+"!"+eName;
    }

    public InputStream getInputStream() throws IOException, FileNotFoundException, ArchiveException
    {
        return pool.get(new PackedFile(path));
    }

    public String getPath()
    {
        return path;
    }

    public File getFile()
    {
        return new File(path);
    }

    public boolean exists()
    {
        return true;//aki
    }

    public boolean canRead()
    {
        return true;//aki
    }

    public boolean canWrite()
    {
        return false;
    }

    public boolean delete()
    {
        return false;
    }


    @Override
    public String toString()
    {
        return path;
    }
}
