/*
 *  ForEachFile.java
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

import com.softenido.cafe.io.packed.PackedFile;
import com.softenido.cafe.util.OSName;
import com.softenido.cafe.util.zip.FixZipInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;


class CachedFile extends File
{

    boolean directory = false;
    boolean directoryCached = false;

    public CachedFile(String pathname)
    {
        super(pathname);
    }

    @Override
    public boolean isDirectory()
    {
        if (!directoryCached)
        {
            directory = super.isDirectory();
            directoryCached = true;
        }
        return directory;
    }
}

/**
 *
 * @author franci
 */
public abstract class ForEachFile implements Runnable
{

    private static int bufSize = 64 * 1024;
    private File base;
    ForEachFileOptions options;
    private FileFilter filter = null;
    private HashSet<File> autoOmitPaths = new HashSet<File>();

    public ForEachFileOptions getOptions()
    {
        return new ForEachFileOptions(options);
    }

    public void setOptions(ForEachFileOptions options)
    {
        this.options = new ForEachFileOptions(options);
    }

    public static int getBufSize()
    {
        return bufSize;
    }

    public static void setBufSize(int bufSize)
    {
        ForEachFile.bufSize = bufSize;
    }

    public ForEachFile(File file)
    {
        this(file, null, null);
    }

    public ForEachFile(File file, FileFilter filter, ForEachFileOptions opt)
    {
        this.base = file;
        this.filter = filter;

        if (OSName.os.isPosix())
        {
            autoOmitPaths.add(new File(File.separator + "dev"));
            autoOmitPaths.add(new File(File.separator + "tmp"));
            autoOmitPaths.add(new File(File.separator + "lost+found"));
        }
        if (OSName.os.isLinux() || OSName.os.isSolaris())
        {
            autoOmitPaths.add(new File(File.separator + "proc"));
        }
        if (OSName.os.isLinux())
        {
            autoOmitPaths.add(new File(File.separator + "sys"));
            autoOmitPaths.add(new File(File.separator + "var" + File.separator + "run"));
            autoOmitPaths.add(new File(File.separator + "var" + File.separator + "lock"));
        }
        if (OSName.os.isSolaris())
        {
            autoOmitPaths.add(new File(File.separator + "devices"));
        }

        options = opt == null ? new ForEachFileOptions() : new ForEachFileOptions(opt);
        if (autoOmitPaths.isEmpty())
        {
            options.autoOmit = false;
        }
    }

    private boolean acceptSize(long size)
    {
        return (size >= options.minSize) && (size <= options.maxSize);
    }

    public void run()
    {
        run(base, 0);
    }

