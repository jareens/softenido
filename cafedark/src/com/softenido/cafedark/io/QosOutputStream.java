/*
 *  QosOutputStream.java
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

import com.softenido.cafedark.qos.QosFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FilterOutputStream;

/**
 *
 * @author franci
 */
public class QosOutputStream extends FilterOutputStream
{
    final QosFilter qos;

    public QosOutputStream(OutputStream out, QosFilter qos)
    {
        super(out);
        this.qos = qos;
    }

    @Override
    public void write(int b) throws IOException
    {
        super.write(b);
        qos.count(2,false);
    }

    @Override
    public void write(byte[] b) throws IOException
    {
        super.write(b);
        qos.count(b.length,false);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException
    {
        super.write(b, off, len);
        qos.count(len-off,false);
    }
}
