/*
 *  FileHashBySizeContent.java
 *
 *  Copyright (C) 2007-2010  Francisco Gómez Carrasco
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
package com.softenido.cafe.io;

import com.softenido.cafe.io.packed.PackedFile;

/**
 *
 * @author franci
 */
public class FileHashByName implements FileHash
{

    private final PackedFile file;
    private final String name;
    private final int size;
    private final boolean ignoreCase;

    public FileHashByName(PackedFile file,boolean ignoreCase)
    {
        this.file = file;
        this.name = file.getName();
        this.size = this.name.length();
        this.ignoreCase = ignoreCase;
    }

    public long getSize()
    {
        return size;
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
        final FileHashByName other = (FileHashByName) obj;
        if (file.equals(other.file))
        {
            return true;
        }
        if(this.size != other.size)
        {
            return false;
        }
        return ignoreCase? name.equalsIgnoreCase(other.name) : name.equals(other.name);
    }

    @Override
    public int hashCode()
    {
        return size;
    }

    public PackedFile getFile()
    {
        return file;
    }

    @Override
    public String toString()
    {
        return file.toString();
    }

}
