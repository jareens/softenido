/*
 * ProgressBarGaugeView.java
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
import android.widget.ProgressBar;
import android.widget.TextView;
import com.softenido.cafecore.gauge.GaugeView;

/**
 * Created with IntelliJ IDEA.
 * User: franci
 * Date: 4/10/12
 * Time: 16:15
 * To change this template use File | Settings | File Templates.
 */
public class DroidGaugeView implements GaugeView
{
    private static final int FULL = 1000;

    private final Handler handler;
    private final ViewGroup gp;
    private final ProgressBar pb;
    private final TextView tv;
    private volatile DroidGaugeBuilder.Mode mode;

    public DroidGaugeView(Handler handler, final ViewGroup gp, final ProgressBar pb, final TextView tv, DroidGaugeBuilder.Mode mode)
    {
        this.handler = handler;
        this.gp = gp;
        this.pb = pb;
        this.tv = tv;
        setMode(mode);
    }
    public DroidGaugeView(Handler handler, ViewGroup gp, ProgressBar pb, TextView tv)
    {
        this(handler, gp, pb, tv, DroidGaugeBuilder.Mode.BAR_TEXT);
    }
    public DroidGaugeView(Handler handler, ViewGroup gp, int pb, int tv, DroidGaugeBuilder.Mode mode)
    {
        this(handler, gp, (ProgressBar) gp.findViewById(pb), (TextView) gp.findViewById(tv), mode);
    }
    public DroidGaugeView(Handler handler, ViewGroup gp, int pb, int tv)
    {
        this(handler, gp, pb, tv, DroidGaugeBuilder.Mode.BAR_TEXT);
    }
    public DroidGaugeView(Handler handler, Activity activity, int gp, int pb, int tv, DroidGaugeBuilder.Mode mode)
    {
        this(handler, (ViewGroup)activity.findViewById(gp), (ProgressBar)activity.findViewById(pb), (TextView)activity.findViewById(tv), mode);
    }
    public DroidGaugeView(Handler handler, Activity activity, int pb, int tv, DroidGaugeBuilder.Mode mode)
    {
        this(handler, null, (ProgressBar)activity.findViewById(pb), (TextView)activity.findViewById(tv), mode);
    }
    public DroidGaugeView(Handler handler, Activity activity, int pb, int tv)
    {
        this(handler, null, (ProgressBar)activity.findViewById(pb), (TextView)activity.findViewById(tv), DroidGaugeBuilder.Mode.BAR_TEXT);
    }
    public void setMode(DroidGaugeBuilder.Mode mode)
    {
        if(this.mode!=mode)
        {
            this.mode = mode;
            force = true;
        }
    }
    private volatile boolean force=false;
    private volatile boolean started=false;
    private volatile double done=0;
    private volatile String msg="";

    public void paint(final boolean started, final int max, final int val, final String prefix, final double done, final String msg)
    {
        if(!force && mode== DroidGaugeBuilder.Mode.NONE )
            return;
        if(!force && started==this.started && done==this.done && msg.equals(this.msg))
            return;
        handler.post(new Runnable()
        {
            public void run()
            {
                if(started && !DroidGaugeView.this.started)
                {
                    init(gp, pb, tv, DroidGaugeView.this.mode);
                    DroidGaugeView.this.started = started;
                }
                else if(!started && DroidGaugeView.this.started)
                {
                    gone(gp, pb, tv);
                    DroidGaugeView.this.started = started;
                }
                DroidGaugeView.this.force=false;
                DroidGaugeView.this.started=false;
                DroidGaugeView.this.done=0;
                DroidGaugeView.this.msg="";

                switch(mode)
                {
                    case NONE:
                        break;
                    case BAR:
                        setProgress(pb, done);
                        break;
                    case TEXT:
                        setText(tv, msg);
                        break;
                    case BAR_TEXT:
                    default:
                        setProgress(pb, done);
                        setText(tv, msg);
                        break;
                }
            }
        });
    }
    public static void init(ViewGroup gp, ProgressBar pb, TextView tv, DroidGaugeBuilder.Mode mode)
    {
        setMax(pb, FULL);
        setProgress(pb, 0);
        setText(tv, "");
        switch(mode)
        {
            case NONE:
                setVisibility(gp, View.GONE);
                setVisibility(pb, View.GONE);
                setVisibility(tv, View.GONE);
                break;
            case BAR:
                setVisibility(gp, View.VISIBLE);
                setVisibility(pb, View.VISIBLE);
                setVisibility(tv, View.GONE);
                break;
            case TEXT:
                setVisibility(gp, View.VISIBLE);
                setVisibility(pb, View.GONE);
                setVisibility(tv, View.VISIBLE);
                break;
            case BAR_TEXT:
            default:
                setVisibility(gp, View.VISIBLE);
                setVisibility(pb, View.VISIBLE);
                setVisibility(tv, View.VISIBLE);
                break;
        }
    }

    public static void gone(ViewGroup gp, ProgressBar pb, TextView tv)
    {
        setVisibility(gp, View.GONE);
        setVisibility(pb, View.GONE);
        setVisibility(tv, View.GONE);
    }

    private static void setVisibility(View view, int visibility)
    {
        if(view!=null)
        {
            view.setVisibility(visibility);
        }
    }

    private static void setMax(ProgressBar pb, int max)
    {
        if(pb !=null)
        {
            pb.setMax(max);
        }
    }
    private static void setProgress(ProgressBar pb, double done)
    {
        if(pb!=null)
        {
            pb.setProgress((int) (done * FULL));
        }
    }
    private static void setText(TextView tv, String msg)
    {
        if(tv!=null)
        {
            tv.setText(msg);
        }
    }
}
