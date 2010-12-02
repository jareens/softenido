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
import com.softenido.cafe.io.Hash;
import com.softenido.cafe.io.virtual.VirtualFile;
import com.softenido.cafe.io.virtual.VirtualFilePool;
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
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
    private static final VirtualFilePool pool = new VirtualFilePool();

    private final ScaleImage scale;
    private final float colorThreshold;
    private final float countThresold;
    private final boolean gray;
 
    public ImageHashBuilder(boolean gray, int size, float colorThreshold, float countThreshold)
    {
        ScaleDimension sd = (size == 0) ? null : new SimpleScaleDimension(size);
        this.scale = new ScaleImage(sd, gray);
        this.colorThreshold = colorThreshold;
        this.countThresold = countThreshold;
        this.gray = gray;
    }
    
    public Hash buildHash(VirtualFile pf) throws FileNotFoundException, IOException, ArchiveException
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
    public Hash buildHash(BufferedImage image)
    {
        count.incrementAndGet();
        image = scale.filter(image);

        int w = image.getWidth();
        int h = image.getHeight();

//        JFrame frame = new JFrame();
//        frame.setTitle("["+w+"x"+h+"]"+" ");
//        frame.add(new JLabel(new ImageIcon(image)));
//        frame.setVisible(true);
//        frame.pack();

        int[] pixels = null;
        pixels = image.getRGB(0, 0, w, h, pixels, 0, w);
        if(colorThreshold>0 && gray)
        {
            byte[] hash = new byte[pixels.length];
            for(int i=0;i<hash.length;i++)
            {
                hash[i] = (byte) (pixels[i] & 0xff);
            }
            int hc = (w*h) + (w-h);
//            System.out.println(Arrays.toString(hash));
            return new StickyImageHash(w, h, hc, hash, colorThreshold, countThresold);
        }
        if(colorThreshold > 0)
        {
            byte[]hash = ArrayUtils.getByteArray(pixels);
            int hc = (w*h) + (w-h);
            System.out.println(Arrays.toString(hash));
            return new StickyImageHash(w, h, hc, hash, colorThreshold, countThresold);
        }
        byte[]hash = buildDigest(pixels);
        int hc = (w*h) + (w-h) + Arrays.hashCode(hash);
        System.out.println(Arrays.toString(hash));
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
