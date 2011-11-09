/*
 *  AbstractPicBulk.java
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
package com.softenido.picbulk;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;


import com.softenido.cafe.imageio.ScaleDimension;
import com.softenido.cafe.imageio.ScaleImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import com.google.gdata.util.ServiceException;

public abstract class AbstractPicBulk implements PicBulkService
{
    static boolean debug = true;

    public boolean addPhoto(File file,ScaleDimension scale, boolean gray) throws FileNotFoundException, IOException, MalformedURLException, ServiceException, Exception
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream in = new FileInputStream(file);
        if(scale!=null)
        {
            String format = ScaleImage.getFormat(file);
            in = new ScaleImage(scale,gray).filter(in,format);
        }
        byte buf[] = new byte[64*1024];
        int r;
        while( (r=in.read(buf))>0)
        {
            baos.write(buf, 0, r);
        }
        buf = baos.toByteArray();
        return addPhoto(file, buf);
    }
}
