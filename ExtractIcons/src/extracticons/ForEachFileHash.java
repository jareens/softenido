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

    public ForEachFileHash(String filename, HashSet<FileHash> hashSet)
    {
        super(filename);
        this.hashSet = hashSet;
    }

    public ForEachFileHash(String filename, int recursive, HashSet<FileHash> hashSet)
    {
        super(filename,recursive);
        this.hashSet = hashSet;
    }

    public ForEachFileHash(File file, HashSet<FileHash> hashSet)
    {
        super(file);
        this.hashSet = hashSet;
    }

    public ForEachFileHash(File file, int recursive, HashSet<FileHash> hashSet)
    {
        super(file,recursive);
        this.hashSet = hashSet;
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
