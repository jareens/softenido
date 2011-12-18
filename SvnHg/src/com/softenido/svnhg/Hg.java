/*
 *  Hg.java
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
import java.text.SimpleDateFormat;
import java.util.Scanner;

/**
 *
 * @author franci
 */
public class Hg extends ChildWrapper
{
    static private final String HG = "hg";
    private final String path;
    static final DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

    public Hg(String path, PrintStream verboseCmd, PrintStream verboseIn, PrintStream verboseOut, PrintStream verboseErr)
    {
        super(verboseCmd,  verboseIn, verboseOut, verboseErr);
        this.path = path;
    }

    public Hg(String path)
    {
        this(path, null,null,null,null);
    } 

    public int clone(String url) throws IOException, InterruptedException
    {
        Process proc = execute(HG,"-y","clone",url,path);

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

    public int add() throws IOException, InterruptedException
    {
        Process proc = execute(HG,"-y","add",path);

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
    public int commit(Revision rev) throws IOException, InterruptedException
    {
        String date = formater.format(rev.getTime());
        Process proc = execute(HG,"-y","commit","-R",path,"-m",rev.getComment(),"-u",rev.getUser(),"-d",date);

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

    public int push() throws IOException, InterruptedException
    {
        Process proc = execute(HG,"-y","push","-R",path);

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
