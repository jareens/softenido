/*
 *  ImageHash.java
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

import org.fjtk.ce.ArrayUtils;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author franci
 */
public class ImageHash
{

    private File file;
    private int width = 0;
    private int height = 0;
    private byte[] imageHash = null;

    public ImageHash(File file)
    {
        this.file = file;
    }

    public int getHeight()
    {
        buildHash();
        return height;
    }

    public int getWidth()
    {
        buildHash();
        return width;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final ImageHash other = (ImageHash) obj;
        if (this.file == other.file || (this.file != null && this.file.equals(other.file)))
        {
            return true;
        }
        this.buildHash();
        other.buildHash();
        if (this.width != other.width)
        {
            return false;
        }
        if (this.height != other.height)
        {
            return false;
        }
        if (this.imageHash != other.imageHash && (this.imageHash == null || !this.imageHash.equals(other.imageHash)))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        buildHash();
        return width+height;
    }
    
    private void buildHash()
    {
        if (imageHash != null)
        {
            return;
        }
        
        try
        {

            Image img = Toolkit.getDefaultToolkit().getImage(file.toString());
            BufferedImage bufImg = toBufferedImage(img);
            width = bufImg.getWidth();
            height= bufImg.getHeight();
            img = null;
            int[] rgbArray = new int[width * height];
            bufImg.getRGB(0, 0, width, height, rgbArray, 0, 1);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(ArrayUtils.getByteArray(rgbArray));
            imageHash = md5.digest();
        }
        catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(ImageHash.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // This method returns a buffered image with the contents of an image
    public static BufferedImage toBufferedImage(Image image)
    {
        if (image instanceof BufferedImage)
        {
            return (BufferedImage) image;
        }

        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();

        // Determine if the image has transparent pixels; for this method's
        // implementation, see e661 Determining If an Image Has Transparent Pixels
        boolean hasAlpha = hasAlpha(image);

        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try
        {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha)
            {
                transparency = Transparency.BITMASK;
            }

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
        }
        catch (HeadlessException e)
        {
        // The system does not have a screen
        }

        if (bimage == null)
        {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha)
            {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics g = bimage.createGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bimage;
    }
    // This method returns true if the specified image has transparent pixels
    public static boolean hasAlpha(Image image)
    {
        // If buffered image, the color model is readily available
        if (image instanceof BufferedImage)
        {
            BufferedImage bimage = (BufferedImage) image;
            return bimage.getColorModel().hasAlpha();
        }

        // Use a pixel grabber to retrieve the image's color model;
        // grabbing a single pixel is usually sufficient
        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try
        {
            pg.grabPixels();
        }
        catch (InterruptedException e)
        {
        }

        // Get the image's color model
        ColorModel cm = pg.getColorModel();
        if(cm==null)
            return false;
        return cm.hasAlpha();
    }
}
