/*
 *  PipeLines.java
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
package com.softenido.cafe.util.pipeline;


/**
 *
 * @author franci
 */
public class PipeLines
{
    static volatile boolean defaultAsync = true;

    public static <A,B> PipeLine<A,B> build(Filter<A,B> filter,boolean async)
    {
        if(async)
        {
            return new ASyncPipeLine<A, B>(filter);
        }
        else
        {
            return new SyncPipeLine<A,B>(filter);
        }
    }
    public static <A,B> PipeLine<A,B> build(Filter<A,B> filter)
    {
        return build(filter,defaultAsync);
    }

    public static boolean isDefaultAsync()
    {
        return defaultAsync;
    }

    public static void setDefaultAsync(boolean defaultAsync)
    {
        PipeLines.defaultAsync = defaultAsync;
    }
}
