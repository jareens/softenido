/*
 *  Files.java
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
package org.fjtk.se;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author franci
 */
public class Files
{
    // Copies src file to dst file.
    // If the dst file does not exist, it is created
    public static void copy(File src, File dst) throws IOException
    {
        InputStream in = new FileInputStream(src);
        try
        {
            OutputStream out = new FileOutputStream(dst);
            try
            {
                // Transfer bytes from in to out
                byte[] buf = new byte[8192];
                int r;
                while ((r = in.read(buf)) > 0)
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
            in.close();
        }
    }
    public static boolean move(File src, File dst) throws IOException
    {
        if(src.renameTo(dst))
            return true;
        copy(src,dst);
        src.delete();
        return true;
    }
}
