/*
 *  ActorSingle.java
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
package com.softenido.cafe.util.concurrent.actor;

import com.softenido.cafe.util.concurrent.ASyncFilter;
import com.softenido.cafe.util.concurrent.ASyncLink;
import com.softenido.cafe.util.concurrent.Filter;
import com.softenido.cafe.util.concurrent.ASyncValue;
import com.softenido.cafe.util.concurrent.Value;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author franci
 */
public class ActorSingle<M,R> extends ActorBase<M,R> implements Runnable
{
    final static ActorPool shared = new ActorPool(100);
    
    public static final int QUEUE_SIZE = 64*1024;
    private final AtomicBoolean eagerLock = new AtomicBoolean(false); //controla si hay un hilo gloton
    private final BlockingQueue<Runnable> queue;
    private final ActorPool pool;
    
    private volatile boolean pendingData = false;
    private volatile boolean runningTask = false;

    ActorSingle(Filter<M,R> filter,ActorPool pool,BlockingQueue<Runnable> queue)
    {
        super(filter);
        this.queue  = (queue!=null) ? queue :new LinkedBlockingQueue<Runnable>(QUEUE_SIZE);
        this.pool   = (pool!=null)? pool:ActorSingle.shared;
    }
    public ActorSingle(Filter<M,R> filter,ActorPool pool)
    {
        this(filter,pool,null);
    }
    public ActorSingle(Filter<M,R> filter)
    {
        this(filter,null);
    }
    public ActorSingle(ActorPool pool)
    {
        this(null,pool);
    }
    public ActorSingle()
    {
        this(null,null);
    }

    @Override
    public ASyncValue<R> send(final M m) throws InterruptedException
    {
        ASyncValue<R> task = new ASyncFilter<M,R>(filter,m);
        queue.put(task);
        pendingData = true;
        if(!runningTask)
        {
            pool.execute(this);
        }
        return task;
    }
    @Override
    public Value<R> send(Value<M> m) throws InterruptedException
    {
        ASyncValue<R> task = new ASyncLink<M,R>(filter,m);
        queue.put(task);
        pendingData = true;
        if(!runningTask)
        {
            pool.execute(this);
        }
        return task;
    }
    
    public void execute(Runnable task) throws InterruptedException
    {
        pool.execute(task);
    }

    public void run()
    {
        if(!eagerLock.compareAndSet(false, true))
            return;
        try
        {
            while(pendingData)
            {
                pendingData = false;
                runningTask = true;
                try
                {
                    Runnable task;
                    while ( (task = queue.poll()) != null)
                    {
                            task.run();
                    }
                }
                catch (Exception ex)
                {
                    pendingData = true;
                    Logger.getLogger(ActorSingle.class.getName()).log(Level.SEVERE, null, ex);
                }
                finally
                {
                    runningTask = false;
                }
            }
        }
        finally
        {
            eagerLock.set(false);
        }
    }

    public boolean isRunning()
    {
        return runningTask;
    }


}
