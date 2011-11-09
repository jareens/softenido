/*
 *  FileComparatorByImage.java
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
package com.softenido.findrepe;

import com.softenido.cafe.imageio.ImageHash;
import com.softenido.cafe.io.FileHash;
import com.softenido.cafe.io.packed.PackedFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.compress.archivers.ArchiveException;

/**
 *
 * @author franci
 */
public class FileComparatorByImage implements Comparator<PackedFile>
{
    private final boolean half;
    private static final Object lock = new Object();
    private static final Map<PackedFile,WeakReference<ImageHash>> map = Collections.synchronizedMap(new WeakHashMap<PackedFile,WeakReference<ImageHash>>());


    private static int pc=100;
    private static int fails=0;

    public FileComparatorByImage(boolean half)
    {
        this.half = half;
    }

    @Override
    public int compare(PackedFile pf1, PackedFile pf2)
    {
        if(pf1==pf2)
        {
            return 0;
        }
//        if(half)
//        {
//            long size1 = pf1.length();
//            long size2 = pf2.length();
//            return (size1<size2 ? -1 : (size1==size2 ? 0 : 1));
//        }
        try
        {
            ImageHash fh1 = getHash(pf1);
            ImageHash fh2 = getHash(pf2);
            return fh1.compareTo(fh2);
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(FileComparatorByImage.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(FileComparatorByImage.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ArchiveException ex)
        {
            Logger.getLogger(FileComparatorByImage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pf1.compareTo(pf2);
    }

    private static ImageHash getHash(PackedFile pf) throws FileNotFoundException, IOException, ArchiveException
    {
        WeakReference<ImageHash> wr = map.get(pf);
        ImageHash fh = wr!=null?wr.get():null;
        if(fh==null)
        {
            synchronized(lock)
            {
                fh = new ImageHash(pf);
                map.put(pf, new WeakReference<ImageHash>(fh));
                fails++;
                int curpc = (int)((map.size()*100.0)/fails);
                if(curpc<pc)
                {
                    Logger.getLogger(FileComparatorByHash.class.getName()).log(Level.CONFIG, "{0}>{1} {2}%",new Integer[]{fails,map.size(),curpc});
                    pc=curpc;
                }
            }
        }
        return fh;
    }

}
