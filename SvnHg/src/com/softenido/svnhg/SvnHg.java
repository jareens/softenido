/*
 *  SvnHg.java
 *
 *  Copyright (C) 2010  Francisco Gómez Carrasco
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
package com.softenido.svnhg;

import com.softenido.cafe.util.launcher.LauncherParser;
import com.softenido.cafe.util.options.ArrayStringOption;
import com.softenido.cafe.util.options.BooleanOption;
import com.softenido.cafe.util.options.InvalidOptionException;
import com.softenido.cafe.util.options.MissingOptionParameterException;
import com.softenido.cafe.util.options.Option;
import com.softenido.cafe.util.options.OptionParser;
import com.softenido.cafe.util.options.StringOption;
import com.softenido.cafe.io.RealConsole;
import com.softenido.cafe.io.VirtualConsole;
import com.softenido.cafe.io.FakeConsole;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

/**
 *
 * @author franci
 */
public class SvnHg
{
    private static final String SVNHG = "svnhg";
    private static final String _SVNHG = ".svnhg";
    private static final String VER = "0.0.1";
    private static final String VERSION =
            "svnhg version " + VER + " alpha  (2010-04-01)\n"
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
            + "svnhg comes with ABSOLUTELY NO WARRANTY. This is free software, and you\n"
            + "are welcome to redistribute it under certain conditions. See the GNU\n"
            + "General Public Licence version 3 for details.\n"
            + "\n"
            + "svnhg migrates a repository from subversion (svn) to mercurial (Hg)\n"
            + "\n"
            + "usage: svnhg [options] path\n"
            + "       java -jar SvnHg.jar [options] path\n"
            + "\n"
            + "Options:\n"
            + " -v, --verbose               increase verbosity\n"
            + " -L, --license               display software license\n"
            + "     --svn-url=url           subversion repository url\n"
            + "     --svn-user=username     subversion repository username\n"
//            + "     --svn-pass=password     subversion repository password\n"
            + "     --hg-url=url            mercurial repository url\n"
//            + "     --hg-user=username      mercurial repository username\n"
//            + "     --hg-pass=password      mercurial repository password\n"
            + "     --hg-push               do mercurial push\n"
            + "     --skip=revisions        range of revisions to skip ( ie:1,3-5,7,13-17)\n"
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
            + "examples of svnhg usage:\n"
            + "\n"
            + " java -jar SvnHg.jar --install\n"
            + " sudo java -jar SvnHg.jar --install\n"
            + " sudo /opt/jdk1.6/bin/java -jar SvnHg.jar --install-home\n"
            + " sudo /opt/jdk1.6/bin/java -jar SvnHg.jar --install-posix\n"
            + " svnhg -v softenido\n"
            + " svnhg softenido --skip 9-30,35,55-65 --hg-push\n"
            + " svnhg softenido --skip 9-30,35,55-65 --hg-push\n"
            + " svnhg -v softenido --svn-url=http://softenido.googlecode.com/svn/trunk/\n"
            + "\n"
            + " send me yours to: <flikxxi@gmail.com>\n";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, MissingOptionParameterException, InterruptedException
    {
        if (args.length < 1)
        {
            System.out.println(HELP);
            return;
        }

        OptionParser options = new OptionParser();

        BooleanOption verbose = options.add(new BooleanOption('v', "verbose"));
        int verboseLevel = 0;

        StringOption optSvnUrl = options.add(new StringOption("svn-url"));
        StringOption optSvnUser = options.add(new StringOption("svn-user"));
        StringOption optSvnPass = options.add(new StringOption("svn-pass"));

        StringOption optHgUrl = options.add(new StringOption("hg-url"));
//        StringOption optHgUser = options.add(new StringOption("hg-user"));
//          StringOption optHgPass = options.add(new StringOption("hg-pass"));
        StringOption optHgPush = options.add(new StringOption("hg-push"));
        ArrayStringOption optSkip = options.add(new ArrayStringOption("skip"));

        Option license = options.add(new BooleanOption('L', "license"));
        Option version = options.add(new BooleanOption("version"));
        Option help = options.add(new BooleanOption('h', "help"));

        BooleanOption examples = options.add(new BooleanOption("examples"));
        BooleanOption debug = options.add(new BooleanOption("debug"));

        try
        {
            args = LauncherParser.parseInstall(SVNHG, null, VER, args);
            if (args == null)
            {
                return;
            }
            args = options.parse(args);
        }
        catch (InvalidOptionException ex)
        {
            System.err.println(ex);
            System.err.println(SVNHG + ": Try --help for more information");
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

        if (args.length == 0)
        {
            System.err.println("svnhg: no path specified");
            return;
        }

        verboseLevel = verbose.getCount();

        String svnUrl = null;
        String svnUser = null;
        String svnPass = null;
        String hgUrl = null;
        String hgUser = null;
        String hgPass = null;

        RevisionRange skipRev[] = new RevisionRange[0];

        if(optSkip.isUsed())
        {
            String[] ranges = optSkip.getValues();
            skipRev = new RevisionRange[ranges.length];
            for(int i=0;i<ranges.length;i++)
            {
                skipRev[i] = new RevisionRange(ranges[i]);
            }
        }

        VirtualConsole con = null;
        if(System.console() != null)
        {
            con = new RealConsole(System.console());
        }
        else if(debug.isUsed())
        {
            con = new FakeConsole(System.in, System.out);
        }

        if(args.length==0)
        {
            System.err.println(SVNHG+": no path specified");
            return;
        }
        
        String pathName = args[0];
        File path = new File(pathName);
        File _svnhg = new File(path,_SVNHG);
        FileOutputStream _svnhgOut =null;
        
        int lastRev = 0;
        String skip;
        boolean svnChecked = false;
        boolean hgCloned   = false;
        boolean hgIgnored  = false;

        if(path.exists())
        {
            if(path.isFile())
            {
                System.err.println(SVNHG+": file "+path+" exists");
                return;
            }
            
            if(_svnhg.exists())
            {
                Scanner sc = new Scanner(new FileInputStream(_svnhg));
                while(sc.hasNext())
                {
                    if(sc.hasNext("skip=.*"))
                    {
                        skip = sc.nextLine();
                    }
                    else if(sc.hasNext("hg"))
                    {
                        String token = sc.next();
                        if(token.equals("hg"))
                            hgCloned = true;
                    }
                    else if(sc.hasNext("svn"))
                    {
                        String token = sc.next();
                        if(token.equals("svn"))
                            svnChecked = true;
                    }
                    else if(sc.hasNext(".hgignore"))
                    {
                        String token = sc.next();
                        if(token.equals(".hgignore"))
                            hgIgnored = true;
                    }
                    else if(sc.hasNext("r[0-9]+"))
                    {
                        String rev = sc.next();
                        lastRev = Math.max(lastRev, Integer.valueOf(rev.substring(1)));
                    }
                    else
                    {
                        sc.nextLine();
                    }
                }
                sc.close();
            }
        }
        Svn svn;
        Hg hg;

        if(verboseLevel>0)
        {
            svn = new Svn(pathName, System.out, System.out, System.out, System.err);
            hg = new Hg(pathName, System.out, System.out, System.out, System.err);
        }
        else
        {
            svn = new Svn(pathName);
            hg = new Hg(pathName);
        }

        if(!hgCloned)
        {
            hgUrl  = optHgUrl.isUsed()  ?  optHgUrl.getValue()  : con.readLine("hg url:");
//            hgUser = optHgUser.isUsed() ?  optHgUser.getValue() : con.readLine("hg user:");
//            hgPass = optHgPass.isUsed() ?  optHgPass.getValue() : new String(con.readPassword("hg pass:"));

            int ret = hg.clone(hgUrl);
            if (ret != 0)
            {
                return;
            }
        }

        PrintStream ps = new PrintStream(new FileOutputStream(_svnhg,true));
        if(!hgCloned)
        {
            ps.println("hg");
        }

        if(!svnChecked)
        {
            svnUrl  = optSvnUrl.isUsed()  ?  optSvnUrl.getValue()  : con.readLine("svn url:");
            svnUser = optSvnUser.isUsed() ?  optSvnUser.getValue() : con.readLine("svn user:");
            if(svnUser.length()>0)
            {
                svnPass = optSvnPass.isUsed() ?  optSvnPass.getValue() : new String(con.readPassword("svn pass:"));
            }
            else
            {
                svnUser = null;
            }
            
            int ret = svn.checkout(svnUrl, svnUser, svnPass, 1);
            if (ret != 0)
            {
                return;
            }
            ps.println("svn");
        }
        
        if (!hgIgnored)
        {
            PrintStream hgignore = new PrintStream(new FileOutputStream(new File(pathName, ".hgignore")));
            hgignore.println("syntax: glob");
            hgignore.println(".svn/**");
            hgignore.println(".hg*");
            hgignore.println("*~");
            hgignore.println(".svnhg");
            hgignore.close();
            
            ps.println(".hgignore");
        }

        for (int i = lastRev+1; i < Integer.MAX_VALUE; i++)
        {
            int ret;
            boolean skipCur = false;
            for(int j=0;j<skipRev.length;j++)
            {
                if(skipRev[j].contains(i))
                {
                    skipCur = true;
                    break;
                }
            }
            if(skipCur)
            {
                System.out.println("skip r"+i);
                continue;
            }

            // se busca si la re
            int retUpdate = svn.update(i);
            Revision rev = svn.log(i);
            if (rev == null)
            {
                if(retUpdate!=0)
                    break;
                continue;
            }

            ret = hg.add();
            if (ret != 0)
            {
                return;
            }
            ret = hg.commit(rev);
            if (ret != 0)
            {
                return;
            }
            ps.println("r"+i);
            ps.flush();
        }
        ps.close();
        if(optHgPush.isUsed())
        {
            hg.push();
        }
    }
}
