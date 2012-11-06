/*
 * NewBenchMark.java
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
public class NewBenchMark extends Benchmark
{
    private static double SPEED = 1.77237;
    private static int SIZE = 250;
    public NewBenchMark(int units, CyclicBarrier barrier)
    {
        super("new", SPEED, barrier, units);
    }
    byte[][] bytes = null;
    char[][] chars = null;
    int[][] ints = null;
    long[][] longs = null;
    
    //let mem be escapable from scope, preventing from been overoptimized
    @Override
    public void loop(int seed)
    {
        byte[][] b = new byte[SIZE][SIZE];
        char[][] c = new char[SIZE][SIZE];
        int[][] i = new int[SIZE][SIZE];
        long[][] ii = new long[SIZE][SIZE];
        int sum = b[0].length + c[0].length + i[0].length + ii[0].length;
        checkSum = sum;
        this.bytes = b;
        this.chars = c;
        this.ints = i;
        this.longs = ii;
    }
}
