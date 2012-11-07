/*
 * LocalServiceConnection.java
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

package com.softenido.cafedroid.services;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Created by IntelliJ IDEA.
 * User: franci
 * Date: 29/11/11
 * Time: 12:37
 * To change this template use File | Settings | File Templates.
 */
public class LocalServiceConnection implements ServiceConnection
{
    final private Context context;
    final private Class<?> serviceClass;
    final private boolean rebind;

    private volatile LocalService service=null;
    private volatile boolean binded = false;

    protected LocalServiceConnection(Context context, Class<?> serviceClass, boolean rebind)
    {
        this.context = context;
        this.serviceClass = serviceClass;
        this.binded = false;
        this.rebind = rebind;
    }
    protected LocalServiceConnection(Context context, Class<?> serviceClass)
    {
        this(context,serviceClass,false);
    }

    final public void onServiceConnected(ComponentName componentName, IBinder localBinder)
    {
        service = ((LocalService.LocalBinder)localBinder).getService();
        onConnected(service);
    }
    public void onConnected(LocalService service)
    {
    }
    final public void onServiceDisconnected(ComponentName componentName)
    {
        service = null;
        onDisconnected();
    }
    public void onDisconnected()
    {
    }
    public LocalService getService()
    {
        return service;
    }
    public void bindService()
    {
        if(binded==false)
        {
            context.bindService(new Intent(context, serviceClass), this, Context.BIND_AUTO_CREATE);
            binded = true;
        }
        else if(rebind)
        {
            onConnected(service);
        }
    }
    public void unbindService()
    {
        if(binded)
        {
            context.unbindService(this);
            binded = false;
        }
        else if(rebind)
        {
            onDisconnected();
        }
    }
    public void startService()
    {
        context.startService(new Intent(context, serviceClass));
    }
    public void stopService()
    {
        context.stopService(new Intent(context, serviceClass));
    }

    @Override
    protected void finalize() throws Throwable
    {
        if(binded)
        {
            unbindService();
        }
        super.finalize();
    }
}
