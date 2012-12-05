/*
 *  LangDetectMain.java
 *
 *  Copyright (C) 2012  Francisco Gómez Carrasco
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
package com.softenido.langdetect;

import com.softenido.cafecore.statistics.classifier.Score;
import com.softenido.cafecore.statistics.classifier.BaseTextClassifier;
import com.softenido.cafecore.statistics.classifier.ClassifierFormatException;
import com.softenido.cafecore.statistics.classifier.Classifiers;
import com.softenido.cafecore.logging.VerboseHandler;
import com.softenido.cafedark.util.launcher.LauncherParser;
import com.softenido.cafedark.util.options.BooleanOption;
import com.softenido.cafedark.util.options.CommandOption;
import com.softenido.cafedark.util.options.InvalidOptionException;
import com.softenido.cafedark.util.options.OptionHelper;
import com.softenido.cafedark.util.options.OptionParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author franci
 */
public class LangDetectMain
{
    private static final String LANG_DETECT = "langdetect";
    private static final String VER = "0.0.0";
    private static final String VERSION =
            "langdetect  version " + VER + " alpha (2012-08-31)\n"
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
            + "langdetect searches the given path for repeated files by content (not name). Such\n"
            + "files are found by comparing file sizes and MD5+SHA1 signatures.\n"
            + "\n"
            + "usage: langdetect [command] [options] [directories]\n"
            + "       java -jar ImageWizard.jar [options] [directories]\n"
            + "\n"
            + "Commands:\n"
            + " coach LANG [FILE|-]\n"
            + " detect [FILE|-]\n"
            + " load FILE\n"
            + " save FILE\n"
            + " loadz FILE\n"
            + " savez FILE\n"
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
            + "examples of langdetect usage:\n"
            + "\n"
            + " java -jar LangDetect.jar --install\n"
            + " sudo java -jar LangDetect.jar --install -J-mx1g\n"
            + " sudo /opt/jdk1.6/bin/java -jar LangDetect.jar --install-home\n"
            + " sudo /opt/jdk1.6/bin/java -jar LangDetect.jar --install-posix\n"
            + " langdetect backup\n"
            + " langdetect -d backup\n"
            + " langdetect -d --min-size=1m c:\\backup e:\\img\n"
            + " langdetect -nd c:\\backup e:\\img\n"
            + " langdetect -vn /opt/ /backup/tools \n"
            + " langdetect -vvn /opt/ --exclude=/opt/nb6.7" + File.pathSeparator + "/opt/nb6.8\n"
            + " langdetect --byimage -znd photos\n"
            + "\n"
            + " send me yours to: <flikxxi@gmail.com>\n";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException, IOException, ClassifierFormatException, NoSuchAlgorithmException
    {
        int verboseLevel = 0;

        OptionParser options = new OptionParser("langdetect");
        OptionHelper helper = new OptionHelper(options,LangDetectMain.class);
        
        CommandOption coach = options.add(new CommandOption("coach"));
        CommandOption detect = options.add(new CommandOption("detect"));
        CommandOption load = options.add(new CommandOption("load"));
        CommandOption save = options.add(new CommandOption("save"));
        CommandOption loadz = options.add(new CommandOption("loadz"));
        CommandOption savez = options.add(new CommandOption("savez"));

        BooleanOption verbose = options.add(new BooleanOption('v', "verbose"));
        BooleanOption verboseLogger = options.add(new BooleanOption('V',"verbose-logger"));
        BooleanOption version = options.add(new BooleanOption("version"));
        BooleanOption help = options.add(new BooleanOption('h', "help"));
        BooleanOption license = options.add(new BooleanOption('L', "license"));
        BooleanOption examples = options.add(new BooleanOption("examples"));
        options.setOneHyphen(false);
        if (args.length < 1)
        {          
            helper.help(System.out,true,true,true);
            return;
        }
        
        try
        {
            args = LauncherParser.parseInstall(LANG_DETECT, null, VER, args);
            if (args == null)
            {
                return;
            }
            args = options.parse(args);
        }
        catch (InvalidOptionException ex)
        {
            System.err.println(ex);
            System.err.println(LANG_DETECT + ": Try --help for more information");
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

        VerboseHandler vh = verboseLogger.isUsed() ? new VerboseHandler(System.err, new SimpleFormatter()) : new VerboseHandler(System.err, "langdetect: ");
        VerboseHandler.register(verboseLevel, vh, ConsoleHandler.class);

        Logger logger = Logger.getLogger(LangDetectMain.class.getName());
        if (logger.isLoggable(Level.CONFIG))
        {
            logger.log(Level.CONFIG, "{0}.version={1}", new String[]
                    {
                        LANG_DETECT, VER
                    });
            logger.log(Level.CONFIG, "logger.level={0}", vh.getLevel().getName());
            options.log();
            vh.flush();
        }
        
        if(load.isUsed() || loadz.isUsed())
        {
            load(args,loadz.isUsed());
        }
        else if(save.isUsed()||savez.isUsed())
        {
            save(args,savez.isUsed());
        }
        else if(coach.isUsed())
        {
            coach(args);
        }
        else if(detect.isUsed())
        {
            detect(args);
        }
        else
        {
            helper.help(System.out,true,true,true);
        }
        
    }
    
    private static void load(String[] args, boolean gzip) throws ClassifierFormatException, NoSuchAlgorithmException, FileNotFoundException, IOException
    {
        BaseTextClassifier classifier = getClassifier();
        File file = new File(args[0]);
                
        if(file.exists())
        {
            InputStream in = new FileInputStream(file);
            if(gzip)
            {
                classifier.loadGZ(in, true);
            }
            else
            {
                classifier.load(in, true);
            }
            putClassifier(classifier);
        }
        else
        {
            System.err.println(LANG_DETECT + ": file not found");
        }
    }
    private static void save(String[] args, boolean gzip) throws FileNotFoundException, IOException, UnsupportedEncodingException, NoSuchAlgorithmException, ClassifierFormatException
    {
        BaseTextClassifier classifier = getClassifier();
        File file = new File(args[0]);
                
        if(file.exists())
        {
            OutputStream out = new FileOutputStream(file);
            if(gzip)
            {
                classifier.saveGZ(out, 0, Integer.MAX_VALUE);
            }
            else
            {
                classifier.save(out, 0, Integer.MAX_VALUE);
            }
        }
        else
        {
            System.err.println(LANG_DETECT + ": file not found");
        }
    }
    
    private static void detect(String[] args) throws FileNotFoundException, ClassifierFormatException, NoSuchAlgorithmException, IOException
    {
        BaseTextClassifier classifier = getClassifier();
        InputStream in = args[0].equals("-")?System.in:new FileInputStream(args[0]);
        Score lang = classifier.classify(in);
        System.out.println(lang.getName()+" "+lang.getValue()+"%");
    }

    private static void coach(String[] args) throws FileNotFoundException, ClassifierFormatException, NoSuchAlgorithmException, IOException
    {
        BaseTextClassifier classifier = getClassifier();
        InputStream in = args[1].equals("-")?System.in:new FileInputStream(args[1]);
        try
        {
            classifier.coach(args[0],in);
        }
        finally
        {
            putClassifier(classifier);
        }
    }

    static File dataFile=null;
    static File backFile=null;
    final static String DICTIONARY= "dictionary.txt.gz";
    private static BaseTextClassifier getClassifier() throws FileNotFoundException, ClassifierFormatException, NoSuchAlgorithmException, IOException
    {
        BaseTextClassifier classifier = Classifiers.synchronizedClassifier(new BaseTextClassifier(null));
        String home = System.getProperty("user.home");
        File dir = new File(home,".langdetect");
        dataFile = new File(dir,"data.txt");
        backFile = new File(dir,"data.txt.bak");
        if(dataFile.exists())
        {
            classifier.load(new FileInputStream(dataFile), true);
        }
        else if(backFile.exists())
        {
            classifier.load(new FileInputStream(backFile), true);
            backFile.renameTo(dataFile);
        }
        else
        {
            classifier.loadGZ(LangDetectMain.class.getResourceAsStream(DICTIONARY), true);
        }
        return classifier;
    }
    private static void putClassifier(BaseTextClassifier classifier) throws UnsupportedEncodingException, NoSuchAlgorithmException, FileNotFoundException, IOException
    {
        dataFile.renameTo(new File(dataFile.toString()+".bak"));
        dataFile.getParentFile().mkdirs();
        if(dataFile.createNewFile())
        {
            classifier.save(new FileOutputStream(dataFile), 0, Integer.MAX_VALUE);
        }
        else
        {
            System.out.println(LANG_DETECT+": can't create file "+dataFile);
        }
    }
    
}

