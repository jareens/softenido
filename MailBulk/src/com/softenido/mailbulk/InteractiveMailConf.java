/*
 *  InteractiveMailConf.java
 *
 *  Copyright (C) 2009-2011  Francisco Gómez Carrasco
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
package com.softenido.mailbulk;

import java.util.Scanner;

/**
 *
 * @author franci
 */
public class InteractiveMailConf extends InteractiveConsole
{

    private String user;
    private String pass;
    private String to;
    private String cc;
    private String bcc;
    private String smtp;
    private String from;
    private boolean ssl;
    private boolean starttls;
    private long mailsize;

    void run()
    {
        user     = readString("user:");
        pass     = readString("pass:");
        to       = readString("to:");
        cc       = readString("cc:");
        bcc      = readString("bcc:");
        smtp     = readString("smtp:");
        from     = readString("from:");
        ssl      = readBoolean("ssl:");
        starttls = readBoolean("starttls:");
        mailsize = readSize("mailsize:");
    }

    public String getBcc()
    {
        return bcc;
    }

    public String getCc()
    {
        return cc;
    }

    public String getFrom()
    {
        return from;
    }

    public long getMailsize()
    {
        return mailsize;
    }

    public String getPass()
    {
        return pass;
    }

    public String getSmtp()
    {
        return smtp;
    }

    public boolean isSsl()
    {
        return ssl;
    }

    public boolean isStarttls()
    {
        return starttls;
    }

    public String getTo()
    {
        return to;
    }

    public String getUser()
    {
        return user;
    }

    
}
