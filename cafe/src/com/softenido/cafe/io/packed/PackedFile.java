/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.cafe.io.packed;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;

/**
 *
 * @author franci
 */
public class PackedFile
{
    public static final String pathSeparator   = "!";
    public static final char pathSeparatorChar = '!';
    private final String path;
    private String items[]=null;
    private final PackedFileSystem fs;
    public PackedFile(File file)
    {
        this.path = file.toString();
        this.fs  = new FilePackedFileSystem(file);
    }
    public PackedFile(String pathname)
    {
        this.path = pathname;
        this.fs   = pathname.contains(pathSeparator)?new ZipPackedFileSystem(pathname):new FilePackedFileSystem(pathname);
    }
    public PackedFile(PackedFile parent, String child)
    {
        this.path = parent.path+PackedFile.pathSeparator+child;
        this.fs = new ZipPackedFileSystem(path);
    }

    public PackedFile(PackedFile pf, ZipEntry child)
    {
        this.path = pf.path+PackedFile.pathSeparator+child.getName();
        this.fs = new CachedZipPackedFileSystem(this.path,child);
    }
    public String[] splitPath()
    {
        if(items==null)
        {
            items = path.split(PackedFile.pathSeparator);
        }
        return items;
    }
    public long length()
    {
        return fs.length();
    }
    public InputStream getInputStream() throws IOException
    {
        return fs.getInputStream();
    }
    public String getCanonicalPath() throws IOException
    {
        return fs.getCanonicalPath();
    }

    public String getPath()
    {
        return fs.getPath();
    }
    public File getFile()
    {
        return fs.getFile();
    }
    public boolean exists()
    {
        return fs.exists();
    }
    public boolean canRead()
    {
        return fs.canRead();
    }
    public boolean canWrite()
    {
        return fs.canWrite();
    }
    public boolean delete()
    {
        return fs.delete();
    }

    @Override
    public String toString()
    {
        return path;
    }
    
}
