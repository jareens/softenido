/*
 * ViewsHandler.java
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

import android.os.Handler;
import android.preference.CheckBoxPreference;

/**
 * Created with IntelliJ IDEA.
 * User: franci
 * Date: 3/12/12
 * Time: 13:30
 * To change this template use File | Settings | File Templates.
 */
public class ViewsHandler
{
    final Handler handler;

    public ViewsHandler(Handler handler)
    {
        this.handler = handler;
    }

    public void setChecked(final CheckBoxPreference cp, final boolean value)
    {
        handler.post(new Runnable()
        {
            public void run()
            {
                cp.setChecked(value);
            }
        });
    }
}
