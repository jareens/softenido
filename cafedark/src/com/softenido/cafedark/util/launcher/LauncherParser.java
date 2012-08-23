/*
 *  LauncherParser.java
 *
 *  Copyright (C) 2009-2012 Francisco Gómez Carrasco
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

import com.softenido.cafecore.os.OSName;
import com.softenido.cafedark.util.options.ArrayStringOption;
import com.softenido.cafedark.util.options.BooleanOption;
import com.softenido.cafedark.util.options.InvalidOptionException;
import com.softenido.cafedark.util.options.OptionParser;
import com.softenido.cafedark.util.options.StringOption;
import java.io.IOException;

/**
 *
 * @author franci
 */
public class LauncherParser
{
    public static String[] parseInstall(String appName,String jar, String appVer,String[] args) throws IOException, InvalidOptionException
    {
            LauncherParser parser = new LauncherParser();
            LauncherOptions options = new LauncherOptions();
            args = parser.parse(args,options);
            if (options.isInstall())
            {
                LauncherBuilder builder = LauncherBuilder.getBuilder();
                if (builder == null && options.isPosix())
                {
                    builder = new PosixLauncherBuilder("posix");
                }
                if (builder == null)
                {
                    System.err.println(appName+": Operating System '" + OSName.os.getName() + "' not supported for install options");
                }
                else if (builder.buildLauncher(options, jar, appName,appVer))
                {
                    System.out.println(appName+": '" + builder.getFileName() + "' created");
                }
                return null;
            }
            return args;
    }

    public String[] parse(String[] args,LauncherOptions options) throws InvalidOptionException
    {
        OptionParser parser = new OptionParser();

        BooleanOption installAuto = parser.add(new BooleanOption("install"));
        StringOption installJava  = parser.add(new StringOption("install-java"));
        StringOption installHome  = parser.add(new StringOption("install-home"));
        BooleanOption installPosix= parser.add(new BooleanOption("install-posix"));
        BooleanOption installVersion= parser.add(new BooleanOption("install-version"));
        ArrayStringOption jvmOption= parser.add(new ArrayStringOption('J',"jvm-option"));

        parser.setOneHyphen(false);
        parser.setIgnoreShort(false);
        parser.setIgnoreUnknownShort(true);
        args = parser.parse(args);

        options.setAuto(installAuto.isUsed());
        options.setJava(installJava.isUsed());
        options.setHome(installHome.isUsed());
        options.setPosix(installPosix.isUsed());
        options.setVersion(installVersion.isUsed());
        options.setJvmOptions(jvmOption.getValues());
        
        if( !options.isAuto() && !options.isJava() && !options.isHome() && (options.isPosix() ||options.isVersion()) )
        {
            options.setAuto(true);
        }
        if (options.isJava())
        {
            options.setJavaPath(installJava.getValue(null));
        }
        if (options.isHome())
        {
            options.setHomePath(installHome.getValue(null));
        }
        options.setInstall(options.isAuto() | options.isJava() | options.isHome() | options.isVersion());

        return args;
    }    
}
