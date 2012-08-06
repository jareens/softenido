/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.cafecore.statistics;

import java.security.InvalidParameterException;

/**
 *
 * @author franci
 */
public abstract class MovingAverage
{
    static public final int SMA=0; 
    static public final int EMA=1; 
    static public final int CMA=1; 
    static public final int WMA=1; 

    public abstract double next(double value);
    public abstract double[] next(double[] ma,double[] value);
    public abstract double[] next(double[] ma,double[] value, boolean down);
    
    static MovingAverage build(int mode, int np)
    {
        switch(mode)
        {
            case SMA: 
                return buildSMA(np);
            case EMA: 
                return buildEMA(np);
            default: 
                throw new InvalidParameterException("Unknown mode "+mode);
        }
    }
    static SimpleMovingAverage buildSMA(int np)
    {
        return new SimpleMovingAverage(np);
    }
    static ExponentialMovingAverage buildEMA(int np)
    {
        return new ExponentialMovingAverage(np);
    }
    
}
