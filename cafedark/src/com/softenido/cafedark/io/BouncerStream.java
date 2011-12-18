/*
 *  BouncerStream.java
 *
 *  Copyright (C) 2008-2010  Francisco Gómez Carrasco
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
package com.softenido.cafedark.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author franci
 */
public class BouncerStream implements Runnable
{

    final private InputStream in;
    final private OutputStream out;
    final private int block;

    public BouncerStream(InputStream in, OutputStream out, int block)
    {
        this.in = in;
        this.out = out;
        this.block = block;
    }

    public BouncerStream(InputStream in, OutputStream out)
    {
        this(in, out, 64*1024);
    }

    public void run()
    {
        try
        {
            int num;
            byte[] buf = new byte[block];
            while ((num = in.read(buf)) > 0)
            {
                out.write(buf, 0, num);
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(BouncerStream.class.getName()).log(Level.ALL, null, ex);
        }
        finally
        {
            try
            {
                in.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(BouncerStream.class.getName()).log(Level.SEVERE, null, ex);
            }
            try
            {
                out.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(BouncerStream.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
