/*
 *  Gray.java
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

import com.softenido.cafedark.image.FixedRatiosScaleDimension;
import com.softenido.cafedark.imageio.ImageFormat;
import com.softenido.cafedark.imageio.ScaleImage;
import java.io.File;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author franci
 */
public class Gray
{
    public static void main(String[] args) throws Exception
    {

        File img = new File(args[0]).getAbsoluteFile();
        File parent = img.getParentFile();
        File img2 = new File(parent,"gray-"+img.getName());
        String fmt = ImageFormat.getFormat(img);
        new ScaleImage(new FixedRatiosScaleDimension(640, 60) ,true).filter(img, img2, fmt);
    }
}

