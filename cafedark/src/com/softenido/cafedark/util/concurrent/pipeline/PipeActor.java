/*
 *  PipeActor.java
 *
 *  Copyright (C) 2009-2010  Francisco Gómez Carrasco
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
package com.softenido.cafedark.util.concurrent.pipeline;

import com.softenido.cafedark.util.concurrent.Filter;
import com.softenido.cafedark.util.concurrent.ASyncValue;
import com.softenido.cafedark.util.concurrent.Value;
import com.softenido.cafedark.util.concurrent.actor.Actor;
import com.softenido.cafedark.util.concurrent.actor.ActorPool;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author franci
 */
public class PipeActor<M,R> implements Pipe<M,R>
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
    
    PipeActor(ActorPool pool,int threads,Filter<M, R> filter)
    {
        this.actor = new Actor(pool,threads,filter);
    }
    PipeActor(int threads,Filter<M, R> filter)
    {
        this.actor = new Actor(threads,filter);
    }
    PipeActor(Filter<M, R> filter)
    {
        this.actor = new Actor(1,filter);
    }

    public void put(M m) throws InterruptedException
    {
        if (alive)
        {
            final Value<R> value = actor.send(m);
            queue.put(value);
        }
    }
    public void close() throws InterruptedException
    {
        if (alive)
        {
            queue.put(poison);
        }
    }

    public R take() throws InterruptedException, ExecutionException
    {
        return get(true);
    }
    public R poll() throws InterruptedException, ExecutionException
    {
        return get(false);
    }
    private R get(boolean blocking) throws InterruptedException, ExecutionException
    {
        R ret;
        boolean keep=false;
        do
        {
            final Value<R> val = blocking?queue.take():queue.poll();
            if (val == poison)
            {
                alive = false;
                queue.put(val);
                return null;
            }
            ret = val!=null?val.get():null;
            keep= val!=null;
        }
        while(alive && ret==null && (keep||blocking));
        return ret;
    }

    public boolean isAlive()
    {
        return alive;
    }

    @Override
    public void execute(Runnable task) throws InterruptedException
    {
        actor.execute(task);
    }

    public int size()
    {
        return queue.size();
    }
}
