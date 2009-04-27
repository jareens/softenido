/*
 *  ForEachImageCopy.java
 *
 *  Copyright (C) 2007-2009  Francisco Gómez Carrasco
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
package extracticons;

import com.softenido.cafe.io.ForEachFileOptions;
import java.io.File;
import java.io.FileFilter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.fjtk.ce.Forks;

/**
 *
 * @author franci
 */
public class ForEachImageCopy extends ForEachFileCopy
{
    private Set<ImageHash> imgSet = null;
    private int minHeight = 0;
    private int maxHeight = 0;
    private int minWidth = 0;
    private int maxWidth = 0;
    private boolean ignoreAlpha = false;
    private int percent = 100;

    public int getMaxHeight()
    {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight)
    {
        this.maxHeight = maxHeight;
    }

    public int getMaxWidth()
    {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth)
    {
        this.maxWidth = maxWidth;
    }

    public int getMinHeight()
    {
        return minHeight;
    }

    public void setMinHeight(int minHeight)
    {
        this.minHeight = minHeight;
    }

    public int getMinWidth()
    {
        return minWidth;
    }

    public void setMinWidth(int minWidth)
    {
        this.minWidth = minWidth;
    }

    public int getPercent()
    {
        return percent;
    }

    public void setPercent(int percent)
    {
        this.percent = percent;
    }

    public boolean isIgnoreAlpha()
    {
        return ignoreAlpha;
    }

    public void setIgnoreAlpha(boolean ignoreAlpha)
    {
        this.ignoreAlpha = ignoreAlpha;
    }

    public ForEachImageCopy(File src, File dst, FileFilter filter, Forks fork, ForEachFileOptions opt)
    {
        super(src, dst, filter, fork,opt);
    }

    @Override
    protected void addHash(File fileDst)
    {
        super.addHash(fileDst);
        imgSet.add(new ImageHash(fileDst, ignoreAlpha, percent));
    }

    @Override
    protected boolean acceptCopy(File file)
    {
        if (!super.acceptCopy(file))
        {
            return false;
        }
        ImageHash imgHash = new ImageHash(file, ignoreAlpha, percent);
        int height = imgHash.getHeight();
        int width = imgHash.getWidth();
        if (minHeight != 0 && (height < minHeight))
        {
            return false;
        }
        if (maxHeight != 0 && (height > maxHeight))
        {
            return false;
        }
        if (minWidth != 0 && (width < minWidth))
        {
            return false;
        }
        if (maxWidth != 0 && (width > maxWidth))
        {
            return false;
        }
        return !imgSet.contains(imgHash);
    }

    @Override
    protected void initSet()
    {
        super.initSet();
        imgSet = Collections.synchronizedSet(new HashSet<ImageHash>());
    }

    private void buildSet()
    {
        ForEachImageHash taskHashMap = new ForEachImageHash(getDst(), fileSet, imgSet, ignoreAlpha, percent,null);
        taskHashMap.run();
        taskHashMap = null;
    }

    @Override
    public void run()
    {
        initSet();
        buildSet();
        super.run();
    }
}
