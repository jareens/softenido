/*
 *  ForEachImageHash.java
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

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.fjtk.ce.Forks;


/**
 *
 * @author franci
 */
public class ForEachImageHash extends ForEachFile
{
    private Set<ImageHash> hashSet;

//    public ForEachImageHash(String filename, HashSet<ImageHash> hashSet)
//    {
//        super(filename);
//        this.hashSet = hashSet;
//    }
//
//    public ForEachImageHash(String filename, int recursive, HashSet<ImageHash> hashSet,Forks fork)
//    {
//        super(filename,recursive,fork);
//        this.hashSet = hashSet;
//    }
//
//    public ForEachImageHash(File file, HashSet<ImageHash> hashSet)
//    {
//        super(file);
//        this.hashSet = hashSet;
//    }
//
    public ForEachImageHash(File file, int recursive, Set<ImageHash> hashSet,Forks fork)
    {
        super(file,recursive,null,fork);
        this.hashSet = hashSet;
    }

    @Override
    protected void doForEeach(File file,String name)
    {
        if (!hashSet.add(new ImageHash(file)))
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
