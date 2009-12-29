/*
 *  PicBulkService.java
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

import com.google.gdata.util.ServiceException;
import com.softenido.cafe.imageio.ScaleDimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 *
 * @author franci
 */
public interface PicBulkService
{

    boolean addAlbum(int albumCount,int photoIndex) throws MalformedURLException, IOException, ServiceException;
    boolean addPhoto(File file, ScaleDimension scale) throws FileNotFoundException, IOException, MalformedURLException, ServiceException, Exception;
    boolean addPhoto(File file, byte[] image) throws MalformedURLException, IOException, ServiceException;
    public void flush();
    //List<AlbumEntry> getAlbums() throws MalformedURLException, IOException, ServiceException;
    //String getLinkByRel(List<Link> links, String relValue);
}
