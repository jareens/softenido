/*
 *  ForEachFileOptions.java
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
import com.softenido.cafedark.io.virtual.VirtualFileFilter;
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
    boolean fifo;
    boolean zip;
    boolean jar;
    boolean tar;
    boolean onlyPacked;
    boolean symlinks;//says if link directories should by followed and link Files (or directories) could be target
    boolean readable;
    FileFilter filter;
    long minSize;
    long maxSize;
    boolean autoOmit;
    boolean hasOmitedPaths;
    HashSet<VirtualFile> omitedPaths;
    boolean hasOmitedFiles;
    HashSet<VirtualFile> omitedFiles;

    boolean hasOmitedDirNames;
    HashSet<VirtualFileFilter> omitedDirNames;
    boolean hasOmitedFileNames;
    HashSet<VirtualFileFilter> omitedFileNames;
    boolean hasAllowedFileNames;
    HashSet<VirtualFileFilter> allowedFileNames;

    public ForEachFileOptions()
    {
        recursive = 99999;
        hidden = false;
        directory = false;
        file = true;
        fifo = false;
        zip = false;
        jar = false;
        tar = false;
        onlyPacked = false;
        symlinks = false;
        readable = false;
        filter = null;
        minSize = 0;
        maxSize = Long.MAX_VALUE;

        autoOmit = true;
        hasOmitedPaths = false;
        omitedPaths = new HashSet<VirtualFile>();
        hasOmitedFiles = false;
        omitedFiles = new HashSet<VirtualFile>();
        
        hasOmitedDirNames = false;
        omitedDirNames = new HashSet<VirtualFileFilter>();
        hasOmitedFileNames = false;
        omitedFileNames = new HashSet<VirtualFileFilter>();
        hasAllowedFileNames = false;
        allowedFileNames = new HashSet<VirtualFileFilter>();
    }

    public ForEachFileOptions(ForEachFileOptions val)
    {
        this.recursive = val.recursive;
        this.hidden = val.hidden;
        this.directory = val.directory;
        this.file = val.file;
        this.fifo = val.fifo;
        this.zip = val.zip;
        this.jar = val.jar;
        this.tar = val.tar;
        this.onlyPacked = val.onlyPacked;
        this.symlinks = val.symlinks;
        this.readable = val.readable;
        this.filter = val.filter;
        this.minSize = val.minSize;
        this.maxSize = val.maxSize;

        this.autoOmit = val.autoOmit;
        this.hasOmitedPaths = val.hasOmitedPaths;
        this.omitedPaths = new HashSet<VirtualFile>(val.omitedPaths);
        this.hasOmitedFiles = val.hasOmitedFiles;
        this.omitedFiles = new HashSet<VirtualFile>(val.omitedFiles);

        this.hasOmitedDirNames = val.hasOmitedDirNames;
        this.omitedDirNames = new HashSet<VirtualFileFilter>(val.omitedDirNames);

        this.hasOmitedFileNames = val.hasOmitedFileNames;
        this.omitedFileNames = new HashSet<VirtualFileFilter>(val.omitedFileNames);
        
        this.hasAllowedFileNames = val.hasAllowedFileNames;
        this.allowedFileNames = new HashSet<VirtualFileFilter>(val.allowedFileNames);
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

    public boolean isFifo()
    {
        return fifo;
    }

    public void setFifo(boolean fifo)
    {
        this.fifo = fifo;
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

    public boolean isTar()
    {
        return tar;
    }

    public void setTar(boolean tar)
    {
        this.tar = tar;
    }

    public boolean isSymlinks()
    {
        return symlinks;
    }

    public void setSymlinks(boolean symlinks)
    {
        this.symlinks = symlinks;
    }

    public boolean isReadable()
    {
        return readable;
    }

    public void setReadable(boolean readable)
    {
        this.readable = readable;
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
    public boolean isOnlyPacked()
    {
        return onlyPacked;
    }

    public void setOnlyPacked(boolean onlyPacked)
    {
        this.onlyPacked = onlyPacked;
    }

    public void addOmitedPath(File path)
    {
        addOmitedPath(new VirtualFile(path.getAbsoluteFile()));
    }
    public void addOmitedPath(VirtualFile path)
    {
        omitedPaths.add(path);
        hasOmitedPaths = true;
    }
    public void addOmitedPath(File[] paths)
    {
        for(File item : paths)
        {
            addOmitedPath(item);
        }
    }
    public void addOmitedPath(VirtualFile[] paths)
    {
        for(VirtualFile item : paths)
        {
            addOmitedPath(item);
        }
    }

    public void addOmitedDirName(String dirName, NameFileFilter.Rule[] requiered)
    {
        VirtualFileFilter omitedDirFilter = VirtualFile.buildFilter(NameFileFilter.getStringInstance(dirName,requiered));
        omitedDirNames.add(omitedDirFilter);
        hasOmitedDirNames = true;
    }
    public void addOmitedDirName(String dirName)
    {
        addOmitedDirName(dirName,null);
    }
    public void addOmitedDirName(String dirName,boolean wildcard,NameFileFilter.Rule[] requiered)
    {
        VirtualFileFilter omitedDirFilter = VirtualFile.buildFilter(wildcard?NameFileFilter.getWildCardInstance(dirName,requiered):NameFileFilter.getRegExInstance(dirName,requiered));
        omitedDirNames.add(omitedDirFilter);
        hasOmitedDirNames = true;
    }
    public void addOmitedDirName(String dirName,boolean wildcard)
    {
        addOmitedDirName(dirName,wildcard,null);
    }
    public void addOmitedFileName(String fileName, NameFileFilter.Rule[] requiered)
    {
        VirtualFileFilter omitedFileFilter = VirtualFile.buildFilter(NameFileFilter.getStringInstance(fileName,requiered));
        omitedFileNames.add(omitedFileFilter);
        hasOmitedFileNames = true;
    }
    public void addOmitedFileName(String fileName)
    {
        addOmitedFileName(fileName,null);
    }
    public void addOmitedFileName(String fileName,boolean wildcard, NameFileFilter.Rule[] requiered)
    {
        VirtualFileFilter omitedFileFilter = VirtualFile.buildFilter(wildcard?NameFileFilter.getWildCardInstance(fileName,requiered):NameFileFilter.getRegExInstance(fileName,requiered));
        omitedFileNames.add(omitedFileFilter);
        hasOmitedFileNames = true;
    }
    public void addOmitedFileName(String fileName,boolean wildcard)
    {
        addOmitedFileName(fileName,wildcard,null);
    }
    public void addAllowedFileName(FileFilter filter)
    {
        allowedFileNames.add(VirtualFile.buildFilter(filter));
        hasAllowedFileNames = true;
    }
    public void addAllowedFileName(String fileName)
    {
        FileFilter allowedFileFilter = NameFileFilter.getStringInstance(fileName);
        addAllowedFileName(allowedFileFilter);
    }
    public void addAllowedFileName(String fileName,boolean wildcard)
    {
        FileFilter allowedFileFilter = wildcard?NameFileFilter.getWildCardInstance(fileName):NameFileFilter.getRegExInstance(fileName);
        addAllowedFileName(allowedFileFilter);
    }
}
