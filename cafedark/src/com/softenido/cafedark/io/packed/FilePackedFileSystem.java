/*
 *  FilePackedFileSystem.java
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
package com.softenido.cafedark.io.packed;

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

    public String getName()
    {
        return file.getName();
    }
}
