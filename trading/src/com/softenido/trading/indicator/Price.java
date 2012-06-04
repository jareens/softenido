/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.trading.indicator;

import java.util.Date;

/**
 *
 * @author franci
 */
public class Price
{
    private final String ticker;
    private final Date[] date;
    private final double[] open;
    private final double[] high;
    private final double[] low;
    private final double[] close;
    private final double[] volume;

    public Price(String ticker, Date[] date, double[] open, double[] high, double[] low, double[] close, double[] volume)
    {
        this.ticker = ticker;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.date = date;
    }

    public String getTicker()
    {
        return ticker;
    }

    public Date[] getDate()
    {
        return date;
    }
    
    public double[] getClose()
    {
        return close;
    }

    public double[] getHigh()
    {
        return high;
    }

    public double[] getLow()
    {
        return low;
    }

    public double[] getOpen()
    {
        return open;
    }

    public double[] getVolume()
    {
        return volume;
    }
}
