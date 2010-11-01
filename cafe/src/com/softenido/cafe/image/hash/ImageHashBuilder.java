/*
 *  ImageHashBuilder.java
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
package com.softenido.cafe.image.hash;

import com.softenido.cafe.image.ScaleDimension;
import com.softenido.cafe.imageio.ScaleImage;
import com.softenido.cafe.image.SimpleScaleDimension;
import com.softenido.cafe.io.virtual.VirtualFile;
import com.softenido.cafe.io.virtual.PackedPool;
import com.softenido.cafe.security.ParallelMessageDigest;
import com.softenido.cafe.util.ArrayUtils;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.apache.commons.compress.archivers.ArchiveException;

/**
 *
 * @author franci
 */
public class ImageHashBuilder
{
    private static final String MD5 = "MD5";
    private static final String SHA1 = "SHA-1";
    private static final String[] DEF_ALG =
    {
        MD5, SHA1
    };
    private static final AtomicInteger count = new AtomicInteger();

    private static final PackedPool pool = new PackedPool();

    private final ScaleImage scale;
 
    public ImageHashBuilder(boolean gray, int size)
    {
        ScaleDimension sd = (size==0)? null :new SimpleScaleDimension(size);
        this.scale = new ScaleImage(sd,gray);
    }

    public ImageHashBuilder(boolean grey)
    {
        this(grey, 0);
    }
    
    public ImageHash buildHash(VirtualFile pf) throws FileNotFoundException, IOException, ArchiveException
    {
        if(pf.length()==0)
        {
            return null;
        }
        InputStream in = pool.get(pf);
        try
        {
            BufferedImage image = ImageIO.read(in);
            if(image!=null)
            {
                return buildHash(image);
            }
            return null;
        }
        catch(Exception ex)
        {
            Logger.getLogger(ImageHashBuilder.class.getName()).log(Level.WARNING, pf.toString(), ex);
            return null;
        }
        finally
        {
            in.close();
        }
    }
    public ImageHash buildHash(BufferedImage image)
    {
        count.incrementAndGet();
        image = scale.filter(image);
        int w = image.getWidth();
        int h = image.getHeight();

        int[] pixels = null;
        //pixels = image.getData().getPixels(0, 0, w, h, pixels);
        pixels = image.getRGB(0, 0, w, h, pixels, 0, w);
//        System.out.println(Arrays.toString(pixels));
//        System.out.println(Arrays.toString(ArrayUtils.getByteArray(pixels)));
        byte[]hash = buildDigest(pixels);
        int hc = (w*h) + (w-h) + Arrays.hashCode(hash);
        return new ImageHash(w, h, hc, hash);
    }
    protected static byte[] buildDigest(int[] pixels)
    {
        try
        {
            MessageDigest md = ParallelMessageDigest.getInstance(DEF_ALG);
            return md.digest(ArrayUtils.getByteArray(pixels));
        }
        catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(ImageHashBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ArrayUtils.getByteArray(pixels);
    }
}
