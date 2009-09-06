/*
 *  ActorSync.java
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
import com.softenido.cafe.util.concurrent.ValueLink;
import com.softenido.cafe.util.concurrent.ValueSync;
import com.softenido.cafe.util.concurrent.Value;

/**
 *
 * @author franci
 */
public class ActorSync<M,R> extends ActorBase<M,R> implements Filter<M,R>
{
    public ActorSync(Filter<M, R> filter)
    {
        super(filter);
    }
    public ActorSync()
    {
        super(null);
    }

    public Value<R> send(final M m) throws InterruptedException
    {
        return new ValueSync<R>(filter.filter(m));
    }

    @Override
    public Value<R> send(Value<M> m) throws InterruptedException
    {
        return new ValueLink<M,R>(filter,m);
    }

    @Override
    public void execute(Runnable task)
    {
        task.run();
    }
}
