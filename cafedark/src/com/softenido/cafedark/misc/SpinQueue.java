/*
 *  SpinQueue.java
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

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import com.softenido.cafedark.misc.FastSynchronizer;

/**
 *
 * @author franci
 */
public class SpinQueue<E> extends AbstractQueue<E> implements Queue<E>, BlockingQueue<E>
{

    private FastSynchronizer syncProducer = new FastSynchronizer();
    private FastSynchronizer syncConsumer = new FastSynchronizer();

    private Queue<E> wQueue = null;
    private Queue<E> rQueue = null;

    private int wNum = 0;
    private int rNum = 0;

    public SpinQueue(int capacity)
    {
        wQueue = new ArrayBlockingQueue<E>(capacity);
        rQueue = new ArrayBlockingQueue<E>(capacity);
    }

    public SpinQueue()
    {
        this(1024);
    }
    
    // solo para consumidores
    private final void spin()
    {
        Queue<E> tmp;
        synchronized (syncProducer)
        {
            if (rQueue.isEmpty() && !wQueue.isEmpty())
            {
                tmp = rQueue;
                rQueue = wQueue;
                wQueue = tmp;
                syncProducer.fastNotifyAll();
            }
        }
    }

    @Override
    public boolean offer(E e)
    {
        boolean ret;
        synchronized (syncProducer)
        {
            ret = wQueue.offer(e);
            if (ret)
            {
                wNum++;
            }
        }
        if (ret)
        {
            syncConsumer.fastNotifyAll();
            return true;
        }
        return false;
    }

    @Override
    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException
    {
        synchronized (syncProducer)
        {
            if (offer(e))
            {
                return true;
            }
            syncProducer.fastWait(timeout, unit);

            if (offer(e))
            {
                return true;
            }
            return false;
        }
    }

    public void put(E e) throws InterruptedException
    {
        while (!offer(e))
        {
            syncProducer.fastWait();
        }
    }

    @Override
    public E poll()
    {
        E ret;
        synchronized (syncConsumer)
        {
            ret = rQueue.poll();
            if (ret != null)
            {
                rNum++;
                return ret;
            }

            spin();

            ret = rQueue.poll();
            if (ret != null)
            {
                rNum++;
                return ret;
            }
            return null;
        }
    }

    public E poll(long timeout, TimeUnit unit) throws InterruptedException
    {
        E ret;

        synchronized (syncConsumer)
        {
            ret = poll();

            spin();

            ret = poll();

            if (ret != null)
            {
                return ret;
            }
            syncConsumer.fastWait(timeout, unit);

            return rQueue.poll();
        }
    }

    public E take() throws InterruptedException
    {
        E ret;
        synchronized (syncConsumer)
        {
            ret = poll();
            while (ret == null)
            {
                spin();

                ret = poll();
                if (ret != null)
                {
                    break;
                }
                syncConsumer.fastWait();
            }
            return ret;
        }
    }

    @Override
    public E peek()
    {
        E ret;
        synchronized (syncConsumer)
        {
            ret = rQueue.peek();
            if (ret != null)
            {
                return ret;
            }
            spin();

            return rQueue.peek();
        }
    }

    public Iterator<E> iterator()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int size()
    {
        return wNum - rNum;
    }


    public int remainingCapacity()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int drainTo(Collection<? super E> c)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int drainTo(Collection<? super E> c, int maxElements)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
