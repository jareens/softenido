/*
 * SqrtBenchMark.java
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

import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author franci
 */
public class SqrtBenchMark extends Benchmark
{
    private static double SPEED = 1.17638;
    static int SIZE = 64*1024;
    public SqrtBenchMark(int units, CyclicBarrier barrier)
    {
        super("sqrt", SPEED, barrier, units);
    }

    @Override
    public void loop(int seed)
    {
        double sum =1;

        for(int i=0;i<SIZE;i++)
        {
            sum  = Math.sqrt(Math.sqrt(sum)+Math.sqrt(seed+i));
        }
        this.checkSum=(int)sum;
    }
}
