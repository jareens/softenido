/*
 *  Verbose.java
 *
 *  Copyright (C) 2010  Francisco Gómez Carrasco
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  Report bugs or new features to: flikxxi@gmail.com
 *
 */
package com.softenido.cafecore.logging;

import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

/**
 *
 * @author franci
 */
public class VerboseHandler extends StreamHandler
{
    private static final Level levels[] = 
    {
        Level.SEVERE,
        Level.WARNING,
        Level.INFO,
        Level.CONFIG,
        Level.FINE,
        Level.FINER,
        Level.FINEST
    };
    private final ReentrantLock lock = new ReentrantLock();

    public VerboseHandler()
    {
        super();
    }

    public VerboseHandler(OutputStream out, Formatter formatter)
    {
        super(out, formatter);
    }
    
    public VerboseHandler(OutputStream out, final String prefix)
    {
        this(out, new Formatter()
        {
            @Override
            public String format(LogRecord record)
            {
                String message = record.getMessage();
                StringBuilder msg = new StringBuilder(prefix);
                boolean space=false;

                if(message!=null)
                {
                    space=true;
                    msg.append(MessageFormat.format(record.getMessage(),record.getParameters()));
                }
                
                if(record.getThrown() != null)
                {
                    msg.append(space?" (":"(");
                    msg.append(record.getThrown().getMessage());
                    msg.append(")");
                }

                msg.append("\n");
                return msg.toString();
            }
        });    
    }

    @Override
    public synchronized void publish(LogRecord record)
    {
        lock.lock();
        try
        {
            super.publish(record);
            super.flush();
        }
        finally
        {
            lock.unlock();
        }
    }

    public void unlock()
    {
        lock.unlock();
    }

    public void lock()
    {
        lock.lock();
    }
    public static Level verboseLevel(int verbosity)
    {
        verbosity = Math.min(verbosity, levels.length-1);
        return levels[verbosity];
    }
    public static void register(int verbosity,VerboseHandler vh, Class<? extends Handler> uh)
    {
        final Logger root = Logger.getLogger("");
        final Level level = verboseLevel(verbosity);

        vh.setLevel(level);
        root.setLevel(getLevelProperty(".level", level));
        
        root.addHandler(vh);
        Handler[] rh = root.getHandlers();
        for(int i = 0; i < rh.length;i++)
        {           
            if(uh.equals(rh[i].getClass()))
            {
                root.removeHandler(rh[i]);
                break;
            }
        }
     }
    static Level getLevelProperty(String name, Level defaultValue)
    {
	String val = LogManager.getLogManager().getProperty(name);
	if (val == null)
        {
	    return defaultValue;
	}
	try
        {
	    return Level.parse(val.trim());
	} 
        catch (Exception ex)
        {
	    return defaultValue;
	}
    }
}
