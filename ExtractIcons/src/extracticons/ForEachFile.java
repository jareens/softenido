/*
 *  ForEachFile.java
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import org.fjtk.ce.Forks;

/**
 *
 * @author franci
 */
public abstract class ForEachFile implements Runnable
{
    private static int bufSize = 64 * 1024;
    private File base;
    private int recursive = 0;
    private boolean hidden = false;
    private boolean directory = false;
    private boolean file = true;
    private boolean zip = false;
    private boolean jar = false;
    private FileFilter filter = null;
    private int minSize = 0;
    private int maxSize = 0;
    private Forks fork = null;

    public static int getBufSize()
    {
        return bufSize;
    }

    public static void setBufSize(int bufSize)
    {
        ForEachFile.bufSize = bufSize;
    }

    public boolean isJar()
    {
        return jar;
    }

    public void setJar(boolean jar)
    {
        this.jar = jar;
    }

    public boolean isZip()
    {
        return zip;
    }

    public void setZip(boolean zip)
    {
        this.zip = zip;
    }

    public boolean isFile()
    {
        return file;
    }

    public void setFile(boolean file)
    {
        this.file = file;
    }

    public boolean isDirectory()
    {
        return directory;
    }

    public void setDirectory(boolean directory)
    {
        this.directory = directory;
    }

    public boolean isHidden()
    {
        return hidden;
    }

    public void setHidden(boolean hidden)
    {
        this.hidden = hidden;
    }

    public int getMaxSize()
    {
        return maxSize;
    }

    public void setMaxSize(int maxSize)
    {
        this.maxSize = maxSize;
    }

    public int getMinSize()
    {
        return minSize;
    }

    public void setMinSize(int minSize)
    {
        this.minSize = minSize;
    }

    public int getRecursive()
    {
        return recursive;
    }

    protected final Forks getFork()
    {
        return fork;
    }

    public ForEachFile(File file, int recursive, FileFilter filter, Forks fork)
    {
        this.base = file;
        this.recursive = recursive;
        this.filter = filter;
        this.fork = (fork != null) ? fork : new Forks();
    }

    public void run()
    {
        run(base, 0);
    }

    private void run(final File file, final int level)
    {
        if (level > recursive)
        {
            return;
        }

        if ((this.directory || !file.isDirectory()) &&
                (this.file || !file.isFile()) &&
                (this.hidden || !file.isHidden()) &&
                (filter == null || filter.accept(file)) &&
                acceptSize(file.length()))
        {
            fork.invoke(new Runnable()
            {
                public void run()
                {
                    doForEeach(file, null);
                }
            });
        }
        if ((level < recursive) && (this.hidden || !file.isHidden()))
        {
            if (file.isDirectory())
            {
                File childs[] = file.listFiles();
                if (childs == null)
                {
                    System.err.printf("%d %s error\n", level, file);
                    return;
                }
                for (File child : childs)
                {
                    run(child, level + 1);
                }
                return;
            }
            String name = file.getName().toLowerCase();
            if ((this.zip && name.endsWith(".zip")) || (this.jar && name.endsWith(".jar")))
            {
                fork.invoke(new Runnable()
                {
                    public void run()
                    {
                        runZip(file, level + 1);
                    }
                });
            }
        }
    }

    private boolean acceptSize(long size)
    {
        return ((minSize == 0 || size >= minSize) && (maxSize == 0 || size <= maxSize));
    }

    private void runZip(File file, int level)
    {
        try
        {
            ZipFile zf = new ZipFile(file);
            try
            {
                Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zf.entries();
                while (entries.hasMoreElements())
                {
                    ZipEntry ze = entries.nextElement();
                    if ((this.directory || !ze.isDirectory()) &&
                            (this.file || ze.isDirectory()) &&
                            (filter == null || filter.accept(new File(ze.getName()))) &&
                            acceptSize(ze.getSize()))
                    {
                        doForEeach(zf, ze);
                    }
                }
            } finally
            {
                zf.close();
            }
        }
        catch (ZipException ex)
        {
            doException(ex);
        }
        catch (IOException ex)
        {
            doException(ex);
        }
    }

    protected File unZip(File file, ZipFile zf, ZipEntry ze) throws IOException
    {
        if (file == null)
        {
            file = File.createTempFile("tmp.", new File(ze.getName()).getName());
            file.deleteOnExit();
        }

        FileOutputStream fos = new FileOutputStream(file);
        byte[] buf = new byte[bufSize];
        InputStream zis = zf.getInputStream(ze);
        int r = zis.read(buf);
        while (r > 0)
        {
            fos.write(buf, 0, r);
            r = zis.read(buf);
        }

        fos.close();
        zis.close();
        return file;
    }

    protected abstract void doForEeach(File file, String name);

    protected abstract void doForEeach(ZipFile zf, ZipEntry ze);

    private void doException(Exception ex)
    {
        Logger.getLogger(ForEachFile.class.getName()).log(Level.SEVERE, null, ex);
    }
}
