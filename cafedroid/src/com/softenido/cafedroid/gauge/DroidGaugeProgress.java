/*
 * ViewGaugeProgress.java
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

package com.softenido.cafedroid.gauge;

import android.app.Activity;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.softenido.cafecore.gauge.AbstractGaugeProgress;

/**
 * Created with IntelliJ IDEA.
 * User: franci
 * Date: 4/10/12
 * Time: 16:15
 * To change this template use File | Settings | File Templates.
 */
public class DroidGaugeProgress extends AbstractGaugeProgress
{
    private final DroidGaugeView view;

    public DroidGaugeProgress(DroidGaugeView view)
    {
        this.view = view;
    }

    public DroidGaugeProgress(Handler handler, final ViewGroup gp, final ProgressBar pb, final TextView tv, DroidGaugeBuilder.Mode mode)
    {
        this.view = new DroidGaugeView(handler, gp, pb, tv, mode);
    }
    public DroidGaugeProgress(Handler handler, ViewGroup gp, ProgressBar pb, TextView tv)
    {
        this.view = new DroidGaugeView(handler, gp, pb, tv);
    }
    public DroidGaugeProgress(Handler handler, ViewGroup gp, int pb, int tv, DroidGaugeBuilder.Mode mode)
    {
        this.view = new DroidGaugeView(handler, gp, pb, tv, mode);
    }
    public DroidGaugeProgress(Handler handler, ViewGroup gp, int pb, int tv)
    {
        this.view = new DroidGaugeView(handler, gp, pb, tv);
    }
    public DroidGaugeProgress(Handler handler, Activity activity, int gp, int pb, int tv, DroidGaugeBuilder.Mode mode)
    {
        this.view = new DroidGaugeView(handler, activity, gp, pb, tv, mode);
    }
    public DroidGaugeProgress(Handler handler, Activity activity, int pb, int tv, DroidGaugeBuilder.Mode mode)
    {
        this.view = new DroidGaugeView(handler, activity, pb, tv, mode);
    }
    public DroidGaugeProgress(Handler handler, Activity activity, int pb, int tv)
    {
        this.view = new DroidGaugeView(handler, activity, pb, tv);
    }
    public void setMode(DroidGaugeBuilder.Mode mode)
    {
        view.setMode(mode);
    }

    @Override
    public void paint(boolean started, int max, int val, String prefix, double done, String msg)
    {
        view.paint(started, max, val, prefix, done, msg);
    }

}
