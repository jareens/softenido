/*
 *  ForEachFileCopy.java
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
import org.fjtk.se.Files;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 * @author franci
 */
public class ForEachFileCopy extends ForEachFile
{

    private HashSet<FileHash> fileHashSet = null;
    private File dst;

    public File getDst()
    {
        return dst;
    }
    
    public ForEachFileCopy(String src, String dst, FileFilter filter)
    {
        super(src, filter);
        this.dst = new File(dst);
    }

    public ForEachFileCopy(String src, int recursive, String dst, FileFilter filter)
    {
        super(src, recursive, filter);
        this.dst = new File(dst);
    }

    public ForEachFileCopy(File src, File dst, FileFilter filter)
    {
        super(src, filter);
        this.dst = dst;
    }

    public ForEachFileCopy(File src, int recursive, File dst, FileFilter filter)
    {
        super(src, recursive, filter);
        this.dst = dst;
    }

    protected void addHash(File fileDst)
    {
        fileHashSet.add(new FileHash(fileDst));
    }
    
    protected boolean acceptCopy(File file)
    {
        return !fileHashSet.contains(new FileHash(file));
    }
   
    @Override
    protected void doForEeach(File file, String name)
    {
        if(acceptCopy(file))
        {
            try
            {
                if (name == null)
                {
                    name = file.getName();
                }
                File fileDst = new File(dst, name);
                for (int i = 0; fileDst.exists() && i < Integer.MAX_VALUE; i++)
                {
                    fileDst = new File(dst, Integer.toString(i) + name);
                }
                Files.copy(file, fileDst);
                addHash(fileDst);
                System.gc();
            }
            catch (IOException ex)
            {
                Logger.getLogger(ForEachFileCopy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            doForRepeated(file);
        }

    }

    @Override
    public void run()
    {
        if(fileHashSet==null) 
        {
            fileHashSet = new HashSet<FileHash>();
            ForEachFileHash taskHashMap = new ForEachFileHash(dst, getRecursive(), fileHashSet);
            taskHashMap.run();
            taskHashMap = null;
        }
        
        super.run();
    }

    @Override
    protected void doForEeach(ZipFile zf, ZipEntry ze)
    {
        try
        {
            File file = unZip(null, zf, ze);
            String name = (new File(ze.getName()).getName());
            doForEeach(file, name);
            file.delete();
        }
        catch (IOException ex)
        {
            Logger.getLogger(ForEachFileCopy.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void doForRepeated(File file)
    {
        System.out.printf("%s already exists\n", file.toString());
    }
}
