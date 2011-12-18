/*
 *  ForEachFile.java
 *
 *  Copyright (C) 2007-2010  Francisco Gómez Carrasco
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

import com.softenido.cafedark.io.virtual.VirtualFile;
import com.softenido.cafedark.io.virtual.VirtualFileFilter;
import com.softenido.cafedark.io.virtual.VirtualFiles;
import com.softenido.cafecore.os.OSName;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;

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
    private final VirtualFile[] base;
    private final ForEachFileOptions options;
    private final VirtualFileFilter filter;
    private final HashSet<File> autoOmitPaths = new HashSet<File>();
    private final CoveredPath coveredPath;
    static final Logger logger = Logger.getLogger(ForEachFile.class.getName());
    private static final ArchiveStreamFactory asf = new ArchiveStreamFactory();

    public ForEachFileOptions getOptions()
    {
        return new ForEachFileOptions(options);
    }

    public static int getBufSize()
    {
        return bufSize;
    }

    public static void setBufSize(int bufSize)
    {
        ForEachFile.bufSize = bufSize;
    }

    public ForEachFile(File[] files, FileFilter filter, ForEachFileOptions opt) throws IOException
    {
        this(VirtualFile.asVirtualFile(files),VirtualFile.buildFilter(filter), opt);
    }
    public ForEachFile(VirtualFile[] files, VirtualFileFilter filter, ForEachFileOptions opt) throws IOException
    {
        options = opt == null ? new ForEachFileOptions() : new ForEachFileOptions(opt);
        this.base = files;
        this.filter = filter;
        this.coveredPath = new CoveredPath(options.symlinks);

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
        for (VirtualFile item : base)
        {
            VirtualFile file;
            try
            {
                file = VirtualFiles.getNoDotFile(item);
                if (file != null)
                {
                    visit(file, null, 0);
                }
            }
            catch (IOException ex)
            {
                Logger.getLogger(ForEachFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void visit(VirtualFile file, InputStream in, final int level)
    {
        if (level > options.recursive)
        {
            return;
        }    
        logger.log(Level.FINEST, "file={0}", file);
        try
        {
            if(!canVisit(file, level))
            {
                return;
            }
            if( canDo(file) )
            {
                doForEach(file);
            }
            if (!file.canRead())
            {
                return;
            }
            if(!file.isComplex() && file.isDirectory())
            {
                followDirectory(file.getBaseFile(),level);
            }
            else if(canFollowArchive(file.getName()))
            {
                followArchive(file, in, level + 1);
            }
        }
        catch (Exception ex)
        {
            doException(file.toString(), ex);
        }
    }

    private void followDirectory(File file, int level)
    {
        File[] childs = file.listFiles();
        if (childs == null)
        {
            logger.log(Level.WARNING, "error in {0}", file);
            return;
        }
        for (File child : childs)
        {
            visit(new VirtualFile(child), null, level + 1);
        }
    }
    private void followArchive(VirtualFile pf, InputStream in, int level) throws ArchiveException, IOException
    {
        if (in == null)
        {
            in = pf.getInputStream();
        }

        ArchiveInputStream zip = asf.createArchiveInputStream(new BufferedInputStream(in));
        ArchiveEntry ent = null;
        try
        {
            while ((ent = zip.getNextEntry()) != null)
            {
                logger.log(Level.FINEST, "file={0}", ent.getName());
                VirtualFile child = new VirtualFile(pf, ent);
                visit(child, zip, level+1);
            }
        }
        catch (Exception ex)
        {
            doException(pf.getPath(), ex);
        }
    }

    private boolean canDo(final VirtualFile file)
    {
        if(options.onlyPacked)
        {
            return false;
        }
        if(file.isFile())
        {
            if(!options.file)
                return false;
            if(!acceptSize(file.length()))
                return false;
        }
        else if(file.isDirectory())
        {
            if(!options.directory)
                return false;
        }
        else
        {
            if(!options.fifo)
                return false;
            if(!acceptSize(0))
                return false;
        }

        if (filter != null && !filter.accept(file))
        {
            return false;
        }
        if (isOmitedFile(file))
        {
            return false;
        }
        return true;
    }


    boolean canFollowArchive(String name)
    {
        name = name.toLowerCase();
        if (options.zip && name.endsWith(".zip"))
        {
            return true;
        }
        if (options.jar && name.endsWith(".jar"))
        {
            return true;
        }
        if (options.tar && name.endsWith(".tar"))
        {
            return true;
        }
        return false;
    }

    protected abstract void doForEach(VirtualFile fe);

    protected void doForEach(File file, String name)
    {
        doForEach(new VirtualFile(file));
    }

    private void doException(String msg, Exception ex)
    {
        logger.log(Level.SEVERE, msg, ex);
    }

    private boolean isOmitedPath(final VirtualFile file, boolean verifyParents)
    {
        final boolean omitImplicit = options.autoOmit && !autoOmitPaths.isEmpty();
        if (omitImplicit && autoOmitPaths.contains(file.getBaseFile()))
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
            File parent = file.getBaseFile();
            while ((parent = parent.getParentFile()) != null)
            {
                if (isOmitedPath(new VirtualFile(parent), false))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isOmitedDirName(final VirtualFile file)
    {
        if (options.hasOmitedDirNames)
        {
            for (VirtualFileFilter item : options.omitedDirNames)
            {
                if (item.accept(file))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isOmitedFile(final VirtualFile file)
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
            for (VirtualFileFilter item : options.omitedFileNames)
            {
                if (item.accept(file))
                {
                    return true;
                }
            }
        }
        // when there are filter for allowed files at least one should be satisfied
        if (options.hasAllowedFileNames)
        {
            for (VirtualFileFilter item : options.allowedFileNames)
            {
                if(item.accept(file))
                {
                    return false;
                }
            }
            return true;
        }

        return false;
    }
   
    private boolean canVisit(VirtualFile file, int level) throws IOException
    {
        if (level != 0 && !options.hidden && file.isHidden())
        {
            return false;
        }
        if(options.readable && !file.canRead())
        {
            return false;
        }
        boolean link = file.isLink();
        if( (level>0) && link && !options.symlinks)
        {
            return false;
        }

        if(file.isDirectory())
        {
            if( link && Files.isCyclicLink(file.getBaseFile()))
            {
                return false;
            }

            // only follow directories that can be read
            if (isOmitedDirName(file))
            {
                return false;
            }

            //        si un directorio no es padre de una ruta excluida no hay que comparar si no es un link
            //        si un directorio contiene una ruta excluida solo hay que comparar al concreto salvo que sea un link
            //        si un directorio canonico tiene menos niveles que una ruta excluida no está excluido
                // (file.isDirectory() && file.canRead() && ((level == 0) || (this.linkDir && !Files.isCyclicLink(file)) || !(Files.isLink(file))))

            //verify isn't omited in absolute form
            final VirtualFile absolute = file.getAbsoluteFile();
            final VirtualFile canonical = file.getCanonicalFile();

            if (isOmitedPath(absolute, false))
            {
                return false;
            }
            if (link && isOmitedPath(canonical, true))
            {
                return false;
            }
        }
        if (!coveredPath.add(file, level == 0, link))
        {
            logger.log(Level.FINE, "already covered file={0} -> {1}", new VirtualFile[]{file, file.getCanonicalFile()});
            return false;
        }
        return true;
    }

}
