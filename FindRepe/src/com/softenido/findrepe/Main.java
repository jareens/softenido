/*
 *  Main.java
 *
 *  Copyright (C) 2009  Francisco Gómez Carrasco
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
import com.softenido.cafe.util.SizeUnits;
import com.softenido.cafe.util.options.BooleanOption;
import com.softenido.cafe.util.options.InvalidOptionException;
import com.softenido.cafe.util.options.Option;
import com.softenido.cafe.util.options.OptionParser;
import com.softenido.cafe.util.options.StringOption;
import com.softenido.cafe.util.launcher.LauncherBuilder;
import com.softenido.cafe.util.options.ArrayStringOption;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author franci
 */
public class Main
{

    private static final String VERSION =
            "findrepe  version 0.6.0 beta  (2009-06-27)\n" +
            "Copyright (C) 2009 by Francisco Gómez Carrasco\n" +
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
            "findrepe comes with ABSOLUTELY NO WARRANTY. This is free software, and you\n" +
            "are welcome to redistribute it under certain conditions. See the GNU\n" +
            "General Public Licence version 3 for details.\n" +
            "\n" +
            "findrepe searches the given path for repeated files by content (not name). Such\n" +
            "files are found by comparing file sizes and MD5+SHA1 signatures.\n" +
            "\n" +
            "usage: findrepe [options] [directories]\n" +
            "       java -jar FindRepe.jar [options] [directories]\n" +
            "\n" +
            "Options:\n" +
            " -v, --verbose               increase verbosity\n" +
            " -L, --license               display software license\n" +
            " -d, --delete                prompt user for files to delete\n" +
            " -n, --noempty               exclude zero-length files\n" +
            " -s, --symlinks              follow symlinks\n" +
            " -m, --min-size=size         minimum file size[bkmgt], exclude shorters\n" +
            " -M, --max-size=size         maximun file size[bkmgt], exclude largers\n" +
            "     --install               install a launcher\n" +
            "     --install-java[=path]   install a launcher using 'java' command\n" +
            "     --install-home[=path]   install a launcher using 'java.home' property\n" +
            
            "     --unique                list only unique files (--count=1)\n" +
            "     --count=N               list files repeated N times  \n" +
            "     --min-count=N           files repated at least N times\n" +
            "     --max-count=N           files repated no more than N times\n" +

            "     --noautoexclude         don't autoexclude some paths (/dev, /proc, ...)\n" +
            "     --exclude=path          don't follow path\n" +

            "     --exclude-rc            ignore revision control directories\n" +
            "     --exclude-svn           ignore subversion (.svn)\n" +
            "     --exclude-cvs           ignore cvs (CVS)\n" +
            "     --exclude-hg            ignore mercurial (.hg and .hgignore)\n" +

            "     --exclude-dir=name      don't follow directories named name\n" +
            "     --exclude-file=name     ignore files named name\n" +

            "     --version               print version number\n" +
            "     --examples              print some useful examples\n" +
            "(-h) --help                  show this help (-h works with no other options)\n" +
            //            "  -w --min-wasted=size minimun wasted size, exclude shorters wasted=size*(n-1)\n" +
            "\n" +
            "size units:\n" +
            " 1=1b, 1k=1024b, 1m=1024k, 1g=1024m, 1t=1024g\n" +
            //            " -r --recurse     \tinclude files residing in subdirectories\n" +
            //            " -H --hardlinks   \tnormally, when two or more files point to the same\n" +
            //            "                  \tdisk area they are treated as non-duplicates; this\n" +
            //            "                  \toption will change this behavior\n" +

            //            " -f --omitfirst   \tomit the first file in each set of matches\n" +
            //            " -1 --sameline    \tlist each set of matches on a single line\n" +
            //            " -S --size        \tshow size of duplicate files\n" +
            "\n" +
            REPORT_BUGS;

