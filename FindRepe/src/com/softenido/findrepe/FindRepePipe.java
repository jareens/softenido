/*
 *  FindRepe.java
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

import com.softenido.cafe.collections.IterableBuilder;
import com.softenido.cafe.io.packed.PackedFile;
import com.softenido.cafe.io.FileHash;
import com.softenido.cafe.io.Files;
import com.softenido.cafe.util.ArrayUtils;
import com.softenido.cafe.util.concurrent.pipeline.Pipe;
import com.softenido.cafe.util.concurrent.pipeline.PipeArray;
import com.softenido.cafe.util.concurrent.pipeline.PipeLine;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author franci
 */
public class FindRepePipe implements Runnable
{
    //añadir criterio para enlaces y archivos ocultos

    private final boolean bugs;
    private final int bufSize;
    private final File[] bases;
    private BlockingQueue<File> bugQueue;
    private BlockingQueue<PackedFile[]> groupsQueue;
    private final File fileEof; // elemento final que marca el final de una cola de files
    private final PackedFile[] filesEof = new PackedFile[0];// elemento final que marca el final de una cola de arrays de files
    private final FindRepeOptions options;

    public FindRepePipe(File[] bases, boolean bugs, int bufSize, FindRepeOptions opt)
    {
        this.bases = bases;
        this.bugs = bugs;
        this.bufSize = bufSize;
        this.options = opt;

        fileEof = bases[0]; // elemento final que marca

        bugQueue = new LinkedBlockingQueue<File>();
        groupsQueue = new LinkedBlockingQueue<PackedFile[]>(bufSize);
    }

