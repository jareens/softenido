/*
 * ProxyGaugeProgress.java
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
package com.softenido.cafecore.misc;

/**
 *
 * @author franci
 */
public class ProxyGaugeProgress extends AbstractGaugeProgress
{
    private volatile GaugeProgress proxy=null;

    public ProxyGaugeProgress()
    {
        proxy = new NullGaugeProgress();
    }
    public ProxyGaugeProgress(GaugeProgress gp)
    {
        setProxy(gp);
    }

    @Override
    public void paint(double done, String msg)
    {
        proxy.paint(done, msg);
    }

    public void start()
    {
        proxy.start();
    }

    public void start(int max)
    {
        proxy.start(max);
    }

    public void close()
    {
        proxy.close();
    }

    public void setPrefix(String prefix)
    {
        proxy.setPrefix(prefix);
    }

    public String getPrefix()
    {
        return proxy.getPrefix();
    }

    public double getDone()
    {
        return proxy.getDone();
    }

    public int getVal()
    {
        return proxy.getVal();
    }

    public int getMax()
    {
        return proxy.getMax();
    }

    public void setVal(int n)
    {
        proxy.setVal(n);
    }

    public void setMax(int n)
    {
        proxy.setMax(n);
    }

    public void step()
    {
        proxy.step();
    }

    public void step(int n)
    {
        proxy.step(n);
    }
    public void setProxy(GaugeProgress proxy)
    {
        this.proxy = proxy!=null?proxy:new NullGaugeProgress();
        if(proxy!=null)
        {
            if(isStarted())
            {
                proxy.start(getMax());
                proxy.setVal(getVal());
            }
            invalidate();            
        }
    }
}