    private void run(final File file, final int level)
    {
        if (level > options.recursive)
        {
            return;
        }
        try
        {
            if (!options.onlyPacked && (options.directory || !file.isDirectory()) && (options.file || !file.isFile()) && (options.hidden || (level == 0) || !file.isHidden()) && (filter == null || filter.accept(file)) && acceptSize(file.length()) && (options.linkFile || !Files.isLink(file)))
            {
                if (!isOmitedFile(file))
                {
                    doForEach(file, null);
                }
            }
            if ((level < options.recursive) && (options.hidden || (level == 0) || !file.isHidden()))
            {
                if (file.isDirectory())
                {
                    if (followDir(file, level))
                    {
                        File[] childs = file.listFiles();
                        if (childs == null)
                        {
                            System.err.printf("%d %s error\n", level, file);
                            return;
                        }
                        for (File child : childs)
                        {
                            run(new CachedFile(child.toString()), level + 1);
                        }
                        return;
                    }
                }
                else if ((options.zip || options.jar) && file.isFile())
                {
                    String name = file.getName().toLowerCase();
                    if ((options.zip && name.endsWith(".zip")) || (options.jar && name.endsWith(".jar")))
                    {
                        ZipInputStream child = new FixZipInputStream(new FileInputStream(file));
                        try
                        {
                            run(new PackedFile(file), child, level + 1);
                        }
                        finally
                        {
                            child.close();
                        }

                    }
                }
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(ForEachFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void run(PackedFile pf, ZipInputStream zip, int level)
    {
        if (level > options.recursive)
        {
            return;
        }
        ZipEntry ent=null;
        try
        {
            while ((ent = zip.getNextEntry()) != null)
            {
                PackedFile child = new PackedFile(pf, ent);
                if ((options.directory || !ent.isDirectory()) && (options.file || ent.isDirectory()) && (filter == null || filter.accept(new File(ent.getName()))) && acceptSize(ent.getSize()))
                {
                    doForEach(child);
                }
                if ((level < options.recursive) && (options.hidden || (level == 0)))
                {
                    if ((options.zip || options.jar) && !ent.isDirectory())
                    {
                        String name = ent.getName().toLowerCase();
                        if ((options.zip && name.endsWith(".zip")) || (options.jar && name.endsWith(".jar")))
                        {
                            run(child, new ZipInputStream(zip), level + 1);
                        }
                    }
                }
            }
        }
        catch (ZipException ex)
        {
            Logger.getLogger(ForEachFile.class.getName()).log(Level.WARNING, "error in file: \"{0}\"", pf);
        }
        catch (IOException ex)
        {
            Logger.getLogger(ForEachFile.class.getName()).log(Level.WARNING, "error in file: \"{0}\"", pf);
        }
    }

    protected abstract void doForEach(PackedFile fe);

    protected void doForEach(File file, String name)
    {
        doForEach(new PackedFile(file));
    }

    private void doException(Exception ex)
    {
        Logger.getLogger(ForEachFile.class.getName()).log(Level.SEVERE, null, ex);
    }

    private boolean isOmitedPath(final File file, boolean verifyParents)
    {
        final boolean omitImplicit = options.autoOmit && !autoOmitPaths.isEmpty();
        if (omitImplicit && autoOmitPaths.contains(file))
        {
            return true;
        }
        final boolean omitExplicit = options.hasOmitedPaths && !options.omitedPaths.isEmpty();
        if (omitExplicit && options.omitedPaths.contains(file))
        {
            return true;
        }
        if ((omitImplicit || omitExplicit) && verifyParents)
        {
            File parent = file;
            while ((parent = parent.getParentFile()) != null)
            {
                if (isOmitedPath(parent, false))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isOmitedDirName(final File file)
    {
        if (options.hasOmitedDirNames)
        {
            for (FileFilter item : options.omitedDirNames)
            {
                if (item.accept(file))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isOmitedFile(final File file)
    {
        if (options.hasOmitedFiles)
        {
            if (options.omitedFiles.contains(file))
            {
                return true;
            }
        }

        if (options.hasOmitedFileNames)
        {
            for (FileFilter item : options.omitedFileNames)
            {
                if (item.accept(file))
                {
                    return true;
                }
            }
        }

        return false;
    }

//        si un directorio no es padre de una ruta excluida no hay que comparar si no es un link
//        si un directorio contiene una ruta excluida solo hay que comparar al concreto salvo que sea un link
//        si un directorio canonico tiene menos niveles que una ruta excluida no está excluido
    // (file.isDirectory() && file.canRead() && ((level == 0) || (this.linkDir && !Files.isCyclicLink(file)) || !(Files.isLink(file))))
    private boolean followDir(final File file, int level)
    {
        try
        {
            // only follow directories that can be read
            if (!file.isDirectory())
            {
                return false;
            }
            if (!file.canRead())
            {
                return false;
            }
            if (isOmitedDirName(file))
            {
                return false;
            }

            //verify isn't omited in absolute form
            final File absolute = file.getAbsoluteFile();
            final File canonical = file.getCanonicalFile();
            final boolean link = !absolute.equals(canonical);

            //verify is not a link or links can be followed
            if (link && level > 0)
            {
                if (!options.linkDir)
                {
                    return false;
                }
                if (Files.isCyclicLink(file))
                {
                    return false;
                }
            }
            if (isOmitedPath(absolute, false))
            {
                return false;
            }
            if (link && isOmitedPath(canonical, true))
            {
                return false;
            }
            return true;
        }
        catch (IOException ex)
        {
            Logger.getLogger(ForEachFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
