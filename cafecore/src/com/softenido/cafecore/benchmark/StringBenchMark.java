/*
 * StringBenchMark.java
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.cafecore.benchmark;

import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author franci
 */
public class StringBenchMark extends Benchmark
{
    private static double SPEED = 1.48768;
    static int SIZE = 512;
    public StringBenchMark(int units, CyclicBarrier barrier)
    {
        super("String", SPEED, barrier, units);
    }
    
    @Override
    public void loop(int seed)
    {
        int sum=0;
        for(int i=0;i<SIZE;i++)
        {
            String s = ("["+(seed+i)+"]").replace("1", "a").replace("2", "B").replace("3", "C");
            String lower = s.toLowerCase();
            String upper = s.toUpperCase();
            sum = (lower.equals(upper)? s+lower+upper:s).length();
        }
        checkSum = sum;
    }
}
