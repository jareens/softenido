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
package com.softenido.cafedark.bouncer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author franci
 */
public class BouncerStream implements Runnable
{
    final private InputStream in;
    final private OutputStream out;
    final private int block;
    final private String name;
    long  full  = 0;
    long  count = 0;
    public BouncerStream(InputStream in, OutputStream out, int block,String name)
    {
        this.in = in;
        this.out = out;
        this.block = block;
        this.name = name;
    }

    public BouncerStream(InputStream in, OutputStream out,String name)
    {
        this(in, out, 64*1024,name);
    }

    public void run()
    {
        try
        {
            int num;
            byte[] buf = new byte[block];
            while ((num = in.read(buf)) > 0)
            {
                count+= num;
                full += num;
                System.out.printf("%s: num=%d count=%d full=%d\n",name,num,count,full);
                out.write(buf, 0, num);
            }
        }
        catch (IOException ex)
        {
            //no special actions needed
        }
    }
}
