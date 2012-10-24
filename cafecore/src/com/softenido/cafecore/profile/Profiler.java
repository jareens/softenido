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
    public int close(ProfileRecord rec)
    {
        //do nothing
        return 0;
    }
    @Override
    public String toString()
    {
        return "Profile{}";
    }
}

class NanoProfiler extends Profiler
{
    private static final int NANOS_PER_MILLIS = 1000000;

    private final Object lock = new Object();
    private long count   = 0;
    private long nsSum = 0;
    private long nsMin = Long.MAX_VALUE;
    private long nsMax = 0;
    private long nsFirst = 0;
    private long nsLast = 0;

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
    public int close(ProfileRecord rec)
    {
        if(rec!=null)
        {
            long delta = rec.estimatedTime();
            synchronized(lock)
            {
                count++;
                nsSum +=delta;
                if(delta< nsMin)
                {
                    nsMin = delta;
                }
                if(delta> nsMax)
                {
                    nsMax = delta;
                }
                if(count==1)
                {
                    nsFirst = delta;
                }
                nsLast = delta;
                return (int)(delta/NANOS_PER_MILLIS);
            }
        }
        return 0;
    }
    @Override
    public String toString()
    {
        long num, msSum, msAvg, msMin, msMax, msFirst, msLast;
        synchronized(lock)
        {
            if(count==0)
            {
                return "Profile{count=0}";
            }
            num    = count;
            msSum  = nsSum  / NANOS_PER_MILLIS;
            msAvg  = msSum  / num;
            msMin  = nsMin  / NANOS_PER_MILLIS;
            msMax  = nsMax  / NANOS_PER_MILLIS;
            msFirst= nsFirst/ NANOS_PER_MILLIS;
            msLast = nsLast / NANOS_PER_MILLIS;
        }
        StringBuilder sb = new StringBuilder();
        String tab   = lineFeed?"  ":"";
        String sep   = lineFeed?"\n":" ";
        String msSep = lineFeed?"ms,\n":"ms, ";
        sb.append("Profile[").append(name).append(']').append(sep)
          .append('{').append(sep)
          .append(tab).append("num=").append(num).append(msSep)
          .append(tab).append("avg=").append(msAvg).append(msSep)
          .append(tab).append("sum=").append(msSum).append(msSep)
          .append(tab).append("min=").append(msMin).append(msSep)
          .append(tab).append("max=").append(msMax).append(msSep)
          .append(tab).append("first=").append(msFirst).append(msSep)
          .append(tab).append("last=").append(msLast).append(msSep)
          .append('}');
        return sb.toString();
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
    public static Profiler getProfiler(Profiler profiler, String name)
    {
        return (profiler!=null) ? profiler : manager.demandLogger(name);
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
    public abstract int close(ProfileRecord rec);
    
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
