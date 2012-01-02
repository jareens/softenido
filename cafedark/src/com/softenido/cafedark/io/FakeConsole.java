/*
 *  FakeConsole.java
 *
 *  Copyright (C) 2010-2011  Francisco Gómez Carrasco
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
package com.softenido.cafedark.io;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author franci
 */
public class FakeConsole implements VirtualConsole
{

    private final InputStream in;
    private final OutputStream out;
    private final Scanner sc;
    private final Object lock = new Object();
    private final PrintWriter pw;
    private final Reader reader;

    public FakeConsole(InputStream in, OutputStream out)
    {
        this.in = in;
        this.out = out;
        this.sc  = new Scanner(in);
        pw = new PrintWriter(out, true)
        {
            @Override
            public void close() {}
        };//do not close the output stream

        reader = new InputStreamReader(in);

    }
    public static VirtualConsole getConsole(boolean debug)
    {
        if(System.console()!=null)
            return new RealConsole(System.console());
        if(debug)
            return new FakeConsole(System.in,System.out);
        throw new IOError(new Exception("There is no console"));
    }

    public void flush()
    {
        try
        {
            out.flush();
        }
        catch (IOException ex)
        {
            Logger.getLogger(FakeConsole.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public VirtualConsole format(String fmt, Object... args)
    {
        pw.format(fmt, args).flush();
        return this;
    }

    @Override
    public VirtualConsole printf(String format, Object... args)
    {
        return format(format, args);
    }

    @Override
    public String readLine(String fmt, Object... args)
    {
        synchronized (lock)
        {
            if (fmt.length() != 0)
            {
                pw.format(fmt, args);
            }
            return readLine();
        }
    }

    @Override
    public String readLine()
    {
        if(sc.hasNextLine())
            return sc.nextLine();
        return null;
    }

    @Override
    public char[] readPassword(String fmt, Object... args)
    {
        return readLine(fmt, args).toCharArray();
    }

    @Override
    public char[] readPassword()
    {
        return readLine().toCharArray();
    }

    @Override
    public Reader reader()
    {
        return reader;
    }

    @Override
    public PrintWriter writer()
    {
        return pw;
    }
}
