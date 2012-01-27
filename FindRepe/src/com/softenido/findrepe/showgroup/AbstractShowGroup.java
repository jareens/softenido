/*
 *  ImageShowGroup.java
 *
 *  Copyright (C) 2010-2012 Francisco Gómez Carrasco
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
import com.softenido.cafedark.io.Files;
import com.softenido.cafedark.io.virtual.VirtualFile;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;

/**
 *
 * @author franci
 */
public abstract class AbstractShowGroup implements ShowGroup
{
    final boolean delete;
    final boolean absPath;
    final SizeUnits units;
    final int deleteMin;
    final File[] autoDelete;
    final boolean delete1Plus;

    public AbstractShowGroup(SizeUnits units,boolean absPath,boolean delete, int deleteMin,File[] autoDelete, boolean delete1Plus)
    {
        this.units  = units;
        this.absPath= absPath;
        this.delete = delete;
        this.deleteMin = deleteMin;
        this.autoDelete= autoDelete;
        this.delete1Plus= delete1Plus;
    }
    
    @Override
    public void showOneGroup(int groupId, VirtualFile files[]) throws IOException
    {
        boolean[] deleted = new boolean[files.length];
        markAndSort(delete, autoDelete, files, deleteMin, deleted);
        list(groupId, files, deleted);
    }

    private void markAndSort(boolean delete, File[] autoDelete, VirtualFile[] files, int deleteMin, boolean[] deleted) throws IOException
    {
        Arrays.fill(deleted, false);
        if(delete && (autoDelete.length > 0 || delete1Plus) && files.length > 1 && files.length >= deleteMin)
        {
            int[] matches = new int[files.length];
            Arrays.fill(matches, 0);
            for (int i = 0; i < files.length; i++)
            {
                for (int j = 0; j < autoDelete.length; j++)
                {
                    if (files[i].canWrite() && Files.isParentOf(autoDelete[j], files[i].getBaseFile(), false))
                    {
                        matches[i]++;
                    }
                }
            }
            int[] sorted = Arrays.copyOf(matches, matches.length);
            Arrays.sort(sorted);
            int minVal = sorted[deleteMin - 2];
            for (int i = 0; i < matches.length; i++)
            {
                if ((matches[i] > minVal) && files[i].canWrite())
                {
                    deleted[i] = true;
                }
            }
            if(delete1Plus)
            {
                int count=0; 
                for(int i=0;i<deleted.length;i++)
                {
                    if(!deleted[i])
                    {
                        count++;
                        if(files[i].canWrite())
                        {
                            deleted[i] = (count>=deleteMin);
                        }
                    }
                }
            }
        }
    }

    protected abstract void list(int groupId,VirtualFile[] files, boolean[] deleted) throws IOException;
    
    protected int execDiff(String stmt,VirtualFile[] files) throws IOException, InterruptedException
    {
        String[] names = new String[files.length];
        for(int i=0;i<files.length;i++)
        {
            names[i]=Files.escape(files[i].toString());
        }
        String cmd = new MessageFormat(stmt).format(names);
        if(cmd.equals(stmt))
        {
            StringBuilder sb = new StringBuilder();
            sb.append(stmt);
            for(String item : names)
            {
                sb.append(" ").append(Files.escape(item.toString()));
            }
            cmd = sb.toString();
        }
        Process child = Runtime.getRuntime().exec(cmd);
        return child.waitFor();
    }

}
