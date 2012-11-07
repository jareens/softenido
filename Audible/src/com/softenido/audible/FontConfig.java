/*
 * Font.java
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

package com.softenido.audible;

import android.graphics.Typeface;
import android.util.TypedValue;

/**
 * Created with IntelliJ IDEA.
 * User: franci
 * Date: 20/09/12
 * Time: 10:24
 * To change this template use File | Settings | File Templates.
 */
public class FontConfig
{
    public final int dp = TypedValue.COMPLEX_UNIT_DIP;
    public final Typeface typeface;
    public final float size;

    public static final FontConfig NORMAL = new FontConfig("Serif", false, 13);

    public FontConfig(String name, boolean bold, float size)
    {
        this.typeface = Typeface.create(name, bold?Typeface.BOLD:Typeface.NORMAL);
        this.size = size;
    }
}
