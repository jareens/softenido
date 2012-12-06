/*
 * AbstractStatusNotifier.java
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

import java.text.MessageFormat;
import java.util.Locale;
import java.util.logging.Level;

/**
 *
 * @author franci
 */
public abstract class AbstractStatusNotifier implements StatusNotifier
{
    private static final int offValue = Level.OFF.intValue();
    final int levelValue;
    public AbstractStatusNotifier(Level level)
    {
        this.levelValue = level.intValue();
    }
    public boolean isLoggable(Level level) 
    {
        if (level.intValue() < levelValue || levelValue == offValue)
        {
            return false;
        }
	return true;
    }
}
