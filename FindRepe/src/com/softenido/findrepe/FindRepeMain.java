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

import com.softenido.cafe.io.Files;
import com.softenido.cafe.io.ForEachFileOptions;
import com.softenido.cafe.io.packed.PackedFile;
import com.softenido.cafe.util.ArrayUtils;
import com.softenido.core.util.SizeUnits;
import com.softenido.cafe.util.VerboseHandler;
import com.softenido.cafe.util.concurrent.actor.Actor;
import com.softenido.cafe.util.concurrent.actor.ActorPool;
import com.softenido.cafe.util.options.BooleanOption;
import com.softenido.cafe.util.options.InvalidOptionException;
import com.softenido.cafe.util.options.InvalidOptionParameterException;
import com.softenido.cafe.util.options.MissingOptionParameterException;
import com.softenido.cafe.util.options.OptionParser;
import com.softenido.cafe.util.launcher.LauncherParser;
import com.softenido.cafe.util.options.ArrayStringOption;
import com.softenido.cafe.util.options.NumberOption;
import com.softenido.cafe.util.options.SizeOption;
import com.softenido.cafe.util.options.StringOption;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author franci
 */
public class FindRepeMain
{

    private static final String FINDREPE = "findrepe";
    private static final String VER = "0.11.0";
    private static final String VERSION =
            "findrepe  version " + VER + " alpha (2010-08-26)\n"
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
            + "     --verbose-logger        format messages as a logger\n"
            + " -L, --license               display software license\n"
            + " -d, --delete                prompt user for files to delete\n"
            + "     --delete-auto=path      smart auto-selection of files for deletion\n"
            + " -n, --noempty               exclude zero-length files\n"
            + " -s, --symlinks              follow symlinks\n"
            + " -m, --min-size=size         exclude files shorter than size[bkmgt]\n"
            + " -M, --max-size=size         exclude files larger than size[bkmgt]\n"
            + " -w  --min-wasted=size       minimum wasted size[bkmgt] copies, size*(n-1)\n"
            + " -S  --size                  show size[bkmgt] of files\n"
            + " -H  --nohide                ignore hide entries\n"
            + " -A  --absolute-path         show absolute path of files\n"
            + "     --install               install a launcher\n"
            + "     --install-java[=path]   install a launcher using 'java' command\n"
            + "     --install-home[=path]   install a launcher using 'java.home' property\n"
            + "     --install-posix         posix flavor for install options when unknown\n"
            + "     --install-version       adds version to launcher name\n"
            + "     --unique                list only unique files (--count=1)\n"
            + "     --count=N               list files repeated N times  \n"
            + " -c  --min-count=N           files repeated at least N times\n"
            + " -C  --max-count=N           files repeated no more than N times\n"
            + "     --noautoexclude         don't autoexclude some paths (/dev, /proc, ...)\n"
            + "     --exclude=path          don't follow path\n"
            + "     --exclude-dir=pattern   don't follow directories matching pattern\n"
            + "     --exclude-file=pattern  ignore files matching pattern\n"
            + "     --exclude-rc            ignore revision control directories\n"
            + "     --exclude-svn           ignore subversion (.svn)\n"
            + "     --exclude-cvs           ignore cvs (CVS)\n"
            + "     --exclude-hg            ignore mercurial (.hg and .hgignore)\n"
            + " -f  --focus=path            focus on files in path\n"
            + "     --focus-dir=pattern     focus on directories matching pattern\n"
            + "     --focus-file=pattern    focus on files matching pattern\n"
            + "     --dir=pattern           only directories matching pattern\n"
            + "     --file=pattern          only files matching pattern\n"
            + " -z  --zip                   recurse into zip files (zip, jar, ...)(ALPHA)\n"
            + " -Z  --zip-only              exclude files not added by option --zip\n"
            //+ "     --diff=difftool         uses difftool with diff command\n"
            + "     --byname                compares by file name not content\n"
            + "     --ibyname               like --byname, but case insensitive\n"
            + " -e  --regex                 uses java regular expresions\n"
            + "     --wildcard              uses wildcards '*', '?' and '[]' (default)\n"
            + " -j  --jobs=N                limits thread use to N (0-1024, developers only)\n"
            + "     --bug                   show filenames with bugs\n"
            + "     --bug-fix               try to fix filenames with bugs\n"
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
            + " findrepe -vn /opt/ /backup/tools \n"
            + " findrepe -vvn /opt/ --exclude=/opt/nb6.7" + File.pathSeparator + "/opt/nb6.8\n"
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
        int verboseLevel = 0;

