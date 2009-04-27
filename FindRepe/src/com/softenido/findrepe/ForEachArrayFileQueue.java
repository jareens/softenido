/*
 *  ForEachArrayFileQueue.java
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

import com.softenido.cafe.io.Files;
import com.softenido.cafe.io.ForEachFileOptions;
import com.softenido.cafe.io.ForEachFileQueue;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author franci
 */
public class ForEachArrayFileQueue implements Runnable
{

    private final File[] files;

    final FileFilter filter;
    final BlockingQueue<File> fileQueue;
    final BlockingQueue<String> nameQueue;
    final File eof;
    
    private ForEachFileOptions options = null;;
    
    /**
     * 
     * @param files array of directories to be enqueued
     * @param recursive
     * @param filter
     * @param fileQueue
     * @param nameQueue
     * @param eof
     */
    public ForEachArrayFileQueue(File[] files, FileFilter filter, BlockingQueue<File> fileQueue, BlockingQueue<String> nameQueue, File eof) throws IOException
    {
        files = Files.uniqueCopyOf(files);
        for (int i = 0; i < files.length; i++)
        {
            files[i] = files[i].getCanonicalFile();
        }
        this.files = Files.uniqueCopyOf(files);

        this.filter = filter;
        this.fileQueue = fileQueue;
        this.nameQueue = nameQueue;
        this.eof = eof;
    }

    public ForEachArrayFileQueue(File[] file, BlockingQueue<File> fileQueue, File eof) throws IOException
    {
        this(file, null, fileQueue, null, eof);
    }

    public void setOptions(ForEachFileOptions opt)
    {
        this.options = new ForEachFileOptions(opt);
    }

    public void run()
    {
        for (File fd : files)
        {
            ForEachFileOptions opt = new ForEachFileOptions(options);
            for (File omited : files)
            {
                if (!omited.equals(fd))
                {
                    opt.addOmitedPath(omited);
                }
            }

            // sacar una copia de optiones para cada busqueda, han de excluir al resto
            ForEachFileQueue fefq = new ForEachFileQueue(fd, filter, fileQueue, nameQueue, null,opt);
            // excluidos el resto de rutas
            fefq.run();
        }
        if (eof != null)
        {
            if (fileQueue != null)
            {
                fileQueue.add(eof);
            }
            if (nameQueue != null)
            {
                nameQueue.add(eof.toString());
            }
        }
    }
}
