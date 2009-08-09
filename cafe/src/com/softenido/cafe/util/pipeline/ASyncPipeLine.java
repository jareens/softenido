/*
 *  ASyncPipeLine.java
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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author franci
 */
public class ASyncPipeLine<A,B> extends SyncPipeLine<A,B>
{
    private static final int CORES = Runtime.getRuntime().availableProcessors();
    private static final int POOL_SIZE = CORES*CORES+CORES;
    private static final int KEEP_ALIVE_TIME = 100;

    private static final BlockingQueue<Runnable> inbox = new LinkedBlockingQueue<Runnable>();
    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(POOL_SIZE, POOL_SIZE, KEEP_ALIVE_TIME , TimeUnit.MILLISECONDS, inbox);
    {
        executor.allowCoreThreadTimeOut(true);
    }

    public ASyncPipeLine(Filter<A, B> filter)
    {
        super(filter);
    }

    public ASyncPipeLine()
    {
    }
    
    @Override
    protected void execute(Runnable task)
    {
        executor.execute(task);
    }

}
