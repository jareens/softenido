/*
 *  ActorBase.java
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
package com.softenido.cafe.util.concurrent.actor;

import com.softenido.cafe.util.concurrent.Filter;
import com.softenido.cafe.util.concurrent.Value;

/**
 *
 * @author franci
 */
public abstract class ActorBase<M,R> implements Filter<M,R>
{
    protected final Filter<M,R> filter;

    public ActorBase(Filter<M, R> filter)
    {
        this.filter = (filter!=null)? filter:this;
    }
    public ActorBase()
    {
        this(null);
    }
    public R filter(M a)
    {
        return null;
    }
    
    abstract public Value<R> send(final M m) throws InterruptedException;
    abstract public Value<R> send(final Value<M> m) throws InterruptedException;

    abstract public void execute(Runnable task) throws InterruptedException;

}