    private static final String EXAMPLES =
            VERSION+
            "\n"+
            "examples of findrepe usage:\n" +
            "\n" +
            " java -jar FindRepe.jar --install\n" +
            " sudo java -jar FindRepe.jar --install\n" +
            " sudo /opt/jdk1.6/bin/java -jar FindRepe.jar --install-home\n" +
            " findrepe backup\n" +
            " findrepe -d backup\n" +
            " findrepe -d --min-size=1m c:\\backup e:\\img\n" +
            " findrepe -nd c:\\backup e:\\img\n" +
            " findrepe -n /opt/ /backup/tools \n" +
            "\n" +
            " send me yours to: <flikxxi@gmail.com>\n";


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
        boolean bugs = true;
        boolean fixBugs = false;
        int queueSize = 10000;

        SizeUnits sizeParser = new SizeUnits();

        OptionParser options = new OptionParser();

        BooleanOption verbose = options.add(new BooleanOption('v', "verbose"));

        Option license = options.add(new BooleanOption('L', "license"));
        BooleanOption delete = options.add(new BooleanOption('d', "delete"));
        BooleanOption noempty = options.add(new BooleanOption('n', "noempty"));

        StringOption minSize = options.add(new StringOption('m', "min-size"));
        StringOption maxSize = options.add(new StringOption('M', "max-size"));

        BooleanOption unique  = options.add(new BooleanOption("unique"));
        StringOption count    = options.add(new StringOption("count"));
        StringOption minCount = options.add(new StringOption("min-count"));
        StringOption maxCount = options.add(new StringOption("max-count"));

        BooleanOption excludeRc  = options.add(new BooleanOption("exclude-rc"));
        BooleanOption excludeSvn = options.add(new BooleanOption("exclude-svn"));
        BooleanOption excludeCvs = options.add(new BooleanOption("exclude-cvs"));
        BooleanOption excludeHg  = options.add(new BooleanOption("exclude-hg"));

        ArrayStringOption excludeDirName  = options.add(new ArrayStringOption("exclude-dir",File.pathSeparatorChar));
        ArrayStringOption excludeFileName = options.add(new ArrayStringOption("exclude-file",File.pathSeparatorChar));

        BooleanOption noautoexclude  = options.add(new BooleanOption("noautoexclude"));
        ArrayStringOption exclude    = options.add(new ArrayStringOption("exclude",File.pathSeparatorChar));
  
        BooleanOption symlinks = options.add(new BooleanOption('s', "symlinks"));

        Option version = options.add(new BooleanOption("version"));
        Option help = options.add(new BooleanOption('h', "help"));

        BooleanOption examples = options.add(new BooleanOption("examples"));


//        StringOption minWasted = options.add(new StringOption('w', "min-wasted"));

//        StringOption recurse = options.add(new StringOption('r', "recurse"));
//        StringOption hardlinks = options.add(new StringOption("H", "hardlinks"));

//        StringOption omitfirst = options.add(new StringOption("f", "omitfirst"));
//        StringOption sameline = options.add(new StringOption("1", "sameline"));
//        StringOption size = options.add(new StringOption("S", "size"));
//        StringOption quiet = options.add(new StringOption("q", "quiet"));

        String[] fileNames;
        try
        {
            LauncherBuilder builder = LauncherBuilder.getBuilder();
            args = builder.parse(args);
            if (builder.isInstall())
            {
                if (builder.buildLauncher("findrepe"))
                {
                    System.out.println("findrepe: '" + builder.getFileName() + "' created");
                }
                return;
            }
            fileNames = options.parse(args);
        }
        catch (InvalidOptionException ex)
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

        String optName = null;
        String optVal = null;
        boolean minSizeUsed = false;
        boolean maxSizeUsed = false;
        long minSizeValue = 0;
        long maxSizeValue = Long.MAX_VALUE;
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
        }
        catch (NumberFormatException ex)
        {
            if (optVal == null)
            {
                System.err.println("findrepe: missing argument to '" + optName + "'");
            }
            else
            {
                System.err.println("findrepe: invalid argument '" + optVal + "' to '" + optName + "'");
            }
            return;
        }
