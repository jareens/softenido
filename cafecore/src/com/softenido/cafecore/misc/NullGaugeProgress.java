/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.cafecore.misc;

/**
 *
 * @author franci
 */
public class NullGaugeProgress implements GaugeProgress
{
    public boolean isStarted()
    {
        return false;
    }

    public void start()
    {
    }

    public void start(int max)
    {
    }

    public void close()
    {
    }

    public void setPrefix(String prefix)
    {
    }

    public String getPrefix()
    {
        return "";
    }

    public double getDone()
    {
        return 0;
    }

    public int getVal()
    {
        return 0;
    }

    public int getMax()
    {
        return 0;
    }

    public void setVal(int n)
    {
    }

    public void setMax(int n)
    {
    }

    public void step()
    {
    }

    public void step(int n)
    {
    }

    public void paint(double done, String msg)
    {
    }
}
