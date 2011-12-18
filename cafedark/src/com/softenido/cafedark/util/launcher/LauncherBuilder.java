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
package com.softenido.cafedark.util.launcher;

import com.softenido.cafedark.io.Files;
import com.softenido.cafecore.os.OSName;
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
    private static final String JAVA_CLASS_PATH = "java.class.path";
    private static final String JAVA_HOME = "java.home";

    // variables to be replaced in statements
    protected static final String $JAVA = "{$java}";
    protected static final String $JAR = "{$jar}";
    protected static final String $OPT = "{$opt}";

    private final String osname;
    private String fileName = null;

    public static LauncherBuilder getBuilder()
    {
        OSName os = OSName.getInstance();

        if(os.isLinux() || os.isSolaris() || os.isMacosx())
        {
            return new PosixLauncherBuilder(os.getName());
        }
        if (os.isWindows())
        {
            return new WindowsLauncherBuilder(os.getName());
        }
        return null;
    }

    public LauncherBuilder(String osname)
    {
        this.osname = osname;
    }
    public abstract String getLauncherFile(String name);

    public abstract String getLauncherStatement();
    
    public final boolean buildLauncher(LauncherOptions options, String jar, String name) throws IOException
    {
        return buildLauncher(options, jar, name,null);
    }
    
    public boolean buildLauncher(LauncherOptions options,String jar, String name, String version) throws IOException
    {
        if( options.isVersion() && version!=null && version.length()>0 )
        {
            name += "-"+version;
        }
        fileName = getLauncherFile(name);
        String fileStmt = getLauncherStatement();

        String javaPath = options.getJavaPath();
        String homePath = options.getHomePath();

        if (options.isAuto())
        {
            javaPath = "java";
        }
        else if (options.isHome())
        {
            if (homePath == null)
            {
                homePath = System.getProperty(JAVA_HOME);
            }
            javaPath = new File(homePath, new File("bin", "java").toString()).toString();
        }
        else if (options.isJava() && javaPath == null)
        {
            javaPath = "java";
        }
        javaPath = escape(javaPath);

        if(jar==null)
        {
            jar = new File(System.getProperty(JAVA_CLASS_PATH)).getAbsolutePath().toString();
        }
        jar = escape(jar);

        String[] opt = options.getJvmOptions();
        StringBuilder sbOpt= new StringBuilder();
        for(int i=0;i<opt.length;i++)
        {
            sbOpt.append(opt[i]).append(" ");
        }
        
        return buildLauncher(fileName, fileStmt, javaPath, jar, sbOpt.toString());
    }

    private static boolean buildLauncher(String fileName, String fileStmt, String java, String jar, String opt) throws IOException
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

    public String getFileName()
    {
        return fileName;
    }

    protected String escape(String fileName)
    {
        return Files.escape(fileName);
    }
    public boolean isSupported()
    {
        return true;
    }
    public String getOsname()
    {
        return osname;
    }    
}