        SizeUnits sizeParser = new SizeUnits();
        OptionParser options = new OptionParser();

        BooleanOption verbose = options.add(new BooleanOption('v', "verbose"));
        BooleanOption verboseLogger = options.add(new BooleanOption("verbose-logger"));
        BooleanOption license = options.add(new BooleanOption('L', "license"));
        BooleanOption delete = options.add(new BooleanOption('d', "delete"));
        ArrayStringOption deleteAuto = options.add(new ArrayStringOption("delete-auto", File.pathSeparatorChar));
        BooleanOption noempty = options.add(new BooleanOption('n', "noempty"));
        BooleanOption symlinks = options.add(new BooleanOption('s', "symlinks"));

        SizeOption minSize = options.add(new SizeOption('m', "min-size"));
        SizeOption maxSize = options.add(new SizeOption('M', "max-size"));
        SizeOption minWasted = options.add(new SizeOption('w', "min-wasted"));
        BooleanOption optSize = options.add(new BooleanOption('S', "size"));
        BooleanOption optNoHide = options.add(new BooleanOption('H', "nohide"));
        BooleanOption optAbsPath = options.add(new BooleanOption('A', "absolute-path"));

        BooleanOption unique = options.add(new BooleanOption("unique"));
        NumberOption count = options.add(new NumberOption("count"));
        NumberOption minCount = options.add(new NumberOption('c', "min-count"));
        NumberOption maxCount = options.add(new NumberOption('C', "max-count"));

        BooleanOption excludeRc = options.add(new BooleanOption("exclude-rc"));
        BooleanOption excludeSvn = options.add(new BooleanOption("exclude-svn"));
        BooleanOption excludeCvs = options.add(new BooleanOption("exclude-cvs"));
        BooleanOption excludeHg = options.add(new BooleanOption("exclude-hg"));

        ArrayStringOption excludeDirName = options.add(new ArrayStringOption("exclude-dir", File.pathSeparatorChar));
        ArrayStringOption excludeFileName = options.add(new ArrayStringOption("exclude-file", File.pathSeparatorChar));

        BooleanOption noautoexclude = options.add(new BooleanOption("noautoexclude"));
        ArrayStringOption exclude = options.add(new ArrayStringOption("exclude", File.pathSeparatorChar));

        ArrayStringOption focusPath = options.add(new ArrayStringOption('f', "focus", File.pathSeparatorChar));
        ArrayStringOption focusDirName = options.add(new ArrayStringOption("focus-dir", File.pathSeparatorChar));
        ArrayStringOption focusFileName = options.add(new ArrayStringOption("focus-file", File.pathSeparatorChar));
        BooleanOption optZip = options.add(new BooleanOption('z', "zip"));
        BooleanOption optZipOnly = options.add(new BooleanOption('Z', "zip-only"));

        BooleanOption optByName = options.add(new BooleanOption("byname"));
        BooleanOption optIByName = options.add(new BooleanOption("ibyname"));
        StringOption optDiff = options.add(new StringOption("diff"));

        ArrayStringOption dirName = options.add(new ArrayStringOption("dir", File.pathSeparatorChar));
        ArrayStringOption fileName = options.add(new ArrayStringOption("file", File.pathSeparatorChar));
        BooleanOption regex = options.add(new BooleanOption("regex"));
        BooleanOption wildcard = options.add(new BooleanOption("wildcard"));
        BooleanOption bug = options.add(new BooleanOption("bug"));
        BooleanOption bugFix = options.add(new BooleanOption("bug-fix"));
        NumberOption opJobs = options.add(new NumberOption('j', "jobs"));

        BooleanOption version = options.add(new BooleanOption("version"));
        BooleanOption help = options.add(new BooleanOption('h', "help"));
        BooleanOption examples = options.add(new BooleanOption("examples"));

        String[] fileNames;
        try
        {
            args = LauncherParser.parseInstall(FINDREPE, null, VER, args);
            if (args == null)
            {
                return;
            }
            fileNames = options.parse(args);
        }
        catch (InvalidOptionException ex)
        {
            System.err.println(ex);
            System.err.println(FINDREPE + ": Try --help for more information");
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

        VerboseHandler vh = verboseLogger.isUsed() ? new VerboseHandler(System.err, new SimpleFormatter()) : new VerboseHandler(System.err, "findrepe: ");
        VerboseHandler.register(verboseLevel, vh, ConsoleHandler.class);

        Logger logger = Logger.getLogger(FindRepeMain.class.getName());
        if (logger.isLoggable(Level.CONFIG))
        {
            logger.log(Level.CONFIG, "{0}.version={1}", new String[]
                    {
                        FINDREPE, VER
                    });
            logger.log(Level.CONFIG, "logger.level={0}", vh.getLevel().getName());
            options.log();
            vh.flush();
        }

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
                minSizeValue = minSize.longValue();
                minSizeUsed = true;
            }
            if (maxSize.isUsed())
            {
                maxSizeValue = maxSize.longValue();
                maxSizeUsed = true;
            }
            if (minWasted.isUsed())
            {
                minWastedValue = minWasted.longValue();
                minWastedUsed = true;
            }

