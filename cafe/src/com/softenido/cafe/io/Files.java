/*
 *  Files.java
 *
 *  Copyright (C) 2007-2009  Francisco Gómez Carrasco
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
package com.softenido.cafe.io;

import com.softenido.cafe.util.ArrayUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 * @author franci
 */
public class Files
{

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
    File file;

    public Files(File file)
    {
        this.file = file;
    }

    // Copies src file to dst file.
    // If the dst file does not exist, it is created
    public static void copy(File src, File dst) throws IOException
    {
        InputStream in = new FileInputStream(src);
        try
        {
            OutputStream out = new FileOutputStream(dst);
            try
            {
                // Transfer bytes from in to out
                byte[] buf = new byte[8192];
                int r;
                while ((r = in.read(buf)) > 0)
                {
                    out.write(buf, 0, r);
                }
            }
            finally
            {
                out.close();
            }
        }
        finally
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

    public String[] list()
    {
        final ArrayList<String> v = new ArrayList<String>();

        ForEachFile fef = new ForEachFile(file, null,null)
        {

            @Override
            protected void doForEeach(File file, String name)
            {
                v.add(file.toString());
            }

            @Override
            protected void doForEeach(ZipFile zf, ZipEntry ze)
            {
            }
        };
        fef.run();
        return (String[]) v.toArray();
    }

    /**
     * Returns an array of strings naming the files and directories in the
     * directory denoted by this abstract pathname that satisfy the specified
     * filter following recursively those directories. The behavior of this
     * method is the same as that of the <code>{@link #list()}</code> method,
     * except that the strings in the
     * returned array must satisfy the filter.  If the given
     * <code>filter</code> is <code>null</code> then all names are accepted.
     * Otherwise, a name satisfies the filter if and only if the value
     * <code>true</code> results when the <code>{@link
     * FilenameFilter#accept}</code> method of the filter is invoked on this
     * abstract pathname and the name of a file or directory in the directory
     * that it denotes.
     *
     * @param  filter  A filename filter
     *
     * @return  An array of strings naming the files and directories in the
     *          directory denoted by this abstract pathname that were accepted
     *          by the given <code>filter</code> following recursively those directories.
     *          The array will be empty if
     *          the directory is empty or if no names were accepted by the
     *          filter.  Returns <code>null</code> if this abstract pathname
     *          does not denote a directory, or if an I/O error occurs.
     */
    public String[] list(FilenameFilter filter)
    {
        String names[] = list();
        if ((names == null) || (filter == null))
        {
            return names;
        }
        ArrayList<String> v = new ArrayList<String>();
        for (int i = 0; i < names.length; i++)
        {
            if (filter.accept(file, names[i]))
            {
                v.add(names[i]);
            }
        }
        return v.toArray(new String[v.size()]);
    }

    /**
     * Returns an array of abstract pathnames denoting the files in the
     * directory denoted by this abstract pathname
     * following recursively those directories.
     *
     * <p> If this abstract pathname does not denote a directory, then this
     * method returns <code>null</code>.  Otherwise an array of
     * <code>File</code> objects is returned, one for each file or directory in
     * the directory and following recursively those directories.
     * Pathnames denoting the directory itself and the
     * directory's parent directory are not included in the result.  Each
     * resulting abstract pathname is constructed from this abstract pathname
     * using the <code>{@link #File(java.io.File, java.lang.String)
     * File(File,&nbsp;String)}</code> constructor.  Therefore if this pathname
     * is absolute then each resulting pathname is absolute; if this pathname
     * is relative then each resulting pathname will be relative to the same
     * directory.
     *
     * <p> There is no guarantee that the name strings in the resulting array
     * will appear in any specific order; they are not, in particular,
     * guaranteed to appear in alphabetical order.
     *
     * @return  An array of abstract pathnames denoting the files and
     *          directories in the directory denoted by this abstract
     *          pathname following recursively those directories.
     *          The array will be empty if the directory is
     *          empty.  Returns <code>null</code> if this abstract pathname
     *          does not denote a directory, or if an I/O error occurs.
     */
    public File[] listFiles()
    {
        return listFiles((FileFilter) null);
    }

    /**
     * Returns an array of abstract pathnames denoting the files and
     * directories in the directory denoted by this abstract pathname that
     * satisfy the specified filter following recursively those directories.
     * The behavior of this method is the
     * same as that of the <code>{@link #listFiles()}</code> method, except
     * that the pathnames in the returned array must satisfy the filter.
     * If the given <code>filter</code> is <code>null</code> then all
     * pathnames are accepted.  Otherwise, a pathname satisfies the filter
     * if and only if the value <code>true</code> results when the
     * <code>{@link FilenameFilter#accept}</code> method of the filter is
     * invoked on this abstract pathname and the name of a file or
     * directory in the directory that it denotes.
     *
     * @param  filter  A filename filter
     *
     * @return  An array of abstract pathnames denoting the files and
     *          directories in the directory denoted by this abstract
     *          pathname.  The array will be empty if the directory is
     *          empty.  Returns <code>null</code> if this abstract pathname
     *          does not denote a directory, or if an I/O error occurs.
     */
    public File[] listFiles(final FilenameFilter filter)
    {
        final ArrayList<File> v = new ArrayList<File>();

        ForEachFile fef = new ForEachFile(file, null,null)
        {

            @Override
            protected void doForEeach(File file, String name)
            {
                if ((filter == null) || filter.accept(file, file.toString()))
                {
                    v.add(file);
                }
            }

            @Override
            protected void doForEeach(ZipFile zf, ZipEntry ze)
            {
            }
        };
        fef.run();
        return v.toArray(new File[0]);
    }

    /**
     * Returns an array of abstract pathnames denoting the files and
     * directories in the directory denoted by this abstract pathname that
     * satisfy the specified filter following recursively those directories.
     * The behavior of this method is the
     * same as that of the <code>{@link #listFiles()}</code> method, except
     * that the pathnames in the returned array must satisfy the filter.
     * If the given <code>filter</code> is <code>null</code> then all
     * pathnames are accepted.  Otherwise, a pathname satisfies the filter
     * if and only if the value <code>true</code> results when the
     * <code>{@link FileFilter#accept(java.io.File)}</code> method of
     * the filter is invoked on the pathname.
     *
     * @param  filter  A file filter
     *
     * @return  An array of abstract pathnames denoting the files and
     *          directories in the directory denoted by this abstract
     *          pathname  following recursively those directories.
     *          The array will be empty if the directory is
     *          empty.  Returns <code>null</code> if this abstract pathname
     *          does not denote a directory, or if an I/O error occurs.
     */
    public File[] listFiles(FileFilter filter)
    {
        final ArrayList<File> v = new ArrayList<File>();

        ForEachFile fef = new ForEachFile(file, filter,null)
        {

            @Override
            protected void doForEeach(File file, String name)
            {
                v.add(file);
            }

            @Override
            protected void doForEeach(ZipFile zf, ZipEntry ze)
            {
            }
        };
        fef.run();
        return (File[]) v.toArray();
    }

    public static String escape(String name)
    {
        for (String[] item : escapeCharacters)
        {
            if(!File.separator.equals(item[0]))
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

    public static boolean isParentOf(File parent, File child)
    {
        return parent.equals(getCommonParentFile(parent, child));
    }

    public static File[] uniqueCopyOf(File[] list)
    {
        // se eliminan los duplicados
        File[] unique = ArrayUtils.uniqueCopyOf(list);
        for (int i = 0; i < unique.length; i++)
        {
            for (int j = i + 1; j < unique.length; j++)
            {
                if (unique[j].equals(unique[i]))
                {
                    continue;
                }
                if (isParentOf(unique[j], unique[i]))
                {
                    unique[i] = unique[j];
                    continue;
                }
                if (isParentOf(unique[i], unique[j]))
                {
                    unique[j] = unique[i];
                    continue;
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

    public static boolean isLink(File file) throws IOException
    {
        if (file.exists())
        {
            String canonical = file.getCanonicalPath();
            String absolute = file.getAbsolutePath();
            return !canonical.equals(absolute);
        }
        return false;
    }

    public static boolean isCyclicLink(File file) throws IOException
    {
        if (file.exists() && file.isDirectory())
        {
            File canonical = file.getCanonicalFile();
            File absolute = file.getAbsoluteFile();
            if (!canonical.equals(absolute))
            {
                File[] parents = getParentFiles(absolute, false);
                for (int i = 0; i < parents.length; i++)
                {
                    if (canonical.equals(parents[i].getCanonicalFile()))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public static byte[] bytesFromFile(File file) throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream in = new FileInputStream(file);
        byte buf[] = new byte[64*1024];
        int r;
        while( (r=in.read(buf))>0)
        {
            baos.write(buf, 0, r);
        }
        return baos.toByteArray();
    }

}
