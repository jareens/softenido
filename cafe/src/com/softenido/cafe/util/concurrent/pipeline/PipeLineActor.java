/*
 *  PipeLineActor.java
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
package com.softenido.cafe.util.concurrent.pipeline;

import com.softenido.cafe.util.concurrent.actor.*;
import com.softenido.cafe.util.concurrent.Filter;
import com.softenido.cafe.util.concurrent.ASyncValue;
import com.softenido.cafe.util.concurrent.Value;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author franci
 */
public class PipeLineActor<M, R> implements PipeLineBase<M, R>, Runnable
{
    private final Actor<M,R> actor;
    private final BlockingQueue<Value<R>> queue = new LinkedBlockingQueue<Value<R>>();
    private volatile boolean alive = true;
    private final ASyncValue<R> poison = new ASyncValue<R>()
    {
        @Override
        protected R call()
        {
            return null;
        }
    };
    private volatile PipeLineActor<R,?> next = null;
    
    private volatile boolean pendingData = false;
    private volatile boolean runningTask = false;
    private final AtomicBoolean eagerLock = new AtomicBoolean(false); //controla si hay un hilo gloton

    public PipeLineActor(ActorPool pool,int threads,Filter<M, R> filter)
    {
        this.actor = new Actor(pool,threads,filter);
    }
    public PipeLineActor(int threads,Filter<M, R> filter)
    {
        this.actor = new Actor(threads,filter);
    }
    public PipeLineActor(Filter<M, R> filter)
    {
        this.actor = new Actor(filter);
    }

    public void put(M m) throws InterruptedException
    {
        if (alive)
        {
            final Value<R> value = actor.send(m);
            queue.put(value);
            if(next!=null)
            {
                pendingData = true;
                if(!runningTask)
                {
                    actor.execute(this);
                }
            }
        }
    }
    public void close() throws InterruptedException
    {
        if (alive)
        {
            queue.put(poison);
            if(next!=null)
            {
                pendingData = true;
                if(!runningTask)
                {
                    actor.execute(this);
                }
            }
        }
    }

    public R take() throws InterruptedException, ExecutionException
    {
        R r = null;
        while (alive && r == null)
        {
            Value<R> val= poll(true);
            if(val!=null)
            {
                 r = val.get();
            }
        }
        return r;
    }

    private Value<R> poll(boolean blocking) throws InterruptedException, ExecutionException
    {
        Value<R> async = blocking?queue.take():queue.poll();
        if (async == poison)
        {
            alive = false;
            queue.put(async);
            return null;
        }
        return async;
    }

    public boolean isAlive()
    {
        return alive;
    }

    public <T> void link(PipeLineActor<R,T> pipe)
    {
        next = pipe;
    }

    public void run()
    {
        if(next==null)
            return;
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
                    Value<R> val;
                    R r;
                    while ( (val = poll(false)) != null)
                    {
                        r = val.get();
                        if(r!= null)
                        {
                            next.put(r);
                        }
                    }
                    if(!alive && next!=null)
                    {
                        next.close();
                    }
                }
                catch (Exception ex)
                {
                    pendingData = true;
                    Logger.getLogger(PipeLineActor.class.getName()).log(Level.SEVERE, null, ex);
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

}
