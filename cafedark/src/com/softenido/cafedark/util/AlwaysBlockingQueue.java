/*
 *  AlwaysBlockingQueue.java
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
package com.softenido.cafedark.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author franci
 */
public class AlwaysBlockingQueue<E> implements BlockingQueue<E>
{
    final BlockingQueue<E> queue;

    public AlwaysBlockingQueue(BlockingQueue<E> queue)
    {
        this.queue = queue;
    }

    public <T> T[] toArray(T[] a)
    {
        return queue.toArray(a);
    }

    public Object[] toArray()
    {
        return queue.toArray();
    }

    public int size()
    {
        return queue.size();
    }

    public boolean retainAll(Collection<?> c)
    {
        return queue.retainAll(c);
    }

    public boolean removeAll(Collection<?> c)
    {
        return queue.removeAll(c);
    }

    public Iterator<E> iterator()
    {
        return queue.iterator();
    }

    public boolean isEmpty()
    {
        return queue.isEmpty();
    }

    public boolean containsAll(Collection<?> c)
    {
        return queue.containsAll(c);
    }

    public void clear()
    {
        queue.clear();
    }

    public boolean addAll(Collection<? extends E> c)
    {
        return queue.addAll(c);
    }

    public E remove()
    {
        try
        {
            return queue.take();
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(AlwaysBlockingQueue.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public E poll()
    {
        return queue.poll();
    }

    public E peek()
    {
        return queue.peek();
    }

    public E element()
    {
        return queue.element();
    }

    public E take() throws InterruptedException
    {
        return queue.take();
    }

    public boolean remove(Object o)
    {
        return queue.remove(o);
    }

    public int remainingCapacity()
    {
        return queue.remainingCapacity();
    }

    public void put(E e) throws InterruptedException
    {
        queue.put(e);
    }

    public E poll(long timeout, TimeUnit unit) throws InterruptedException
    {
        return queue.poll(timeout, unit);
    }

    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException
    {
        return queue.offer(e, timeout, unit);
    }

    public boolean offer(E e)
    {
        return queue.offer(e);
    }

    public int drainTo(Collection<? super E> c, int maxElements)
    {
        return queue.drainTo(c, maxElements);
    }

    public int drainTo(Collection<? super E> c)
    {
        return queue.drainTo(c);
    }

    public boolean contains(Object o)
    {
        return queue.contains(o);
    }

    public boolean add(E e)
    {
        try
        {
            queue.put(e);
            return true;
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(AlwaysBlockingQueue.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
