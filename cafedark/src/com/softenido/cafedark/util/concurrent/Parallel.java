/*
 *  Parallel.java
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
package com.softenido.cafedark.util.concurrent;

import com.softenido.cafedark.util.concurrent.actor.Actor;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author franci
 */
public class Parallel
{
    public static <A,B> void foreach(A[] a, B[] b, Exception[] e, Filter<A,B> filter,int threads)
    {
        Actor<A,B> act = new Actor<A,B>(threads, filter);
        Value<B>[] values = new Value[a.length];
        for(int i = 0;i<a.length;i++)
        {
            b[i]=null;
            e[i]=null;
            try
            {
                values[i] = act.send(a[i]);
            }
            catch (InterruptedException ex)
            {
                e[i]=ex;
                Logger.getLogger(Parallel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for(int i = 0;i<values.length;i++)
        {
            if(b[i]!=null)
            {
                try
                {
                    b[i] = values[i].get();
                }
                catch (ExecutionException ex)
                {
                    e[i]=ex;
                    Logger.getLogger(Parallel.class.getName()).log(Level.SEVERE, null, ex);
                }
                catch (InterruptedException ex)
                {
                    e[i]=ex;
                    Logger.getLogger(Parallel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
         }
    }
    public static <A,B> void foreach(A[] a, B[] b, Exception[] e, Filter<A,B> filter)
    {
        foreach(a, b, e, filter,1);
    }
    public static <A,B> void foreach(A[] a, B[] b, Filter<A,B> filter)
    {
        Exception[] e = new Exception[a.length];
        foreach(a, b, e, filter,1);
    }

}
