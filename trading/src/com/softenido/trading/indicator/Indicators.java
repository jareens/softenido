/*
 * Indicators.java
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.trading.indicator;

/**
 *
 * @author franci
 */
public class Indicators
{
    static private double[] add(double a[], double[] b, double[] c)
    {
        for(int i=0;i<c.length;i++)
        {
            c[i] = a[i]+b[i];
        }
        return c;
    }
    static private double[] add(double a[], double[] b)
    {
        return add(a,b,new double[a.length]);
    }
    static private double[] sub(double a[], double[] b, double[] c)
    {
        for(int i=0;i<c.length;i++)
        {
            c[i] = a[i]-b[i];
        }
        return c;
    }
    static private double[] sub(double a[], double[] b)
    {
        return sub(a,b,new double[a.length]);
    }
   
    double[] EMA(int n, double[] price, double[] ema)
    {
        assert (ema!=null);
        assert (price!=null);
        if(ema.length>0)
        {
            double k = 2/(n+1);
            ema[ema.length-1] = price[ema.length-1];
            for(int i=ema.length-1;i>=0;i--)
            {
                ema[i] = price[i]*k + ema[i+1]*(1-k);
            }
        }
        return ema;
    }
    double[] EMA(int n, double[] price)
    {
        assert (price!=null);
        return EMA(n,price, new double[price.length]);
    }
    
    void MACD(int fastPeriod, int slowPeriod, int signalPeriod, double[] price, double[] line, double[] signal, double[] histogram)
    {
        assert (line!=null);
        assert (signal!=null);
        assert (histogram!=null);
        assert (price!=null);
        double[] fastEMA = EMA(fastPeriod,price);
        double[] slowEMA = EMA(slowPeriod,price);
        sub(fastEMA,slowEMA,line);
        EMA(signalPeriod,line, signal);
        sub(line,signal,histogram);
    }
    double[] MACDL(int fastPeriod, int slowPeriod, int signalPeriod, double[] price)
    {
        assert (price!=null);
        double[] line;
        MACD(fastPeriod, slowPeriod, signalPeriod, price, line=new double[price.length], new double[price.length], new double[price.length]);
        return line;
    }
    double[] MACDS(int fastPeriod, int slowPeriod, int signalPeriod, double[] price)
    {
        assert (price!=null);
        double[] signal;
        MACD(fastPeriod, slowPeriod, signalPeriod, price, new double[price.length], signal=new double[price.length], new double[price.length]);
        return signal;
    }
    double[] MACDH(int fastPeriod, int slowPeriod, int signalPeriod, double[] price)
    {
        assert (price!=null);
        double[] h;
        MACD(fastPeriod, slowPeriod, signalPeriod, price, new double[price.length], new double[price.length], h=new double[price.length]);
        return h;
    }
    double[] ForceIndex(double[] forceIndex, double[] price, double[] volume)
    {
        assert (forceIndex!=null);
        assert (price!=null);
        assert (volume!=null);
        if(price.length>0)
        {
            for(int i=1;i<price.length;i++)
            {
                forceIndex[i] = (price[i]-price[i-1])*volume[i];
            }
        }
        return forceIndex;
    }
    double[] ForceIndex(double[] close, double[] volume)
    {
        return ForceIndex(new double[close.length],close,volume);
    }
}
