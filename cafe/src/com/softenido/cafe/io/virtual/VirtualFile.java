/*
 *  VirtualFile.java
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
package com.softenido.cafe.io.virtual;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;

/**
 *
 * @author franci
 */
public class VirtualFile implements Comparable<VirtualFile>, Cloneable
{
    public static final String pathSeparator   = "!";
    public static final char pathSeparatorChar = '!';
    private final String path;
    private final String items[];
    private final VirtualFileSystem fs;
    private final boolean complex;

    public VirtualFile(File file)
    {
        this.path = file.toString();
        this.items= new String[]{this.path};
        this.fs  = new FileVirtualFileSystem(file);
        this.complex = false;
    }
    public VirtualFile(String pathname)
    {
        this.path = pathname;
        this.items = path.split(VirtualFile.pathSeparator);
        this.fs   = pathname.contains(pathSeparator)?new ZipVirtualFileSystem(this.items,pathname):new FileVirtualFileSystem(pathname);
        this.complex=(this.items.length>1);
    }
    public VirtualFile(VirtualFile parent, String child)
    {
        this.path = parent.path+VirtualFile.pathSeparator+child;
        this.items = Arrays.copyOf(parent.items, parent.items.length+1);
        this.items[parent.items.length]=child;
        this.fs = new ZipVirtualFileSystem(this.items,this.path);
        this.complex=true;
    }
    public VirtualFile(VirtualFile parent, ArchiveEntry child)
    {
        this.path = parent.path+VirtualFile.pathSeparator+child.getName();
        this.items = Arrays.copyOf(parent.items, parent.items.length+1);
        this.items[parent.items.length]=child.toString();
        this.fs = new CachedZipVirtualFileSystem(this.items,this.path,child);
        this.complex=true;
    }
    VirtualFile(String[] items, VirtualFileSystem fs, boolean complex)
    {
        this.items = items;
        StringBuilder pathBuilder=new StringBuilder();
        pathBuilder.append(items[0]);
        for(int i=1;i<items.length;i++)
        {
            pathBuilder.append(VirtualFile.pathSeparator);
            pathBuilder.append(items[i]);
        }
        this.path = pathBuilder.toString();
        this.complex = complex;
        this.fs = fs;
    }
    public String[] splitPath()
    {
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
    public VirtualFile getCanonicalFile() throws IOException
    {
        VirtualFileSystem cf = fs.getCanonicalFile();
        if(cf!=cf)
        {
            return new VirtualFile(cf.splitPath(),cf,complex);
        }
        return this;
    }
    public VirtualFile getAbsoluteFile() throws IOException
    {
        VirtualFileSystem af = fs.getAbsoluteFile();
        if(af!=af)
        {
            return new VirtualFile(af.splitPath(),af,complex);
        }
        return this;
    }

    public String getPath()
    {
        return fs.getPath();
    }
    public File getBaseFile()
    {
        return fs.getBaseFile();
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

    public boolean isHidden()
    {
        return fs.isHidden();
    }

    public boolean isFile()
    {
        return fs.isFile();
    }

    public boolean isDirectory()
    {
        return fs.isDirectory();
    }

    public boolean isComplex()
    {
        return complex;
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

    public int compareTo(VirtualFile other)
    {
        return path.compareTo(other.path);
    }

    public String getLastPath()
    {
        return items[items.length-1];
    }

    @Override
    public VirtualFile clone() throws CloneNotSupportedException
    {
        return (VirtualFile) super.clone();
    }
    public static VirtualFileFilter buildFilter(final FileFilter filter)
    {
        if (filter != null)
        {
            return new VirtualFileFilter()
            {
                public boolean accept(VirtualFile pathname)
                {
                    return filter.accept(new File(pathname.getLastPath()));
                }
            };
        }
        return null;
    }
    public static VirtualFile[] asPacketFile(File[] files)
    {
        if(files!=null)
        {
            VirtualFile[] pf = new VirtualFile[files.length];
            for(int i=0;i<files.length;i++)
            {
                pf[i] = new VirtualFile(files[i]);
            }
            return pf;
        }
        return null;
    }

    public boolean isLink() throws IOException
    {
        return fs.isLink();
    }

}
