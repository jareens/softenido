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
package com.softenido.cafe.io.virtual;

import com.softenido.cafe.io.Files;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import org.apache.commons.compress.archivers.ArchiveException;

/**
 *
 * @author franci
 */
class ZipVirtualFileSystem implements VirtualFileSystem
{
    private static PackedPool pool = new PackedPool();
    private final String[] items;
    private final String path;
    private final String name;

    public ZipVirtualFileSystem(String[] items,String path)
    {
        this.items = items;
        this.path = path;
        this.name = items[items.length-1];
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
        return pool.get(items);
    }

    public String getPath()
    {
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

    public String getName()
    {
        return name;
    }

    public VirtualFileSystem getCanonicalFile() throws IOException
    {
        String cf = new File(items[0]).getCanonicalPath();
        if(!cf.equals(items[0]))
        {
            String[] paths = new String[items.length];
            paths[0] = cf;
            for(int i=1;i<paths.length;i++)
            {
                    paths[i] = items[i];
            }
            return new ZipVirtualFileSystem(paths,path);
        }
        return this;
    }

    public VirtualFileSystem getAbsoluteFile() throws IOException
    {
        String cf = new File(items[0]).getAbsolutePath();
        if(!cf.equals(items[0]))
        {
            String[] paths = new String[items.length];
            paths[0] = cf;
            for(int i=1;i<paths.length;i++)
            {
                    paths[i] = items[i];
            }
            return new ZipVirtualFileSystem(paths,path);
        }
        return this;
    }

    public boolean isHidden()
    {
        return false;
    }

    public boolean isFile()
    {
        return true;
    }

    public boolean isDirectory()
    {
        return false;
    }

    public boolean isLink()
    {
        return false;
    }

    public boolean isLink(boolean path) throws IOException
    {
        return Files.isLink(new File(items[0]), path);
    }
}
