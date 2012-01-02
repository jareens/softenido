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

import com.softenido.cafecore.equals.EqualsDataBuilder;
import com.softenido.cafedark.image.hash.ImageHashBuilder;
import com.softenido.cafedark.io.Hash;
import com.softenido.cafedark.io.virtual.VirtualFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.compress.archivers.ArchiveException;

/**
 *
 * @author franci
 */
public class FileComparatorByImage extends EqualsDataBuilder<VirtualFile,Hash>
{
    private final boolean half;
    private static final Object lock = new Object();
    private static final Map<VirtualFile,WeakReference<Hash>> map = Collections.synchronizedMap(new WeakHashMap<VirtualFile,WeakReference<Hash>>());

    private final ImageHashBuilder ihb;


    public FileComparatorByImage(boolean half,boolean gray, int size, float colorThreshold, float countThreshold, boolean blur)
    {
        this.half = half;
        this.ihb  = new ImageHashBuilder(gray,size,colorThreshold, countThreshold);
    }

    private Hash getHash(VirtualFile pf) throws FileNotFoundException, IOException, ArchiveException
    {
        WeakReference<Hash> wr = map.get(pf);
        Hash fh = wr!=null?wr.get():null;
        if(fh==null)
        {
            fh = ihb.buildHash(pf);
            if(fh!=null)
            {
                map.put(pf, new WeakReference<Hash>(fh));
            }
            //fh = new FileHash(pf);
        }
        return fh;
    }

    @Override
    public Hash buildData(VirtualFile e)
    {
        try
        {
            return getHash(e);
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
        return null;
    }

}
