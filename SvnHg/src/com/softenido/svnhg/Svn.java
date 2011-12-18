/*
 *  Svn.java
 *
 *  Copyright (C) 2010-2011 Francisco Gómez Carrasco
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
package com.softenido.svnhg;

import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author franci
 */
public class Svn extends ChildWrapper
{
    static private final String SVN = "svn";
    private final String path;
    static final DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

    static
    {
        formater.setLenient(false);
    }

    public Svn(String path, PrintStream verboseCmd, PrintStream verboseIn, PrintStream verboseOut, PrintStream verboseErr)
    {
        super(verboseCmd, verboseIn, verboseOut, verboseErr);
        this.path = path;
    }

    public Svn(String path)
    {
        this(path, null, null, null, null);
    }

    public int checkout(String url, String user, String pass, int rev) throws IOException, InterruptedException
    {
        if (rev > 0)
        {
            url += "@" + rev;
        }
        Process proc;
        if(user==null)
        {
             proc = execute(SVN, "checkout", url, path);
        }
        else if(pass==null)
        {
            proc = execute(SVN, "checkout", url, path,"--username",user);
        }
        else
        {
            setObfuscated(pass);
            proc = execute(SVN, "checkout", url, path,"--username",user,"--password",pass);
            setObfuscated(null);
        }

        Scanner scErr = new Scanner(err);
        while (scErr.hasNext())
        {
            scErr.nextLine();
        }

        Scanner sc = new Scanner(in);
        while (sc.hasNext())
        {
            sc.nextLine();
        }
        proc.waitFor();
        return proc.exitValue();
    }

    public int checkout(String url, String user, String pass ) throws IOException, InterruptedException
    {
        return checkout(url,user, pass,1);
    }
    public int checkout(String url) throws IOException, InterruptedException
    {
        return checkout(url,null,null,1);
    }

    public Revision log(int rev) throws IOException, InterruptedException
    {

        Process proc = execute(SVN, "log", path, "-r", Integer.toString(rev));

        try
        {

            Scanner scErr = new Scanner(err);
            while (scErr.hasNext())
            {
                scErr.nextLine();
            }

            Scanner sc = new Scanner(in);
            //looking for the initial line separator
            while (sc.hasNext())
            {
                String line = sc.nextLine();
                if (line.startsWith("----------"))
                {
                    break;
                }
            }
            if (!sc.hasNextLine())
            {
                return null;
            }
            String coLine = sc.nextLine();
            String[] coItems = coLine.split("\\|");
            String rNum = coItems[0].trim();
            String who = coItems[1].trim();
            String when = coItems[2].trim();
            String comment = null;
            //looking for an empty line
            while (sc.hasNext())
            {
                comment = sc.nextLine();
                if (comment.length() > 0)
                {
                    break;
                }
            }

            //saving the comment
            while (sc.hasNext())
            {
                String line = sc.nextLine();
                if (line.startsWith("----------"))
                {
                    break;
                }
                comment += "\n" + line;
            }

            int revision = Integer.parseInt(rNum.substring(1));
            Date date;
            try
            {
                int len = when.indexOf("(");
                if (len > 0)
                {
                    when = when.substring(0, len);
                }
                when = when.trim();
                date = formater.parse(when);
            }
            catch (ParseException ex)
            {
                Logger.getLogger(Svn.class.getName()).log(Level.SEVERE, null, ex);
                date = new Date();
            }
            return new Revision(revision, who, date, comment);
        }
        finally
        {
            proc.waitFor();
        }
        
    }

    public int update(int rev) throws IOException, InterruptedException
    {
        Process proc = execute(SVN, "update", path, "-r", Integer.toString(rev));

        Scanner scErr = new Scanner(err);
        while (scErr.hasNext())
        {
            scErr.nextLine();
        }

        Scanner sc = new Scanner(in);
        while (sc.hasNext())
        {
            sc.nextLine();
        }
        proc.waitFor();
        return proc.exitValue();
    }
}
//------------------------------------------------------------------------
//r1 | (sin autor) | 2007-12-06 23:40:25 +0100 (jue 06 de dic de 2007) | 1 line
//
//Initial directory structure.
//------------------------------------------------------------------------

