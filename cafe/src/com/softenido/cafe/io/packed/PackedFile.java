/*
 *  PackedFile.java
 *
 *  Copyright (C) 2010  Francisco Gómez Carrasco
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
package com.softenido.cafe.io.packed;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;

/**
 *
 * @author franci
 */
public class PackedFile
{
    public static final String pathSeparator   = "!";
    public static final char pathSeparatorChar = '!';
    private final String path;
    private String name =null;
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

    public PackedFile(PackedFile pf, ArchiveEntry child)
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
    public InputStream getInputStream() throws IOException, ArchiveException
    {
        return fs.getInputStream();
    }
    public String getCanonicalPath() throws IOException
    {
        return fs.getCanonicalPath();
    }
    public String getAbsolutePath() throws IOException
    {
        return fs.getAbsolutePath();
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

    public String getName()
    {
        return fs.getName();
    }
    
}
