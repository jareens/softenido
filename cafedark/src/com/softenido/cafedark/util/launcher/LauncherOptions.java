/*
 *  LauncherOptions.java
 *
 *  Copyright (C) 2009-2010 Francisco Gómez Carrasco
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

/**
 *
 * @author franci
 */
public class LauncherOptions
{
    private boolean install = false;
    private boolean auto = false;
    private boolean java = true;
    private boolean home = false;
    private boolean posix = false;
    private boolean version= false;
    private String javaPath = null;
    private String homePath = null;
    private String[] jvmOptions = new String[0];

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

    public boolean isVersion()
    {
        return version;
    }

    public void setVersion(boolean version)
    {
        this.version = version;
    }

    public String[] getJvmOptions()
    {
        return jvmOptions;
    }

    public void setJvmOptions(String[] jvmOptions)
    {
        this.jvmOptions = jvmOptions;
    }
}
