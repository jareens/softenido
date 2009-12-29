/*
 *  PicBulkWeb.java
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

import com.google.gdata.data.photos.UserFeed;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.Link;
import com.google.gdata.data.OtherContent;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.GphotoEntry;
import com.google.gdata.data.photos.PhotoEntry;

import java.util.Date;
import java.util.List;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.ServiceException;
import com.softenido.easymail.MimeTypes;
import java.io.File;

public class PicBulkWeb extends AbstractPicBulk
{
    private static final String API_PREFIX = "http://picasaweb.google.com/data/feed/api/user/";
    private PicasawebService service = null;
    private String username = "default";
    private String feedUrl = API_PREFIX + username;
    private int photoCount=0;

    private AlbumEntry album = null;
    private PhotoEntry entry;
    private String title;
    private boolean publicAccess;
    private String location;

    public PicBulkWeb(String serviceName,String title, boolean publicAccess, String location)
    {
        this.title = title;
        this.publicAccess = publicAccess;
        this.location = location;
        service = new PicasawebService(serviceName);
    }

    public PicBulkWeb(String title, boolean publicAccess, String location)
    {
        this("PicaSend",title,publicAccess,location);
    }

    public void setUserPassword(String uname, String passwd) throws AuthenticationException
    {
        service.setUserCredentials(uname, passwd);
    }

    public List<AlbumEntry> getAlbums() throws MalformedURLException, IOException, ServiceException
    {
        UserFeed userFeed = service.getFeed(new URL(feedUrl), UserFeed.class);
        List<GphotoEntry> entries = userFeed.getEntries();
        List<AlbumEntry> albums = new ArrayList<AlbumEntry>();
        for (GphotoEntry item : entries)
        {
            GphotoEntry adapted = item.getAdaptedEntry();
            if (adapted instanceof AlbumEntry)
            {
                albums.add((AlbumEntry) adapted);
            }
        }
        return albums;
    }

    public boolean addAlbum(int albumCount,int photoIndex) throws MalformedURLException, IOException, ServiceException
    {
        // crear un nuevo album
        AlbumEntry emptyAlbum = new AlbumEntry();
        emptyAlbum.setTitle(new PlainTextConstruct(title+"["+albumCount+"]"));
        emptyAlbum.setDescription(new PlainTextConstruct(title));
        emptyAlbum.setAccess(publicAccess ? "public" : "private");
        emptyAlbum.setLocation(location);
        emptyAlbum.setDate(new Date());
        this.album = service.insert(new URL(feedUrl), emptyAlbum);
        return this.album!=null;
    }

    public String getLinkByRel(List<Link> links, String relValue)
    {
        for (Link link : links)
        {
            if (relValue.equals(link.getRel()))
            {
                return link.getHref();
            }
        }
        throw new IllegalArgumentException("Missing " + relValue + " link.");
    }

    public boolean addPhoto(File file, byte[] image) throws MalformedURLException, IOException, ServiceException
    {
        photoCount++;
        PhotoEntry photo = new PhotoEntry();
        photo.setTitle(new PlainTextConstruct(title+"["+photoCount+"]"));

        photo.setDescription(new PlainTextConstruct(title+"["+photoCount+"]"));
        photo.setTimestamp(new Date());

        OtherContent content = new OtherContent();

        content.setBytes(image);
        content.setMimeType(new ContentType(MimeTypes.getMimeType(file)));
        photo.setContent(content);

        String _feedUrl = getLinkByRel(album.getLinks(), Link.Rel.FEED);
        entry = service.insert(new URL(_feedUrl), photo);
        return entry!=null;
    }

    public void flush()
    {
        // nothing to do
    }

}
