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

import com.softenido.cafe.util.options.BooleanOption;
import com.softenido.cafe.util.options.InvalidOptionException;
import com.softenido.cafe.util.options.Option;
import com.softenido.cafe.util.options.OptionParser;
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
            "findrepe, a repeated file finder. Version 0.0.2 alfa\n" +
            "Copyright (C) 2009  Francisco Gómez Carrasco\n";
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
            "Report bugs to <flikxxi@gmail.com>.\n";
    private static final String HELP =
            VERSION +
            "\n" +
            "    usage: findrepe [flags and directories in any order]\n" +
            "\n" +
            "   -h --help     print this message\n" +
            "   -L --license  display software license\n" +
            "   -v --version  display software version\n" +
            "   -d --delete   prompt user for files to delete\n" +
            "   -n --noempty  exclude zero-length files from consideration\n" +
            //            " -r --recurse     \tinclude files residing in subdirectories\n" +
            //            " -s --symlinks    \tfollow symlinks\n" +
            //            " -H --hardlinks   \tnormally, when two or more files point to the same\n" +
            //            "                  \tdisk area they are treated as non-duplicates; this\n" +
            //            "                  \toption will change this behavior\n" +

            //            " -f --omitfirst   \tomit the first file in each set of matches\n" +
            //            " -1 --sameline    \tlist each set of matches on a single line\n" +
            //            " -S --size        \tshow size of duplicate files\n" +
            //            " -q --quiet       \thide progress indicator\n" +
            //            "                  \tothers; important: under particular circumstances,\n" +
            //            "                  \tdata may be lost when using this option together\n" +
            //            "                  \twith -s or --symlinks, or when specifying a\n" +
            //            "                  \tparticular directory more than once; refer to the\n" +
            //            "                  \tfdupes documentation for additional information\n" +
            "\n" +
            "Report bugs to <flikxxi@gmail.com>.\n";

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
        int queueSize = 100;

        OptionParser options = new OptionParser();

        Option help = options.add(new BooleanOption('h', "help"));
        Option version = options.add(new BooleanOption('v', "version"));
        Option license = options.add(new BooleanOption('L', "license"));
        BooleanOption delete = options.add(new BooleanOption('d', "delete"));
        BooleanOption noempty = options.add(new BooleanOption('n', "noempty"));

//        StringOption recurse = options.add(new StringOption('r', "recurse"));
//        StringOption symlinks = options.add(new StringOption("s", "symlinks"));
//        StringOption hardlinks = options.add(new StringOption("H", "hardlinks"));

//        StringOption omitfirst = options.add(new StringOption("f", "omitfirst"));
//        StringOption sameline = options.add(new StringOption("1", "sameline"));
//        StringOption size = options.add(new StringOption("S", "size"));
//        StringOption quiet = options.add(new StringOption("q", "quiet"));

        String[] fileNames;
        try
        {
            fileNames = options.parse(args);
        }
        catch (InvalidOptionException ex)
        {
            System.err.println(ex);
            System.err.println("Try --help for more information");
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

        File[] files = new File[fileNames.length];
        for (int i = 0; i < files.length; i++)
        {
            files[i] = new File(fileNames[i]);
        }
        FindRepe findTask = new FindRepe(files, bugs, queueSize);
        if (noempty.isUsed())
        {
            findTask.setMinSize(1);
        }

        new Thread(findTask).start();

        if (bugs)
        {
            showBugs(findTask.getBugIterable(), fixBugs);
        }
        showGroups(findTask.getGroupsIterable(), delete.isUsed());
    }

    private static void showBugs(Iterable<File> bugList, boolean fix)
    {
        for (File bug : bugList)
        {
            System.out.println("bug:" + bug.toString());
        }
    }

    private static void showGroups(Iterable<File[]> groupsList, boolean delete)
    {
        int groupId = 0;
        // se obtienen listas de ficheros del mismo tamaño y se calcula el MD5 de 1KB
        for (File[] group : groupsList)
        {
            if (group.length > 1)
            {
                showOneGroup(groupId,group, delete);
                groupId++;
            }
        }
    }

    private static void showOneGroup(int groupId,File files[], boolean delete)
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

            System.out.printf("\nGroup %d, delete files [0 - %d]: ", groupId,files.length-1);
            String line = sc.nextLine();
            if (line.isEmpty())
            {
                break;
            }
            Scanner scLine = new Scanner(line);

            while(scLine.hasNextInt())
            {
                int index = scLine.nextInt();
                if (index >= 0 && index<files.length)
                {
                    deleted[index] = true;
                }
                else
                {
                    System.out.printf("%d ignored\n",index);
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
        if(deletedCount>0)
        {
            System.out.printf("\n  files deleted: %d\n\n",deletedCount);
        }
    }
}
