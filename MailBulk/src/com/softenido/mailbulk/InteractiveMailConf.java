/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
