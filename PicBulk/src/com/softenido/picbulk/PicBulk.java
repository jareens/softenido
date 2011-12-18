/*
 *  PicBulk.java
 *
 *  Copyright (C) 2009-2011  Francisco Gómez Carrasco
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
package com.softenido.picbulk;

import com.softenido.cafedark.image.ScaleDimension;
import com.softenido.cafedark.image.SimpleScaleDimension;
import com.softenido.cafedark.io.FakeConsole;
import com.softenido.cafedark.io.RealConsole;
import com.softenido.cafedark.io.VirtualConsole;
import com.softenido.cafedark.misc.ConsoleGauge;
import com.softenido.cafedark.misc.Gauge;
import com.softenido.cafedark.util.launcher.LauncherParser;
import com.softenido.cafedark.util.options.BooleanOption;
import com.softenido.cafedark.util.options.InvalidOptionException;
import com.softenido.cafedark.util.options.Option;
import com.softenido.cafedark.util.options.OptionParser;
import com.softenido.cafedark.util.options.StringOption;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Properties;

public class PicBulk
{
    private static final String PICBULK = "picbulk";
    private static final String VER = "0.0.2";
    private static final String VERSION =
            "picbulk version "+VER+" alpha  (2010-06-15)\n" +
            "Copyright (C) 2007-2010 by Francisco Gómez Carrasco\n" +
            "<http://www.softenido.com>\n";
    private static final String REPORT_BUGS =
            "Report bugs to <flikxxi@gmail.com>\n" +
            "            or <http://code.google.com/p/softenido/issues>\n";
    private static final String LICENSE =
            VERSION +
            "\n" +
            "This program is free software: you can redistribute it and/or modify\n" +
            "it under the terms of the GNU General Public License as published by\n" +
            "the Free Software Foundation, either version 3 of the License, or\n" +
            "(at your option) any later version.\n" +
            "\n" +
            "This program is distributed in the hope that it will be useful,\n" +
            "but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
            "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +
            "GNU General Public License for more details.\n" +
            "\n" +
            "You should have received a copy of the GNU General Public License\n" +
            "along with this program.  If not, see <http://www.gnu.org/licenses/>.\n" +
            "\n" +
            REPORT_BUGS;

    private static final String HELP =
            VERSION +
            "\n" +
            "picbulk comes with ABSOLUTELY NO WARRANTY. This is free software, and you\n" +
            "are welcome to redistribute it under certain conditions. See the GNU\n" +
            "General Public Licence version 3 for details.\n" +
            "\n" +
            "picbulk sends images to picasa or to email addresses specified.\n" +
            "\n" +
            "usage: picbulk [options] title directory\n" +
            "usage: picbulk [options] title directory --to=friend@example.com\n" +
            "       java -jar PicBulk.jar [options] title directory\n" +
            "\n" +
            "Options:\n" +
            " -v, --verbose               increase verbosity\n" +
            " -L, --license               display software license\n" +
            "     --user=username         username for identification\n" +
            "     --pass=password         password for identification\n" +
            "     --to=address            send by email to address\n" +
            "     --cc=address            send by email to address\n" +
            "     --bcc=address           send by email to address\n" +
            "     --smtp=host             smtp host\n" +
            "     --from=address          from email address\n" +
            "     --ssl=[true|false]      use ssl for smtp\n" +
            "     --starttls=[true|false] use starttls for smtp\n" +
            "     --albumsize=N           max number of images by album or email\n" +
            "     --shrink=SIZE           shrink image to fit into SIZExSIZE\n" +
            "     --grayscale             convert image to grayscale\n" +
            "     --skip=N                skip first N images\n" +
            "     --loadconf              load configuration (user, ... from .picbulk\n" +
            "     --saveconf              save configuration (user, ... from .picbulk\n" +
            "     --passconf              to use pass in configuration file (unsafe)\n" +
            "     --install               install a launcher\n" +
            "     --install-java[=path]   install a launcher using 'java' command\n" +
            "     --install-home[=path]   install a launcher using 'java.home' property\n" +
            "     --install-posix         posix flavor for install options when unknown\n" +
            "     --install-version       adds version to launcher name\n" +
            "     --version               print version number\n" +
            "     --examples              print some useful examples\n" +
            "     --debug                 debug mode for developers\n" +
            "(-h) --help                  show this help (-h works with no other options)\n" +
            "\n" +
            REPORT_BUGS;

    private static final String EXAMPLES =
            VERSION+
            "\n"+
            "examples of picbulk usage:\n" +
            "\n" +
            " java -jar PicBulk.jar --install\n" +
            " sudo java -jar PicBulk.jar --install\n" +
            " sudo /opt/jdk1.6/bin/java -jar PicBulk.jar --install-home\n" +
            " sudo /opt/jdk1.6/bin/java -jar PicBulk.jar --install-posix\n" +
            " picbulk --shrink=1024 weekend weekend/2009-12-29\n" +
            " picbulk --shrink=800 kyten c:\\backup\\weekend\\2009-12-29\n" +
            " picbulk --to=friend@example.com --shrink=1024 party c:\\backup\\pics \n" +
            " picbulk --cc=friend@example.com --saveconf party c:\\backup\\pics \n" +
            " picbulk --bcc=friend@example.com --loadconf party c:\\backup\\pics \n" +
            "\n" +
            " send me yours to: <flikxxi@gmail.com>\n";

    private static final String USER_HOME = "user.home";
    private static final String USER = "user";
    private static final String PASS = "pass";
    private static final String SMTP = "smtp";
    private static final String FROM = "from";
    private static final String SSL  = "ssl";
    private static final String STARTTLS  = "starttls";
    private static final String _PICBULK = ".picbulk";
    private static String[] FILETYPES = {".jpg", ".jpeg", ".png", ".gif"};

    public static void main(String[] args) throws Exception
    {
        if (args.length < 1)
        {
            System.out.println(HELP);
            return;
        }
        final File confFile = new File(System.getProperty(USER_HOME),_PICBULK);

        OptionParser options = new OptionParser();

        BooleanOption verbose = options.add(new BooleanOption('v', "verbose"));
        int verboseLevel = 0;

        StringOption optUser = options.add(new StringOption("user"));
        StringOption optPass = options.add(new StringOption("pass"));
        StringOption to  = options.add(new StringOption("to"));
        StringOption cc  = options.add(new StringOption("cc"));
        StringOption bcc  = options.add(new StringOption("bcc"));
        StringOption optSmtp = options.add(new StringOption("smtp"));
        StringOption optFrom = options.add(new StringOption("from"));
        StringOption optSsl = options.add(new StringOption("ssl"));
        StringOption optStarttls = options.add(new StringOption("starttls"));

        BooleanOption loadconf = options.add(new BooleanOption("loadconf"));
        BooleanOption passconf = options.add(new BooleanOption("passconf"));
        BooleanOption saveconf = options.add(new BooleanOption("saveconf"));

        Option license = options.add(new BooleanOption('L', "license"));
        StringOption shrink = options.add(new StringOption("shrink"));
        BooleanOption grayscale = options.add(new BooleanOption('g', "grayscale"));

        StringOption skip   = options.add(new StringOption("skip"));
        StringOption albumsize = options.add(new StringOption("albumsize"));
        Option version = options.add(new BooleanOption("version"));
        Option help = options.add(new BooleanOption('h', "help"));

        BooleanOption examples = options.add(new BooleanOption("examples"));
        BooleanOption debug = options.add(new BooleanOption("debug"));

        try
        {
            args = LauncherParser.parseInstall(PICBULK, null, VER, args);
            if(args==null)
            {
                return;
            }
            args = options.parse(args);
        }
        catch (InvalidOptionException ex)
        {
            System.err.println(ex);
            System.err.println(PICBULK+": Try --help for more information");
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
        if( verbose.isUsed())
        {
            verboseLevel = verbose.getCount();
        }
        String optName = null;
        String optVal = null;
        int shrinkValue = 0;
        int albumMaxSize = 500;
        int skipValue = 0;
        boolean email = false;

        if(to.isUsed()||cc.isUsed()||bcc.isUsed())
        {
            email = true;
            albumMaxSize = 5;
        }

        try
        {
            if (shrink.isUsed())
            {
                optName = shrink.getUsedName();
                optVal  = shrink.getValue();
                shrinkValue = Integer.parseInt(optVal);
            }
            if(albumsize.isUsed())
            {
                optName = albumsize.getUsedName();
                optVal  = albumsize.getValue();
                albumMaxSize = Integer.parseInt(optVal);
            }
            if(skip.isUsed())
            {
                optName = skip.getUsedName();
                optVal  = skip.getValue();
                skipValue = Integer.parseInt(optVal);
            }

        }
        catch (NumberFormatException ex)
        {
            if (optVal == null)
            {
                System.err.println("picbulk: missing argument to '" + optName + "'");
            }
            else
            {
                System.err.println("picbulk: invalid argument '" + optVal + "' to '" + optName + "'");
            }
            return;
        }

        if (args.length == 0)
        {
            System.err.println("picbulk: no directories specified");
            return;
        }

        if(verboseLevel>0)
        {
            String title = args[0];
            String dirname = args[1];
            System.out.println("dirname=" + dirname);
            System.out.println("title=" + title);
        }

        String username = null;
        String password = null;
        String smtp     = null;
        String from     = null;
        Boolean ssl     = null;
        Boolean starttls= null;

        VirtualConsole cons = null;
        if(System.console() != null)
        {
            cons = new RealConsole(System.console());
        }
        else if(debug.isUsed())
        {
            cons = new FakeConsole(System.in, System.out);
        }

        if(optUser.isUsed())
        {
            username = optUser.getValue();
        }
        if(optPass.isUsed())
        {
            password = optUser.getValue();
        }
        if(optSmtp.isUsed())
        {
            smtp = optSmtp.getValue();
        }
        if(optFrom.isUsed())
        {
            from = optFrom.getValue();
        }
        if(optSsl.isUsed())
        {
            ssl = Boolean.parseBoolean(optSsl.getValue());
        }
        if(optStarttls.isUsed())
        {
            starttls= Boolean.parseBoolean(optStarttls.getValue());
        }

        if (loadconf.isUsed())
        {
            Properties conf = new Properties();
            conf.load(new FileInputStream(confFile));
            if(username==null)
            {
                username = conf.getProperty(USER);
            }
            if(password==null && passconf.isUsed())
            {
                password = conf.getProperty(PASS);
            }
            if (smtp == null)
            {
                smtp = conf.getProperty(SMTP);
            }
            if(from==null)
            {
                from = conf.getProperty(FROM);
            }
            if(ssl==null)
            {
                ssl = Boolean.parseBoolean(conf.getProperty(SSL));
            }
            if(starttls==null)
            {
                starttls = Boolean.parseBoolean(conf.getProperty(STARTTLS));
            }
        }
        
        if (username==null && (cons!= null))
        {
            username = cons.readLine("[%s]", "user:");
        }
        if (password==null && (cons != null))
        {
            char[] passwd;
            passwd = cons.readPassword("[%s]", "password:");
            password = new String(passwd);
        }
        if (username == null || password == null)
        {
            System.err.print("exit\n");
            return;
        }
        if (smtp==null && email && (cons != null))
        {
            smtp = cons.readLine("[%s]", "smtp:");
        }
        if (from==null && email && (cons != null))
        {
            from = cons.readLine("[%s]", "from:");
        }
        if (ssl==null && email && (cons != null))
        {
            ssl = Boolean.parseBoolean(cons.readLine("[%s]", "ssl:"));
        }
        if (starttls==null && email && (cons != null))
        {
            starttls = Boolean.parseBoolean(cons.readLine("[%s]", "starttls:"));
        }

        ScaleDimension dim = null;
        if(shrinkValue>0)
        {
            dim = new SimpleScaleDimension(shrinkValue);
        }

        String title = args[0];
        String dirname = args[1];

        PicBulkService service;
        if(email)
        {
            PicBulkMail mail = new PicBulkMail(title);
            mail.setUserPassword(smtp,username, password,ssl,starttls);
            if (to.isUsed())
            {
                mail.setTo(to.getValue());
            }
            if (cc.isUsed())
            {
                mail.setCC(cc.getValue());
            }
            if (bcc.isUsed())
            {
                mail.setBcc(bcc.getValue());
            }
            mail.setFrom(from);
            mail.setDebug(debug.isUsed());
            service = mail;
        }
        else
        {
            PicBulkWeb web = new PicBulkWeb(title,true,"");
            web.setUserPassword(username, password);
            service = web;
        }
        
        if (saveconf.isUsed())
        {           
            Properties conf = new Properties();
            conf.setProperty(USER,username!=null?username:"");
            if(passconf.isUsed())
            {
                conf.setProperty(PASS,password);
            }
            conf.setProperty(SMTP,smtp!=null?smtp:"");
            conf.setProperty(FROM,from!=null?from:"");
            conf.setProperty(SSL,ssl!=null?Boolean.toString(ssl):"false");
            conf.setProperty(STARTTLS,starttls!=null?Boolean.toString(starttls):"false");
            conf.store(new FileOutputStream(confFile),"--picbulk--");
        }
      
        File dir = new File(dirname);


        File[] files = dir.listFiles();
        long[] sizes = new long[files.length];
        Arrays.sort(files);

        long fullsize = 0;
        Gauge gauge = new ConsoleGauge();

        gauge.begin(100);

        for (int i = skipValue; i < files.length; i++)
        {
            boolean typeOk = false;
            for (String ext : FILETYPES)
            {
                if (files[i].getName().toLowerCase().endsWith(ext))
                {
                    typeOk = true;
                    break;
                }
            }
            if (typeOk)
            {
                sizes[i] = files[i].length();
                fullsize += sizes[i];
            }
            else
            {
                files[i] = null;
                sizes[i] = 0;
            }
        }
        gauge.begin((int) fullsize);

        for (int i = skipValue; i < files.length;)
        {
            int albumCount = (i/albumMaxSize)+1;
            service.addAlbum(albumCount,i);
            for(int j = i%albumMaxSize ; j<albumMaxSize && i<files.length ;j++,i++)
            {
                if (files[i] == null)
                    continue;
                gauge.setPrefix(files[i].getName());
            
                boolean photo = service.addPhoto(files[i],dim, grayscale.isUsed());
                gauge.step((int) sizes[i]);
            }
            service.flush();
        }
        gauge.end();
    }
}