//        if(minWasted.isUsed())
//        {
//            long size = sizeParser.parse(minWasted.getValue());
//            findTask.setMinWasted(size);
//        }

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
        if(noautoexclude.isUsed())
        {
            opt.setAutoOmit(false);
        }
        if(exclude.isUsed())
        {
            String[] paths = exclude.getValues();
            for(String item : paths)
            {
                opt.addOmitedPath(new File(item));
            }
        }
        excludeRc(opt, excludeRc, excludeSvn, excludeCvs, excludeHg);
        excludeDirAndFile(opt, excludeDirName, excludeFileName);

        // ignore groups of 1 unless it specified by options
        opt.setMinCount(2);
        countFilter(opt, unique, count, minCount, maxCount);

        FindRepe findTask = new FindRepe(files, bugs, queueSize,opt);
        new Thread(findTask).start();

        if (bugs)
        {
            showBugs(findTask.getBugIterable(), fixBugs);
        }
        showGroups(opt, findTask.getGroupsIterable(), delete.isUsed());
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

    private static void excludeRc(ForEachFileOptions opt, BooleanOption excludeRc, BooleanOption excludeSvn, BooleanOption excludeCvs, BooleanOption excludeHg)
    {
        if (excludeRc.isUsed() || excludeSvn.isUsed())
        {
            opt.addOmitedDirName(".svn");
        }
        if (excludeRc.isUsed() || excludeCvs.isUsed())
        {
            opt.addOmitedDirName("CVS");
        }
        if (excludeRc.isUsed() || excludeHg.isUsed())
        {
            opt.addOmitedDirName(".hg");
            opt.addOmitedFileName(".hgignore");
        }
    }
    private static void countFilter(FindRepeOptions opt, BooleanOption unique, StringOption count, StringOption minCount, StringOption maxCount)
    {
        int lastUsed = 0;
        if( unique.isUsed() )
        {
            opt.setMinCount(1);
            opt.setMaxCount(1);
            lastUsed = unique.getLastUsed();
        }
        if(count.isUsed() && count.getLastUsed()>lastUsed)
        {
            int num = Integer.parseInt(count.getValue());
            if(num>0)
            {
                opt.setMinCount(num);
                opt.setMaxCount(num);
            }
            lastUsed = count.getLastUsed();
        }
        if(minCount.isUsed() && minCount.getLastUsed()>lastUsed)
        {
            int num = Integer.parseInt(minCount.getValue());
            if(num>0)
            {
                opt.setMinCount(num);
            }
        }
        if(maxCount.isUsed() && maxCount.getLastUsed()>lastUsed)
        {
            int num = Integer.parseInt(maxCount.getValue());
            if(num>0)
            {
                opt.setMaxCount(num);
            }
        }
    }

    private static void showBugs(Iterable<File> bugList, boolean fix)
    {
        for (File bug : bugList)
        {
            System.out.println("bug:" + bug.toString());
        }
    }

    private static void showGroups(FindRepeOptions opt, Iterable<File[]> groupsList, boolean delete)
    {
        int groupId = 0;

        for (File[] group : groupsList)
        {
            if (group.length >= opt.minCount && group.length <= opt.maxCount)
            {
                showOneGroup(groupId, group, delete);
                groupId++;
            }
        }
    }

    private static void showOneGroup(int groupId, File files[], boolean delete)
    {
        boolean[] deleted = new boolean[files.length];
        Arrays.fill(deleted, false);
        Scanner sc = new Scanner(System.in);
        while (true)
        {
            System.out.println();
            for (int i = 0; i < files.length; i++)
            {
                System.out.printf("[%s] %s\n", (deleted[i] ? "-" : "" + i), files[i].toString());
            }
            if (!delete)
            {
                return;
            }

            System.out.printf("\nGroup %d, delete files [0 - %d]: ", groupId, files.length - 1);
            String line = sc.nextLine();
            if (line.isEmpty())
            {
                break;
            }
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
        System.out.println();
        int deletedCount = 0;
        for (int i = 0; i < files.length; i++)
        {
            if (deleted[i])
            {
                files[i].delete();
                deletedCount++;
            }
            System.out.printf("  [%s] %s\n", (deleted[i] ? "-" : "+"), files[i].toString());
        }
        if (deletedCount > 0)
        {
            System.out.printf("\n  files deleted: %d\n\n", deletedCount);
        }
    }

}
