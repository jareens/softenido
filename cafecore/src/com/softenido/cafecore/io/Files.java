/*
 *  Files.java
 *
 *  Copyright (C) 2007-2012 Francisco Gómez Carrasco
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
package com.softenido.cafecore.io;

import com.softenido.cafecore.util.ArrayUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 *
 * @author franci
 */
public class Files 
{
    enum Holder
    {
        INSTANCE;
        final String home = System.getProperty("user.home");
    }

    private static final String[][] escapeCharacters =
    {
        {
            "\\", "\\\\'"
        },
        {
            " ", "\\ "
        },
        {
            "\t", "\\\t"
        },
        {
            "\"", "\\\""
        },
        {
            "\'", "\\\'"
        },
        {
            "[", "\\["
        },
        {
            "]", "\\]"
        },
        {
            "(", "\\("
        },
        {
            ")", "\\)"
        },
        {
            "&", "\\&"
        },
    };
    public static File getHomeFile()
    {
        return new File(Holder.INSTANCE.home);
    }
    public static String getHomePath()
    {
        return Holder.INSTANCE.home;
    }
    
    private static boolean equals(File a, File b, boolean useCanonical) throws IOException
    {
        if (useCanonical)
        {
            a = a.getCanonicalFile();
            b = b.getCanonicalFile();
        }
        return a.equals(b);
    }

    // Copies src file to dst file.
    // If the dst file does not exist, it is created
    public static void copy(File src, File dst) throws IOException
    {
        InputStream in = new BufferedInputStream(new FileInputStream(src));
        try
        {
            OutputStream out = new BufferedOutputStream(new FileOutputStream(dst));
            try
            {
                // Transfer bytes from in to out
                byte[] buf = new byte[8192];
                int r;
                while ((r = in.read(buf)) > 0)
                {
                    out.write(buf, 0, r);
                }
            } finally
            {
                out.close();
            }
        } finally
        {
            in.close();
        }
    }

    public static boolean move(File src, File dst) throws IOException
    {
        if (src.renameTo(dst))
        {
            return true;
        }
        copy(src, dst);
        src.delete();
        return true;
    }

    public static String escape(String name)
    {
        for (String[] item : escapeCharacters)
        {
            if (!File.separator.equals(item[0]))
            {
                name = name.replace(item[0], item[1]);
            }
        }
        return name;
    }

    public static String wildcard(String name)
    {
        return name.replace('\uFFFD', '?');
    }

    public static String normalize(String name)
    {
        return name.replace('\uFFFD', '.');
    }

    public static String[] getParents(File file, boolean includeFile)
    {
        File[] items = getParentFiles(file, includeFile);
        String[] names = new String[items.length];
        for (int i = 0; i < items.length; i++)
        {
            names[i] = items[i].toString();
        }
        return names;
    }

    public static String[] getParents(File file)
    {
        return getParents(file, false);
    }

    public static File[] getParentFiles(File file, boolean includeFile)
    {
        File item = file;
        ArrayList<File> files = new ArrayList<File>();
        if (includeFile)
        {
            files.add(item);
        }
        while ((item = item.getParentFile()) != null)
        {
            files.add(item);
        }
        return ArrayUtils.reverseCopyOf(files.toArray(new File[0]));
    }

    public static File[] getParentFiles(File file)
    {
        return getParentFiles(file, false);
    }

    public static String getCommonParent(File a, File b)
    {
        File parent = getCommonParentFile(a, b);
        if (parent != null)
        {
            return parent.toString();
        }
        return null;
    }

    public static File getCommonParentFile(File a, File b)
    {
        File[] listA = getParentFiles(a, true);
        File[] listB = getParentFiles(b, true);
        int max = Math.min(listA.length, listB.length);
        File common = null;
        for (int i = 0; i < max && listA[i].equals(listB[i]); i++)
        {
            common = listA[i];
        }
        return common;
    }

    public static boolean haveCommonParent(File a, File b)
    {
        return (getCommonParentFile(a, b) != null);
    }

    public static boolean isParentOf(File parent, File child, boolean canonical) throws IOException
    {
        File pa = parent.getAbsoluteFile();
        File ca = child.getAbsoluteFile();
        if (pa.equals(getCommonParentFile(pa, ca)))
        {
            return true;
        }
        if (canonical)
        {
            File pc = parent.getCanonicalFile();
            File cc = child.getCanonicalFile();
            if (pc.equals(parent) && cc.equals(child))
            {
                return false;
            }
            return pc.equals(getCommonParentFile(pc, cc));
        }
        return false;
    }

