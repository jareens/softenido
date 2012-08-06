/*
 * CumulativeExponentialMovingAverage.java
 *
 * Copyright (c) 2012  Francisco Gómez Carrasco
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
package com.softenido.cafecore.statistics;

/**
 *
 * @author franci
 */
public class CumulativeExponentialMovingAverage extends MovingAverage
{
    private int count;
    private double cma=0;

    public CumulativeExponentialMovingAverage()
    {
    }

    @Override
    public double next(double value)
    {
        if(count==0)
        {
            cma = value;
        }
        else
        {
            cma = cma + (value-cma)/(count+1);
        }
        count++;
        return cma;
    }
    public double[] next(double[] ema, double[] value)
    {
        if(ema.length!=value.length)
        {
            ema = new double[value.length];
        }
        for(int i=0;i<value.length;i++)
        {
            ema[i] = next(value[i]);
        }
        for(int i=value.length;i<ema.length;i++)
        {
            ema[i] = 0;
        }
        return ema;
    }

    @Override
    public double[] next(double[] ma, double[] value, boolean down)
    {
        return new double[0];
    }


}
