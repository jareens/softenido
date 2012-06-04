/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.trading.indicator;

/**
 *
 * @author franci
 */
public class CachedIndicators extends Indicators
{
    final Object lock = new Object();

    double[] emaPrice=null;
    double[] emaValue=null;
    @Override
    double[] EMA(int n, double[] price)
    {
        synchronized(lock)
        {
            if(emaPrice!=price)
            {
                emaPrice = price;
                emaValue = super.EMA(n, price);
            }
            return emaValue;
        }
    }

    double[] forceIndexPrice=null;
    double[] forceIndexVolume=null;
    double[] forceIndexValue=null;
    @Override
    double[] ForceIndex(final double[] price, final double[] volume)
    {
        synchronized(lock)
        {
            if(forceIndexPrice!=price||forceIndexVolume!=volume)
            {
                forceIndexPrice=price;
                forceIndexVolume=volume;
                forceIndexValue=super.ForceIndex(price, volume);
            }
            return forceIndexValue;
        }
    }

    int macdFastPeriod=0;
    int macdSlowPeriod=0;
    double[] macdPrice=null;
    double[] macdLine=null;
    double[] macdSignal=null;
    double[] macdHistogram=null;
    private void MACD(int fastPeriod, int slowPeriod, int signalPeriod, double[] price)
    {
        synchronized(lock)
        {
            if(macdFastPeriod!=fastPeriod || macdSlowPeriod!=slowPeriod || macdPrice!=price)
            {
                macdFastPeriod=fastPeriod;
                macdSlowPeriod=slowPeriod;
                macdPrice=price;
                macdLine=new double[price.length];
                macdSignal=new double[price.length];
                macdHistogram=new double[price.length];
                super.MACD(fastPeriod, slowPeriod, signalPeriod, price, macdLine, macdSignal, macdHistogram);                
            }
        }
    }

    @Override
    double[] MACDH(int fastPeriod, int slowPeriod, int signalPeriod, double[] price)
    {
        synchronized(lock)
        {
            MACD(fastPeriod, slowPeriod, signalPeriod, price);
            return macdHistogram;
        }        
    }

    @Override
    double[] MACDL(int fastPeriod, int slowPeriod, int signalPeriod, double[] price)
    {
        synchronized(lock)
        {
            MACD(fastPeriod, slowPeriod, signalPeriod, price);
            return macdLine;
        }        
    }

    @Override
    double[] MACDS(int fastPeriod, int slowPeriod, int signalPeriod, double[] price)
    {
        synchronized(lock)
        {
            MACD(fastPeriod, slowPeriod, signalPeriod, price);
            return macdSignal;
        }        
    }
    
}
