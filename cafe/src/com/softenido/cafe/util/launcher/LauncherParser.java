/*
 *  LauncherParser.java
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

import com.softenido.cafe.util.options.BooleanOption;
import com.softenido.cafe.util.options.InvalidOptionException;
import com.softenido.cafe.util.options.OptionParser;
import com.softenido.cafe.util.options.StringOption;

/**
 *
 * @author franci
 */
public class LauncherParser
{
    private boolean install = false;
    private boolean auto = false;
    private boolean java = false;
    private boolean home = false;
    private boolean posix= false;
    private String javaPath = null;
    private String homePath = null;

    public String[] parse(String[] args) throws InvalidOptionException
    {
        OptionParser parser = new OptionParser();

        BooleanOption installAuto = parser.add(new BooleanOption("install"));
        StringOption installJava  = parser.add(new StringOption("install-java"));
        StringOption installHome  = parser.add(new StringOption("install-home"));
        BooleanOption installPosix= parser.add(new BooleanOption("install-posix"));

        parser.setIgnoreShort(true);// no short options parsed in this parser
        args = parser.parse(args);

        auto = installAuto.isUsed();
        java = installJava.isUsed();
        home = installHome.isUsed();
        posix= installPosix.isUsed();
        
        if( !auto && !java && !home && posix )
        {
            auto = true;
        }

        if (java)
        {
            javaPath = installJava.getValue();
        }
        if (home)
        {
            homePath = installHome.getValue();
        }
        install = auto | java | home;

        return args;
    }
    public boolean isAuto()
    {
        return auto;
    }

    public void setAuto(boolean auto)
    {
        this.auto = auto;
    }

    public boolean isHome()
    {
        return home;
    }

    public void setHome(boolean home)
    {
        this.home = home;
    }

    public boolean isInstall()
    {
        return install;
    }

    public void setInstall(boolean install)
    {
        this.install = install;
    }

    public boolean isJava()
    {
        return java;
    }

    public void setJava(boolean java)
    {
        this.java = java;
    }

    public String getHomePath()
    {
        return homePath;
    }

    public void setHomePath(String homePath)
    {
        this.homePath = homePath;
    }

    public String getJavaPath()
    {
        return javaPath;
    }

    public void setJavaPath(String javaPath)
    {
        this.javaPath = javaPath;
    }

    public boolean isPosix()
    {
        return posix;
    }

    public void setPosix(boolean posix)
    {
        this.posix = posix;
    }
}
