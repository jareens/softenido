/*
 *  Forks.java
 *
 *  Copyright (C) 2007  Francisco G�mez Carrasco
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
package com.softenido.cafedark.misc;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author franci
 */
public class Forks
{
    private volatile int count = 0;
    private final int max;
    private final Thread[] tasks;
    private final boolean priority;
    
    public Forks(int max, boolean priority)
    {
        this.max = max;
        this.tasks = new Thread[max];
        this.priority = priority;
    }

    public Forks(int max)
    {
        this(max, false);
    }

    public Forks()
    {
        this(0, false);
    }

    private Thread wrappTask(Runnable task)
    {
        return new Thread(task)
        {
            public void run()
            {
                super.run();
                remove(this);
            }
        };
    }

    public synchronized void invoke(Runnable task)
    {
        if (max <= 0)
        {
            task.run();
            return;
        }
        // se espera ha tener espacio
        while (count >= max)
        {
            try
            {
                wait();
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(Forks.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Thread th = wrappTask(task);
        if (priority)
        {
            th.setPriority(Math.max(Thread.NORM_PRIORITY - count, Thread.MIN_PRIORITY));
        }
        tasks[count++] = th;
        th.start();
    }

    public synchronized void invokeNow(Runnable task)
    {
        if (count < max)
        {
            invoke(task);
        }
        else
        {
            task.run();
        }
    }

    public synchronized boolean tryInvoke(Runnable task)
    {
        if (count < max)
        {
            invoke(task);
            return true;
        }
        return false;
    }

    private synchronized void remove(Runnable task)
    {
        int i;
        boolean found = false;
        // se busca el elemento
        for (i = 0; i < max; i++)
        {
            if (tasks[i] == task)
            {
                found = true;
                break;
            }
        }
        if (found)
        {
            for (i++; i < max; i++)
            {
                if (tasks[i] != null)
                {
                    tasks[i].setPriority(Math.max(Thread.NORM_PRIORITY - i + 1, Thread.MIN_PRIORITY));
                }
                tasks[i - 1] = tasks[i];
            }
            tasks[--count] = null;
            notifyAll();
        }
    }

    public int getCount()
    {
        return count;
    }

    public synchronized void waitForOne()
    {
        if (count > 0)
        {
            try
            {
                wait();
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(Forks.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public synchronized void waitForAll()
    {
        // se espera ha tener espacio
        while (count > 0)
        {
            waitForOne();
        }
    }

    protected void finalize() throws Throwable
    {
        waitForAll();
        super.finalize();
    }
}
