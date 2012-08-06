/*
 * ExponentialMovingAverage.java
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
public class ExponentialMovingAverage extends MovingAverage
{
    final double k;
    private int count;
    private double ema=0;
    final int n;

    public ExponentialMovingAverage(int np)
    {
        this.n = np;
        k = 2/(np+1);
    }

    @Override
    public double next(double value)
    {
        if(count==0)
        {
            ema = value;
        }
        else
        {
            ema = k*ema + (1-k)*ema;
        }
        count++;
        return ema;
    }

    @Override
    public double[] next(double[] ma, double[] value)
    {
        return new double[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public double[] next(double[] ema, double[] value, boolean down)
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
    
    
}