    public static File[] uniqueCopyOf(File[] list, boolean canonical, boolean mergeWithParent) throws IOException
    {
        // se eliminan los duplicados
        File[] unique = ArrayUtils.uniqueCopyOf(list);
        for (int i = 0; i < unique.length; i++)
        {
            for (int j = i + 1; j < unique.length; j++)
            {
                if (equals(unique[j], unique[i], true))
                {
                    continue;
                }
                if (mergeWithParent)
                {
                    if (unique[j].isDirectory() && isParentOf(unique[j], unique[i], canonical))
                    {
                        unique[i] = unique[j];
                        continue;
                    }
                    if (unique[i].isDirectory() && isParentOf(unique[i], unique[j], canonical))
                    {
                        unique[j] = unique[i];
                        continue;
                    }
                }
            }
        }
        return ArrayUtils.uniqueCopyOf(unique);
    }

    public static boolean isLink(String name) throws IOException
    {
        return isLink(new File(name));
    }

    public static boolean isBugName(File file) throws IOException
    {
        String name = file.toString();
        String name2 = normalize(name);
        return !name.equals(name2);
    }

    /**
     * Tests whether the file denoted by this abstract pathname is a link.
     * @param file
     * @return
     * @throws IOException
     */
    public static boolean isLink(File file) throws IOException
    {
        return isLink(file, false);
    }

    /**
     * Tests whether the file denoted by this abstract pathname is a link, or his path is in a link.
     * @param file
     * @return
     * @throws IOException
     */
    public static boolean isLink(File file, boolean path) throws IOException
    {
        File c = file.getCanonicalFile();
        File a = file.getAbsoluteFile();
        boolean link = !c.equals(a);
        if (!link || path)
        {
            return link;
        }
        File ap = a.getParentFile();
        File cp = c.getParentFile();
        if (ap.equals(cp))
        {
            return true;
        }
        File apc = a.getParentFile().getCanonicalFile();
        String name = file.getName();
        File apcName = new File(apc, name);
        link = !apcName.equals(c);
        return link;
    }

    public static File getNoDotFile(File file) throws IOException
    {
        // "." => ""
        File absolute = file.getAbsoluteFile();
        File canonical = file.getCanonicalFile();
        if (absolute.equals(canonical))
        {
            return file;
        }

        // .../. => ...
        String name = file.getName();
        while (name.equals("..") || name.equals("."))
        {
            File parent = file.getParentFile();
            if (parent == null)
            {
                parent = file.getAbsoluteFile().getParentFile();
            }
            if (parent == null)
            {
                return canonical;
            }
            if (name.equals(".") && !canonical.equals(parent.getCanonicalFile()))
            {
                return canonical;
            }
            file = parent;
            name = file.getName();
        }
        String path = file.getPath();

        if (path.startsWith("." + File.separator) || path.startsWith(".." + File.separator))
        {
            return canonical;
        }
        if (path.contains(File.separator + "." + File.separator) || path.contains(File.separator + ".." + File.separator))
        {
            return canonical;
        }
        return file;
    }

    public static boolean isCyclicLink(File file) throws IOException
    {
        if (file.isDirectory() && isLink(file))
        {
            File canonical = file.getCanonicalFile();
            File absolute = file.getAbsoluteFile();
            File[] parents = getParentFiles(absolute, false);
            for (int i = 0; i < parents.length; i++)
            {
                if (canonical.equals(parents[i].getCanonicalFile()))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public static byte[] bytesFromFile(InputStream in) throws IOException
    {
        return bytesFromFile(in, Integer.MAX_VALUE);
    }
    public static byte[] bytesFromFile(InputStream in, int readlimit) throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte buf[] = new byte[64 * 1024];
        int r;
        int count=0;
        while ((r = in.read(buf)) > 0 && count<readlimit)
        {
            baos.write(buf, 0, r);
            count+=r;
        }
        return baos.toByteArray();
    }
    public static byte[] bytesFromFile(File file) throws IOException
    {
        return bytesFromFile(file, Integer.MAX_VALUE);
    }
    public static byte[] bytesFromFile(File file, int readlimit) throws IOException
    {
        return bytesFromFile(new FileInputStream(file), readlimit);
    }

    public static File[] getAbsoluteFile(File[] files)
    {
        File[] abs = new File[files.length];
        for (int i = 0; i < files.length; i++)
        {
            abs[i] = files[i].getAbsoluteFile();
        }
        return abs;
    }

    public static File[] toFileArray(String[] fileNames)
    {
        if (fileNames == null)
        {
            return null;
        }
        File[] files = new File[fileNames.length];
        for (int i = 0; i < files.length; i++)
        {
            files[i] = new File(fileNames[i]);
        }
        return files;
    }

    public static String[] briefPath(String[] fileNames, int max)
    {
//        comprobar si la longitud es menor del máximo, en ese caso no hacer nada
//                en caso contrario se sustituye la parte del medio por ...
        return fileNames;
    }

    public static String briefPath(String fileName, int max)
    {
        return fileName;
    }

    public static boolean delete(File file)
    {
        if (file.isDirectory())
        {
            File[] childs = file.listFiles();
            for (File item : childs)
            {
                if (!delete(item))
                {
                    return false;
                }
            }
        }
        return file.delete();
    }
}
