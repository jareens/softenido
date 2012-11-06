/*
 * MD5BenchMark.java
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author franci
 */
public class MD5BenchMark extends Benchmark
{
    private static double SPEED = 7.91348;
    static int SIZE = 64;
    public MD5BenchMark(int units, CyclicBarrier barrier)
    {
        super("md5", SPEED, barrier, units);
    }

    @Override
    public void loop(int seed)
    {
        try
        {
            Random random = new Random(seed);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] data = new byte[1024];
            random.nextBytes(data);
            for(int i=0;i<SIZE;i++)
            {
                md.digest(data);
            }
            this.checkSum=(int)md.digest().length;
        }
        catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(MD5BenchMark.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
