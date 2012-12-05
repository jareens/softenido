/*
 *  MailBulk.java
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
package com.softenido.mailbulk;

import com.softenido.cafecore.gauge.Gauge;
import com.softenido.cafecore.gauge.GaugeProgress;
import com.softenido.cafedark.gauge.ConsoleGauge;
import com.softenido.cafedark.util.launcher.LauncherParser;
import com.softenido.cafedark.util.options.BooleanOption;
import com.softenido.cafedark.util.options.InvalidOptionException;
import com.softenido.cafedark.util.options.Option;
import com.softenido.cafedark.util.options.OptionParser;
import com.softenido.cafedark.util.options.StringOption;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Properties;

public class MailBulk
{

    private static final String MAILBULK = "mailbulk";
    private static final String VER = "0.0.1";
    private static final String VERSION =
            "mailbulk version " + VER + " alpha  (2010-03-14)\n"
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
            + "mailbulk comes with ABSOLUTELY NO WARRANTY. This is free software, and you\n"
            + "are welcome to redistribute it under certain conditions. See the GNU\n"
            + "General Public Licence version 3 for details.\n"
            + "\n"
            + "mailbulk sends images to picasa or to email addresses specified.\n"
            + "\n"
            + "usage: mailbulk [options] title directory\n"
            + "usage: mailbulk [options] title directory --to=friend@example.com\n"
            + "       java -jar MailBulk.jar [options] title directory\n"
            + "\n"
            + "Options:\n"
            + " -i, --interactive           interactive\n"
            + " -v, --verbose               increase verbosity\n"
            + " -L, --license               display software license\n"
            + "     --user=username         username for identification\n"
            + "     --pass=password         password for identification\n"
            + "     --to=address            send by email to address\n"
            + "     --cc=address            send by email to address\n"
            + "     --bcc=address           send by email to address\n"
            + "     --smtp=host             smtp host\n"
            + "     --from=address          from email address\n"
            + "     --ssl=[true|false]      use ssl for smtp\n"
            + "     --starttls=[true|false] use starttls for smtp\n"
            + "     --albumsize=N           max number of images by album or email\n"
            + "     --shrink=SIZE           shrink image to fit into SIZExSIZE\n"
            + "     --skip=N                skip first N images\n"
            + "     --loadconf              load configuration (user, ... from .mailbulk\n"
            + "     --saveconf              save configuration (user, ... from .mailbulk\n"
            + "     --passconf              to use pass in configuration file (unsafe)\n"
            + "     --install               install a launcher\n"
            + "     --install-java[=path]   install a launcher using 'java' command\n"
            + "     --install-home[=path]   install a launcher using 'java.home' property\n"
            + "     --install-posix         posix flavor for install options when unknown\n"
            + "     --install-version       adds version to launcher name\n"
            + "     --version               print version number\n"
            + "     --examples              print some useful examples\n"
            + "     --debug                 debug mode for developers\n"
            + "(-h) --help                  show this help (-h works with no other options)\n"
            + "\n"
            + REPORT_BUGS;
    private static final String EXAMPLES =
            VERSION
            + "\n"
            + "examples of mailbulk usage:\n"
            + "\n"
            + " java -jar MailBulk.jar --install\n"
            + " sudo java -jar MailBulk.jar --install\n"
            + " sudo /opt/jdk1.6/bin/java -jar MailBulk.jar --install-home\n"
            + " sudo /opt/jdk1.6/bin/java -jar MailBulk.jar --install-posix\n"
            + " mailbulk --shrink=1024 weekend weekend/2009-12-29\n"
            + " mailbulk --shrink=800 kyten c:\\backup\\weekend\\2009-12-29\n"
            + " mailbulk --to=friend@example.com --shrink=1024 party c:\\backup\\pics \n"
            + " mailbulk --cc=friend@example.com --saveconf party c:\\backup\\pics \n"
            + " mailbulk --bcc=friend@example.com --loadconf party c:\\backup\\pics \n"
            + "\n"
            + " send me yours to: <flikxxi@gmail.com>\n";
    private static final String USER_HOME = "user.home";
    private static final String USER = "user";
    private static final String PASS = "pass";
    private static final String SMTP = "smtp";
    private static final String FROM = "from";
    private static final String SSL = "ssl";
    private static final String STARTTLS = "starttls";
    private static final String _MAILBULK = ".mailbulk";

    public static void main(String[] args) throws Exception
    {
        if (args.length < 1)
        {
            System.out.println(HELP);
            return;
        }
        final File confFile = new File(System.getProperty(USER_HOME), _MAILBULK);

        OptionParser options = new OptionParser();

        BooleanOption interactive = options.add(new BooleanOption('i', "interactive"));
        BooleanOption verbose = options.add(new BooleanOption('v', "verbose"));
        int verboseLevel = 0;

        StringOption optUser = options.add(new StringOption("user"));
        StringOption optPass = options.add(new StringOption("pass"));
        StringOption to = options.add(new StringOption("to"));
        StringOption cc = options.add(new StringOption("cc"));
        StringOption bcc = options.add(new StringOption("bcc"));
        StringOption optSmtp = options.add(new StringOption("smtp"));
        StringOption optFrom = options.add(new StringOption("from"));
        StringOption optSsl = options.add(new StringOption("ssl"));
        StringOption optStarttls = options.add(new StringOption("starttls"));

        BooleanOption loadconf = options.add(new BooleanOption("loadconf"));
        BooleanOption passconf = options.add(new BooleanOption("passconf"));
        BooleanOption saveconf = options.add(new BooleanOption("saveconf"));

        Option license = options.add(new BooleanOption('L', "license"));
        StringOption shrink = options.add(new StringOption("shrink"));
        StringOption skip = options.add(new StringOption("skip"));
        StringOption mailSizeOpt = options.add(new StringOption("albumsize"));
        Option version = options.add(new BooleanOption("version"));
        Option help = options.add(new BooleanOption('h', "help"));

        BooleanOption examples = options.add(new BooleanOption("examples"));
        BooleanOption debug = options.add(new BooleanOption("debug"));

        try
        {
            args = LauncherParser.parseInstall(MAILBULK, null, VER, args);
            if (args == null)
            {
                return;
            }
            args = options.parse(args);
        }
        catch (InvalidOptionException ex)
        {
            System.err.println(ex);
            System.err.println(MAILBULK + ": Try --help for more information");
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
        String optName = null;
        String optVal = null;
        int mailMaxSize = 1;
        int skipValue = 0;
        boolean email = false;

        if (to.isUsed() || cc.isUsed() || bcc.isUsed())
        {
            email = true;
            mailMaxSize = 1;
        }

        try
        {
            if (mailSizeOpt.isUsed())
            {
                optName = mailSizeOpt.getUsedName();
                optVal = mailSizeOpt.getValue();
                mailMaxSize = Integer.parseInt(optVal);
            }
            if (skip.isUsed())
            {
                optName = skip.getUsedName();
                optVal = skip.getValue();
                skipValue = Integer.parseInt(optVal);
            }

        }
        catch (NumberFormatException ex)
        {
            if (optVal == null)
            {
                System.err.println("mailbulk: missing argument to '" + optName + "'");
            }
            else
            {
                System.err.println("mailbulk: invalid argument '" + optVal + "' to '" + optName + "'");
            }
            return;
        }

        if (args.length == 0)
        {
            System.err.println("mailbulk: no directories specified");
            return;
        }

        String username = null;
        String password = null;
        String smtp = null;
        String from = null;
        Boolean ssl = null;
        Boolean starttls = null;
        Console cons = System.console();

        if (optUser.isUsed())
        {
            username = optUser.getValue();
        }
        if (optPass.isUsed())
        {
            password = optUser.getValue();
        }
        if (optSmtp.isUsed())
        {
            smtp = optSmtp.getValue();
        }
        if (optFrom.isUsed())
        {
            from = optFrom.getValue();
        }
        if (optSsl.isUsed())
        {
            ssl = Boolean.parseBoolean(optSsl.getValue());
        }
        if (optStarttls.isUsed())
        {
            starttls = Boolean.parseBoolean(optStarttls.getValue());
        }

        if (loadconf.isUsed())
        {
            Properties conf = new Properties();
            conf.load(new FileInputStream(confFile));
            if (username == null)
            {
                username = conf.getProperty(USER);
            }
            if (password == null && passconf.isUsed())
            {
                password = conf.getProperty(PASS);
            }
            if (smtp == null)
            {
                smtp = conf.getProperty(SMTP);
            }
            if (from == null)
            {
                from = conf.getProperty(FROM);
            }
            if (ssl == null)
            {
                ssl = Boolean.parseBoolean(conf.getProperty(SSL));
            }
            if (starttls == null)
            {
                starttls = Boolean.parseBoolean(conf.getProperty(STARTTLS));
            }
        }

        if (interactive.isUsed())
        {
            InteractiveMailConf ask;
            ask = new InteractiveMailConf();
            ask.run();
            username = ask.getUser();
            password = ask.getPass();
            smtp = ask.getSmtp();
            from = ask.getFrom();
            ssl = ask.isSsl();
            starttls = ask.isStarttls();
        }

        if (username == null || password == null)
        {
            System.err.print("exit\n");
            return;
        }

        String subject = args[0];
        String dirname = args[1];

        SendMailBulk sender;
        SendMailBulk mail = new SendMailBulk(subject);
        mail.setUserPassword(smtp, username, password, ssl, starttls);
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
        sender = mail;

        if (saveconf.isUsed())
        {
            Properties conf = new Properties();
            conf.setProperty(USER, username != null ? username : "");
            if (passconf.isUsed())
            {
                conf.setProperty(PASS, password);
            }
            conf.setProperty(SMTP, smtp != null ? smtp : "");
            conf.setProperty(FROM, from != null ? from : "");
            conf.setProperty(SSL, ssl != null ? Boolean.toString(ssl) : "false");
            conf.setProperty(STARTTLS, starttls != null ? Boolean.toString(starttls) : "false");
            conf.store(new FileOutputStream(confFile), "--mailbulk--");
        }

        File dir = new File(dirname);

        File[] files = dir.listFiles();
        long[] sizes = new long[files.length];
        Arrays.sort(files);

        long fullsize = 0;
        GaugeProgress gauge = new ConsoleGauge();

        gauge.start(100);

        for (int i = skipValue; i < files.length; i++)
        {
            sizes[i] = files[i].length();
            fullsize += sizes[i];
        }
        gauge.start((int) fullsize);

        for (int i = skipValue; i < files.length;)
        {
            int albumCount = (i / mailMaxSize) + 1;
            sender.addFile(albumCount, i);
            for (int j = i % mailMaxSize; j < mailMaxSize && i < files.length; j++, i++)
            {
                if (files[i] == null)
                {
                    continue;
                }
                gauge.setPrefix(files[i].getName());

                boolean photo = sender.addFile(files[i]);
                gauge.step((int) sizes[i]);
            }

            sender.flush();
        }
        gauge.close();
    }
}
