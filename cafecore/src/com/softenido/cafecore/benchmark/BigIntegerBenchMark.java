/*
 * BigIntegerBenchMark.java
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

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author franci
 */
public class BigIntegerBenchMark extends Benchmark
{
    private static double SPEED = 1.53338;
    private static int SIZE = 1024;
    public BigIntegerBenchMark(int units, CyclicBarrier barrier)
    {
        super("BigInteger", SPEED, barrier, units);
    }

    @Override
    public void loop(int seed)
    {
        BigInteger sum =BigInteger.ZERO;
        Random random = new Random(seed);
        for(int i=0;i<SIZE;i++)
        {
            BigInteger n1  = BigInteger.valueOf(random.nextInt(SIZE+seed));
            BigInteger n2  = BigInteger.valueOf(i*i+1);
            BigInteger mul = n1.multiply(n2);
            n1 = n1.add(BigInteger.ONE);
            n2 = n2.add(BigInteger.ONE);
            BigInteger div = n1.divide(n2);
            n1 = n1.add(BigInteger.ONE);
            n2 = n2.add(BigInteger.ONE);
            BigInteger and = n1.and(n2);
            n1 = n1.add(BigInteger.ONE);
            n2 = n2.add(BigInteger.ONE);
            BigInteger or  = n1.or(n2);
            n1 = n1.add(BigInteger.ONE);
            n2 = n2.add(BigInteger.ONE);
            BigInteger xor = n1.xor(n2);
            n1 = n1.add(BigInteger.ONE);
            n2 = n2.add(BigInteger.ONE);
            sum = sum.add(mul).add(div).add(and).add(or).add(xor).add(n1).add(n2);
        }
        this.checkSum= sum.mod(BigInteger.valueOf(Integer.MAX_VALUE)).intValue();
    }
}
