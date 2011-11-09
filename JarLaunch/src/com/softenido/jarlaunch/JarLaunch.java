/*
 *  JarLaunch.java
 *
 *  Copyright (C) 2010 Francisco Gómez Carrasco
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
package com.softenido.jarlaunch;

import com.softenido.cafe.util.OSName;
import com.softenido.cafe.util.launcher.LauncherBuilder;
import com.softenido.cafe.util.launcher.LauncherOptions;
import com.softenido.cafe.util.launcher.LauncherParser;
import com.softenido.cafe.util.launcher.PosixLauncherBuilder;
import com.softenido.cafe.util.options.BooleanOption;
import com.softenido.cafe.util.options.InvalidOptionException;
import com.softenido.cafe.util.options.OptionParser;
import com.softenido.cafe.util.options.StringOption;
import java.io.IOException;

/**
 *
 * @author franci
 */
public class JarLaunch
{
    private static final String JARLAUNCH = "jarlaunch";
    private static final String JARLAUNCH_JAR = "JarLaunch.jar";

    private static final String VER = "0.0.1";
    private static final String VERSION =
            JARLAUNCH+"  version "+VER+" beta (2010-01-16)\n"
            + "Copyright (C) 2010 by Francisco Gómez Carrasco\n"
            + "<http://www.softenido.com>\n";
    private static final String REPORT_BUGS =
            "Report bugs to <flikxxi@gmail.com>\n"
            + "            or <http://code.google.com/p/softenido/issues>\n";
    private static final String LICENSE =
            VERSION
            + "\n"
            + "This program is free software: you can redistribute it and/or modify\n"
            + "it under the terms of the GNU General Public License as published by\n"
            + "the Free Software Foundation, either version 3 of the License, or\n"
            + "(at your option) any later version.\n"
            + "\n"
            + "This program is distributed in the hope that it will be useful,\n"
            + "but WITHOUT ANY WARRANTY; without even the implied warranty of\n"
            + "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n"
            + "GNU General Public License for more details.\n"
            + "\n"
            + "You should have received a copy of the GNU General Public License\n"
            + "along with this program.  If not, see <http://www.gnu.org/licenses/>.\n"
            + "\n"
            + REPORT_BUGS;
    private static final String HELP =
            VERSION
            + "\n"
            + JARLAUNCH+" comes with ABSOLUTELY NO WARRANTY. This is free software, and you\n"
            + "are welcome to redistribute it under certain conditions. See the GNU\n"
            + "General Public Licence version 3 for details.\n"
            + "\n"
            + JARLAUNCH+" is a builder for java application launchers\n"
            + "\n"
            + "usage: "+JARLAUNCH+" [option]... Application.jar\n"
            + "       java -jar "+JARLAUNCH_JAR+" [option]... Application.jar\n"
            + "\n"
            + "Options:\n"
            + " -v, --verbose               increase verbosity\n"
            + " -L, --license               display software license\n"
            + " -n  --name=NAME             name for the launcher\n"
            + "     --java[=path]           install a launcher using 'java' command\n"
            + "     --home[=path]           install a launcher using 'java.home' property\n"
            + "     --posix                 posix flavor for install options when unknown\n"
            + "     --install               install a launcher\n"
            + "     --install-java[=path]   install a launcher using 'java' command\n"
            + "     --install-home[=path]   install a launcher using 'java.home' property\n"
            + "     --install-posix         posix flavor for install options when unknown\n"
            + "     --install-version       adds version to launcher name\n"
            + "     --version               print version number\n"
            + "     --examples              print some useful examples\n"
            + "(-h) --help                  show this help (-h works with no other options)\n"
            + "\n"
            + REPORT_BUGS;
    private static final String EXAMPLES =
            VERSION
            + "\n"
            + "examples of "+JARLAUNCH+" usage:\n"
            + "\n"
            + " gij -jar "+JARLAUNCH_JAR+" --install-java gij\n"
            + " java -jar "+JARLAUNCH_JAR+" --install\n"
            + " sudo java -jar "+JARLAUNCH_JAR+" --install\n"
            + " sudo /opt/jdk1.6/bin/java -jar "+JARLAUNCH_JAR+" --install-home\n"
            + " sudo /opt/jdk1.6/bin/java -jar "+JARLAUNCH_JAR+" --install-posix\n"
            + " "+JARLAUNCH+" MyProgram.jar\n"
            + " "+JARLAUNCH+" 1111 3333 5555 7777 -c16\n"
            + " "+JARLAUNCH+" 7777 -nc16 \n"
            + " "+JARLAUNCH+" 7777 -d3600 -t60 -T300\n"
            + " "+JARLAUNCH+" 7777 -s64k\n"
            + "\n"
            + " send me yours to: <flikxxi@gmail.com>\n";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException, IOException, InvalidOptionException
    {
        if (args.length < 1)
        {
            System.out.println(HELP);
            return;
        }
        int verboseLevel = 0;

        OptionParser options = new OptionParser();

        BooleanOption optVerbose = options.add(new BooleanOption('v', "verbose"));
        BooleanOption optLicense = options.add(new BooleanOption('L', "license"));
        StringOption optJava  = options.add(new StringOption("java"));
        StringOption optHome  = options.add(new StringOption("home"));
        BooleanOption optPosix= options.add(new BooleanOption("posix"));
        BooleanOption version = options.add(new BooleanOption("version"));
        BooleanOption help = options.add(new BooleanOption('h', "help"));
        BooleanOption examples = options.add(new BooleanOption("examples"));

        String[] jars;
        try
        {
            args = LauncherParser.parseInstall(JARLAUNCH,null,VER,args);
            if (args == null)
            {
                return;
            }
            jars = options.parse(args);
        }
        catch (InvalidOptionException ex)
        {
            System.err.println(ex);
            System.err.println(JARLAUNCH+": Try --help for more information");
            return;
        }

        if (help.isUsed())
        {
            System.out.println(HELP);
            return;
        }
        if (optLicense.isUsed())
        {
            System.out.println(LICENSE);
            return;
        }
        if (version.isUsed())
        {
            System.out.println(VERSION);
            return;
        }
        if (examples.isUsed())
        {
            System.out.println(EXAMPLES);
            return;
        }
        if (optVerbose.isUsed())
        {
            verboseLevel = optVerbose.getCount();
        }
        if (jars.length == 0)
        {
            System.err.println(JARLAUNCH+": missing jar");
            return;
        }
        
        String appName = jars[0];
        LauncherBuilder builder = LauncherBuilder.getBuilder();
        LauncherOptions launcherOptions = new LauncherOptions();
        if (builder == null && optPosix.isUsed())
        {
            builder = new PosixLauncherBuilder("posix");
        }
        if (builder == null)
        {
            System.err.println(appName+": Operating System '" + OSName.os.getName() + "' not supported for install options");
            return;
        }
        if(optJava.isUsed())
        {
            launcherOptions.setJava(true);
            launcherOptions.setJavaPath(optJava.getValue(null));
        }
        if(optHome.isUsed())
        {
            launcherOptions.setHome(true);
            launcherOptions.setHomePath(optHome.getValue(null));
        }
        launcherOptions.setPosix(optPosix.isUsed());       

        if (builder.buildLauncher(launcherOptions,null, appName))
        {
            System.out.println(appName+": '" + builder.getFileName() + "' created");
        }

    }

}
