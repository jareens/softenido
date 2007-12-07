/*
 *  ExtracIcons.java
 *
 *  Copyright (C) 2007  Francisco Gómez Carrasco
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

import java.io.File;
import java.io.FileFilter;

class FileFilterImg implements FileFilter
{

    private static final String[] IMG = {".png", ".gif", ".jpg", ".bmp", ".jpeg"};
    private final String[] img;

    public FileFilterImg()
    {
        this.img = IMG;
    }

    public FileFilterImg(String[] img)
    {
        this.img = img;
    }

    public boolean accept(File pathname)
    {
        String name = pathname.getName().toLowerCase();
        for (String ext : IMG)
        {
            if (name.endsWith(ext))
            {
                return true;
            }
        }
        return false;
    }
}

/**
 *
 * @author franci
 */
public class ExtracIcons implements Runnable
{

    //private static final String[] IMG = {".png", ".gif", ".jpg", ".bmp", ".jpeg"};
    private static final String[] IMG = {".png", ".gif"};
    private static final String[] ZIP = {".zip", ".jar"};
    private File src;
    private File dst;
    private boolean imageAlgorithm;
    private ForEachFileCopy taskCopy = null;
    private int minHeight = 0;
    private int maxHeight = 0;
    private int minWidth = 0;
    private int maxWidth = 0;
    private boolean hidden = false;
    private boolean zip = false;
    private boolean jar = false;
    private FileFilter filter = null;
    private int minSize = 0;
    private int maxSize = 0;

    public ExtracIcons(File src, File dst, boolean imageAlgorithm)
    {
        this.src = src;
        this.dst = dst;
        this.imageAlgorithm = imageAlgorithm;
    }

    public ExtracIcons(String src, String dst, boolean imageAlgorithm)
    {
        this(new File(src), new File(dst), imageAlgorithm);
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

    public int getMaxHeight()
    {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight)
    {
        this.maxHeight = maxHeight;
    }

    public int getMaxSize()
    {
        return maxSize;
    }

    public void setMaxSize(int maxSize)
    {
        this.maxSize = maxSize;
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

    public int getMinSize()
    {
        return minSize;
    }

    public void setMinSize(int minSize)
    {
        this.minSize = minSize;
    }

    public int getMinWidth()
    {
        return minWidth;
    }

    public void setMinWidth(int minWidth)
    {
        this.minWidth = minWidth;
    }

    public boolean isZip()
    {
        return zip;
    }

    public void setZip(boolean zip)
    {
        this.zip = zip;
    }

    public void run()
    {
        if (imageAlgorithm)
        {
            ForEachImageCopy taskImgCopy = new ForEachImageCopy(src, 999999, dst, new FileFilterImg());
            taskImgCopy.setMinHeight(minHeight);
            taskImgCopy.setMaxHeight(maxHeight);
            taskImgCopy.setMinWidth(minWidth);
            taskImgCopy.setMaxWidth(maxWidth);
            taskCopy = taskImgCopy;
        }
        else
        {
            taskCopy = new ForEachFileCopy(src, 999999, dst, new FileFilterImg());

        }
        taskCopy.setZip(zip);
        taskCopy.setMinSize(minSize);
        taskCopy.setMaxSize(maxSize);
        taskCopy.setJar(jar);
        taskCopy.setHidden(hidden);
        taskCopy.setFile(true);
        taskCopy.setDirectory(false);
        taskCopy.run();
    }
}
