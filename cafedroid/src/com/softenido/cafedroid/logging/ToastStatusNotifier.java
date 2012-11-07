/*
 * ToastStatusNotifier.java
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

package com.softenido.cafedroid.logging;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;
import com.softenido.cafecore.logging.AbstractStatusNotifier;

import java.text.MessageFormat;
import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA.
 * User: franci
 * Date: 26/10/12
 * Time: 16:17
 * To change this template use File | Settings | File Templates.
 */
public class ToastStatusNotifier extends AbstractStatusNotifier
{
    final Context context;
    final Handler handler;
    final int duration;

    public ToastStatusNotifier(Context context, Handler handler, Level level, boolean brief)
    {
        super(level);
        this.context = context;
        this.handler = handler;
        this.duration= brief?Toast.LENGTH_SHORT:Toast.LENGTH_LONG;
    }
    public ToastStatusNotifier(Context context, Handler handler, Level level)
    {
        this(context, handler, level, false);
    }

    public void log(Level level, final String msg, final Object... arguments)
    {
        if(this.isLoggable(level))
        {
            handler.post(new Runnable()
            {
                public void run()
                {
                    Toast.makeText(context, MessageFormat.format(msg, arguments), duration).show();
                }
            });
        }
    }
}
