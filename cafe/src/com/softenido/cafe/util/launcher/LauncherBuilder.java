/*
 *  LauncherBuilder.java
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

import com.softenido.cafe.io.Files;
import com.softenido.cafe.util.options.BooleanOption;
import com.softenido.cafe.util.options.InvalidOptionException;
import com.softenido.cafe.util.options.OptionParser;
import com.softenido.cafe.util.options.StringOption;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author franci
 */
public abstract class LauncherBuilder
{
    // property keys
    private static final String OS_NAME = "os.name";
    private static final String JAVA_CLASS_PATH = "java.class.path";
    private static final String JAVA_HOME = "java.home";

    // variables to be replaced in statements
    protected static final String $JAVA = "{$java}";
    protected static final String $JAR = "{$jar}";
    protected static final String $OPT = "{$opt}";
    
    private boolean install = false;
    private boolean auto = false;
    private boolean java = false;
    private boolean home = false;
    private String javaPath = null;
    private String homePath = null;
    private String fileName = null;

    public static LauncherBuilder getBuilder()
    {
        String osname = System.getProperties().getProperty(OS_NAME);
        if (osname == null)
        {
            return null;
        }
        osname = osname.toLowerCase();

        if (osname.equals("linux"))
        {
            return new PosixLauncherBuilder();
        }
        if (osname.startsWith("windows"))
        {
            return new WindowsLauncherBuilder();
        }
        if (osname.equals("solaris") || osname.equals("sunos"))
        {
            return new PosixLauncherBuilder();
        }
        return null;
    }

    public abstract String getLauncherFile(String name);

    public abstract String getLauncherStatement();

    public boolean buildLauncher(String name) throws IOException
    {
        fileName = getLauncherFile(name);
        String fileStmt = getLauncherStatement();

        if (auto)
        {
            javaPath = "java";
        }
        else if (home)
        {
            if (homePath == null)
            {
                homePath = System.getProperty(JAVA_HOME);
            }
            javaPath = new File(homePath, new File("bin", "java").toString()).toString();
        }
        else if (java && javaPath == null)
        {
            javaPath = "java";
        }
        javaPath = escape(javaPath);

        String jar = new File(System.getProperty(JAVA_CLASS_PATH)).getAbsolutePath().toString();
        jar = escape(jar);
        
        return buildLauncher(fileName, fileStmt, javaPath, jar, "");
    }

    public static boolean buildLauncher(String fileName, String fileStmt, String java, String jar, String opt) throws IOException
    {
        fileStmt = fileStmt.replace($JAVA, java).replace($JAR, jar).replace($OPT, opt);
        PrintWriter out = new PrintWriter(new FileWriter(fileName));
        try
        {
            out.print(fileStmt);
        }
        finally
        {
            out.close();
        }

        return true;
    }

    public String[] parse(String[] args) throws InvalidOptionException
    {
        OptionParser parser = new OptionParser();
        
        BooleanOption installAuto = parser.add(new BooleanOption("install"));
        StringOption installJava = parser.add(new StringOption("install-java"));
        StringOption installHome = parser.add(new StringOption("install-home"));

        parser.setIgnoreShort(true);// no short options parsed in this parser
        args = parser.parse(args);

        auto = installAuto.isUsed();
        java = installJava.isUsed();
        home = installHome.isUsed();

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

    public String getFileName()
    {
        return fileName;
    }
    protected String escape(String fileName)
    {
        return Files.escape(fileName);
    }
}
