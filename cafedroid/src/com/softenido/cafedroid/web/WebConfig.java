/*
 * WebConfig.java
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

package com.softenido.cafedroid.web;

import android.provider.SyncStateContract;
import com.softenido.cafedroid.R;

/**
 * Created with IntelliJ IDEA.
 * User: franci
 * Date: 18/11/12
 * Time: 23:15
 * To change this template use File | Settings | File Templates.
 */
public class WebConfig
{
    private static int NONE     = 0;

    private static int BACK     = 1;
    private static int FORWARD  = 2;
    private static int HOME     = 4;
    private static int SEARCH   = 8;

    private static int ZOOMCTRL = 1;
    private static int BUILTIN  = 2;
    private static int WIDEVIEW = 4;

    final int layout;
    final int modeBar;
    final int modeZoom;

    public WebConfig(int layout, int modeBar, int modeZoom)
    {
        this.layout = layout;
        this.modeBar  = modeBar;
        this.modeZoom = modeZoom;
    }

    public static WebConfig getSimpleInstance()
    {
        return new WebConfig(R.layout.web_layout_black, NONE, BUILTIN|WIDEVIEW);
    }
    public static WebConfig getBlueInstance()
    {
        return new WebConfig(R.layout.web_layout_blue, NONE, BUILTIN|WIDEVIEW);
    }
    public static WebConfig getHelpInstance()
    {
        return new WebConfig(R.layout.web_layout_black, BACK|FORWARD|HOME|SEARCH, BUILTIN|WIDEVIEW);
    }

    public int getLayout()
    {
        return layout;
    }

    public boolean hasBar()
    {
        return (modeBar!=0);
    }
    public boolean hasBack()
    {
        return (modeBar&BACK)==BACK;
    }

    public boolean hasForward()
    {
        return (modeBar&FORWARD)==FORWARD;
    }

    public boolean hasHome()
    {
        return (modeBar&HOME)==HOME;
    }

    public boolean hasSearch()
    {
        return (modeBar&SEARCH)==SEARCH;
    }

    public boolean hasZoom()
    {
        return (modeZoom!=0);
    }
    public boolean hasZoomCtrl()
    {
        return (modeZoom&ZOOMCTRL)==ZOOMCTRL;
    }
    public boolean hasBuiltIn()
    {
        return (modeZoom&BUILTIN)==BUILTIN;
    }
    public boolean hasWideView()
    {
        return (modeZoom&WIDEVIEW)==WIDEVIEW;
    }
}
