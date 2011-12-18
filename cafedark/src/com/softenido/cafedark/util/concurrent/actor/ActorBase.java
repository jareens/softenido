/*
 *  ActorBase.java
 *
 *  Copyright (C) 2009-2010  Francisco Gómez Carrasco
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
package com.softenido.cafedark.util.concurrent.actor;

import com.softenido.cafedark.util.concurrent.Value;

/**
 *
 * @author franci
 */
public interface ActorBase<M,R>
{
    public Value<R> send(final M m) throws InterruptedException;
    public Value<R> send(final Value<M> m) throws InterruptedException;
    public void execute(Runnable task) throws InterruptedException;
}
