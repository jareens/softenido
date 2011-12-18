/*
 *  CoveredPath.java
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
package com.softenido.cafedark.io;

import com.softenido.cafedark.io.virtual.VirtualFile;
import com.softenido.cafedark.io.virtual.VirtualFiles;
import java.io.IOException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author franci
 */
class CoveredPath
{
    private final Object lock = new Object();
    private final boolean symlinks;
    private final HashSet<VirtualFile> paths = new HashSet<VirtualFile>();

    public CoveredPath(boolean symlinks)
    {
        this.symlinks = symlinks;
    }

    public boolean add(VirtualFile file, boolean base) throws IOException
    {
        return add(file,base,file.isLink());
    }
    public boolean add(VirtualFile file, boolean base, boolean link) throws IOException
    {
        final VirtualFile canonical = file.getCanonicalFile();
        synchronized(lock)
        {
            if(paths.contains(canonical))
            {
                Logger.getLogger(CoveredPath.class.getName()).log(Level.FINE,"refused=''{0}'' as ''{1}''",new Object[]{file,canonical});
                return false;
            }
            //if( base || link || symlinks)
            if( base || link )
            {
                VirtualFile[] parents = VirtualFiles.getParentFiles(canonical);
                for (VirtualFile item : parents)
                {
                    if(paths.contains(item))
                    {
                        Logger.getLogger(CoveredPath.class.getName()).log(Level.FINE,"refused=''{0}'' as ''{1}'' by ''{2}''",new Object[]{file,canonical,item});
                        return false;
                    }
                }
                
                for (VirtualFile item : paths)
                {
                    if (VirtualFiles.isParentOf(item, canonical, true))
                    {
                        Logger.getLogger(CoveredPath.class.getName()).log(Level.FINE,"refused=''{0}'' as ''{1}'' by ''{2}''",new Object[]{file,canonical,item});
                        return false;
                    }
                }
                paths.add(canonical);
                Logger.getLogger(CoveredPath.class.getName()).log(Level.FINE,"path=''{0}'' as ''{1}''",new Object[]{file,canonical});
                return true;
            }
        }
        return true;
    }
}
