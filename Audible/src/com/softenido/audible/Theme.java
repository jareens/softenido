/*
 * Theme.java
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

import android.graphics.Color;

/**
 * Created with IntelliJ IDEA.
 * User: franci
 * Date: 20/09/12
 * Time: 9:53
 * To change this template use File | Settings | File Templates.
 */
public enum Theme
{
    Light(Color.BLACK, Color.WHITE),
    Gray(Color.WHITE, Color.DKGRAY),
    Dark(Color.WHITE, Color.BLACK);

    private Theme(int foreGroundColor, int backGroundColor)
    {
        this.textColor = foreGroundColor;
        this.backGroundColor = backGroundColor;
    }

    final int textColor;
    final int backGroundColor;

}
