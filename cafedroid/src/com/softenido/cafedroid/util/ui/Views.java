/*
 * Views.java
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

package com.softenido.cafedroid.util.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.widget.LinearLayout;
import com.softenido.cafedroid.gauge.DroidGaugeProgress;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: franci
 * Date: 21/10/12
 * Time: 21:36
 * To change this template use File | Settings | File Templates.
 */
public class Views
{
    private static View inflate(Activity activity, LinearLayout root, int inflatable, int order)
    {
        View view;
        try
        {
            LayoutInflater inflater = activity.getLayoutInflater();
            if(order==Integer.MAX_VALUE)
            {
                view = inflater.inflate(inflatable, root);
                view.setVisibility(View.GONE);
            }
            else if(root!=null)
            {
                view = inflater.inflate(inflatable, null);
                view.setVisibility(View.GONE);
                root.addView(view, order);
            }
            else
            {
                view = inflater.inflate(inflatable, null);
                view.setVisibility(View.GONE);
                ViewParent parent = view.getParent();
                if(parent instanceof LinearLayout)
                {
                    root = (LinearLayout) parent;
                    root.addView(view, order);
                }
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(DroidGaugeProgress.class.getSimpleName()).log(Level.SEVERE,"inflate",ex);
            view = null;
        }
        return view;
    }
    public static View inflateAfter(Activity activity, int root, int inflatable, int after)
    {
        LinearLayout layout = (LinearLayout)activity.findViewById(root);
        View afterView = activity.findViewById(after);
        int order = layout.indexOfChild(afterView);
        order = (order<0)? Integer.MAX_VALUE : order+1;
        return inflate(activity, (LinearLayout)activity.findViewById(root), inflatable, order);
    }
    public static View inflateAt(Activity activity, int root, int inflatable, int order)
    {
        return inflate(activity, (LinearLayout)activity.findViewById(root), inflatable, order);
    }
    public static View inflateBefore(Activity activity, int root, int inflatable, int after)
    {
        LinearLayout layout = (LinearLayout)activity.findViewById(root);
        View afterView = activity.findViewById(after);
        int order = Math.max(layout.indexOfChild(afterView),0);
        return inflate(activity, (LinearLayout)activity.findViewById(root), inflatable, order);
    }
    public static View inflate(Activity activity, int root, int inflatable)
    {
        return inflate(activity, (LinearLayout)activity.findViewById(root), inflatable, Integer.MAX_VALUE);
    }
    public static View inflate(Activity activity, int inflatable)
    {
        return inflate(activity, null, inflatable, Integer.MAX_VALUE);
    }
}
