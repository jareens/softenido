/*
 * Screens.java
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
import android.content.Context;
import android.os.Handler;
import android.os.PowerManager;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created with IntelliJ IDEA.
 * User: franci
 * Date: 22/09/12
 * Time: 12:53
 * To change this template use File | Settings | File Templates.
 */
public class Screens
{
    public static void lockSmooth(Activity activity, Handler handler)
    {
        lockSmooth(activity.getWindow(), handler);
    }
    public static void lockSmooth(final Window window, final Handler handler)
    {
        final Runnable task = new Runnable()
        {
            boolean first = true;
            public void run()
            {
                setBrightness(window,first?0:-1);
                if(first) handler.postDelayed(this, 123);
                first=false;
            }
        };
        handler.post(task);
    }

    public static float getBrightness(Window window)
    {
        WindowManager.LayoutParams params = window.getAttributes();
        return params.screenBrightness;
    }
    public static void setBrightness(Window window, float value)
    {
        WindowManager.LayoutParams params = window.getAttributes();
        //params.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.screenBrightness = value;
        window.setAttributes(params);
    }
    public static void setBrightness(Activity activity, float value)
    {
        setBrightness(activity.getWindow(), value);
    }

    public static boolean isScreenOn(Context context)
    {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        return pm.isScreenOn();
    }
}
