/*
 *  ForEachFileHash.java
 *
 *  Copyright (C) 2007  Francisco Gómez Carrasco
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
package extracticons;

import java.io.FileFilter;
import org.fjtk.ce.Forks;
import org.fjtk.se.FileHash;
import java.io.File;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 *
 * @author franci
 */
public class ForEachFileHash extends ForEachFile
{
    private HashSet<FileHash> hashSet;

    ForEachFileHash(File file, int recursive, FileFilter filter, HashSet<FileHash> fileHashSet, Forks fork)
    {
        super(file, recursive, filter, fork);
        this.hashSet = fileHashSet;
    }
    ForEachFileHash(File dst, int recursive, HashSet<FileHash> fileHashSet, Forks fork)
    {
        this(dst,recursive,null,fileHashSet,fork);
    }

    @Override
    protected void doForEeach(File file,String name)
    {
        if (!hashSet.add(new FileHash(file)))
            doForRepeated(file);
    }

    
    private void doForRepeated(File file)
    {
        System.out.printf("%s\n",file.toString());
    }

    @Override
    protected void doForEeach(ZipFile zf, ZipEntry ze)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
