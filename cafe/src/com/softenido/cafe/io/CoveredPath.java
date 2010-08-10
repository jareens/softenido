/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.softenido.cafe.io;

import java.io.File;
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
    private final HashSet<File> paths = new HashSet<File>();

    public CoveredPath(boolean symlinks)
    {
        this.symlinks = symlinks;
    }

    public boolean add(File file, boolean base) throws IOException
    {
        return add(file,base,Files.isLink(file));
    }
    public boolean add(File file, boolean base, boolean link) throws IOException
    {
        if( base || link || symlinks)
        {
            synchronized(lock)
            {
                final File canonical = file.getCanonicalFile();
                if(paths.contains(canonical))
                {
                    return false;
                }
                if( base || link )
                {
                    for (File item : paths)
                    {
                        if (Files.isParentOf(item, canonical, true))
                        {
                            return false;
                        }
                    }
                    paths.add(canonical);
                    Logger.getLogger(CoveredPath.class.getName()).log(Level.FINE,"lockedPath={0}",canonical);
                    return true;
                }
            }
        }
        return true;
    }
}
