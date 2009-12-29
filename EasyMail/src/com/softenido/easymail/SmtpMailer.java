/*
 *  SmtpMailer.java
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

import com.sun.mail.smtp.SMTPTransport;
import java.io.IOException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import java.util.Date;

/**
 *
 * @author franci
 */
public class SmtpMailer
{
    private boolean debug = false;
    private boolean verbose = false;
    private final String mailer;

    public SmtpMailer(String mailer)
    {
        this.mailer = mailer;
    }

    public void send(EmailBuilder email, SmtpConfig smtp) throws AddressException, MessagingException, IOException
    {
        send(new EmailBuilder[] {email},smtp);
    }
    public void send(EmailBuilder[] emails, SmtpConfig smtp) throws AddressException, MessagingException, IOException
    {
        Properties props = System.getProperties();
        if (smtp.getHost() != null)
        {
            props.put("mail.smtp.host", smtp.getHost());
        }
        if (smtp.isAuth())
        {
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtps.auth", "true");
        }
        if (smtp.isStarttls())
        {
            props.put("mail.smtp.starttls.enable", "true");
        }
        // Get a Session object
        Session session = Session.getInstance(props, null);
        if (debug)
        {
            session.setDebug(true);
        }
        
        SMTPTransport transport = null;
        try
        {
            for(EmailBuilder email : emails)
            {
                // construct the message
                Message msg = email.getMessage(email, session);
                msg.setHeader("X-Mailer", mailer);
                msg.setSentDate(new Date());

                if(transport==null)
                {
                    transport = (SMTPTransport) session.getTransport(smtp.isSsl() ? "smtps" : "smtp");
                    if(smtp.isAuth())
                    {
                        transport.connect(smtp.getHost(), smtp.getUser(), smtp.getPassword());
                    }
                    else
                    {
                        transport.connect();
                    }
                }
                try
                {
                    transport.sendMessage(msg, msg.getAllRecipients());
                }
                finally
                {
                    if (verbose)
                    {
                        System.out.println("SmtpMailer: Response = " + transport.getLastServerResponse());
                    }
                }
                if (verbose)
                {
                    System.out.println("SmtpMailer: E-Mail successfully sent.");
                }
            }
        }
        finally
        {
            if(transport!=null)
                transport.close();
        }
    }

    public String getMailer()
    {
        return mailer;
    }

    public boolean isDebug()
    {
        return debug;
    }

    public void setDebug(boolean debug)
    {
        this.debug = debug;
    }

    public boolean isVerbose()
    {
        return verbose;
    }

    public void setVerbose(boolean verbose)
    {
        this.verbose = verbose;
    }

}