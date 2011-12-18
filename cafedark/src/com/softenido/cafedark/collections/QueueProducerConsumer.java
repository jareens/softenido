/*
 *  QueueProducerConsumer.java
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
package com.softenido.cafedark.collections;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author franci
 */
public class QueueProducerConsumer<E> implements ProducerConsumer<E>, Iterator<E>
{

    private final Object lock = new Object();
    private final BlockingQueue<E> queue;
    private final E poison;
    private boolean alive = true;
    private E cache = null;

    public QueueProducerConsumer(BlockingQueue<E> queue, E poison)
    {
        this.queue = queue;
        this.poison = poison;
    }

    public boolean hasNext()
    {
        synchronized (lock)
        {
            try
            {
                while (alive && cache == null)
                {
                    cache = queue.take();
                }
                if (cache == poison)
                {
                    alive = false;
                }
                return alive;
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(QueueProducerConsumer.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
    }

    public E next()
    {
        synchronized (lock)
        {
            if (hasNext())
            {
                E val = cache;
                cache = null;
                return val;
            }
            return null;
        }
    }

    public void add(E o)
    {
        try
        {
            queue.put(o);
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(QueueProducerConsumer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Iterator<E> iterator()
    {
        return this;
    }

    public void remove()
    {
        throw new UnsupportedOperationException();
    }
}