    private PackedFile readable(PackedFile item)
    {
        if (item.exists())
        {
            // ignoring unreadable files
            if (item.canRead())
            {
                return item;
            }
        }
        else if (bugs)
        {
            try
            {
                if (Files.isBugName(item.getFile()))
                {
                    bugQueue.put(item.getFile());
                }
                else
                {
                    System.err.println("findrepe: wrong link '" + item.toString() + "'");
                }
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(Readable.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (IOException ex)
            {
                Logger.getLogger(FindRepePipe.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    private static PackedFile filterDirFile(PackedFile file, FileFilter[] dirsRegEx, FileFilter[] filesRegEx)
    {
        PackedFile ret = file;
        if (filesRegEx.length > 0)
        {
            for (FileFilter filter : filesRegEx)
            {
                if (filter.accept(file.getFile()))
                {
                    return file;
                }
            }
            ret = null;
        }

        if (dirsRegEx.length > 0)
        {
            for (String names : Files.getParents(file.getFile()))
            {
                for (FileFilter filter : dirsRegEx)
                {
                    if (filter.accept(new File(names)))
                    {
                        return file;
                    }
                }
            }
            ret = null;
        }
        return ret;
    }

    private static FileHash[] focusFilter(File[] paths, FileFilter[] dirsRegEx, FileFilter[] filesRegEx, FileHash[] list)
    {
        FileHash[] ret = list;
        // if any file has the correct name
        if (filesRegEx.length > 0)
        {
            for (FileHash item : list)
            {
                File unpacked = item.getFile().getFile();
                for (FileFilter filter : filesRegEx)
                {
                    if (filter.accept(unpacked))
                    {
                        return list;
                    }
                }
            }
            ret = null;
        }

        // if any dir has the correct name
        if (dirsRegEx.length > 0)
        {
            for (FileHash item : list)
            {
                File unpacked = item.getFile().getFile();
                for (String names : Files.getParents(unpacked))
                {
                    for (FileFilter filter : dirsRegEx)
                    {
                        if (filter.accept(new File(names)))
                        {
                            return list;
                        }
                    }
                }
            }
            ret = null;
        }
        // if any file has the correct path
        if (paths.length > 0)
        {
            for (FileHash item : list)
            {
                File file = item.getFile().getFile();
                for (File dir : paths)
                {
                    if (dir.isFile())
                    {
                        if (dir.equals(item.getFile()))
                        {
                            return list;
                        }
                    }
                    else if (Files.isParentOf(dir, file))
                    {
                        return list;
                    }
                }
            }
            ret = null;
        }
        return ret;
    }

    private static long wastedSize(FileHash[] files)
    {
        if (files.length <= 1)
        {
            return 0;
        }
        long full = 0;
        long min = Long.MAX_VALUE;
        for (FileHash item : files)
        {
            long size = item.getSize();
            min = Math.min(min, item.getSize());
            full += size;
        }

        return (full - min);
    }

    FileHash[][] getFileHashBySize() throws IOException, InterruptedException
    {
        final FileHashSizeComparator cmp = new FileHashSizeComparator();
        final File[] basesAndFocus = ArrayUtils.cat(bases, options.getFocusPaths());
        //obtener ficheros en bruto
        final FileFilter[] dirNameRegEx = options.getDirNames();
        final FileFilter[] fileNameRegEx = options.getFileNames();
        final BucketMap<FileHash> sizeMap = new BucketMap<FileHash>(cmp);

        Pipe<PackedFile, FileHash> pipe = new PipeLine<PackedFile, PackedFile>()//readable
        {

            @Override
            public PackedFile filter(PackedFile a)
            {
                return readable(a);//AL PONER AQUI UN PUNTO DE RUPTURA DA ERROR EN NETBEANS
            }
        }.link(new PipeLine<PackedFile, PackedFile>()//readable
        {

            @Override
            public PackedFile filter(PackedFile a)
            {
                return filterDirFile(a, dirNameRegEx, fileNameRegEx);//AL PONER AQUI UN PUNTO DE RUPTURA DA ERROR EN NETBEANS
            }
        }).link(new PipeLine<PackedFile, FileHash>()//toHash
        {

            @Override
            public FileHash filter(PackedFile a)
            {
                return new FileHash(a);
            }
        }).link(new PipeLine<FileHash, FileHash>()//toBucketMap(size)
        {

            @Override
            public FileHash filter(FileHash a)
            {
                sizeMap.add(a);
                return null;
            }
        });

        ForEachArrayFilePipe foreach = new ForEachArrayFilePipe(basesAndFocus, pipe, true);
        foreach.setOptions(options);
        new Thread(foreach).start();

        // wait until pipe is alive to obtain all items for each bucket
        while (pipe.isAlive())
        {
            try
            {
                pipe.take();
            }
            catch (ExecutionException ex)
            {
                Logger.getLogger(FindRepePipe.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        bugQueue.put(fileEof);
        // now each bucket is
        FileHash[][] list = sizeMap.toArray();
        return (list == null ? new FileHash[0][] : list);
    }

    public void run()
    {
        final File[] paths = Files.getAbsoluteFile(options.getFocusPaths());
        final FileFilter[] dirsRegEx = options.getFocusDirs();
        final FileFilter[] filesRegEx = options.getFocusFiles();
        final long minWastedSize = options.getMinWasted();
        final boolean wastedFilter = (minWastedSize != 0);

        try
        {
            FileHash[][] hashes = getFileHashBySize();

            Pipe<FileHash[], PackedFile[]> pipe = new PipeLine<FileHash[], FileHash[]>()//readable
            {

                @Override
                public FileHash[] filter(FileHash[] a)//min+focus
                {
                    if (a.length < options.getMinCount())
                    {
                        return null;
                    }

                    if (wastedFilter && wastedSize(a) < minWastedSize)
                    {
                        return null;
                    }

                    return focusFilter(paths, dirsRegEx, filesRegEx, a);
                }
            }.link(new PipeLine<FileHash[], FileHash[][]>()//split
            {

                @Override
                public FileHash[][] filter(FileHash[] a)
                {
                    return ArrayUtils.splitEquals(a);
                }
            }).link(new PipeArray<FileHash[], FileHash[]>()//toBucketMap(size)
            {

                @Override
                public FileHash[] filter(FileHash[] a)
                {
                    if (a.length < options.getMinCount())
                    {
                        return null;
                    }
                    if (a.length > options.getMaxCount())
                    {
                        return null;
                    }
                    if (wastedFilter && wastedSize(a) < minWastedSize)
                    {
                        return null;
                    }
                    return focusFilter(paths, dirsRegEx, filesRegEx, a);
                }
            }).link(new PipeLine<FileHash[], PackedFile[]>()
            {

                @Override
                public PackedFile[] filter(FileHash[] a)
                {
                    PackedFile[] dst = new PackedFile[a.length];
                    for (int i = 0; i < a.length; i++)
                    {
                        dst[i] = a[i].getFile();
                    }
                    return dst;
                }
            }).link(new PipeLine<PackedFile[], PackedFile[]>()
            {

                @Override
                public PackedFile[] filter(PackedFile[] a)
                {
                    try
                    {
                        groupsQueue.put(a);
                    }
                    catch (InterruptedException ex)
                    {
                        Logger.getLogger(FindRepePipe.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return null;
                }
            });

            for (int i = 0; i < hashes.length; i++)
            {
                pipe.put(hashes[i]);
                hashes[i] = null;
            }
            pipe.close();
            // wait until pipe is alive to obtain all items for each bucket
            while (pipe.isAlive())
            {
                try
                {
                    pipe.take();
                }
                catch (ExecutionException ex)
                {
                    Logger.getLogger(FindRepePipe.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            groupsQueue.put(filesEof);
        }
        catch (IOException ex)
        {
            Logger.getLogger(FindRepePipe.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(FindRepePipe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Iterable<File> getBugIterable()
    {
        return IterableBuilder.build(bugQueue, fileEof);
    }

    public Iterable<PackedFile[]> getGroupsIterable()
    {
        return IterableBuilder.build(groupsQueue, filesEof);
    }
}
