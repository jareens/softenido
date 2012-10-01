/*
 * Notifier.java
 *
 * Copyright (c) 2012 Francisco Gómez Carrasco
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

package com.softenido.droiddesk.util.ui;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: franci
 * Date: 7/09/12
 * Time: 23:55
 * To change this template use File | Settings | File Templates.
 */
public abstract class Notifier
{
    //for static use
    public static void toast(Context context, String msg, boolean lengthLong)
    {
        Toast.makeText(context, msg, lengthLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }
    public static void toast(final Context context, final String msg, final boolean lengthLong, Handler handler)
    {
        handler.post(new Runnable()
        {
            public void run()
            {
                Notifier.toast(context, msg, lengthLong);
            }
        });
    }

    //builders
    public static Notifier build(final Notifier... notifiers)
    {
        if(notifiers.length==0)
        {
            return Notifier.build();
        }
        else if(notifiers.length==1)
        {
            return notifiers[0];
        }

        return new Notifier()
        {
            @Override
            void log(int level, String msg, Throwable tr)
            {
                if(level<this.level)
                {
                    return;
                }
                for(Notifier n : notifiers)
                {
                    n.log(level, msg, tr);
                }
            }
        };
    }

    public static Notifier build(final Context context, final boolean longLength)
    {
        return new Notifier()
        {
            @Override
            void log(int level, String msg, Throwable tr)
            {
                Notifier.toast(context, msg, longLength);
            }
        };
    }
    public static Notifier build(final Context context, final Handler handler, final boolean longLength)
    {
        return new Notifier()
        {
            @Override
            void log(int level, String msg, Throwable tr)
            {
                Notifier.toast(context, msg, longLength, handler);
            }
        };
    }
    public static Notifier build(final String tag, final int level)
    {
        return new Notifier()
        {
            @Override
            void log(int level, String msg, Throwable tr)
            {
                if(level<this.level)
                {
                    return;
                }
                if(tr!=null)
                {
                    switch(level)
                    {
                        case Log.DEBUG:
                            Log.d(tag,msg, tr);
                            break;
                        case Log.ERROR:
                            Log.d(tag,msg, tr);
                            break;
                        case Log.INFO:
                            Log.d(tag,msg, tr);
                            break;
                        case Log.VERBOSE:
                            Log.d(tag,msg, tr);
                            break;
                        case Log.WARN:
                            Log.d(tag,msg, tr);
                            break;
                    }
                }
                else
                {
                    switch(level)
                    {
                        case Log.DEBUG:
                            Log.d(tag,msg);
                            break;
                        case Log.ERROR:
                            Log.d(tag,msg);
                            break;
                        case Log.INFO:
                            Log.d(tag,msg);
                            break;
                        case Log.VERBOSE:
                            Log.d(tag,msg);
                            break;
                        case Log.WARN:
                            Log.d(tag,msg);
                            break;
                    }
                }
            }
        };
    }
    public static Notifier build()
    {
        return new Notifier()
        {
            @Override
            void log(int level, String msg, Throwable tr)
            {
                //do nothing
            }
        };
    }

    volatile int level=Log.INFO;

    abstract void log(int level, String msg, Throwable tr);
    final void log(int level, String msg)
    {
        log(level, msg,null);
    }

    public final void v(final String msg)
    {
        log(Log.VERBOSE, msg, null);
    }
    public final void v(final String msg, Throwable tr)
    {
        log(Log.VERBOSE, msg, tr);
    }
    public final void d(final String msg)
    {
        log(Log.DEBUG, msg, null);
    }
    public final void d(final String msg, Throwable tr)
    {
        log(Log.DEBUG, msg, tr);
    }
    public final void i(final String msg)
    {
        log(Log.INFO, msg, null);
    }
    public final void i(final String msg, Throwable tr)
    {
        log(Log.INFO, msg, tr);
    }
    public final void w(final String msg)
    {
        log(Log.WARN, msg, null);
    }
    public final void w(final String msg, Throwable tr)
    {
        log(Log.WARN, msg, tr);
    }
    public final void e(final String msg)
    {
        log(Log.ERROR, msg, null);
    }
    public final void e(final String msg, Throwable tr)
    {
        log(Log.ERROR, msg, tr);
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }
}
