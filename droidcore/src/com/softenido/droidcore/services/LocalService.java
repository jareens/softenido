/*
 * LocalService.java
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

package com.softenido.droidcore.services;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Created by IntelliJ IDEA.
 * User: franci
 * Date: 29/11/11
 * Time: 11:33
 * To change this template use File | Settings | File Templates.
 */
public abstract class LocalService extends Service
{
    static private volatile boolean toastDebug = false;
    static private volatile int toastLength = Toast.LENGTH_LONG;

    private final boolean debug = toastDebug;
    private final int length = toastLength;

    private final String name = getClass().getSimpleName();

    class LocalBinder extends Binder
    {
        public LocalService getService()
        {
            return LocalService.this;
        }
    }

    private final IBinder binder = new LocalBinder();
    @Override
    final public IBinder onBind(Intent intent)
    {
        if(debug) Toast.makeText(this, name+".onBind", length).show();
        return binder;
    }

    @Override
    public void onCreate()
    {
        if(debug) Toast.makeText(this, name+".onCreate", length).show();
        super.onCreate();
    }

    @Override
    final public void onStart(Intent intent, int startId)
    {
        // deprecated so should not be used in new code
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if(debug) Toast.makeText(this, name+".onStartCommand", length).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        if(debug) Toast.makeText(this, name+".onDestroy", length).show();
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        if(debug) Toast.makeText(this, name+".onConfigurationChanged", length).show();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory()
    {
        if(debug) Toast.makeText(this, name+".onLowMemory", length).show();
        super.onLowMemory();
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        if(debug) Toast.makeText(this, name+".onUnbind", length).show();
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent)
    {
        if(debug) Toast.makeText(this, name+".onRebind", length).show();
        super.onRebind(intent);
    }

    public static void setToastDebug(boolean toastDebug)
    {
        LocalService.toastDebug = toastDebug;
    }

    public static void setToastLength(int toastLength)
    {
        LocalService.toastLength = toastLength;
    }
}
