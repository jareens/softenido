/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.mailbulk;

import com.softenido.core.util.SizeUnits;
import java.io.Console;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 *
 * @author franci
 */
public class InteractiveConsole
{

    private static final String[] TRUE =
    {
        "1", Boolean.toString(true)
    };
    private static final String[] FALSE =
    {
        "0", Boolean.toString(false)
    };

    static SizeUnits sizeParser = new SizeUnits();

    protected final InputStream in;
    protected final PrintStream out;
    protected final Console con;
    protected final Scanner sc;

    public InteractiveConsole(InputStream in, PrintStream out, Console con)
    {
        this.in = in;
        this.out = out;
        this.con = con;
        this.sc = new Scanner(in);
    }

    public InteractiveConsole(InputStream in, PrintStream out)
    {
        this(in, out, null);
    }

    public InteractiveConsole()
    {
        this(System.in, System.out, System.console());
    }

    protected String readString(String prompt)
    {
        if (con != null)
        {
            return con.readLine(prompt);
        }
        out.printf(prompt);
        return sc.nextLine();
    }

    protected boolean readBoolean(String prompt)
    {
        while (true)
        {
            String val = readString(prompt);
            if (val == null)
            {
                break;
            }
            for (int i = 0; i < TRUE.length; i++)
            {
                if (val.endsWith(TRUE[i]))
                {
                    return true;
                }
            }
            for (int i = 0; i < FALSE.length; i++)
            {
                if (val.endsWith(FALSE[i]))
                {
                    return false;
                }
            }

        }
        return false;

    }

    protected boolean readBoolean(String prompt, boolean defVal)
    {
        String val = readString(prompt);
        if (val != null)
        {
            for (int i = 0; i < TRUE.length; i++)
            {
                if (val.endsWith(TRUE[i]))
                {
                    return true;
                }
            }
            for (int i = 0; i < FALSE.length; i++)
            {
                if (val.endsWith(FALSE[i]))
                {
                    return false;
                }
            }
        }
        return defVal;
    }
    protected long readSize(String prompt)
    {
        while(true)
        {
            String val = readString(prompt);
            if(val==null)
                break;
            long size = sizeParser.parse(val);
            return size;
        }
        return 0L;
    }
    protected long readSize(String prompt,long defVal)
    {
        String val = readString(prompt);
        if(val!=null)
        {
            return sizeParser.parse(val);
        }
        return defVal;
    }

}
