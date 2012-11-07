/*
 * ShortCuts.java
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

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * Created by IntelliJ IDEA.
 * User: franci
 * Date: 15/02/12
 * Time: 15:45
 * To change this template use File | Settings | File Templates.
 */
public class ShortCuts
{
    // needs <uses-permission android:name="com.android.launcher.permission.INSTALL_SHORTCUT" />
    public static void createShortcut(Activity activity, String name, boolean duplicate, int icon)
    {
        createShortcut(activity, name, duplicate, icon, activity.getPackageName(), activity.getLocalClassName());
    }
    public static void createShortcut(Context context, String name, boolean duplicate, int icon, String pkgName, String className)
    {
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name); // Shortcut name
        shortcut.putExtra("duplicate", duplicate);           // Just create once

        // Setup current activity should be shortcut object
        ComponentName comp = new ComponentName(pkgName, "."+className);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));

        // Set shortcut icon
        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(context, icon);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);

        context.sendBroadcast(shortcut);
    }

    // needs <uses-permission android:name="com.android.launcher.permission.UNINSTALL_SHORTCUT" />
    public static void deleteShortcut(Activity activity, String name)
    {
        deleteShortcut(activity, name, activity.getPackageName(), activity.getLocalClassName());
    }
    public static void deleteShortcut(Context context, String name, String pkgName, String className)
    {
        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");

        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);// Shortcut name
        ComponentName comp = new ComponentName(pkgName, pkgName+"."+className);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));
        context.sendBroadcast(shortcut);
    }
}
