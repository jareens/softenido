/*
 *  EmailBuilder.java
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
package com.softenido.easymail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


interface Attachable
{
    MimeBodyPart attach() throws IOException,MessagingException;
}

class FileAttach implements Attachable
{
    private final File file;

    public FileAttach(File file)
    {
        this.file = file;
    }

    public MimeBodyPart attach() throws IOException, MessagingException
    {
        MimeBodyPart mbp = new MimeBodyPart();
        mbp.attachFile(file);
        return mbp;
    }
    
}
class BytesAttach implements Attachable
{
    private final byte[] data;
    private final String type;
    private final String filename;

    public BytesAttach(byte[] data, String type, String filename)
    {
        this.data = data;
        this.type = type;
        this.filename = filename;
    }
    public BytesAttach(byte[] data, String type)
    {
        this(data,type,null);
    }
    public MimeBodyPart attach() throws IOException, MessagingException
    {
        MimeBodyPart mbp = new MimeBodyPart();
        mbp.setContent(data,type);
        if(filename!=null)
            mbp.setFileName(filename);
        return mbp;
    }
}

/**
 *
 * @author franci
 */
public class EmailBuilder
{


    private String from = null;
    private String to = null;
    private String cc = null;
    private String bcc = null;
    private String subject = null;
    private String body = null;
    private String url = null;
    private ArrayList<Attachable> attach = new ArrayList();

    public void addFile(File file)
    {
        attach.add(new FileAttach(file));
    }
    public void addFile(String fileName)
    {
        addFile(new File(fileName));
    }
    public void addBytes(byte[] data,String type)
    {
        attach.add(new BytesAttach(data,type));
    }
    public void addBytes(byte[] data,String type, String filename)
    {
        attach.add(new BytesAttach(data,type,filename));
    }

    String getTo()
    {
        return to;
    }

    public void setTo(String to)
    {
        this.to = to;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public String getFrom()
    {
        return from;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public String getCc()
    {
        return cc;
    }

    public void setCc(String cc)
    {
        this.cc = cc;
    }

    public String getBcc()
    {
        return bcc;
    }

    public void setBcc(String bcc)
    {
        this.bcc = bcc;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }
    public Message getMessage(EmailBuilder email,Session session) throws AddressException, MessagingException, IOException
    {
        Message msg = new MimeMessage(session);
        if (from != null)
        {
            msg.setFrom(new InternetAddress(from));
        }
        else
        {
            msg.setFrom();
        }
        if (to != null)
        {
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
        }
        if (cc != null)
        {
            msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc, false));
        }
        if (bcc != null)
        {
            msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc, false));
        }
        msg.setSubject(subject);

        if (attach.size()>0)
        {
            MimeMultipart mp = new MimeMultipart();

            if(body!=null)
            {
                MimeBodyPart mbp = new MimeBodyPart();
                mbp.setText(body);
                mp.addBodyPart(mbp);
            }
            for (Attachable item : attach)
            {
                mp.addBodyPart(item.attach());
            }
            msg.setContent(mp);
        }
        else
        {
            msg.setText(body);
        }
        return msg;
    }

}