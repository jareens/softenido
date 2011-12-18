/*
 *  QosInputStream.java
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
package com.softenido.cafedark.qos;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author franci
 */
public class QosInputStream extends FilterInputStream
{
    final QosFilter qos;

    public QosInputStream(InputStream in, QosFilter qos)
    {
        super(in);
        this.qos = qos;
    }

    @Override
    public int read() throws IOException
    {
        int ret = super.read();
        if (ret > 0)
        {
            qos.count(ret,false);
        }
        return ret;
    }

    @Override
    public int read(byte b[]) throws IOException
    {
        int ret = super.read(b, 0, b.length);
        if (ret > 0)
        {
            qos.count(ret,false);
        }
        return ret;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException
    {
        int ret = super.read(b, off, len);
        if (ret > 0)
        {
            qos.count(ret,false);
        }
        return ret;
    }
}
