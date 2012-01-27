/*
 *  ImageShowGroup.java
 *
 *  Copyright (C) 2010-2011 Francisco Gómez Carrasco
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
package com.softenido.findrepe.showgroup;

import com.softenido.cafecore.util.SizeUnits;
import com.softenido.cafedark.io.virtual.VirtualFile;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.compress.archivers.ArchiveException;

/**
 *
 * @author franci
 */
public class ImageShowGroup extends AbstractShowGroup
{
    JImageDiffFrame frame = null;

    public ImageShowGroup(SizeUnits units, boolean absPath, boolean delete, int deleteMin, File[] autoDelete, boolean delete1Plus)
    {
        super(units, absPath, delete, deleteMin, autoDelete, delete1Plus);
    }

    @Override
    protected void list(final int groupId, final VirtualFile[] files, final boolean[] deleted) throws IOException
    {
        final ImageShowGroup sg = this;
        try
        {
            java.awt.EventQueue.invokeAndWait(new Runnable()
            {

                @Override
                public void run()
                {
                    try
                    {
                        if (frame == null)
                        {
                            frame = new JImageDiffFrame(sg);
                            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
                            frame.setVisible(true);
                            frame.pack();
                            frame.validate();
                        }
                        frame.showImages(groupId, files, deleted);
                    }
                    catch (IOException ex)
                    {
                        Logger.getLogger(JImageDiffFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    catch (ArchiveException ex)
                    {
                        Logger.getLogger(JImageDiffFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(ImageShowGroup.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InvocationTargetException ex)
        {
            Logger.getLogger(ImageShowGroup.class.getName()).log(Level.SEVERE, null, ex);
        }
        synchronized(this)
        {
            try
            {
                this.wait();
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(ImageShowGroup.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    protected void finalize() throws Throwable
    {
        frame.dispose();
        super.finalize();
    }

    public void delete(final VirtualFile[] files, final boolean[] deleted)
    {
        int deletedCount = 0;
        int notDeletedCount = 0;
        for (int i = 0; i < files.length; i++)
        {
            boolean notdeleted = false;
            if (deleted[i])
            {
                if (!files[i].delete())
                {
                    notdeleted = true;
                    notDeletedCount++;
                }
                else
                {
                    deletedCount++;
                }
            }
            System.out.printf("  [%s] %s\n", (notdeleted ? "e" : (deleted[i] ? "-" : "+")), files[i].toString());
        }
        if (deletedCount > 0)
        {
            System.out.printf("\n  files deleted: %d\n\n", deletedCount);
        }
        if (notDeletedCount > 0)
        {
            System.out.printf("\n  files not deleted: %d\n\n", notDeletedCount);
        }
    }

    @Override
    public void close()
    {
        if(frame!=null)
        {
            frame.setVisible(false);
            frame.dispose();
        }
    }
}
