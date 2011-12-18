/*
 *  FileVirtualFileSystem.java
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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author franci
 */
class FileVirtualFileSystem implements VirtualFileSystem
{
    final File file;

    public FileVirtualFileSystem(File file)
    {
        this.file = file;
    }
    public FileVirtualFileSystem(String fileName)
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
    public String getAbsolutePath() throws IOException
    {
        return file.getAbsolutePath();
    }

    public String getCanonicalPath() throws IOException
    {
        return file.getCanonicalPath();
    }

    public String getPath()
    {
        return file.getPath();
    }
    public File getBaseFile()
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

    public String getName()
    {
        return file.getName();
    }

    public FileVirtualFileSystem getCanonicalFile() throws IOException
    {
        File cf = file.getCanonicalFile();
        if(!cf.equals(file))
        {
            return new FileVirtualFileSystem(cf);
        }
        return this;
    }

    public VirtualFileSystem getAbsoluteFile() throws IOException
    {
        File af = file.getAbsoluteFile();
        if(!af.equals(file))
        {
            return new FileVirtualFileSystem(af);
        }
        return this;
    }

    public boolean isHidden()
    {
        return file.isHidden();
    }

    public boolean isFile()
    {
        return file.isFile();
    }

    public boolean isDirectory()
    {
        return file.isDirectory();
    }

    public boolean isLink() throws IOException
    {
        return Files.isLink(file);
    }

    public boolean isLink(boolean path) throws IOException
    {
        return Files.isLink(file, path);
    }

    public String[] splitPath()
    {
        return new String[]{file.toString()};
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
        final FileVirtualFileSystem other = (FileVirtualFileSystem) obj;
        if (this.file != other.file && (this.file == null || !this.file.equals(other.file)))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        return (this.file != null ? this.file.hashCode() : 0);
    }

    @Override
    public String toString()
    {
        return file.toString();
    }

    public String getLastPath()
    {
        return file.getPath();
    }

    public boolean isComplex()
    {
        return false;
    }

    public FileVirtualFileSystem getParentFile()
    {
        File parent = file.getParentFile();
        return (parent != null) ? new FileVirtualFileSystem(parent) : null;
    }
   
}
