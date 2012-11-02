/*
 * StreamStatusNotifier.java
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
package com.softenido.cafecore.logging;

import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.logging.Level;

/**
 *
 * @author franci
 */
public class StreamStatusNotifier extends AbstractStatusNotifier
{
    final PrintStream out;

    public StreamStatusNotifier(PrintStream out, Level level)
    {
        super(level);
        this.out = out;
    }
    public StreamStatusNotifier(Level level)
    {
        this(System.out, level);
    }
    public void log(Level level, String msg, Object... arguments)
    {
        if(this.isLoggable(level))
        {
            out.println(MessageFormat.format(msg, arguments));
        }
    }
    
}
