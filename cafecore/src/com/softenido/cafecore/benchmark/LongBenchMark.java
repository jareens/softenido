/*
 * LongBenchMark.java
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

import java.util.Random;
import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author franci
 */
public class LongBenchMark extends Benchmark
{
    private static double SPEED = 1.12707;
    private static int SIZE = 32*1024;
    public LongBenchMark(int units, CyclicBarrier barrier)
    {
        super("long", SPEED, barrier, units);
    }

    @Override
    public void loop(int seed)
    {
        long sum =0;
        Random random = new Random(seed);
        for(int i=0;i<SIZE;i++)
        {
            long n1  = random.nextInt(SIZE);
            long n2  = i*i+1;
            long mul = n1++ * n2++;
            long div = n1++ / n2++;
            long and = n1++ & n2++;
            long or  = n1++ | n2++;
            long xor = n1++ ^ n2++;
            sum += mul + div + and + or + xor + n1 + n2;
        }
        sum += seed;
        this.checkSum=(int)sum;
    }
}
