/*
 * AboutGPL3Activity.java
 *
 * Copyright (c) 2011-2012  Francisco Gómez Carrasco
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

import android.os.Bundle;
import com.softenido.droidcore.R;

public class AboutGPL3Activity extends AboutActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // we set the layout and the parent view for the banner
        this.layout = R.layout.about_gpl3;
        this.bannerLayoutId=R.id.aboutLayout;

        super.onCreate(savedInstanceState);
   }
}