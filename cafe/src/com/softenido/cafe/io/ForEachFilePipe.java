/*
 *  ForEachFilePipe.java
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

import com.softenido.cafe.util.concurrent.pipeline.Pipe;
import java.io.File;
import java.io.FileFilter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 * @author franci
 */
public class ForEachFilePipe extends ForEachFile
{
    private final boolean eof;
    private final Pipe<File,?> filePipe;
    private final Pipe<String,?> namePipe;

    public ForEachFilePipe(File file, FileFilter filter, Pipe<File,?> filePipe, Pipe<String,?> namePipe, boolean eof,ForEachFileOptions opt)
    {
        super(file, filter,opt);
        this.eof      = eof;
        this.filePipe = filePipe;
        this.namePipe = namePipe;
    }

    public ForEachFilePipe(File file, Pipe<File,?> rawPipe, boolean eof)
    {
        this(file, null, rawPipe, null, eof,null);
    }

    public ForEachFilePipe(File file, FileFilter filter, Pipe<File,?> filePipe, boolean eof)
    {
        this(file, filter, filePipe, null, eof,null);
    }

    public ForEachFilePipe(File file, FileFilter filter, Pipe<File,?> filePipe)
    {
        this(file, filter, filePipe, null, false,null);
    }

    public ForEachFilePipe(File file, int recursive, Pipe<File,?> filePipe)
    {
        this(file, null, filePipe, null, false, null);
    }

    @Override
    protected void doForEeach(File file, String name)
    {
        try
        {
            if (filePipe != null)
            {
                filePipe.put(file);
            }
            if (namePipe != null)
            {
                namePipe.put(name);
            }
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(ForEachFilePipe.class.getName()).log(Level.SEVERE, null, ex);
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
            if (eof)
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
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(ForEachFilePipe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Pipe<File,?> getFilePipe()
    {
        return filePipe;
    }

    public Pipe<String,?> getNamePipe()
    {
        return namePipe;
    }
}
