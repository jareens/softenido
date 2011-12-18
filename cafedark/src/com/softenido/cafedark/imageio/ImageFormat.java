/*
 *  ImageFormat.java
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
package com.softenido.cafedark.imageio;

import java.io.File;
import java.io.FileFilter;

/**
 *
 * @author franci
 */
public class ImageFormat
{

    public static final String BMP = "bmp";
    public static final String PNG = "png";
    public static final String JPEG = "jpeg";
    public static final String GIF = "gif";
    private static final String[][] EXT_FMT =
    {
        {".bmp", BMP},
        {".png", PNG},
        {".jpg", JPEG},
        {".jpeg", JPEG},
        {".gif", GIF},
    };

    public static String getFormat(String fileName)
    {
        String name = fileName.toLowerCase();
        for (String[] item : EXT_FMT)
        {
            if (name.endsWith(item[0]))
            {
                return item[1];
            }
        }
        return null;
    }

    public static String getFormat(File file)
    {
        return getFormat(file.getName());
    }
    public static FileFilter getImageFileFilter()
    {
        return new FileFilter()
        {
            public boolean accept(File pathname)
            {
                return (getFormat(pathname)!=null);
            }
        };
    }
}
