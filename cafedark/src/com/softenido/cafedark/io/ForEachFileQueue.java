/*
 *  ForEachFileQueue.java
 *
 *  Copyright (C) 2009-2010  Francisco Gómez Carrasco
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
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author franci
 */
public class ForEachFileQueue extends ForEachFile
{

    private final File eof;
    private final BlockingQueue<File> fileQueue;
    private final BlockingQueue<String> nameQueue;

    public ForEachFileQueue(File[] file, FileFilter filter, BlockingQueue<File> fileQueue, BlockingQueue<String> nameQueue, File eof,ForEachFileOptions opt) throws IOException
    {
        super(file, filter,opt);
        this.eof = eof;
        this.fileQueue = fileQueue;
        this.nameQueue = nameQueue;
    }

    public ForEachFileQueue(File[] file, BlockingQueue<File> rawQueue, File eof) throws IOException
    {
        this(file, null, rawQueue, null, eof,null);
    }

    public ForEachFileQueue(File[] file, FileFilter filter, BlockingQueue<File> fileQueue, File eof) throws IOException
    {
        this(file, filter, fileQueue, null, eof,null);
    }

    public ForEachFileQueue(File[] file, FileFilter filter, BlockingQueue<File> fileQueue) throws IOException
    {
        this(file, filter, fileQueue, null, null,null);
    }

    public ForEachFileQueue(File[] file, int recursive, BlockingQueue<File> fileQueue) throws IOException
    {
        this(file, null, fileQueue, null, null,null);
    }

    @Override
    protected void doForEach(File file, String name)
    {
        try
        {
            if (fileQueue != null)
            {
                fileQueue.put(file);
            }
            if (nameQueue != null)
            {
                nameQueue.put(name);
            }
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(ForEachFileQueue.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run()
    {
        super.run();
        try
        {
            if (eof != null)
            {
                if (fileQueue != null)
                {
                    fileQueue.put(eof);
                }
                if (nameQueue != null)
                {
                    nameQueue.put(eof.toString());
                }
            }
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(ForEachFileQueue.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public File getEofFile()
    {
        return eof;
    }

    public String getEofName()
    {
        return (eof==null)?null:eof.toString();
    }

    public BlockingQueue<File> getFileQueue()
    {
        return fileQueue;
    }

    public BlockingQueue<String> getNameQueue()
    {
        return nameQueue;
    }

    @Override
    protected void doForEach(VirtualFile fe)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
