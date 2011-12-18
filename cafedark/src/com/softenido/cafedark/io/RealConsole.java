/*
 *  RealConsole.java
 *
 *  Copyright (C) 2010  Francisco Gómez Carrasco
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

import java.io.Console;
import java.io.PrintWriter;
import java.io.Reader;

/**
 *
 * @author franci
 */
public class RealConsole implements VirtualConsole
{
    private final Console console;

    public RealConsole(Console console)
    {
        this.console = console;
    }  

    public void flush()
    {
        console.flush();
    }

    public VirtualConsole format(String fmt, Object... args)
    {
        console.format(fmt, args);
        return this;
    }

    public VirtualConsole printf(String format, Object... args)
    {
        console.printf(format, args);
        return this;
    }

    public String readLine(String fmt, Object... args)
    {
        return console.readLine(fmt, args);
    }

    @Override
    public String readLine()
    {
        return console.readLine();
    }

    @Override
    public char[] readPassword(String fmt, Object... args)
    {
        return console.readPassword(fmt, args);
    }

    @Override
    public char[] readPassword()
    {
        return console.readPassword();
    }

    @Override
    public Reader reader()
    {
        return console.reader();
    }

    @Override
    public PrintWriter writer()
    {
        return console.writer();
    }

}
