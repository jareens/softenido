/*
 *  ForEachFile.java
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
package com.softenido.cafe.io;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 * @author franci
 */
public class ForEachFileQueue extends ForEachFile
{

    private final File eofFile;
    private final String eofName;
    private final BlockingQueue<File> fileQueue;
    private final BlockingQueue<String> nameQueue;

    public ForEachFileQueue(File file, int recursive, FileFilter filter, BlockingQueue<File> fileQueue, BlockingQueue<String> nameQueue)
    {
        super(file, recursive, filter);

        this.eofFile = file;
        this.eofName = file.toString();

        this.fileQueue = fileQueue;
        this.nameQueue = nameQueue;
    }

    public ForEachFileQueue(File file, int recursive, FileFilter filter, BlockingQueue<File> fileQueue)
    {
        this(file, recursive, filter, fileQueue, null);
    }

    public ForEachFileQueue(File file, int recursive, BlockingQueue<File> fileQueue)
    {
        this(file, recursive, null, fileQueue, null);
    }

    @Override
    protected void doForEeach(File file, String name)
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
    protected void doForEeach(ZipFile zf, ZipEntry ze)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void run()
    {
        super.run();
        try
        {
            if (fileQueue != null)
            {
                fileQueue.put(eofFile);
            }
            if (nameQueue != null)
            {
                nameQueue.put(eofName);
            }
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(ForEachFileQueue.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public File getEofFile()
    {
        return eofFile;
    }

    public String getEofName()
    {
        return eofName;
    }

    public BlockingQueue<File> getFileQueue()
    {
        return fileQueue;
    }

    public BlockingQueue<String> getNameQueue()
    {
        return nameQueue;
    }

}
