/*
 * GaugeBuilder.java
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
import android.view.View;
import android.view.ViewGroup;
import com.softenido.cafedroid.R;
import com.softenido.cafedroid.util.ui.Views;

/**
 * Created with IntelliJ IDEA.
 * User: franci
 * Date: 25/10/12
 * Time: 14:03
 * To change this template use File | Settings | File Templates.
 */
public class DroidGaugeBuilder
{
    private static final int[] INFLATABLE =
    {
            R.layout.gauge_progress_view,
            R.layout.gauge_progress_view_med,
            R.layout.gauge_progress_view_thin,
    };
    public static enum Thin
    {
        NORMAL(0), MED(1), THIN(2);
        final int value;
        private Thin(int value)
        {
            this.value = value;
        }
    }
    public static enum Mode
    {
        NONE, BAR, TEXT, BAR_TEXT;
    }

    public static View inflateAfter(Activity activity, int root, int after, Thin thin)
    {
        View view = Views.inflateAfter(activity, root, INFLATABLE[thin.value], after);
        view.setVisibility(View.GONE);
        return view;
    }

    public static View inflateAt(Activity activity, int root, int order, Thin thin)
    {
        View view = Views.inflateAt(activity, root, INFLATABLE[thin.value], order);
        view.setVisibility(View.GONE);
        return view;
    }

    public static View inflateBefore(Activity activity, int root, int before, Thin thin)
    {
        View view = Views.inflateBefore(activity, root, INFLATABLE[thin.value], before);
        view.setVisibility(View.GONE);
        return view;
    }

    public static View inflate(Activity activity, int root, Thin thin)
    {
        View view = Views.inflate(activity, root, INFLATABLE[thin.value]);
        view.setVisibility(View.GONE);
        return view;
    }
    public static View inflate(Activity activity, Thin thin)
    {
        View view = Views.inflate(activity, INFLATABLE[thin.value]);
        view.setVisibility(View.GONE);
        return view;
    }

    public static DroidGaugeProgress createProgressAfter(Handler handler, Activity activity, int root, int after, Mode mode, Thin thin)
    {
        ViewGroup group = (ViewGroup)inflateAfter(activity, root, after, thin);
        return new DroidGaugeProgress(handler, group, R.id.gauge_progress_bar, R.id.gauge_progress_text, mode);
    }
    public static DroidGaugeProgress createProgressAt(Handler handler, Activity activity, int root, int order, Mode mode, Thin thin)
    {
        ViewGroup group = (ViewGroup)inflateAt(activity, root, order, thin);
        return new DroidGaugeProgress(handler, group, R.id.gauge_progress_bar, R.id.gauge_progress_text, mode);
    }
    public static DroidGaugeProgress createProgressBefore(Handler handler, Activity activity, int root, int before, Mode mode, Thin thin)
    {
        ViewGroup group = (ViewGroup)inflateBefore(activity, root, before, thin);
        return new DroidGaugeProgress(handler, group, R.id.gauge_progress_bar, R.id.gauge_progress_text, mode);
    }

    public static DroidGaugeView createViewAfter(Handler handler, Activity activity, int root, int after, Mode mode, Thin thin)
    {
        ViewGroup group = (ViewGroup)inflateAfter(activity, root, after, thin);
        return new DroidGaugeView(handler, group, R.id.gauge_progress_bar, R.id.gauge_progress_text, mode);
    }
    public static DroidGaugeView createViewAt(Handler handler, Activity activity, int root, int order, Mode mode, Thin thin)
    {
        ViewGroup group = (ViewGroup)inflateAt(activity, root, order, thin);
        return new DroidGaugeView(handler, group, R.id.gauge_progress_bar, R.id.gauge_progress_text, mode);
    }
    public static DroidGaugeView createViewBefore(Handler handler, Activity activity, int root, int before, Mode mode, Thin thin)
    {
        ViewGroup group = (ViewGroup)inflateBefore(activity, root, before, thin);
        return new DroidGaugeView(handler, group, R.id.gauge_progress_bar, R.id.gauge_progress_text, mode);
    }
}
