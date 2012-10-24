/*
 * AbstractGaugeProgress.java
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

import com.softenido.cafecore.text.HumanMillisFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author franci
 */
public abstract class AbstractGaugeProgress implements GaugeProgress
{
    private static final double MINSTART = 0.01;
    private static final int MINSTEP = 1;
    private static final double MINTIME = 2000;
    //--- time variables
    private long iniTime = 0;   // momento en el que se inicio el proceso
    private long curTime = 0;   // momento del ultimo paint
    //--- val variables
    private int curVal = 0;     // valor actual
    private int maxVal = 100;   // valor máximo
    //--- porcentajes
    private double done = 0.0;  // porcentaje atual
    //--- otros
    private boolean started=false;
    private boolean force = false;
    private String prefix = "";
    // --- modos
    private volatile boolean showPrev = false;
    private volatile boolean showNext = true;
    private volatile boolean showFull = false;
    static final NumberFormat fmt = NumberFormat.getPercentInstance(Locale.US);
    static HumanMillisFormat fmtTime = new HumanMillisFormat(1,2);
    static
    {
        fmt.setMinimumIntegerDigits(2);
        fmt.setMinimumFractionDigits(2);
        fmt.setMaximumFractionDigits(2);
    }

    public String getPrefix()
    {
        return prefix;
    }

    public void setPrefix(String prefix)
    {
        force = force || !prefix.equals(this.prefix);
        this.prefix = prefix;
    }

    public AbstractGaugeProgress()
    {
        super();
    }
    public boolean isStarted()
    {
        return started;
    }
    public void start()
    {
        start(100);
    }

    public void start(int max)
    {
        iniTime = System.currentTimeMillis();
        curTime = 0;
        curVal = 0;
        maxVal = max;
        done = 0.0;
        force = true;
        paintLazy();
    }

    public void close()
    {
        started = false;
        curVal = maxVal;
        force = true;
        paintLazy();
    }

    public double getDone()
    {
        return done;
    }

    public int getVal()
    {
        return curVal;
    }

    public int getMax()
    {
        return maxVal;
    }

    public void setVal(int n)
    {
        curVal = n;
        paintLazy();
    }

    public void setMax(int n)
    {
        maxVal = n;
        paintLazy();
    }

    public void step()
    {
        step(1);
    }

    public void step(int n)
    {
        curVal += n;
        paintLazy();
    }

    public abstract void paint(double done, String msg);

    private void paintLazy()
    {
        double cur = (double) curVal / (double) maxVal;
        long now = System.currentTimeMillis();
        long difTime = now - curTime;
        double dif = Math.abs(done - cur);

        if (dif >= MINSTEP || difTime >= MINTIME || force)
        {
            done = cur;
            curTime = now;

            String txt = prefix + " " + fmt.format(done);

            if (done >= MINSTART)
            {
                long prevTime = curTime - iniTime;
                long endTime = (long) (iniTime + (prevTime / cur));
                long nextTime = endTime - curTime;

                if (showPrev)
                {
                    txt += " (" + fmtTime.toString(prevTime) + ")";
                }
                if (showNext)
                {
                    txt += " [" + fmtTime.toString(nextTime) + "]";
                }
                if (showFull)
                {
                    txt += " <" + fmtTime.toString(endTime - iniTime) + ">";
                }
            }
            force = false;
            paint(done, txt);
        }
    }

    public boolean isShowPrev()
    {
        return showPrev;
    }

    public void setShowPrev(boolean showPrev)
    {
        this.showPrev = showPrev;
    }

    public boolean isShowNext()
    {
        return showNext;
    }

    public void setShowNext(boolean showNext)
    {
        this.showNext = showNext;
    }

    public boolean isShowFull()
    {
        return showFull;
    }

    public void setShowFull(boolean showFull)
    {
        this.showFull = showFull;
    }
    public void setShow(boolean showPrev, boolean showNext, boolean showFull)
    {
        this.showPrev = showPrev;
        this.showNext = showNext;
        this.showFull = showFull;
    }

    protected void invalidate()
    {
        this.force = true;
        paintLazy();
    }
}
 