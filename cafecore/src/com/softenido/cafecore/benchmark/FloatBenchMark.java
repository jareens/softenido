/*
 * FloatBenchMark.java
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
public class FloatBenchMark extends Benchmark
{
    private static double SPEED = 2.40192;
    private static int SIZE = 32*1024;
    public FloatBenchMark(int units, CyclicBarrier barrier)
    {
        super("float", SPEED, barrier, units);
    }

    @Override
    public void loop(int seed)
    {
        Random random = new Random(seed);
        float sum =0;

        for(int i=0;i<SIZE;i++)
        {
            float fi = i;
            float n1  = random.nextFloat();
            float n2  = fi*fi+1f;
            float mul = n1++ * n2++;
            float div = n1++ / n2++;
            sum += mul + div + n1 + n2;
        }
        sum += seed;
        this.checkSum=(int)sum;
    }
}
