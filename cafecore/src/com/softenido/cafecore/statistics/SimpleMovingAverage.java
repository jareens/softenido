/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.cafecore.statistics;

/**
 *
 * @author franci
 */
public class SimpleMovingAverage extends MovingAverage 
{
    private int count;
    private double sma=0;
    final int n;
    boolean down = false;

    public SimpleMovingAverage(int np)
    {
        this.n = np;
    }

    @Override
    public double next(double value)
    {
        if(count==0)
        {
            sma = value;
        }
        else
        {
            sma = sma - (sma/n) + (value/n);
        }
        count++;
        return sma;
    }
    
    public double[] next(double[] ema, double[] value)
    {
        if(ema.length!=value.length)
        {
            ema = new double[value.length];
        }
        if(!down)
        {
            for(int i=0;i<value.length;i++)
            {
                ema[i] = next(value[i]);
            }
            for(int i=value.length;i<ema.length;i++)
            {
                ema[i] = 0;
            }
        }
        else
        {
//            tal vez esta función mejor en MovingAverage
        }
        return ema;
    }

    @Override
    public double[] next(double[] ma, double[] value, boolean down)
    {
        return new double[0];  //To change body of implemented methods use File | Settings | File Templates.
    }
}
