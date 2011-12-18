/*
 *  FastSynchronizer.java
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

/**
 *
 * @author franci
 */
public class FastSynchronizer
{

    private boolean waiting = false;
    private Object lock = this;
    
    public FastSynchronizer()
    {
        super();
    }
    public FastSynchronizer(Object lock)
    {
        super();
        this.lock = lock;
    }

    public final void fastWait() throws java.lang.InterruptedException
    {
        synchronized (lock)
        {
            waiting = true;
            lock.wait();
        }
    }

    public final void fastWait(long timeout, java.util.concurrent.TimeUnit unit) throws java.lang.InterruptedException
    {
        long millis = unit.toMillis(timeout);
        int nanos = (int) (unit.toNanos(timeout) % 1000000L);
        synchronized (lock)
        {
            waiting = true;
            lock.wait(millis, nanos);
        }
    }

    public final void fastNotify()
    {
        synchronized (lock)
        {
            if (waiting)
            {
                lock.notify();
                waiting = false;
            }
        }
    }

    public final void fastNotifyAll()
    {
        synchronized (lock)
        {
            if (waiting)
            {
                lock.notifyAll();
                waiting = false;
            }
        }
    }
}
