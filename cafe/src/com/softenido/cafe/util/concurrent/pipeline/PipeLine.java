/*
 *  PipeLine.java
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

import com.softenido.cafe.util.concurrent.Filter;
import com.softenido.cafe.util.concurrent.actor.ActorPool;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author franci
 */
public class PipeLine<M,R> implements PipeLineBase<M,R>, Filter<M,R>
{
    private final PipeLineActor<M,?> write;
    private final PipeLineActor<?,R> read;
    
    private PipeLine(PipeLineActor<M,?> write,PipeLineActor<?,R> read)
    {
        this.write = write;
        this.read = read;
    }
    public PipeLine(ActorPool pool,int threads,Filter<M,R> filter)
    {
        PipeLineActor<M,R> pipe = new PipeLineActor<M,R>(pool,threads, filter!=null?filter:this);
        this.write = pipe;
        this.read = pipe;
    }
    public PipeLine(ActorPool pool,int threads)
    {
        PipeLineActor<M,R> pipe = new PipeLineActor<M,R>(pool,threads,this);
        this.write = pipe;
        this.read = pipe;
    }
    public PipeLine(int threads)
    {
        PipeLineActor<M,R> pipe = new PipeLineActor<M,R>(threads,this);
        this.write = pipe;
        this.read = pipe;
    }
    public PipeLine()
    {
        PipeLineActor<M,R> pipe = new PipeLineActor<M,R>(this);
        this.write = pipe;
        this.read = pipe;
    }

    public void put(M m) throws InterruptedException
    {
        write.put(m);
    }

    public R take() throws InterruptedException, ExecutionException
    {
        return read.take();
    }

    public void close() throws InterruptedException
    {
        write.close();
    }

    public boolean isAlive()
    {
        return read.isAlive();
    }
    public <T> PipeLine<M,T> link(PipeLine<R,T> pipe)
    {
        read.link(pipe.write);
        return new PipeLine(write,pipe.read);
    }

    public R filter(M a)
    {
        return null;
    }
}
