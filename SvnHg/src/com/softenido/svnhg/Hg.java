/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.softenido.svnhg;

import com.softenido.svnhg.ChildWrapper;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
