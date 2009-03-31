/*
 *  FindRepe.java
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

import com.softenido.cafe.collections.Consumer;
import com.softenido.cafe.collections.IterableBuilder;
import com.softenido.cafe.collections.ProducerConsumer;
import com.softenido.cafe.io.FileHash;
import com.softenido.cafe.util.SplitEquals;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

class FileHashSizeComparator implements Comparator<FileHash>
{

    public FileHashSizeComparator()
    {
    }

    public int compare(FileHash f1, FileHash f2)
    {
        if (f1.getSize() < f2.getSize())
        {
            return -1;
        }
        if (f1.getSize() > f2.getSize())
        {
            return +1;
        }
        return 0;
    }
}

/**
 *
 * @author franci
 */
public class FindRepe implements Runnable
{
    //añadir criterio para enlaces y archivos ocultos

    private final boolean bugs;
    private final int bufSize;
    private final File[] file;
    private BlockingQueue<File> bugQueue;
    private BlockingQueue<File[]> groupsQueue;
    private final File fileEof; // elemento final que marca el final de una cola de files
    private final File[] filesEof = new File[0];// elemento final que marca el final de una cola de arrays de files
    private final FileHash hashEof; // elemento final en una cola de FileHash
    private final FileHash[] hashEofArray = new FileHash[0];// elemento final en una cola de FileHash[]
    private final FileHash[] emptyHash = new FileHash[0];   // elemento de muestra para crear arrays en los split
    private long minSize = 0L;
    private long maxSize = Long.MAX_VALUE;
    private boolean hidden = true;

    public FindRepe(File[] file, boolean bugs, int bufSize)
    {
        this.file = file;
        this.bugs = false;
        this.bufSize = bufSize;

        fileEof = file[0]; // elemento final que marca
        hashEof = new FileHash(fileEof); // elemento final en una cola de FileHash

        bugQueue = new LinkedBlockingQueue<File>();
        groupsQueue = new LinkedBlockingQueue<File[]>(bufSize);

    }

    public FindRepe(File[] file, boolean bugs)
    {
        this(file, bugs, 100);
    }

    public void run()
    {
        final long minLen = minSize;
        final long maxLen = maxSize;
        try
        {
            //obtener ficheros en bruto
            final BlockingQueue<File> fileQueue = new LinkedBlockingQueue<File>(bufSize);
            ForEachArrayFileQueue feafq = new ForEachArrayFileQueue(file, 999, fileQueue, fileEof);
            feafq.setHidden(hidden);
            new Thread(feafq).start();
            // envolver con FileHash y y detectar bugs en el nombre
            final BlockingQueue<FileHash> hashQueue = new LinkedBlockingQueue<FileHash>(bufSize);
            new Thread(new Runnable()
            {

                public void run()
                {
                    try
                    {
                        File item;
                        while ((item = fileQueue.take()) != fileEof)
                        {
                            if (item.exists())
                            {
                                if(item.length()>=minLen && item.length()<=maxLen)
                                {
                                    hashQueue.put(new FileHash(item));
                                }
                            }
                            else if (bugs)
                            {
                                bugQueue.put(item);
                            }
                        }
                        bugQueue.put(fileEof);
                        hashQueue.put(hashEof);
                    }
                    catch (InterruptedException ex)
                    {
                        Logger.getLogger(FindRepe.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }).start();
            // agrupar por tamaño
            final FileHashSizeComparator cmp = new FileHashSizeComparator();
            final Consumer<FileHash> hashConsumer = IterableBuilder.build(hashQueue, hashEof);
            final BlockingQueue<FileHash[]> sizeQueue = new LinkedBlockingQueue<FileHash[]>(bufSize);
            final ProducerConsumer<FileHash[]> sizeProducer = IterableBuilder.build(sizeQueue, hashEofArray);
            new Thread(SplitEquals.buildSplit(hashConsumer, sizeProducer, cmp, emptyHash, hashEofArray)).start();
            // agrupar por contenido
            BlockingQueue<FileHash[]> equalQueue = new LinkedBlockingQueue<FileHash[]>(bufSize);
            final ProducerConsumer<FileHash[]> equalProducer = IterableBuilder.build(equalQueue, hashEofArray);
            new Thread(SplitEquals.buildSplitAgain(sizeProducer, equalProducer, null, emptyHash, hashEofArray)).start();
            for (FileHash[] listHash : equalProducer)
            {
                File[] listFile = new File[listHash.length];
                for (int i = 0; i < listHash.length; i++)
                {
                    listFile[i] = listHash[i].getFile();
                }
                groupsQueue.put(listFile);
            }
            groupsQueue.put(filesEof);
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(FindRepe.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(FindRepe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Iterable<File> getBugIterable()
    {
        return IterableBuilder.build(bugQueue, fileEof);
    }

    public Iterable<File[]> getGroupsIterable()
    {
        return IterableBuilder.build(groupsQueue, filesEof);
    }

    public long getMaxSize()
    {
        return maxSize;
    }

    public void setMaxSize(long maxSize)
    {
        this.maxSize = maxSize;
    }

    public long getMinSize()
    {
        return minSize;
    }

    public void setMinSize(long minSize)
    {
        this.minSize = minSize;
    }

    public boolean isHidden()
    {
        return hidden;
    }

    public void setHidden(boolean hidden)
    {
        this.hidden = hidden;
    }

}
