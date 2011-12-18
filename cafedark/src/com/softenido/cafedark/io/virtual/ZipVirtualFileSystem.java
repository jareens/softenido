/*
 *  ZipVirtualFileSystem.java
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
package com.softenido.cafedark.io.virtual;

import com.softenido.cafedark.io.Files;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveEntry;

/**
 *
 * @author franci
 */
class ZipVirtualFileSystem implements VirtualFileSystem
{

    private static VirtualFilePool pool = new VirtualFilePool();
    private final String[] items;
    private final String name;
    private String path;
    private volatile long length;
    private final boolean directory;

    public ZipVirtualFileSystem(String[] items, String path, ArchiveEntry entry)
    {
        this.items = items;
        this.name = items[items.length - 1];
        this.path = path;
        if (entry != null)
        {
            this.length = entry.getSize();
            this.directory = entry.isDirectory();
        }
        else
        {
            this.length = 0;
            this.directory = false;
        }
    }
    public ZipVirtualFileSystem(String[] items,String path)
    {
        this(items,path,null);
    }

    public ZipVirtualFileSystem(String[] items, ArchiveEntry entry)
    {
        this(items, null, entry);
    }

    public ZipVirtualFileSystem(String[] items)
    {
        this(items, (String) null);
    }

    public long length()
    {
        if(length<0)
        {
            synchronized(this)
            {
                int r =0;
                long count =0;
                byte[] b = new byte[8*1024];
                try
                {
                    InputStream data = getInputStream();
                    try
                    {
                        while( (r=data.read(b))>0)
                        {
                            count+=r;
                        }
                        length=count;
                    }
                    finally
                    {
                        data.close();
                    }
                }
                catch(Exception ex)
                {
                    length=0;
                    Logger.getLogger(ZipVirtualFileSystem.class.getName()).log(Level.WARNING, "ZipEntry={0}", this);
                }
            }
        }
        return length;
    }

    @Override
    public boolean canRead()
    {
        return true;
    }

    @Override
    public boolean canWrite()
    {
        return false;
    }

    @Override
    public boolean delete()
    {
        return false;
    }

    @Override
    public boolean exists()
    {
        return true;
    }

    @Override
    public boolean isDirectory()
    {
        return directory;
    }

    public String getCanonicalPath() throws IOException
    {
        return getPath();//zFile.getCanonicalPath()+"!"+eName;
    }

    public String getAbsolutePath() throws IOException
    {
        return getPath();//zFile.getCanonicalPath()+"!"+eName;
    }

    public InputStream getInputStream() throws IOException, FileNotFoundException, ArchiveException
    {
        return pool.get(items);
    }

    public String getPath()
    {
        if (path == null)
        {
            StringBuilder sb = new StringBuilder();
            sb.append(items[0]);
            for (int i = 1; i < items.length; i++)
            {
                sb.append(VirtualFileSystem.pathSeparator);
                sb.append(items[i]);
            }
            path = sb.toString();
        }
        return path;
    }

    public String[] splitPath()
    {
        return items;
    }

    public File getBaseFile()
    {
        return new File(items[0]);
    }

    @Override
    public String toString()
    {
        return getPath();
    }

    public String getName()
    {
        return name;
    }

    public VirtualFileSystem getCanonicalFile() throws IOException
    {
        String cf = new File(items[0]).getCanonicalPath();
        if (!cf.equals(items[0]))
        {
            String[] paths = new String[items.length];
            paths[0] = cf;
            for (int i = 1; i < paths.length; i++)
            {
                paths[i] = items[i];
            }
            return new ZipVirtualFileSystem(paths);
        }
        return this;
    }

    public VirtualFileSystem getAbsoluteFile() throws IOException
    {
        String cf = new File(items[0]).getAbsolutePath();
        if (!cf.equals(items[0]))
        {
            String[] paths = new String[items.length];
            paths[0] = cf;
            for (int i = 1; i < paths.length; i++)
            {
                paths[i] = items[i];
            }
            return new ZipVirtualFileSystem(paths);
        }
        return this;
    }

    public boolean isHidden()
    {
        return false;
    }

    @Override
    public boolean isFile()
    {
        return (!directory);
    }

    public boolean isLink()
    {
        return false;
    }

    public boolean isLink(boolean path) throws IOException
    {
        return Files.isLink(new File(items[0]), path);
    }

    public int compareTo(VirtualFileSystem other)
    {
        String[] otherItems = other.splitPath();
        int num = Math.min(items.length, otherItems.length);
        for (int i = 0; i < num; i++)
        {
            int cmp = items[i].compareTo(otherItems[i]);
            if (cmp != 0)
            {
                return cmp;
            }
        }
        return (items.length < otherItems.length ? -1 : (items.length == otherItems.length ? 0 : 1));
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
        final ZipVirtualFileSystem other = (ZipVirtualFileSystem) obj;
        if (!Arrays.deepEquals(this.items, other.items))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        return Arrays.deepHashCode(this.items);
    }

    public String getLastPath()
    {
        return items[items.length - 1];
    }

    public boolean isComplex()
    {
        return true;
    }

    public VirtualFileSystem getParentFile()
    {
        String[] parent = Arrays.copyOf(items, items.length-1);
        if(parent.length>1)
        {
            return new ZipVirtualFileSystem(parent);
        }
        if(parent.length==1)
        {
            return new ZipVirtualFileSystem(parent);
        }
        return null;
    }
}
