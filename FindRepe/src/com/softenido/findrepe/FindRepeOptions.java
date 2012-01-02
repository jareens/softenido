/*
 *  FindRepeOptions.java
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

import com.softenido.cafecore.equals.EqualsBuilder;
import com.softenido.cafedark.io.ForEachFileOptions;
import com.softenido.cafedark.io.NameFileFilter;
import com.softenido.cafedark.io.virtual.VirtualFile;
import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;

/**
 *
 * @author franci
 */
public class FindRepeOptions extends ForEachFileOptions
{
    private int minCount;
    private int maxCount;
    private long minWasted;
    private boolean eager;

    private boolean hasFocusPaths;
    private final HashSet<File> focusPaths;
    private boolean hasFocusDir;
    private final HashSet<FileFilter> focusDir;
    private boolean hasFocusFile;
    private final HashSet<FileFilter> focusFile;

    private boolean hasDirName;
    private final HashSet<FileFilter> dirName;
    private boolean hasFileName;
    private final HashSet<FileFilter> fileName;

    public final static EqualsBuilder<VirtualFile> BYHASH_HALF_COMPARATOR = new FileComparatorByHash(true);
    public final static EqualsBuilder<VirtualFile> BYHASH_FULL_COMPARATOR = new FileComparatorByHash(false);
    public final static EqualsBuilder<VirtualFile> DEFAULT_HALF_COMPARATOR = BYHASH_HALF_COMPARATOR;
    public final static EqualsBuilder<VirtualFile> DEFAULT_FULL_COMPARATOR = BYHASH_FULL_COMPARATOR;
        
    private EqualsBuilder<VirtualFile> halfCmp;
    private EqualsBuilder<VirtualFile> fullCmp;

    public FindRepeOptions()
    {
        super();
        minCount = 0;
        maxCount = Integer.MAX_VALUE;
        eager    = false;

        hasFocusPaths = false;
        focusPaths = new HashSet<File>();
        hasFocusDir = false;
        focusDir = new HashSet<FileFilter>();
        hasFocusFile= false;
        focusFile= new HashSet<FileFilter>();

        hasDirName = false;
        dirName= new HashSet<FileFilter>();//at least one of the parent directories must match a rule for every file
        hasFileName = false;
        fileName = new HashSet<FileFilter>();// every file must match a rule
        halfCmp = DEFAULT_HALF_COMPARATOR;
        fullCmp = DEFAULT_FULL_COMPARATOR;
    }

    public FindRepeOptions(ForEachFileOptions val)
    {
        super(val);
        minCount = 0;
        maxCount = Integer.MAX_VALUE;
        eager    = false;

        hasFocusPaths = false;
        focusPaths = new HashSet<File>();
        hasFocusDir = false;
        focusDir = new HashSet<FileFilter>();
        hasFocusFile= false;
        focusFile= new HashSet<FileFilter>();

        hasDirName = false;
        dirName= new HashSet<FileFilter>();//at least one of the parent directories must match a rule for every file
        hasFileName = false;
        fileName = new HashSet<FileFilter>();// every file must match a rule
        halfCmp = DEFAULT_HALF_COMPARATOR;
        fullCmp = DEFAULT_FULL_COMPARATOR;
    }

    public FindRepeOptions(FindRepeOptions val)
    {
        super(val);
        minCount = val.minCount;
        maxCount = val.maxCount;
        minWasted = val.minWasted;
        eager    = val.eager;

        hasFocusPaths       = val.hasFocusPaths;
        focusPaths          = new HashSet<File>(val.focusPaths);
        hasFocusDir    = val.hasFocusDir;
        focusDir       = new HashSet<FileFilter>(val.focusDir);
        hasFocusFile   = val.hasFocusFile;
        focusFile      = new HashSet<FileFilter>(val.focusFile);

        hasDirName = val.hasDirName;
        dirName= new HashSet<FileFilter>(val.dirName);
        hasFileName = val.hasFileName;
        fileName = new HashSet<FileFilter>(val.fileName);
        halfCmp = val.halfCmp;
        fullCmp = val.fullCmp;
    }

    public int getMaxCount()
    {
        return maxCount;
    }

    public void setMaxCount(int maxCount)
    {
        this.maxCount = maxCount;
    }

    public int getMinCount()
    {
        return minCount;
    }

    public void setMinCount(int minCount)
    {
        this.minCount = minCount;
    }

    public long getMinWasted()
    {
        return minWasted;
    }

    public void setMinWasted(long minWasted)
    {
        this.minWasted = minWasted;
    }

    public boolean isEager()
    {
        return eager;
    }

    public void setEager(boolean eager)
    {
        this.eager = eager;
    }
    
    
    public void addFocusPath(File path)
    {
        focusPaths.add(path);
        hasFocusPaths = true;
    }
    public void addFocusPath(String path)
    {
        addFocusPath(new File(path));
    }
    public void addFocusDir(String name,boolean wildcard)
    {
        FileFilter filter = wildcard?NameFileFilter.getWildCardInstance(name):NameFileFilter.getRegExInstance(name);
        focusDir.add(filter);
        hasFocusDir = true;
    }
    public void addFocusFile(String name,boolean wildcard)
    {
        FileFilter filter = wildcard?NameFileFilter.getWildCardInstance(name):NameFileFilter.getRegExInstance(name);
        focusFile.add(filter);
        hasFocusFile = true;
    }

    public void addDirName(String name,boolean wildcard)
    {
        FileFilter filter = wildcard?NameFileFilter.getWildCardInstance(name):NameFileFilter.getRegExInstance(name);
        dirName.add(filter);
        hasDirName = true;
    }
    public void addFileName(String name,boolean wildcard)
    {
        FileFilter filter = wildcard?NameFileFilter.getWildCardInstance(name):NameFileFilter.getRegExInstance(name);
        fileName.add(filter);
        hasFileName = true;
    }

    public File[] getFocusPaths()
    {
        return focusPaths.toArray(new File[0]);
    }

    public FileFilter[] getFocusDirs()
    {
        return focusDir.toArray(new FileFilter[0]);
    }

    public FileFilter[] getFocusFiles()
    {
        return focusFile.toArray(new FileFilter[0]);
    }

    FileFilter[] getDirNames()
    {
        return dirName.toArray(new FileFilter[0]);
    }

    FileFilter[] getFileNames()
    {
        return fileName.toArray(new FileFilter[0]);
    }

    public void setComparators(EqualsBuilder<VirtualFile> half, EqualsBuilder<VirtualFile> full)
    {
        this.halfCmp = half;
        this.fullCmp = full;
    }

    public EqualsBuilder<VirtualFile> getFullCmp()
    {
        return fullCmp;
    }

    public EqualsBuilder<VirtualFile> getHalfCmp()
    {
        return halfCmp;
    }
}
