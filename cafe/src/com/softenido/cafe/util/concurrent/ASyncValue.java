/*
 *  ASyncValue.java
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
package com.softenido.cafe.util.concurrent;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author franci
 */
public abstract class ASyncValue<V> implements Runnable
{
    private final Object lock = new Object();
    private V value = null;
    private Exception exception = null;
    private volatile boolean done = false;

    public V getValue()
    {
        if (!done)
        {
            run();
        }
        return value;
    }

    public Exception getException()
    {
        if (!done)
        {
            run();
        }
        return exception;
    }

    public final void run()
    {
        synchronized (lock)
        {
            if (!done)
            {
                try
                {
                    value = call();
                    done = true;
                } 
                catch (Exception ex)
                {
                    value = null;
                    exception = ex;
                    done = true;
                    Logger.getLogger(ASyncValue.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    protected abstract V call();
}