            if (fileNames.length == 0)
            {
                System.err.println(FINDREPE + ": no directories specified");
                return;
            }

            File[] files = Files.toFileArray(fileNames);
            File[] autoDeleteFiles = new File[0];
            if (deleteAuto.isUsed())
            {
                autoDeleteFiles = Files.getAbsoluteFile(Files.toFileArray(deleteAuto.getValues()));
                if (autoDeleteFiles.length > 0)
                {
                    files = ArrayUtils.cat(files, autoDeleteFiles);
                }
            }

            FindRepeOptions opt = new FindRepeOptions();

            opt.setHidden(!optNoHide.isUsed());
            opt.setSymlinks(symlinks.isUsed());

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
                }
            }
            boolean useRegEx = regex.isUsed() && (!wildcard.isUsed() || regex.getLastUsed() > wildcard.getLastUsed());

            boolean fixBugs = bugFix.isUsed();
            boolean bugs = fixBugs || bug.isUsed();

            excludeRc(opt, excludeRc, excludeSvn, excludeCvs, excludeHg, verboseLevel);
            excludeDirAndFile(opt, excludeDirName, excludeFileName, useRegEx);
            focusPathDirFile(opt, focusPath, focusDirName, focusFileName, useRegEx);
            dirFile(opt, dirName, fileName, useRegEx);

            if (optZip.isUsed())
            {
                opt.setZip(true);
                opt.setJar(true);
            }
            if (optZipOnly.isUsed())
            {
                opt.setZip(true);
                opt.setJar(true);
                opt.setOnlyPacked(true);
            }
            if(optIByName.isUsed()||optByName.isUsed())
            {
                opt.setByName(true);
                opt.setByNameIgnoreCase(optIByName.isUsed());
            }
            String diff=null;
            if(optDiff.isUsed())
            {
                diff = optDiff.getValue();
            }

            // ignore groups of 1 unless it specified by options
            opt.setMinCount(2);
            countFilter(opt, unique, count, minCount, maxCount, verboseLevel);

            if (logger.isLoggable(Level.CONFIG))
            {
                for (int i = 0; i < fileNames.length; i++)
                {
                    logger.log(Level.CONFIG, "paths[{0}]={1}", new Object[]{i, fileNames[i]});
                }
                vh.flush();
            }

            if (opJobs.isUsed())
            {
                int jobs = opJobs.intValue();
                jobs = Math.min(jobs, 1024);
                if (jobs <= 0)
                {
                    Actor.setForceSync(true);
                }
                else
                {
                    ActorPool.setDefaultPoolSize(jobs);
                }
            }

            FindRepePipe findTask = new FindRepePipe(files, bugs, queueSize, opt);
            new Thread(findTask).start();

            if (bugs)
            {
                showBugs(findTask.getBugIterable(), fixBugs, vh);
            }
            showGroups(opt, findTask.getGroupsIterable(), delete.isUsed(), (optSize.isUsed() ? sizeParser : null), autoDeleteFiles, vh, optAbsPath.isUsed(), diff);
        }
        catch (MissingOptionParameterException ex)
        {
            System.err.println(FINDREPE + ":" + ex.getMessage());
        }
        catch (InvalidOptionParameterException ex)
        {
            System.err.println(FINDREPE + ":" + ex.getMessage());
        }
        catch (NumberFormatException ex)
        {
            System.err.println(FINDREPE + ":" + ex.getMessage());
        }

    }

    private static void excludeDirAndFile(ForEachFileOptions opt, ArrayStringOption excludeDirName, ArrayStringOption excludeFileName, boolean useRegEx)
    {
        if (excludeDirName.isUsed())
        {
            String[] paths = excludeDirName.getValues();
            for (String item : paths)
            {
                opt.addOmitedDirName(item, !useRegEx);
            }
        }
        if (excludeFileName.isUsed())
        {
            String[] paths = excludeFileName.getValues();
            for (String item : paths)
            {
                opt.addOmitedFileName(item, !useRegEx);
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
                System.out.println(FINDREPE + ": excluded subversion files");
            }

        }
        if (excludeRc.isUsed() || excludeCvs.isUsed())
        {
            opt.addOmitedDirName("CVS");
            if (verboseLevel > 0)
            {
                System.out.println(FINDREPE + ": excluded CVS files");
            }
        }
        if (excludeRc.isUsed() || excludeHg.isUsed())
        {
            opt.addOmitedDirName(".hg");
            opt.addOmitedFileName(".hgignore");
            if (verboseLevel > 0)
            {
                System.out.println(FINDREPE + ": excluded mercurial files");
            }
        }
    }

    private static void countFilter(FindRepeOptions opt, BooleanOption unique, NumberOption count, NumberOption minCount, NumberOption maxCount, int verboseLevel) throws MissingOptionParameterException, InvalidOptionParameterException
    {
        int lastUsed = -1;
        if (unique.isUsed())
        {
            opt.setMinCount(1);
            opt.setMaxCount(1);
            lastUsed = unique.getLastUsed();
        }
        if (count.isUsed() && count.getLastUsed() > lastUsed)
        {
            int num = count.intValue();
            if (num > 0)
            {
                opt.setMinCount(num);
                opt.setMaxCount(num);
            }
            lastUsed = count.getLastUsed();
        }
        if (minCount.isUsed() && minCount.getLastUsed() > lastUsed)
        {
            int num = minCount.intValue();
            if (num > 0)
            {
                opt.setMinCount(num);
            }
        }
        if (maxCount.isUsed() && maxCount.getLastUsed() > lastUsed)
        {
            int num = maxCount.intValue();
            if (num > 0)
            {
                opt.setMaxCount(num);
            }
        }
        if (verboseLevel > 0)
        {
            if (opt.getMinCount() == opt.getMaxCount())
            {
                System.out.println(FINDREPE + ": exactly " + opt.getMinCount() + " occurrence" + ((opt.getMinCount() == 1) ? "" : "s"));
            }
            else if (opt.getMaxCount() != Integer.MAX_VALUE)
            {
                System.out.println(FINDREPE + ": between " + opt.getMinCount() + " and " + opt.getMaxCount() + " occurrences");
            }
            else if (opt.getMinCount() != 2)
            {
                System.out.println(FINDREPE + ": at least " + opt.getMinCount() + " occurrence" + ((opt.getMinCount() == 1) ? "" : "s"));
            }
        }

    }

    private static void showBugs(Iterable<File> bugList, boolean fix, VerboseHandler vh)
    {
        //METER NUEVA OPCIÓN Y COLA PARA MOSTRAR LOS LINKS SIN DESTINO
        //        EVITANDO ASÍ SUPERPONERSE CON LAS PREGUNTAS

        try
        {
            Scanner sc = new Scanner(System.in);
            for (File bug : bugList)
            {
                vh.flush();
                vh.lock();
                String line;
                try
                {
                    System.out.println();
                    System.out.println("bug:" + bug.toString());
                    if(!fix)
                        continue;
                    
                    System.out.print("fix?[y/N]");
                    line = sc.nextLine();
                }
                finally
                {
                    vh.unlock();
                }
                if (line.equalsIgnoreCase("y"))
                {
                    NormalizeFile normalize = new NormalizeFile(bug);
                    String name = normalize.normalize();
                    if(normalize.getValue()!=0)
                    {
                        Logger.getLogger(FindRepeMain.class.getName()).log(Level.SEVERE, "could not be fixed");
                        Logger.getLogger(FindRepeMain.class.getName()).log(Level.WARNING, normalize.getErrorMessage());
                    }
                    else
                    {
                        Logger.getLogger(FindRepeMain.class.getName()).log(Level.INFO, "fixed: {0}",name);
                    }
                }
            }
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(FindRepeMain.class.getName()).log(Level.SEVERE, null, ex);
        }                
        catch (IOException ex)
        {
            Logger.getLogger(FindRepeMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void showGroups(FindRepeOptions opt, Iterable<PackedFile[]> groupsList, boolean delete, SizeUnits units, File[] autoDelete, VerboseHandler vh, boolean absPath, String diff)
    {
        int groupId = 0;
        int deleteMin = opt.getMinCount();

        for (PackedFile[] group : groupsList)
        {
            if (group.length >= opt.getMinCount() && group.length <= opt.getMaxCount())
            {
                vh.flush();
                vh.lock();
                try
                {
                    VERIFY(group);
                    showOneGroup(groupId, group, delete, units, deleteMin, autoDelete, absPath,diff);
                }
                catch (IOException ex)
                {
                    Logger.getLogger(FindRepeMain.class.getName()).log(Level.SEVERE, null, ex);
                }
                finally
                {
                    vh.unlock();
                }
                groupId++;
            }
        }
    }

    private static void showOneGroup(int groupId, PackedFile files[], boolean delete, SizeUnits units, int deleteMin, File[] autoDelete, boolean absPath,String diff) throws IOException
    {
        boolean showResult = false;
        boolean[] deleted = new boolean[files.length];
        Arrays.fill(deleted, false);
        Scanner sc = new Scanner(System.in);

        if (delete && autoDelete.length > 0 && files.length > 1 && files.length >= deleteMin)
        {
            int matches[] = new int[files.length];
            Arrays.fill(matches, 0);
            for (int i = 0; i < files.length; i++)
            {
                for (int j = 0; j < autoDelete.length; j++)
                {
                    if(files[i].canWrite() && Files.isParentOf(autoDelete[j], files[i].getFile(),false))
                    {
                        matches[i]++;
                    }
                }
            }
            int sorted[] = Arrays.copyOf(matches, matches.length);
            Arrays.sort(sorted);
            int minVal = sorted[deleteMin - 2];
            for (int i = 0; i < matches.length; i++)
            {
                if( (matches[i]>minVal) && files[i].canWrite())
                {
                    showResult = true;
                    deleted[i] = true;
                }
            }
        }
        
        while (true)
        {
            System.out.flush();
            System.out.println();
            for (int i = 0; i < files.length; i++)
            {
                String id = deleted[i] ? "-" : (files[i].canWrite() ? "" + i : "r");
                String size = units == null ? "" : units.toString(files[i].length(), true);
                String name = (absPath?files[i].getAbsolutePath():files[i].toString());
                System.out.printf("[%s]%s %s\n", id, size, name);
            }
            if (!delete)
            {
                return;
            }
            String prompt = diff==null?"\nGroup %d, delete files [0 - %d, all, none]: ":
                                       "\nGroup %d, delete files [0 - %d, all, none, diff]: ";
            System.out.printf(prompt, groupId, files.length - 1);
            String line = sc.nextLine();
            if (line.length() == 0)
            {
                break;
            }
            if (line.trim().equalsIgnoreCase("all"))
            {
                for (int i = 0; i < deleted.length; i++)
                {
                    showResult = true;
                    deleted[i] = true;
                }
            }
            else if (line.trim().equalsIgnoreCase("none"))
            {
                for (int i = 0; i < deleted.length; i++)
                {
                    deleted[i] = false;
                }
            }
            else if(diff != null && line.trim().equalsIgnoreCase("diff"))
            {
                try
                {
                    execDiff(diff, files);
                }
                catch (InterruptedException ex)
                {
                    Logger.getLogger(FindRepeMain.class.getName()).log(Level.SEVERE, null, ex);
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
                        showResult = true;
                        deleted[index] = true;
                    }
                    else
                    {
                        System.out.printf("%d ignored\n", index);
                    }
                }
            }
        }
        if(showResult)
        {
            System.out.println();
            int deletedCount = 0;
            int notDeletedCount = 0;
            for (int i = 0; i < files.length; i++)
            {
                boolean notdeleted = false;
                if (deleted[i])
                {
                    if (!files[i].delete())
                    {
                        notdeleted = true;
                        notDeletedCount++;
                    }
                    else
                    {
                        deletedCount++;
                    }
                }
                System.out.printf("  [%s] %s\n", (notdeleted ? "e" : (deleted[i] ? "-" : "+")), files[i].toString());
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

    private static void VERIFY(PackedFile[] group)
    {
        try
        {
            for (int i = 0; i < group.length; i++)
            {
                String a = group[i].getFile().getCanonicalFile().toString();
                for (int j = i + 1; j < group.length; j++)
                {
                    String b = group[j].getFile().getCanonicalFile().toString();
                    if(a.equals(b))
                    {
                        System.err.printf("YOU HAVE FOUND A BUG: THE SAME FILE REPORTED TWICE\n");
                        System.err.printf("A=%s\n",a);
                        System.err.printf("B=%s\n",b);
                    }
                }
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(FindRepeMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static int execDiff(String diff,PackedFile[] files) throws IOException, InterruptedException
    {
        String[] cmd = new String[files.length+1];
        cmd[0]=diff;
        for(int i=0;i<files.length;i++)
        {
            cmd[i+1] = files[i].toString();
        }
        Process child = Runtime.getRuntime().exec(cmd);
        return child.waitFor();
    }
}
