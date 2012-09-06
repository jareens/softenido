/*
 * Profiler.java
 *
 * Copyright (c) 2012  Francisco Gómez Carrasco
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

package com.softenido.cafecore.profile;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Set;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

class NullProfiler extends Profiler
{
    NullProfiler(String name)
    {
        super(name);
    }

    @Override
    public ProfileRecord open()
    {
        return null;
    }

    @Override
    public void close(ProfileRecord rec)
    {
        //do nothing
    }
    int k;

    @Override
    public String toString()
    {
        return "Profile{}";
    }
}

class NanoProfiler extends Profiler
{
    private final Object lock = new Object();
    private long count = 0;
    private long nanos = 0;
    private long min = Long.MAX_VALUE;
    private long max = 0;
    private long first = 0;
    private long last = 0;

    NanoProfiler(String name)
    {
        super(name);
    }

    @Override
    public ProfileRecord open()
    {
        return new ProfileRecord();
    }

    @Override
    public void close(ProfileRecord rec)
    {
        if(rec!=null)
        {
            long delta = rec.estimatedTime();
            synchronized(lock)
            {
                count++;
                nanos+=delta;
                if(delta<min)
                {
                    min = delta;
                }
                if(delta>max)
                {
                    max = delta;
                }
                if(count==1)
                {
                    first = delta;
                }
                last = delta;
            }
        }
    }
    @Override
    public String toString()
    {
        long num, millis, avgms, minms, maxms, firstms, lastms;
        synchronized(lock)
        {
            if(count==0)
            {
                return "Profile{count=0}";
            }
            num = count;
            millis= nanos/1000000;
            avgms = millis/num;
            minms = min/1000000;
            maxms = max/1000000;
            firstms = max/1000000;
            lastms = max/1000000;
        }
        StringBuilder sb = new StringBuilder();
        String ln  = lineFeed?"\n":" ";
        String tab = lineFeed?"  ":"";

        return "Profile["+name+"]{ num="+num+
                  ", avg="+avgms+
                "ms, sum="+millis+
                "ms, min="+minms+
                "ms, max="+maxms+
                "ms, first="+firstms+
                "ms, last="+lastms+"ms }";
    }
    
}
/**
 *
 * @author franci
 */
public abstract class Profiler
{
    final String name;
    Profiler(String name)
    {
        this.name = name;
    }

    static volatile ProfileManager manager = ProfileManager.getProfileManager(false);
    static volatile boolean active = false;
    
    public static Profiler getProfiler(String name)
    {
        return manager.demandLogger(name);
    }
    public static void setActive(boolean value)
    {
        manager = ProfileManager.getProfileManager(value);
        active = value;
    }
    public static boolean getActive(boolean value)
    {
        return active;
    }
    
    public abstract ProfileRecord open();
    public abstract void close(ProfileRecord rec);
    
    static boolean lineFeed=false;//use lineFeed

    public static boolean isLineFeed()
    {
        return lineFeed;
    }

    public static void setLineFeed(boolean lineFeed)
    {
        Profiler.lineFeed = lineFeed;
    }
    public static void print(PrintStream out)
    {
        if(active)
        {
            String[] keys = manager.map.keySet().toArray(new String[0]);
            Arrays.sort(keys);
            int count=0;
            for( String key : keys)
            {
                out.println(Profiler.getProfiler(key).toString());
            }
            out.println("profilers printed:"+count);
        }
    }    
}
