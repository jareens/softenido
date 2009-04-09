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
package com.softenido.cafe.util.launcher;

/**
 *
 * @author franci
 */
public class WindowsLauncherBuilder extends LauncherBuilder
{
    private static final String WINDOWS_STATEMENT =
            "@echo off\n" +
            "set ARGS=\n" +
            ":buildargs" +
            "if \"\"%1\"\"==\"\"\"\" goto done\n" +
            "set ARGS=%ARGS% %1\n" +
            "shift\n" +
            "goto buildargs\n" +
            ":done\n" +
            "call {$java} -jar {$jar} {$opt} %ARGS%\n";

    @Override
    public String getLauncherFile(String name)
    {
        return WINDOWS_STATEMENT;
    }

    @Override
    public String getLauncherStatement()
    {
        return WINDOWS_STATEMENT;
    }

}
