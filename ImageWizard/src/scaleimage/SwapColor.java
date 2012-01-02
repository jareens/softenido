/*
 *  SwapColor.java
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
package scaleimage;

import com.softenido.cafedark.image.SwapImageColor;
import com.softenido.cafedark.imageio.ImageFormat;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author franci
 */
public class SwapColor
{
    public static void main(String[] args) throws Exception
    {
        File fd = new File(args[0]).getAbsoluteFile();
        File parent = fd.getParentFile();
        String fmt = ImageFormat.getFormat(fd);
        
        BufferedImage bii = ImageIO.read(fd);
        for(int i = SwapImageColor.MIN;i<=SwapImageColor.MAX;i++)
        {
            BufferedImage bio = ImageIO.read(fd);
            SwapImageColor swap = new SwapImageColor(bii, bio, i);
            File fd2 = new File(parent,i+"-"+swap.getRGB()+"-"+fd.getName());
            swap.run();
            ImageIO.write(bio, fmt, fd2);
        }
    }
}

