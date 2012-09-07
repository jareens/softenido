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
package com.softenido.cafedark.io;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author franci
 */
public class Files extends com.softenido.cafecore.io.Files
{
    File file;

    public Files(File file)
    {
        this.file = file;
    }

//    public String[] list() throws IOException
//    {
//        final ArrayList<String> v = new ArrayList<String>();
//
//        ForEachFile fef = new ForEachFile(file)
//        {
//
//            @Override
//            protected void doForEach(File file, String name)
//            {
//                v.add(file.toString());
//            }
//
//            @Override
//            protected void doForEach(PackedFile fe)
//            {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//        };
//        fef.run();
//        return (String[]) v.toArray();
//    }
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
//    public String[] list(FilenameFilter filter) throws IOException
//    {
//        String names[] = list();
//        if((names == null) || (filter == null))
//        {
//            return names;
//        }
//        ArrayList<String> v = new ArrayList<String>();
//        for(int i = 0; i < names.length; i++)
//        {
//            if(filter.accept(file, names[i]))
//            {
//                v.add(names[i]);
//            }
//        }
//        return v.toArray(new String[v.size()]);
//    }
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
//    public File[] listFiles() throws IOException
//    {
//        return listFiles((FileFilter) null);
//    }
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
//    public File[] listFiles(final FilenameFilter filter) throws IOException
//    {
//        final ArrayList<File> v = new ArrayList<File>();
//
//        ForEachFile fef = new ForEachFile(file, null, null)
//        {
//
//            @Override
//            protected void doForEach(File file, String name)
//            {
//                if((filter == null) || filter.accept(file, file.toString()))
//                {
//                    v.add(file);
//                }
//            }
//
//            @Override
//            protected void doForEach(PackedFile fe)
//            {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//        };
//        fef.run();
//        return v.toArray(new File[0]);
//    }
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
//    public File[] listFiles(FileFilter filter) throws IOException
//    {
//        final ArrayList<File> v = new ArrayList<File>();
//
//        ForEachFile fef = new ForEachFile(file, filter, null)
//        {
//
//            @Override
//            protected void doForEach(File file, String name)
//            {
//                v.add(file);
//            }
//
//            @Override
//            protected void doForEach(PackedFile fe)
//            {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//        };
//        fef.run();
//        return (File[]) v.toArray();
//    }
}
