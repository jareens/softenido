/*
 *  ZipPackedFileSystem.java
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
package com.softenido.headless.io.packed;

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
    private String name = null;

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

    public String getName()
    {
        if (name == null)
        {
            String[] items = path.split(PackedFile.pathSeparator);
            name = items[items.length-1];
        }
        return name;
    }
}
