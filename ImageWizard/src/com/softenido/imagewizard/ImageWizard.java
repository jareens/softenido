/*
 *  ImageWizard.java
 *
 *  Copyright (C) 2012 Francisco Gómez Carrasco
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
package com.softenido.imagewizard;

import com.softenido.cafecore.logging.VerboseHandler;
import com.softenido.cafecore.util.SizeUnits;
import com.softenido.cafedark.util.launcher.LauncherParser;
import com.softenido.cafedark.util.options.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author franci
 */
public class ImageWizard
{
    private static final String IMAGEWIZARD = "imagewizard";
    private static final String VER = "0.0.0";
    private static final String VERSION =
            "imagewizard  version " + VER + " alpha (2012-01-16)\n"
            + "Copyright (C) 2012 by Francisco Gómez Carrasco\n"
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
            + "ImageWizard comes with ABSOLUTELY NO WARRANTY. This is free software, and you\n"
            + "are welcome to redistribute it under certain conditions. See the GNU\n"
            + "General Public Licence version 3 for details.\n"
            + "\n"
            + "imagewizard searches the given path for repeated files by content (not name). Such\n"
            + "files are found by comparing file sizes and MD5+SHA1 signatures.\n"
            + "\n"
            + "usage: imagewizard [command] [options] [directories]\n"
            + "       java -jar ImageWizard.jar [options] [directories]\n"
            + "\n"
            + "Commands:\n"
            + " scale  chage image size\n"
            + " swap   change colors\n"
            + " rename rename image file name\n"//android.png -> android-128x100.png o android-128x100-16k.png
            + "\n"
            + "Options:\n"
            + " -v, --verbose               increase verbosity\n"
            + " -V  --verbose-logger        format messages as a logger\n"
            + " -L, --license               display software license\n"
            + "     --install               install a launcher\n"
            + "     --install-java[=path]   install a launcher using 'java' command\n"
            + "     --install-home[=path]   install a launcher using 'java.home' property\n"
            + "     --install-posix         posix flavor for install options when unknown\n"
            + "     --install-version       adds version to launcher name\n"
            + " -J  --jvm-option=jvm_option pass jvm_option to JVM(for --install...)\n"
            + "     --version               print version number\n"
            + "     --examples              print some useful examples\n"
            + "(-h) --help                  show this help (-h works with no other options)\n"
            + "\n"
            + "size units:\n"
            + " 1=1b, 1k=1024b, 1m=1024k, 1g=1024m, 1t=1024g\n"
            + " separator character is " + File.pathSeparator + "\n"
            + "\n"
            + REPORT_BUGS;
    private static final String EXAMPLES =
            VERSION
            + "\n"
            + "examples of imagewizard usage:\n"
            + "\n"
            + " java -jar ImageWizard.jar --install\n"
            + " sudo java -jar ImageWizard.jar --install -J-mx1g\n"
            + " sudo /opt/jdk1.6/bin/java -jar ImageWizard.jar --install-home\n"
            + " sudo /opt/jdk1.6/bin/java -jar ImageWizard.jar --install-posix\n"
            + " imagewizard backup\n"
            + " imagewizard -d backup\n"
            + " imagewizard -d --min-size=1m c:\\backup e:\\img\n"
            + " imagewizard -nd c:\\backup e:\\img\n"
            + " imagewizard -vn /opt/ /backup/tools \n"
            + " imagewizard -vvn /opt/ --exclude=/opt/nb6.7" + File.pathSeparator + "/opt/nb6.8\n"
            + " imagewizard --byimage -znd photos\n"
            + "\n"
            + " send me yours to: <flikxxi@gmail.com>\n";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException, IOException
    {
        int verboseLevel = 0;

        SizeUnits sizeParser = new SizeUnits();
        OptionParser options = new OptionParser("imagewizard");
        OptionHelper helper = new OptionHelper(options,ImageWizard.class);
        
        CommandOption scale = options.add(new CommandOption("scale"));
        CommandOption swap = options.add(new CommandOption("swap"));
//        CommandOption alpha = options.add(new CommandOption("alpha"));
//        CommandOption mosaic = options.add(new CommandOption("mosaic"));

        BooleanOption verbose = options.add(new BooleanOption('v', "verbose"));
        BooleanOption verboseLogger = options.add(new BooleanOption('V',"verbose-logger"));
        BooleanOption version = options.add(new BooleanOption("version"));
        BooleanOption help = options.add(new BooleanOption('h', "help"));
        BooleanOption license = options.add(new BooleanOption('L', "license"));
        BooleanOption examples = options.add(new BooleanOption("examples"));
        if (args.length < 1)
        {
            
            helper.help(System.out,true,true,true);
            return;
        }
        
        try
        {
            args = LauncherParser.parseInstall(IMAGEWIZARD, null, VER, args);
            if (args == null)
            {
                return;
            }
            args = options.parse(args);
        }
        catch (InvalidOptionException ex)
        {
            System.err.println(ex);
            System.err.println(IMAGEWIZARD + ": Try --help for more information");
            return;
        }

        if (help.isUsed())
        {
            System.out.println(HELP);
            return;
        }
        if (license.isUsed())
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
        if (verbose.isUsed())
        {
            verboseLevel = verbose.getCount();
        }

        VerboseHandler vh = verboseLogger.isUsed() ? new VerboseHandler(System.err, new SimpleFormatter()) : new VerboseHandler(System.err, "imagewizard: ");
        VerboseHandler.register(verboseLevel, vh, ConsoleHandler.class);

        Logger logger = Logger.getLogger(ImageWizard.class.getName());
        if (logger.isLoggable(Level.CONFIG))
        {
            logger.log(Level.CONFIG, "{0}.version={1}", new String[]
                    {
                        IMAGEWIZARD, VER
                    });
            logger.log(Level.CONFIG, "logger.level={0}", vh.getLevel().getName());
            options.log();
            vh.flush();
        }
        try
        {
            if (swap.isUsed())
            {
                SwapColor.main(args);
                return;
            }
            if (scale.isUsed())
            {
                //ScaleImage
                System.out.println("Not implemented yet.");
                return;
            }

        }
        catch (MissingOptionParameterException ex)
        {
            System.err.println(IMAGEWIZARD + ":" + ex.getMessage());
        }
        catch (InvalidOptionParameterException ex)
        {
            System.err.println(IMAGEWIZARD + ":" + ex.getMessage());
        }
        catch (NumberFormatException ex)
        {
            System.err.println(IMAGEWIZARD + ":" + ex.getMessage());
        }
        catch (Exception ex)
        {
            System.err.println(IMAGEWIZARD + ":" + ex.getMessage());
        }        
    }
}
