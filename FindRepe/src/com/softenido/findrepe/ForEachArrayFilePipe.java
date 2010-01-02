/*
 *  ForEachArrayFilePipe.java
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

import com.softenido.cafe.io.Files;
import com.softenido.cafe.io.ForEachFileOptions;
import com.softenido.cafe.io.ForEachFilePipe;
import com.softenido.cafe.util.concurrent.pipeline.Pipe;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author franci
 */
public class ForEachArrayFilePipe implements Runnable
{

    private final File[] files;

    final FileFilter filter;
    final Pipe<File,?> filePipe;
    final Pipe<String,?> namePipe;
    final boolean eof;
    
    private ForEachFileOptions options = null;;
    
    /**
     * 
     * @param files array of directories to be enPiped
     * @param recursive
     * @param filter
     * @param filePipe
     * @param namePipe
     * @param eof
     */
    public ForEachArrayFilePipe(File[] files, FileFilter filter, Pipe<File,?> filePipe, Pipe<String,?> namePipe, boolean eof) throws IOException
    {
        files = Files.uniqueCopyOf(files);
        for (int i = 0; i < files.length; i++)
        {
            files[i] = files[i].getCanonicalFile();
        }
        this.files = Files.uniqueCopyOf(files);

        this.filter = filter;
        this.filePipe = filePipe;
        this.namePipe = namePipe;
        this.eof = eof;
    }

    public ForEachArrayFilePipe(File[] file, Pipe<File,?> filePipe, boolean eof) throws IOException
    {
        this(file, null, filePipe, null, eof);
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
            ForEachFilePipe fefq = new ForEachFilePipe(fd, filter, filePipe, namePipe, false,opt);
            // excluidos el resto de rutas
            fefq.run();
        }
        if (eof)
        {
            try
            {
                if (filePipe != null)
                {
                    filePipe.close();
                }
                if (namePipe != null)
                {
                    namePipe.close();
                }
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(ForEachArrayFilePipe.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
