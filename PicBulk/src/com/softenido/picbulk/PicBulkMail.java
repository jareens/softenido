/*
 *  PicBulkMail.java
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

import com.softenido.easymail.EmailBuilder;
import com.softenido.easymail.MimeTypes;
import com.softenido.easymail.SmtpConfig;
import com.softenido.easymail.SmtpMailer;
import java.io.File;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public class PicBulkMail extends AbstractPicBulk
{

    private SmtpMailer smtp = null;
    private SmtpConfig conf = null;
    private EmailBuilder msg = null;
    private String from;
    private String to;
    private String cc;
    private String bcc;
    private int albumCount;
    private int photoCount;
    private int photoIndex;
    private String title;
    private boolean debug=false;

    public PicBulkMail(String title)
    {
        this.title = title;
        this.smtp = new SmtpMailer("PicAndSend");
    }

    public boolean isDebug()
    {
        return debug;
    }

    public void setDebug(boolean debug)
    {
        this.debug = debug;
        smtp.setDebug(debug);
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public void setTo(String to)
    {
        this.to = to;
    }
    void setCC(String cc)
    {
        this.cc = cc;
    }
    void setBcc(String bcc)
    {
        this.bcc = bcc;
    }


    void setUserPassword(String host, String user, String pass,boolean ssl, boolean starttls)
    {
        this.conf = new SmtpConfig(host,user,pass,ssl,starttls);
    }

    public boolean addAlbum(int albumCount,int photoIndex)
    {
        flush();
        msg = new EmailBuilder();
        msg.setFrom(from);
        msg.setTo(to);
        msg.setCc(cc);
        msg.setBcc(bcc);
        msg.setSubject(title+"["+albumCount+"]");
        this.albumCount = albumCount;
        this.photoCount = 0;
        this.photoIndex = photoIndex;
        return true;
    }

    public boolean addPhoto(File file, byte[] image)
    {
        msg.addBytes(image,MimeTypes.getMimeType(file),file.getName());
        photoCount++;
        msg.setSubject(title+"["+albumCount+"]["+(photoIndex+1)+"-"+(photoIndex+photoCount)+"]");
        return true;
    }

    public void flush()
    {
        if (msg != null)
        {
            try
            {
                smtp.send(msg, conf);
                msg = null;
            }
            catch (AddressException ex)
            {
                Logger.getLogger(PicBulkMail.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (MessagingException ex)
            {
                Logger.getLogger(PicBulkMail.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (IOException ex)
            {
                Logger.getLogger(PicBulkMail.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
