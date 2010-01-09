/*
 *  EchoSocket.java
 *
 *  Copyright (C) 2010 Francisco Gómez Carrasco
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
package com.softenido.echoserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author franci
 */
public class EchoSocket implements Runnable
{
    private static final int PERMITS = 1024;
    private static int permits = 1024;
    private static final AtomicInteger tasks = new AtomicInteger();
    private static final AtomicInteger count = new AtomicInteger();
    private static final Object lock = new Object();
    private static final Semaphore sem = new Semaphore(PERMITS);
    private static final byte[] EXIT_R = "EXIT\r".getBytes();
    private static final byte[] EXIT_N = "EXIT\n".getBytes();

    private final Socket sc;
    private final int size;
    private final int timeout;
    private final boolean exit;

    public static int getPermits()
    {
        synchronized(lock)
        {
            return permits;
        }
    }

    public static int getTasks()
    {
        return tasks.get();
    }
    public static int getCount()
    {
        return count.get();
    }

    public static void setPermits(int permits) throws InterruptedException
    {
        synchronized(lock)
        {
            int dif = (permits - EchoSocket.permits);
            if(dif>0)
            {
                sem.release(dif);
            }
            else if(dif<0)
            {
                sem.acquire(-dif);
            }
            EchoSocket.permits = permits;
        }
    }

    public EchoSocket(Socket sc,int size, int timeout, boolean exit)
    {
        this.sc = sc;
        this.size = size;
        this.timeout = timeout;
        this.exit = exit;
    }
    public EchoSocket(Socket sc,int size, int timeout)
    {
        this(sc, size, timeout, false);
    }

    public static EchoSocket getEchoSocket(Socket sc,int size, int timeout) throws InterruptedException
    {
        return getEchoSocket(sc, size, timeout, false);
    }
    public static EchoSocket getEchoSocket(Socket sc,int size, int timeout, boolean exit) throws InterruptedException
    {
        sem.acquire();
        try
        {
            return new EchoSocket(sc, size, timeout, exit);
        }
        finally
        {
            sem.release();
        }
    }
    public void run()
    {
        count.incrementAndGet();
        tasks.incrementAndGet();
        try
        {
            sem.acquire();
            try
            {
                try
                {
                    byte[] buf = new byte[size];
                    sc.setSoTimeout(timeout);
                    InputStream is = sc.getInputStream();
                    OutputStream os = sc.getOutputStream();
                    int r;
                    while ((r = is.read(buf)) >= 0)
                    {
                        if(exit && isExit(buf))
                            break;
                        os.write(buf, 0, r);
                    }
                }
                catch (SocketTimeoutException ex)
                {
                    return;
                }
                catch (IOException ex)
                {
                    Logger.getLogger(EchoSocket.class.getName()).log(Level.SEVERE, null, ex);
                    return;
                }
                finally
                {
                    try
                    {
                        sc.close();
                    }
                    catch (IOException ex)
                    {
                        Logger.getLogger(EchoSocket.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            finally
            {
                sem.release();
            }
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(EchoSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            tasks.decrementAndGet();
        }
    }

    private static boolean isExit(byte[] buf)
    {
        byte[] exit = Arrays.copyOf(buf,EXIT_R.length);
        return (Arrays.equals(EXIT_R,exit) || Arrays.equals(EXIT_N,exit));
    }

}
