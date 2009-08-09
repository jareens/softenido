/*
 *  SyncPipeLine.java
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
package com.softenido.cafe.util.pipeline;

import com.softenido.cafe.util.concurrent.ASyncValue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author franci
 */
public class SyncPipeLine<A,B> implements PipeLine<A,B> , Filter<A,B>
{
    private final Filter<A,B> filter;
    private volatile boolean alive = true;
    private volatile PipeLine<B,?> next = null;
    private final Object lock = new Object();
    
    private final ASyncValue<B> poison = new ASyncValue<B>()
    {
        @Override
        protected B call()
        {
            return null;
        }
    };

    private final BlockingQueue<ASyncValue<B>> queue = new LinkedBlockingQueue<ASyncValue<B>>();

    public SyncPipeLine()
    {
        this.filter = this;
    }
    public SyncPipeLine(Filter<A,B> filter)
    {
        this.filter = filter;
    }

    public void put(A a) throws InterruptedException
    {
        if(alive)
        {
            ASyncFilter<A,B> task = new ASyncFilter<A,B>(a, filter);
            queue.put(task);
            execute(task);
            if(next!=null)
            {
                push();
            }
        }
    }
    public B get() throws InterruptedException
    {
        return get(true);
    }
    B get(boolean eager) throws InterruptedException
    {
        B ret;
        do
        {
            ASyncValue<B> async = queue.take();
            if(async==poison)
            {
                queue.put(async);
                if(next!=null)
                {
                    next.close();
                }
                return null;
            }
            ret = async.getValue();
        }
        while(ret == null && eager);
        return ret;
    }
    public void close() throws InterruptedException
    {
        if(alive)
        {
            alive = false;
            queue.put(poison);
            if(next != null)
            {
                push();
            }
        }
    }

    public B filter(A a)
    {
        return null;
    }

    protected void execute(Runnable task)
    {
        task.run();
    }
    public void link(PipeLine<B,?> next)
    {
        this.next = next;
    }

    private void push()
    {
        execute(new Runnable()
        {
            public void run()
            {
                synchronized (lock)
                {
                    try
                    {
                        B b = get(false);
                        if (b != null)
                        {
                            next.put(b);
                        }
                    } 
                    catch (InterruptedException ex)
                    {
                        Logger.getLogger(SyncPipeLine.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }
}
