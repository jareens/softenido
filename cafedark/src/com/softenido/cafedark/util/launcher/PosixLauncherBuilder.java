/*
 *  PosixLauncherBuilder.java
 *
 *  Copyright (C) 2009-2010  Francisco Gómez Carrasco
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
import java.io.IOException;

/**
 *
 * @author franci
 */
public class PosixLauncherBuilder extends LauncherBuilder
{
    private static final String ROOT = "root";
    private static final String USER_HOME = "user.home";
    private static final String USER_NAME = "user.name";
    private static final String USER_BIN = "/usr/bin/";
    private static final String POSIX_STATEMENT =
            "#!/bin/sh\n" +
            "{$java} {$opt} -jar {$jar} \"$@\"\n";

    public PosixLauncherBuilder(String osname)
    {
        super(osname);
    }

    @Override
    public String getLauncherFile(String name)
    {
        String userName = System.getProperty(USER_NAME);
        if(userName.equals(ROOT))
        {
            return new File(USER_BIN,name).toString();
        }
        String userHome = System.getProperty(USER_HOME);
        return new File(userHome, name).toString();
    }

    @Override
    public String getLauncherStatement()
    {
        return POSIX_STATEMENT;
    }

    @Override
    public boolean buildLauncher(LauncherOptions options, String jar, String name, String version) throws IOException
    {
        boolean val = super.buildLauncher(options, jar, name, version);
        if(val)
        {
            val = new File(getFileName()).setExecutable(true,false);
        }
        return val;
    }

}
