/*
 *  ImageHash.java
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
package com.softenido.cafe.imageio;

import com.softenido.cafe.io.packed.PackedFile;
import com.softenido.cafe.io.packed.PackedPool;
import com.softenido.cafe.util.ArrayUtils;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.BitSet;
import javax.imageio.ImageIO;
import org.apache.commons.compress.archivers.ArchiveException;

/**
 *
 * @author franci
 */
public class ImageHash implements Comparable<ImageHash>
{
    private static final int SIZE = 160;
    private static final int SIZE2 = SIZE*SIZE;
    private static final ScaleImage scale = new ScaleImage(new FixedScaleDimension(SIZE,SIZE),true,true, false);
    private static final PackedPool pool = new PackedPool();

    private static int colorThreshold = 128;
    private static int countThreshold = SIZE2/50;

    private final BitSet hash;
    private final int[] wh;

    public ImageHash(BufferedImage image) throws Exception
    {
        hash = buildHash(image);
        int w = image.getWidth();
        int h = image.getHeight();
        wh= new int[]{w*h,w,h};
    }

    public ImageHash(PackedFile pf) throws FileNotFoundException, IOException, ArchiveException
    {
        InputStream in = pool.get(pf);
        try
        {
            BufferedImage image = ImageIO.read(in);
            hash = buildHash(image);
            int w = image.getWidth();
            int h = image.getHeight();
            wh= new int[]{w*h,w,h};
        }
        finally
        {
            in.close();
        }
    }

    public int compareTo(ImageHash other)
    {
        //int cmp = ArrayUtils.compare(hash, other.hash);

        //comparar tamaños, y luego si no hay diferencias comparar el resto;

        for(int i=0;i<SIZE2;i++)
        {
            boolean a = hash.get(i);
            boolean b = other.hash.get(i);
            if(a!=b)
            {
                if(a)
                    return +1;
                return -1;
            }
        }
        return 0;
    }

    public static BitSet buildHash(BufferedImage image)
    {
        int[] pixels = null;

        image = scale.filter(image);
        pixels = image.getData().getPixels(0, 0, SIZE, SIZE, (int[])null);
        BitSet bits = new BitSet(SIZE2);
        
        for(int i = 0 ;i<SIZE2;i++)
        {
            boolean p = (pixels[i]>=colorThreshold);
            bits.set(i,p);
        }
        return bits;
    }

}
