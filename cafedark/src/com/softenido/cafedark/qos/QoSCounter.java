/*
 *  QosRule.java
 *
 *  Copyright (C) 2010  Francisco Gómez Carrasco
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
package com.softenido.cafedark.qos;

/**
 *
 * @author franci
 */
public class QoSCounter
{
    private static final long TIMES[] = {100,200,400,800,1600,3200,6400,12800};
    private final long times[] = new long[TIMES.length];
    private final long bytes[] = new long[TIMES.length];
    
    private final Object lock= new Object();
    private QoSCounter parent=null;
    private long count;
    private volatile int bps;
    private volatile int delay;

    public QoSCounter(int bps, int delay)
    {
        this.bps = bps;
        this.delay = delay;
    }

    public int getBps()
    {
        return bps;
    }

    public void setBps(int bps)
    {
        this.bps = bps;
    }

    public long getCount()
    {
        return count;
    }

    public int getDelay()
    {
        return delay;
    }

    public void setDelay(int delay)
    {
        this.delay = delay;
    }

    public QoSCounter getParent()
    {
        return parent;
    }

    public void setParent(QoSCounter parent)
    {
        this.parent = parent;
    }
    public void update(int count)
    {
        synchronized (lock)
        {
            long tini = System.currentTimeMillis();
//            parent.add(count);
            this.count += count;
        }
    }


}
