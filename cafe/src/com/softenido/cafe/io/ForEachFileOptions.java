/*
 *  ForEachFileOptions.java
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
import java.util.HashSet;

/**
 *
 * @author franci
 */
public class ForEachFileOptions
{

    int recursive;
    boolean hidden;
    boolean directory;
    boolean file;
    boolean zip;
    boolean jar;
    boolean linkDir;//says if link directories should by followed
    boolean linkFile;//says if link Files (or directories) could be target
    FileFilter filter;
    long minSize;
    long maxSize;
    boolean autoOmit;
    boolean hasOmitedPaths;
    HashSet<File> omitedPaths;
    boolean hasOmitedFiles;
    HashSet<File> omitedFiles;

    boolean hasOmitedDirNames;
    HashSet<File> omitedDirNames;
    boolean hasOmitedFileNames;
    HashSet<File> omitedFileNames;

    public ForEachFileOptions()
    {
        recursive = 99999;
        hidden = false;
        directory = false;
        file = true;
        zip = false;
        jar = false;
        linkDir = false;//says if link directories should by followed
        linkFile = false;//says if link Files (or directories) could be target
        filter = null;
        minSize = 0;
        maxSize = Long.MAX_VALUE;

        autoOmit = true;
        hasOmitedPaths = false;
        omitedPaths = new HashSet<File>();
        hasOmitedFiles = false;
        omitedFiles = new HashSet<File>();
        
        hasOmitedDirNames = false;
        omitedDirNames = new HashSet<File>();
        hasOmitedFileNames = false;
        omitedFileNames = new HashSet<File>();
    }

    public ForEachFileOptions(ForEachFileOptions val)
    {
        this.recursive = val.recursive;
        this.hidden = val.hidden;
        this.directory = val.directory;
        this.file = val.file;
        this.zip = val.zip;
        this.jar = val.jar;
        this.linkDir = val.linkDir;
        this.linkFile = val.linkFile;
        this.filter = val.filter;
        this.minSize = val.minSize;
        this.maxSize = val.maxSize;

        this.autoOmit = val.autoOmit;
        this.hasOmitedPaths = val.hasOmitedPaths;
        this.omitedPaths = new HashSet<File>(val.omitedPaths);
        this.hasOmitedFiles = val.hasOmitedFiles;
        this.omitedFiles = new HashSet<File>(val.omitedFiles);

        this.hasOmitedDirNames = val.hasOmitedDirNames;
        this.omitedDirNames = new HashSet<File>(val.omitedDirNames);

        this.hasOmitedFileNames = val.hasOmitedFileNames;
        this.omitedFileNames = new HashSet<File>(val.omitedFileNames);
    }

    public boolean isAutoOmit()
    {
        return autoOmit;
    }

    public void setAutoOmit(boolean autoOmit)
    {
        this.autoOmit = autoOmit;
    }

    public boolean isDirectory()
    {
        return directory;
    }

    public void setDirectory(boolean directory)
    {
        this.directory = directory;
    }

    public boolean isFile()
    {
        return file;
    }

    public void setFile(boolean file)
    {
        this.file = file;
    }

    public FileFilter getFilter()
    {
        return filter;
    }

    public void setFilter(FileFilter filter)
    {
        this.filter = filter;
    }

    public boolean isHasOmitedFiles()
    {
        return hasOmitedFiles;
    }

    public boolean isHasOmitedPaths()
    {
        return hasOmitedPaths;
    }

    public boolean isHidden()
    {
        return hidden;
    }

    public void setHidden(boolean hidden)
    {
        this.hidden = hidden;
    }

    public boolean isJar()
    {
        return jar;
    }

    public void setJar(boolean jar)
    {
        this.jar = jar;
    }

    public boolean isLinkDir()
    {
        return linkDir;
    }

    public void setLinkDir(boolean linkDir)
    {
        this.linkDir = linkDir;
    }

    public boolean isLinkFile()
    {
        return linkFile;
    }

    public void setLinkFile(boolean linkFile)
    {
        this.linkFile = linkFile;
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

    public int getRecursive()
    {
        return recursive;
    }

    public void setRecursive(int recursive)
    {
        this.recursive = recursive;
    }

    public boolean isZip()
    {
        return zip;
    }

    public void setZip(boolean zip)
    {
        this.zip = zip;
    }

    public void addOmitedPath(File path)
    {
        omitedPaths.add(path);
        hasOmitedPaths = true;
    }
    public void addOmitedPath(File[] paths)
    {
        for(File item : paths)
        {
            omitedPaths.add(item);
        }
        hasOmitedPaths = hasOmitedPaths || (!omitedPaths.isEmpty());
    }

    public void addOmitedDirName(File dir)
    {
        omitedDirNames.add(dir);
        hasOmitedDirNames = true;
    }
    public void addOmitedDirName(String dir)
    {
        addOmitedDirName(new File(dir));
    }
    public void addOmitedFileName(File fileName)
    {
        omitedFileNames.add(fileName);
        hasOmitedFileNames = true;
    }
    public void addOmitedFileName(String fileName)
    {
        addOmitedFileName(new File(fileName));
    }
}
