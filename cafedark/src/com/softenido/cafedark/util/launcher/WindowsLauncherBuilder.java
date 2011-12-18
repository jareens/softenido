/*
 *  WindowsLauncherBuilder.java
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
package com.softenido.cafedark.util.launcher;

import java.io.File;

/**
 *
 * @author franci
 */
public class WindowsLauncherBuilder extends LauncherBuilder
{
    private static final String WINDOWS_STATEMENT =
            "@echo off\n" +
            "set ARGS=\n" +
            ":buildargs\n" +
            "if \"\"%1\"\"==\"\"\"\" goto done\n" +
            "set ARGS=%ARGS% %1\n" +
            "shift\n" +
            "goto buildargs\n" +
            ":done\n" +
            "call {$java} {$opt} -jar {$jar} %ARGS%\n";

    public WindowsLauncherBuilder(String osname)
    {
        super(osname);
    }

    @Override
    public String getLauncherFile(String name)
    {
        String windir = System.getenv("windir");
        return new File(windir,name+".bat").toString();
    }

    @Override
    public String getLauncherStatement()
    {
        return WINDOWS_STATEMENT;
    }

    @Override
    protected String escape(String fileName)
    {
        return "\""+fileName+"\"";
    }
}
