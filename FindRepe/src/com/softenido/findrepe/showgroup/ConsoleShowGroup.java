/*
 *  ConsoleShowGroup.java
 *
 *  Copyright (C) 2010 Francisco Gómez Carrasco
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
import com.softenido.findrepe.FindRepeMain;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author franci
 */
public class ConsoleShowGroup extends AbstractShowGroup
{
    private final Scanner sc;
    
    public ConsoleShowGroup(SizeUnits units,boolean absPath,boolean delete,int deleteMin,File[] autoDelete, boolean delete1Plus)
    {
        super(units,absPath,delete,deleteMin,autoDelete, delete1Plus);
        this.sc = new Scanner(System.in);
    }

    @Override
    protected void list(int groupId, VirtualFile[] files, boolean[] deleted) throws IOException
    {
        boolean showResult = false;
        while (true)
        {
            System.out.flush();
            System.out.println();

            showResult=false;
            for (int i = 0; i < files.length; i++)
            {
                showResult = showResult && deleted[i];
                
                String id = deleted[i] ? "-" : (files[i].canWrite() ? "" + i : "r");
                String size = units == null ? "" : units.toString(files[i].length(), true);
                String name = absPath ? files[i].getAbsolutePath() : files[i].toString();
                System.out.printf("[%s]%s %s\n", id, size, name);
            }
            if (!delete)
            {
                return;
            }
            String prompt = "\nGroup %d, delete files [0 - %d, all, none, :sh]: ";
            System.out.printf(prompt, groupId, files.length - 1);
            String line = sc.nextLine();
            if (line.length() == 0)
            {
                break;
            }
            if (line.trim().equalsIgnoreCase("all"))
            {
                for (int i = 0; i < deleted.length; i++)
                {
                    showResult = true;
                    deleted[i] = true;
                }
            }
            else if (line.trim().equalsIgnoreCase("none"))
            {
                for (int i = 0; i < deleted.length; i++)
                {
                    deleted[i] = false;
                }
            }
            else if(line.trim().startsWith(":sh"))
            {
                try
                {
                    line = line.trim().substring(3);
                    execDiff(line,files);
                }
                catch (InterruptedException ex)
                {
                    Logger.getLogger(FindRepeMain.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                Scanner scLine = new Scanner(line);

                while (scLine.hasNextInt())
                {
                    int index = scLine.nextInt();
                    if (index >= 0 && index < files.length)
                    {
                        showResult = true;
                        deleted[index] = true;
                    }
                    else
                    {
                        System.out.printf("%d ignored\n", index);
                    }
                }
            }
        }
        for(int i=0; !showResult && i<deleted.length ; i++)
        {
            showResult = deleted[i];
        }
        if(showResult)
        {
            System.out.println();
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
    }

    @Override
    public void close()
    {
        System.out.flush();
    }
}
