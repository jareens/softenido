/*
 *  ScaleImage.java
 *
 *  Copyright (C) 2009  Francisco Gómez Carrasco
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

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;

/**
 *
 * @author franci
 */
public class ScaleImage
{
    public static boolean scaleImage(File inputImage, File outputImage, int width, int height) throws Exception
    {
        InputStream in = new FileInputStream(inputImage);
        try
        {
            InputStream in2 = scaleImage(in, width, height);
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
        return false;
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

    public static InputStream scaleImage(InputStream inputImage, int maxWidth, int maxHeight) throws Exception
    {
        return scaleImage(inputImage, new SimpleScaleDimension(maxWidth,maxHeight));
    }
    public static InputStream scaleImage(InputStream inputImage, ScaleDimension scale) throws Exception
    {

        InputStream imageStream = new BufferedInputStream(inputImage);
        Image image = (Image) ImageIO.read(imageStream);

        // Make sure the aspect ratio is maintained, so the image is not skewed
        Dimension size  = new Dimension(image.getWidth(null),image.getHeight(null));
        Dimension resize= scale.convert(size);
        if(resize==null)
        {
            resize = size;
        }

        // Draw the scaled image
        BufferedImage scaledImage = new BufferedImage(resize.width, resize.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = scaledImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(image, 0, 0, resize.width, resize.height, null);

        // Write the scaled image to the outputstream
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(scaledImage, "jpg", out);

        // Read the outputstream into the inputstream for the return value
        ByteArrayInputStream bis = new ByteArrayInputStream(out.toByteArray());

        return bis;
    }
}
