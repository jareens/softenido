/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.softenido.cafe.io.packed;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author franci
 */
class FilePackedFileSystem implements PackedFileSystem
{
    final File file;

    public FilePackedFileSystem(File file)
    {
        this.file = file;
    }
    public FilePackedFileSystem(String fileName)
    {
        this.file = new File(fileName);
    }

    public long length()
    {
        return file.length();
    }

    public InputStream getInputStream() throws IOException
    {
        return new FileInputStream(file);
    }

    public String getCanonicalPath() throws IOException
    {
        return file.getCanonicalPath();
    }

    public String getPath()
    {
        return file.getPath();
    }
    public File getFile()
    {
        return file;
    }

    public boolean exists()
    {
        return file.exists();
    }

    public boolean canRead()
    {
        return file.canRead();
    }

    public boolean canWrite()
    {
        return file.canWrite();
    }

    public boolean delete()
    {
        return file.delete();
    }
}
