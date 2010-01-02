/*
 *  FindRepeMain.java
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
package com.softenido.findrepe;

import com.softenido.cafe.io.ForEachFileOptions;
import com.softenido.cafe.util.OSName;
import com.softenido.cafe.util.SizeUnits;
import com.softenido.cafe.util.concurrent.actor.ActorPool;
import com.softenido.cafe.util.options.BooleanOption;
import com.softenido.cafe.util.options.InvalidOptionException;
import com.softenido.cafe.util.options.Option;
import com.softenido.cafe.util.options.OptionParser;
import com.softenido.cafe.util.options.StringOption;
import com.softenido.cafe.util.launcher.LauncherBuilder;
import com.softenido.cafe.util.launcher.LauncherParser;
import com.softenido.cafe.util.launcher.PosixLauncherBuilder;
import com.softenido.cafe.util.options.ArrayStringOption;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author franci
 */
public class FindRepeMain
{

    private static final String VERSION =
            "findrepe  version 0.7.0 beta  (2010-01-01)\n"
            + "Copyright (C) 2009-2010 by Francisco Gómez Carrasco\n"
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
            + "findrepe comes with ABSOLUTELY NO WARRANTY. This is free software, and you\n"
            + "are welcome to redistribute it under certain conditions. See the GNU\n"
            + "General Public Licence version 3 for details.\n"
            + "\n"
            + "findrepe searches the given path for repeated files by content (not name). Such\n"
            + "files are found by comparing file sizes and MD5+SHA1 signatures.\n"
            + "\n"
            + "usage: findrepe [options] [directories]\n"
            + "       java -jar FindRepe.jar [options] [directories]\n"
            + "\n"
            + "Options:\n"
            + " -v, --verbose               increase verbosity\n"
            + " -L, --license               display software license\n"
            + " -d, --delete                prompt user for files to delete\n"
            + " -n, --noempty               exclude zero-length files\n"
            + " -s, --symlinks              follow symlinks\n"
            + " -m, --min-size=size         minimum file size[bkmgt], exclude shorters\n"
            + " -M, --max-size=size         maximun file size[bkmgt], exclude largers\n"
            + " -w  --min-wasted=size       minimun wasted size[bkmgt] copies, size*(n-1)\n"
            + " -S  --size                  show size[bkmgt] of files\n"
            + "     --install               install a launcher\n"
            + "     --install-java[=path]   install a launcher using 'java' command\n"
            + "     --install-home[=path]   install a launcher using 'java.home' property\n"
            + "     --install-posix         posix flavor for install options when unknown\n"
            + "     --unique                list only unique files (--count=1)\n"
            + "     --count=N               list files repeated N times  \n"
            + " -c  --min-count=N           files repeated at least N times\n"
            + " -C  --max-count=N           files repeated no more than N times\n"
            + "     --noautoexclude         don't autoexclude some paths (/dev, /proc, ...)\n"
            + "     --exclude=path          don't follow path\n"
            + "     --exclude-dir=pattern   don't follow directories named name\n"
            + "     --exclude-file=pattern  ignore files named name\n"
            + "     --exclude-rc            ignore revision control directories\n"
            + "     --exclude-svn           ignore subversion (.svn)\n"
            + "     --exclude-cvs           ignore cvs (CVS)\n"
            + "     --exclude-hg            ignore mercurial (.hg and .hgignore)\n"
            + " -f  --focus=path            focus on files in path\n"
            + "     --focus-dir=pattern     focus on directories matching pattern\n"
            + "     --focus-file=pattern    focus on files matching pattern\n"
            + "     --dir=pattern           only directories matching pattern\n"
            + "     --file=pattern          only files matching pattern\n"
            + " -e  --regex                 uses java regular expresions\n"
            + "     --wildcard              uses wildcards '*', '?' and '[]' (default)\n"
            + "     --old                   old algorithm, as in version 0.6.2 (developers)\n"
            + "     --bug                   show filenames with bugs\n"
            + "     --bug-fix               try to fix filenames with bugs\n"
            + "     --version               print version number\n"
            + "     --examples              print some useful examples\n"
            + "(-h) --help                  show this help (-h works with no other options)\n"
            +"\n"
            + "size units:\n"
            + " 1=1b, 1k=1024b, 1m=1024k, 1g=1024m, 1t=1024g\n"
            + " separator character is " + File.pathSeparator + "\n"
            + "\n"
            + REPORT_BUGS;
    private static final String EXAMPLES =
            VERSION
            + "\n"
            + "examples of findrepe usage:\n"
            + "\n"
            + " java -jar FindRepe.jar --install\n"
            + " sudo java -jar FindRepe.jar --install\n"
            + " sudo /opt/jdk1.6/bin/java -jar FindRepe.jar --install-home\n"
            + " sudo /opt/jdk1.6/bin/java -jar FindRepe.jar --install-posix\n"
            + " findrepe backup\n"
            + " findrepe -d backup\n"
            + " findrepe -d --min-size=1m c:\\backup e:\\img\n"
            + " findrepe -nd c:\\backup e:\\img\n"
            + " findrepe -n /opt/ /backup/tools \n"
            + " findrepe -n /opt/ --exclude=/opt/nb6.7" + File.pathSeparator + "/opt/nb6.8\n"
            + "\n"
            + " send me yours to: <flikxxi@gmail.com>\n";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException, IOException
    {
        if (args.length < 1)
        {
            System.out.println(HELP);
            return;
        }
        int queueSize = 10000;
        SizeUnits sizeParser = new SizeUnits();
        OptionParser options = new OptionParser();

        BooleanOption verbose = options.add(new BooleanOption('v', "verbose"));
        int verboseLevel = 0;

        Option license = options.add(new BooleanOption('L', "license"));
        BooleanOption delete = options.add(new BooleanOption('d', "delete"));
        BooleanOption noempty = options.add(new BooleanOption('n', "noempty"));
        BooleanOption symlinks = options.add(new BooleanOption('s', "symlinks"));

        StringOption minSize = options.add(new StringOption('m', "min-size"));
        StringOption maxSize = options.add(new StringOption('M', "max-size"));
        StringOption minWasted = options.add(new StringOption('w', "min-wasted"));
        BooleanOption optSize = options.add(new BooleanOption('S',"size"));

        BooleanOption unique = options.add(new BooleanOption("unique"));
        StringOption count = options.add(new StringOption("count"));
        StringOption minCount = options.add(new StringOption('c',"min-count"));
        StringOption maxCount = options.add(new StringOption('C',"max-count"));

        BooleanOption excludeRc = options.add(new BooleanOption("exclude-rc"));
        BooleanOption excludeSvn = options.add(new BooleanOption("exclude-svn"));
        BooleanOption excludeCvs = options.add(new BooleanOption("exclude-cvs"));
        BooleanOption excludeHg = options.add(new BooleanOption("exclude-hg"));

        ArrayStringOption excludeDirName = options.add(new ArrayStringOption("exclude-dir", File.pathSeparatorChar));
        ArrayStringOption excludeFileName = options.add(new ArrayStringOption("exclude-file", File.pathSeparatorChar));

        BooleanOption noautoexclude = options.add(new BooleanOption("noautoexclude"));
        ArrayStringOption exclude = options.add(new ArrayStringOption("exclude", File.pathSeparatorChar));

        ArrayStringOption focusPath = options.add(new ArrayStringOption('f',"focus", File.pathSeparatorChar));
        ArrayStringOption focusDirName = options.add(new ArrayStringOption("focus-dir", File.pathSeparatorChar));
        ArrayStringOption focusFileName = options.add(new ArrayStringOption("focus-file", File.pathSeparatorChar));

        ArrayStringOption dirName = options.add(new ArrayStringOption("dir", File.pathSeparatorChar));
        ArrayStringOption fileName = options.add(new ArrayStringOption("file", File.pathSeparatorChar));
        BooleanOption regex = options.add(new BooleanOption("regex"));
        BooleanOption wildcard = options.add(new BooleanOption("wildcard"));
        BooleanOption old = options.add(new BooleanOption("old"));
        BooleanOption bug = options.add(new BooleanOption("bug"));
        BooleanOption bugFix = options.add(new BooleanOption("bug-fix"));

        Option version = options.add(new BooleanOption("version"));
        Option help = options.add(new BooleanOption('h', "help"));

        BooleanOption examples = options.add(new BooleanOption("examples"));
       
        String[] fileNames;
        try
        {
            LauncherParser parser = new LauncherParser();
            args = parser.parse(args);
            if (parser.isInstall())
            {
                LauncherBuilder builder = LauncherBuilder.getBuilder();
                if (builder == null && parser.isPosix())
                {
                    builder = new PosixLauncherBuilder("posix");
                }

                if (builder == null)
                {
                    System.err.println("findrepe: Operating System '" + OSName.os.getName() + "' not supported for install options");
                }
                else if (builder.buildLauncher(parser, "findrepe"))
                {
                    System.out.println("findrepe: '" + builder.getFileName() + "' created");
                }
                return;
            }
            fileNames = options.parse(args);
        } catch (InvalidOptionException ex)
        {
            System.err.println(ex);
            System.err.println("findrepe: Try --help for more information");
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
        boolean minSizeUsed = false;
        boolean maxSizeUsed = false;
        boolean minWastedUsed = false;
        long minSizeValue = 0;
        long maxSizeValue = Long.MAX_VALUE;
        long minWastedValue = Long.MAX_VALUE;
        try
        {
            if (minSize.isUsed())
            {
                minSizeUsed = true;
                optName = minSize.getUsedName();
                optVal = minSize.getValue();
                minSizeValue = sizeParser.parse(optVal);
            }
            if (maxSize.isUsed())
            {
                maxSizeUsed = true;
                optName = maxSize.getUsedName();
                optVal = maxSize.getValue();
                maxSizeValue = sizeParser.parse(optVal);
            }
            if (minWasted.isUsed())
            {
                minWastedUsed = true;
                optName = minWasted.getUsedName();
                optVal = minWasted.getValue();
                minWastedValue = sizeParser.parse(optVal);
            }
        }
        catch (NumberFormatException ex)
        {
            if (optVal == null)
            {
                System.err.println("findrepe: missing argument to '" + optName + "'");
            } else
            {
                System.err.println("findrepe: invalid argument '" + optVal + "' to '" + optName + "'");
            }
            return;
        }

        if (fileNames.length == 0)
        {
            System.err.println("findrepe: no directories specified");
            return;
        }

        File[] files = new File[fileNames.length];
        for (int i = 0; i < files.length; i++)
        {
            files[i] = new File(fileNames[i]);
        }
        FindRepeOptions opt = new FindRepeOptions();

        opt.setHidden(true);
        opt.setLinkDir(symlinks.isUsed());

        if (noempty.isUsed())
        {
            opt.setMinSize(1);
        }
        if (minSizeUsed)
        {
            opt.setMinSize(minSizeValue);
        }
        if (maxSizeUsed)
        {
            opt.setMaxSize(maxSizeValue);
        }
        if (minWastedUsed)
        {
            opt.setMinWasted(minWastedValue);
        }
        if (noautoexclude.isUsed())
        {
            opt.setAutoOmit(false);
        }
        if (exclude.isUsed())
        {
            String[] paths = exclude.getValues();
            for (String item : paths)
            {
                opt.addOmitedPath(new File(item));
                if (verboseLevel > 0)
                {
                    System.out.println("findrepe: excluded path '" + item + "'");
                }
            }
        }
        boolean useRegEx = regex.isUsed() && (!wildcard.isUsed() || regex.getLastUsed() > wildcard.getLastUsed());

        boolean fixBugs = bugFix.isUsed();
        boolean bugs = fixBugs || bug.isUsed();
        
        excludeRc(opt, excludeRc, excludeSvn, excludeCvs, excludeHg, verboseLevel);
        excludeDirAndFile(opt, excludeDirName, excludeFileName);
        focusPathDirFile(opt, focusPath, focusDirName, focusFileName, useRegEx);
        dirFile(opt, dirName, fileName, useRegEx);

        // ignore groups of 1 unless it specified by options
        opt.setMinCount(2);
        countFilter(opt, unique, count, minCount, maxCount, verboseLevel);

        if (old.isUsed())
        {
            FindRepe findTask = new FindRepe(files, bugs, queueSize, opt);
            new Thread(findTask).start();

            if (bugs)
            {
                showBugs(findTask.getBugIterable(), fixBugs);
            }
            showGroups(opt, findTask.getGroupsIterable(), delete.isUsed(), (optSize.isUsed()?sizeParser:null) );
        }
        else
        {
            ActorPool.setKeepAliveTime(1234);

            FindRepePipe findTask = new FindRepePipe(files, bugs, queueSize, opt);
            new Thread(findTask).start();

            if (bugs)
            {
                showBugs(findTask.getBugIterable(), fixBugs);
            }
            showGroups(opt, findTask.getGroupsIterable(), delete.isUsed(), (optSize.isUsed()?sizeParser:null) );
        }
    }

    private static void excludeDirAndFile(ForEachFileOptions opt, ArrayStringOption excludeDirName, ArrayStringOption excludeFileName)
    {
        if (excludeDirName.isUsed())
        {
            String[] paths = excludeDirName.getValues();
            for (String item : paths)
            {
                opt.addOmitedDirName(item);
            }
        }
        if (excludeFileName.isUsed())
        {
            String[] paths = excludeFileName.getValues();
            for (String item : paths)
            {
                opt.addOmitedFileName(item);
            }
        }
    }

    private static void focusPathDirFile(FindRepeOptions opt, ArrayStringOption focusPath, ArrayStringOption focusDirName, ArrayStringOption focusFileName, boolean useRegEx)
    {
        if (focusPath.isUsed())
        {
            String[] paths = focusPath.getValues();
            for (String item : paths)
            {
                opt.addFocusPath(item);
            }
        }
        if (focusDirName.isUsed())
        {
            String[] dirName = focusDirName.getValues();
            for (String item : dirName)
            {
                opt.addFocusDir(item, !useRegEx);
            }
        }
        if (focusFileName.isUsed())
        {
            String[] fileName = focusFileName.getValues();
            for (String item : fileName)
            {
                opt.addFocusFile(item, !useRegEx);
            }
        }
    }
    private static void dirFile(FindRepeOptions opt, ArrayStringOption dirName, ArrayStringOption fileName, boolean useRegEx)
    {
        if (dirName.isUsed())
        {
            String[] names = dirName.getValues();
            for (String item : names)
            {
                opt.addDirName(item, !useRegEx);
            }
        }
        if (fileName.isUsed())
        {
            String[] names = fileName.getValues();
            for (String item : names)
            {
                opt.addFileName(item, !useRegEx);
            }
        }
    }

    private static void excludeRc(ForEachFileOptions opt, BooleanOption excludeRc, BooleanOption excludeSvn, BooleanOption excludeCvs, BooleanOption excludeHg, int verboseLevel)
    {
        if (excludeRc.isUsed() || excludeSvn.isUsed())
        {
            opt.addOmitedDirName(".svn");
            if (verboseLevel > 0)
            {
                System.out.println("findrepe: excluded subversion files");
            }

        }
        if (excludeRc.isUsed() || excludeCvs.isUsed())
        {
            opt.addOmitedDirName("CVS");
            if (verboseLevel > 0)
            {
                System.out.println("findrepe: excluded CVS files");
            }
        }
        if (excludeRc.isUsed() || excludeHg.isUsed())
        {
            opt.addOmitedDirName(".hg");
            opt.addOmitedFileName(".hgignore");
            if (verboseLevel > 0)
            {
                System.out.println("findrepe: excluded mercurial files");
            }
        }
    }

    private static void countFilter(FindRepeOptions opt, BooleanOption unique, StringOption count, StringOption minCount, StringOption maxCount, int verboseLevel)
    {
        int lastUsed = 0;
        if (unique.isUsed())
        {
            opt.setMinCount(1);
            opt.setMaxCount(1);
            lastUsed = unique.getLastUsed();
        }
        if (count.isUsed() && count.getLastUsed() > lastUsed)
        {
            int num = Integer.parseInt(count.getValue());
            if (num > 0)
            {
                opt.setMinCount(num);
                opt.setMaxCount(num);
            }
            lastUsed = count.getLastUsed();
        }
        if (minCount.isUsed() && minCount.getLastUsed() > lastUsed)
        {
            int num = Integer.parseInt(minCount.getValue());
            if (num > 0)
            {
                opt.setMinCount(num);
            }
        }
        if (maxCount.isUsed() && maxCount.getLastUsed() > lastUsed)
        {
            int num = Integer.parseInt(maxCount.getValue());
            if (num > 0)
            {
                opt.setMaxCount(num);
            }
        }
        if (verboseLevel > 0)
        {
            if (opt.getMinCount() == opt.getMaxCount())
            {
                System.out.println("findrepe: exactly " + opt.getMinCount() + " occurrence" + ((opt.getMinCount() == 1) ? "" : "s"));
            } else if (opt.getMaxCount() != Integer.MAX_VALUE)
            {
                System.out.println("findrepe: between " + opt.getMinCount() + " and " + opt.getMaxCount() + " occurrences");
            } else if (opt.getMinCount() != 2)
            {
                System.out.println("findrepe: at least " + opt.getMinCount() + " occurrence" + ((opt.getMinCount() == 1) ? "" : "s"));
            }
        }

    }

    private static void showBugs(Iterable<File> bugList, boolean fix)
    {
        //METER NUEVA OPCIÓN Y COLA PARA MOSTRAR LOS LINKS SIN DESTINO
        //        EVITANDO ASÍ SUPERPONERSE CON LAS PREGUNTAS

        NormalizeFile normalize= new NormalizeFile();
        try
        {
            Scanner sc = new Scanner(System.in);
            for (File bug : bugList)
            {
                System.out.println("bug:" + bug.toString());
                if(fix)
                {
                    System.out.println("fix?[y/N]");
                    String line = sc.nextLine();
                    if (line.equalsIgnoreCase("y"))
                    {
                        try
                        {
                            String name = normalize.normalize(bug);
                            System.out.println("fixed: "+name);
                        }
                        catch (IOException ex)
                        {
                            Logger.getLogger(FindRepeMain.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
        finally
        {
            try
            {
                normalize.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(FindRepeMain.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(FindRepeMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static void showGroups(FindRepeOptions opt, Iterable<File[]> groupsList, boolean delete, SizeUnits units)
    {
        int groupId = 0;

        for (File[] group : groupsList)
        {
            if (group.length >= opt.getMinCount() && group.length <= opt.getMaxCount())
            {
                showOneGroup(groupId, group, delete,units);
                groupId++;
            }
        }
    }

    private static void showOneGroup(int groupId, File files[], boolean delete, SizeUnits units)
    {
        boolean[] deleted = new boolean[files.length];
        Arrays.fill(deleted, false);
        Scanner sc = new Scanner(System.in);
        while (true)
        {
            System.out.println();
            for (int i = 0; i < files.length; i++)
            {
                String id = deleted[i]? "-" : (files[i].canWrite()?""+i:"r");
                String size = units==null? "":units.toString(files[i].length(),true);
                System.out.printf("[%s]%s %s\n", id, size, files[i].toString());
            }
            if (!delete)
            {
                return;
            }

            System.out.printf("\nGroup %d, delete files [0 - %d, all, none]: ", groupId, files.length - 1);
            String line = sc.nextLine();
            if (line.length() == 0)
            {
                break;
            }
            if(line.trim().equalsIgnoreCase("all"))
            {
                for(int i=0;i<deleted.length;i++)
                {
                    deleted[i] = true;
                }
            }
            else if(line.trim().equalsIgnoreCase("none"))
            {
                for(int i=0;i<deleted.length;i++)
                {
                    deleted[i] = false;
                }
            }
            else
            {
                Scanner scLine = new Scanner(line);

                while (scLine.hasNextInt())
                {
                    int index = scLine.nextInt();
                    if (index >= 0 && index < files.length)
                    {
                        deleted[index] = true;
                    }
                    else
                    {
                        System.out.printf("%d ignored\n", index);
                    }
                }
            }
        }
        System.out.println();
        int deletedCount = 0;
        int notDeletedCount =0;
        for (int i = 0; i < files.length; i++)
        {
            boolean notdeleted=false;
            if (deleted[i])
            {
                if(!files[i].delete())
                {
                    notdeleted = true;
                    notDeletedCount++;
                }
                else
                {
                    deletedCount++;
                }
            }
            System.out.printf("  [%s] %s\n", (notdeleted?"e":(deleted[i] ? "-" : "+")), files[i].toString());
        }
        if (deletedCount > 0)
        {
            System.out.printf("\n  files deleted: %d\n\n", deletedCount);
        }
        if (notDeletedCount > 0)
        {
            System.out.printf("\n  files not deleted: %d\n\n", notDeletedCount);
        }

    }
}
