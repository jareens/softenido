/*
 *  ParallelTask.java
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
package com.softenido.cafedark.util.concurrent;

/**
 *
 * @author franci
 */
public abstract class ParallelTask implements Runnable
{

    private final Object lock = new Object();
    private volatile Runnable task;
    private int taskCount = 0;

    public ParallelTask(Runnable task)
    {
        this.task = task;
    }

    public void run()
    {
        if (task == null)
        {
            return;
        }
        synchronized (lock)
        {
            taskCount++;
        }
        try
        {
            if (task != null)
            {
                task.run();
            }
        }
        finally
        {
            synchronized (lock)
            {
                taskCount--;
                if (taskCount == 0 && task != null)
                {
                    task = null;
                    tail();
                }
            }
        }
    }

    public abstract void tail();
}
