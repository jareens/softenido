/*
 * MetaData.java
 *
 * Copyright (c) 2011  Francisco Gómez Carrasco
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

package com.softenido.droiddesk.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by IntelliJ IDEA.
 * User: franci
 * Date: 15/11/11
 * Time: 14:53
 * To change this template use File | Settings | File Templates.
 */
public class MetaData
{
    static public Bundle getBundle(Context ctx)
    {
        Bundle bundle = null;
        ApplicationInfo ai = null;
        try
        {
            ai = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
            return ai.metaData;
        }
        catch (PackageManager.NameNotFoundException ex)
        {
            Log.e(MetaData.class.getName(),"Failed to load meta-data",ex);
        }
        return null;
    }
}
