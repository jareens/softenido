/*
 *  ScaleImage.java
 *
 *  Copyright (C) 2009-2011  Francisco Gómez Carrasco
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
package com.softenido.cafedark.imageio;

import com.softenido.cafedark.image.ScaleDimension;
import com.softenido.cafedark.image.SimpleScaleDimension;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;

/**
 *
 * @author franci
 */
public class FilterImage
{
    private final ConvolveOp blurOp = new ConvolveOp(new Kernel(3, 3, new float[] { 0.1111f, 0.1111f, 0.1111f,0.1111f, .1111f, .1111f,0.1111f, 0.1111f, 0.1111f }));
    private final ScaleDimension scale;
    private final boolean gray;
    private final boolean nop;

    public FilterImage(ScaleDimension scale, boolean gray)
    {
        this.scale = scale;
        this.gray  = gray;
        this.nop   = (scale==null) && !gray;
    }

    public FilterImage(ScaleDimension scale)
    {
        this(scale,false);
    }

    public FilterImage(boolean gray)
    {
        this(null,gray);
    }
    public FilterImage(int maxWidth, int maxHeight, boolean grayscale)
    {
        this(new SimpleScaleDimension(maxWidth,maxHeight),grayscale);
    }
    public FilterImage(int maxWidth, int maxHeight)
    {
        this(maxWidth,maxHeight,false);
    }
    
    public boolean filter(File inputImage, File outputImage, String format) throws Exception
    {
        InputStream in = new FileInputStream(inputImage);
        try
        {
            InputStream in2 = filter(in, format);
            try
            {
                OutputStream out = new FileOutputStream(outputImage);
                try
                {
                    byte buf[] = new byte[64 * 1024];
                    int r;
                    while ((r = in2.read(buf)) > 0)
                    {
                        out.write(buf, 0, r);
                    }
                }
                finally
                {
                    out.close();
                }
                return (in!=in2);
            }
            finally
            {
                in2.close();
            }
        }
        finally
        {
            in.close();
        }
    }

//        // Write the scaled image to the outputstream
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(thumbImage);
//        int quality = 100; // Use between 1 and 100, with 100 being highest quality
//        quality = Math.max(0, Math.min(quality, 100));
//        param.setQuality((float) quality / 100.0f, false);
//        encoder.setJPEGEncodeParam(param);
//        encoder.encode(thumbImage);
//        ImageIO.write(thumbImage, "jpg", out);


    public InputStream filter(InputStream imageStream, String format) throws IOException
    {
        if(nop)
        {
            return imageStream;
        }
        
        BufferedImage image = ImageIO.read(new BufferedInputStream(imageStream));
        image = filter(image);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, format, out);
        
        ByteArrayInputStream bis = new ByteArrayInputStream(out.toByteArray());

        return bis;
    }

    public BufferedImage filter(BufferedImage img)
    {
        if(nop)
        {
            return img;
        }

        // Make sure the aspect ratio is maintained, so the image is not skewed
        Dimension size  = new Dimension(img.getWidth(),img.getHeight());
        Dimension resize= (scale==null)?null:scale.convert(size);
        if(resize==null)
        {
            resize = size;
        }

        if(!resize.equals(size) || gray)
        {
            img = scaleByGraphics2D(img,resize.width, resize.height, gray);
        }
        return img;
    }

    private static BufferedImage scaleByAffine(BufferedImage img, int w, int h)
    {
        double sx = ((double) w) / ((double) img.getWidth());
        double sy = ((double) h) / ((double) img.getHeight());

        AffineTransform ops = new AffineTransform();
        ops.scale(sx, sy);

        AffineTransformOp op = new AffineTransformOp(ops, AffineTransformOp.TYPE_BICUBIC);       
        return op.filter(img, op.createCompatibleDestImage(img, ColorModel.getRGBdefault()));
    }
    private static BufferedImage scaleByGraphics2D(BufferedImage img, int w, int h, boolean gray)
    {
        BufferedImage scaledImage = new BufferedImage(w, h, gray?BufferedImage.TYPE_BYTE_GRAY:BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = scaledImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics2D.drawImage(img, 0, 0, w, h, null);
        return scaledImage;
    }
    
    private static BufferedImage grayScale(BufferedImage img)
    {
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        ColorConvertOp op = new ColorConvertOp(cs, null);
        return op.filter(img, null);
    }
    

}