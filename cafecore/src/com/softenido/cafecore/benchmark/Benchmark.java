/*
 * Benchmark.java
 *
 * Copyright (c) 2012 Francisco Gómez Carrasco
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Report bugs or new features to: flikxxi@gmail.com
 */
package com.softenido.cafecore.benchmark;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author franci
 */
public abstract class Benchmark implements Runnable
{
    private final String name;
    private final AtomicBoolean started = new AtomicBoolean(true);
    private final AtomicInteger count= new AtomicInteger();
    private final AtomicInteger round= new AtomicInteger();
    private final AtomicInteger millis= new AtomicInteger();
    private final double speed;
    private final CyclicBarrier barrier;
    private final int units;
    volatile int checkSum=0;

    public Benchmark(String name, double speed, CyclicBarrier barrier, int units)
    {
        this.name=name;
        this.speed=speed;
        this.barrier=barrier;
        this.units=units;
    }
    public Benchmark(String name, double speed, CyclicBarrier barrier)
    {
        this(name, speed, barrier, BenchmarkManager.UNITS);
    }
    
    public abstract void loop(int seed);
    
    public final void run()
    {
        try
        {
            barrier.await();
            long ini = System.nanoTime();
            while(this.started.get())
            {
                loop(count.getAndIncrement());
            }
            long end = System.nanoTime();
            long ms = (end-ini)/BenchmarkManager.NANOS_PER_MILLIS;
            millis.addAndGet((int)ms);
            round.incrementAndGet();
            barrier.await();
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(Benchmark.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (BrokenBarrierException ex)
        {
            Logger.getLogger(Benchmark.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getCount()
    {
        return count.get();
    }

    protected int getCheckSum()
    {
        return checkSum;
    }

    public String getName()
    {
        return name;
    }

    public double getRate()
    {
        return (units*count.get()*round.get())/(millis.get()*speed);
    }
    void start()
    {
        this.started.set(true);
    }
    void stop()
    {
        this.started.set(false);
    }
}
