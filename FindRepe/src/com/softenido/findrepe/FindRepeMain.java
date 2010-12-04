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

import com.softenido.findrepe.showgroup.ImageShowGroup;
import com.softenido.findrepe.showgroup.ConsoleShowGroup;
import com.softenido.cafe.imageio.ImageFormat;
import com.softenido.cafe.io.Files;
import com.softenido.cafe.io.ForEachFileOptions;
import com.softenido.cafe.io.virtual.VirtualFile;
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
import com.softenido.findrepe.showgroup.ShowGroup;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
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
    private static final String VER = "0.12.0.rc";
    private static final String VERSION =
            "findrepe  version " + VER + " alpha (2010-12-04)\n"
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
            + " -V  --verbose-logger        format messages as a logger\n"
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
            + " -J  --jvm-option=jvm_option pass jvm_option to JVM(for --install...)\n"
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
            + "     --byiname               like --byname, but case insensitive\n"
            + "     --byimage               compares images like humans (bmp,gif,jpg,jpeg,png)\n"
            + " -e  --regex                 uses java regular expresions\n"
            + "     --wildcard              uses wildcards '*', '?' and '[]' (default)\n"
            + " -j  --jobs=N                limits thread use to N (0-1024, developers only)\n"
//            + " -E  --eager                 eager use of CPU\n"
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
            + " sudo java -jar FindRepe.jar --install -Jmx1g\n"
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
        BooleanOption verboseLogger = options.add(new BooleanOption('V',"verbose-logger"));
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
        BooleanOption optByIName = options.add(new BooleanOption("byiname"));
        BooleanOption optByImage = options.add(new BooleanOption("byimage"));
        ArrayStringOption dirName = options.add(new ArrayStringOption("dir", File.pathSeparatorChar));
        ArrayStringOption fileName = options.add(new ArrayStringOption("file", File.pathSeparatorChar));
        BooleanOption regex = options.add(new BooleanOption('e',"regex"));
        BooleanOption wildcard = options.add(new BooleanOption("wildcard"));
        BooleanOption bug = options.add(new BooleanOption("bug"));
        BooleanOption bugFix = options.add(new BooleanOption("bug-fix"));
        NumberOption opJobs = options.add(new NumberOption('j', "jobs"));
        NumberOption opEager = options.add(new NumberOption('E', "eager"));
        BooleanOption optLowMem = options.add(new BooleanOption("lowmem"));

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
//            if(optByHash.isUsed())
//            {
//                opt.setByHash(true);
//            }
//
//            if(optIByPath.isUsed()||optByPath.isUsed())
//            {
//                opt.setByName(true,0);
//                opt.setByNameIgnoreCase(optIByName.isUsed());
//            }
//            else
            if(optByIName.isUsed() || optByName.isUsed())
            {
                opt.setComparators
                (
                    new FileComparatorByName(true, optByIName.isUsed()),
                    new FileComparatorByName(false, optByIName.isUsed())
                );
            }
            else if(optByImage.isUsed())
            {
                //ImageIO.setUseCache(false);
                opt.setComparators
                (
                    new FileComparatorByImage(true, true,64,0.05f,0.10f,false),
                    new FileComparatorByImage(false,true,64,0.05f,0.10f,false)
                );
                opt.addAllowedFileName(ImageFormat.getImageFileFilter());
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
            opt.setEager(opEager.isUsed());
            FindRepePipe findTask = new FindRepePipe(files, bugs, queueSize, opt);
            if(optLowMem.isUsed())
            {
                findTask.setLowmem(true);
            }
            
            new Thread(findTask).start();

            if (bugs)
            {
                showBugs(findTask.getBugIterable(), fixBugs, vh);
            }
            showGroups(opt, findTask.getGroupsIterable(), delete.isUsed(), (optSize.isUsed() ? sizeParser : null), autoDeleteFiles, vh, optAbsPath.isUsed(), optByImage.isUsed());        }
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

    private static void showGroups(FindRepeOptions opt, Iterable<VirtualFile[]> groupsList, boolean delete, SizeUnits units, File[] autoDelete, VerboseHandler vh, boolean absPath, boolean byimage)
    {
        int groupId = 0;
        int deleteMin = opt.getMinCount();
        
        ShowGroup sg = (delete && byimage && !GraphicsEnvironment.isHeadless())
                ? new ImageShowGroup(units, absPath, delete, deleteMin, autoDelete)
                : new ConsoleShowGroup(units, absPath, delete, deleteMin, autoDelete);

        for (VirtualFile[] group : groupsList)
        {
            if (group.length >= opt.getMinCount() && group.length <= opt.getMaxCount())
            {
                vh.flush();
                vh.lock();
                try
                {
                    VERIFY(group);
                    sg.showOneGroup(groupId, group);
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
        sg.close();
    }

    private static void VERIFY(VirtualFile[] group)
    {
        try
        {
            HashMap<String,VirtualFile> items = new HashMap<String,VirtualFile>(group.length);
            for (int i = 0; i < group.length; i++)
            {
                String a = group[i].getCanonicalPath();
                VirtualFile dup = items.put(a,group[i]);
                if(dup!=null)
                {
                    String b = dup.getCanonicalPath();
                    if(a.equals(b))
                    {
                        System.err.printf("YOU HAVE FOUND A BUG: THE SAME FILE REPORTED TWICE\n");
                        System.err.printf("A=%s\n",group[i].toString());
                        System.err.printf("B=%s\n",dup.toString());
                    }
                }
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(FindRepeMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
