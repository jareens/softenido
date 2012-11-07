/*
 * LogCatHandler.java
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

package com.softenido.cafedroid.util.logging;

import android.util.Log;
import java.text.MessageFormat;
import java.util.logging.*;

/**
 * Created with IntelliJ IDEA.
 * User: franci
 * Date: 10/10/12
 * Time: 23:02
 * to setup android log level properly use 'setprop log.tag.<YOUR_LOG_TAG> <LEVEL>'
 * you can use the command 'adb shell' and then type 'setprop log.tag. VERBOSE' to set VERBOSE level
 * for all loggers or 'setprop log.tag.MyClass VERBOSE' to set VERBOSE level to MyClass logger
 *
 */
public class LogCatHandler extends Handler
{
    private static final Object lock = new Object();
    private static final LogCatHandler instance = new LogCatHandler();
    private static volatile Handler[] oldHandlers = null;

    @Override
    public void close()
    {
    }

    @Override
    public void flush()
    {
    }

    @Override
    public void publish(LogRecord record)
    {
        int androidLevel = toAndroidLevel(record.getLevel().intValue());

        String tag = record.getLoggerName();
        if(tag.length()>23)
        {
            int dot = tag.lastIndexOf('.');
            if(dot>=0)
            {
                tag = tag.substring(dot+1);
            }
        }
        if(Log.isLoggable(tag,androidLevel))
        {
            //to setup android log level properly use 'setprop log.tag.<YOUR_LOG_TAG> <LEVEL>'
            String msg = MessageFormat.format(record.getMessage(),record.getParameters());
            Throwable tr = record.getThrown();
            if(tr!=null)
            {
                msg += '\n' + Log.getStackTraceString(tr);
            }
            Log.println(androidLevel,tag,msg);
        }
    }
    public static void setHandler()
    {
        setHandler(getDeviceLevel());
    }
    public static void setHandler(Level level)
    {
        synchronized(lock)
        {
            Logger logger = Logger.getLogger("");
            if(oldHandlers==null)
            {
                oldHandlers = logger.getHandlers();
                for(Handler handler : oldHandlers)
                {
                    logger.removeHandler(handler);
                }
                logger.addHandler(instance);
            }
            logger.setLevel(level);
            instance.setLevel(level);
        }
    }

    public static void unsetHandler()
    {
        synchronized(lock)
        {
            Logger logger = Logger.getLogger("");
            logger.removeHandler(instance);
            if(oldHandlers!=null)
            {
                for(Handler handler : oldHandlers)
                {
                    logger.addHandler(handler);
                }
            }
        }
    }
    public static int toAndroidLevel(int javaLevel)
    {
        int androidLevel;
        //unrolled loop
        if(javaLevel<=Level.ALL.intValue())
            androidLevel = Log.VERBOSE;
        else if(javaLevel<=Level.CONFIG.intValue())
            androidLevel = Log.DEBUG;
        else if(javaLevel<=Level.INFO.intValue())
            androidLevel = Log.INFO;
        else if(javaLevel<=Level.WARNING.intValue())
            androidLevel = Log.WARN;
        else if(javaLevel<=Level.SEVERE.intValue())
            androidLevel = Log.ERROR;
        else
            androidLevel = Log.ASSERT;
        return androidLevel;
    }
    public static Level getDeviceLevel()
    {
        Level javaLevel;
        //heuristic unrolled loop
        if(Log.isLoggable("",Log.INFO))
        {
            if(Log.isLoggable("",Log.DEBUG))
            {
                if(Log.isLoggable("",Log.VERBOSE))
                {
                    return Level.ALL;
                }
                return Level.CONFIG;
            }
            return Level.INFO;
        }
        else if(Log.isLoggable("",Log.ERROR))
        {
            if(Log.isLoggable("",Log.WARN))
            {
                return Level.WARNING;
            }
            return Level.SEVERE;
        }
        return Level.OFF;
    }
    private static Handler removeAndroidHandler(Logger logger)
    {
        //logger.getHandlers()
        return null;
    }

}
