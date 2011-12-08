/*
 * BatteryListener.java
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

package com.softenido.droidcore.os;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import com.softenido.droidcore.R;
import com.softenido.hardcore.util.GenericObservable;
import com.softenido.hardcore.util.GenericObserver;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: franci
 * Date: 26/11/11
 * Time: 11:49
 * To change this template use File | Settings | File Templates.
 */
public class BatteryObservable<S>
{
    final Object lock = new Object();
    final Context context;
    final BroadcastReceiver receiver;
    boolean active = false;
    private volatile Battery battery=null;
    final GenericObservable<S,Battery> genericObservable;

    public BatteryObservable(S sender,Context context, boolean active)
    {
        this.genericObservable = new GenericObservable<S,Battery>(sender);
        this.context = context;
        this.receiver = new BroadcastReceiver()
        {
            public void onReceive(Context ctx, Intent intent)
            {
                BatteryObservable.this.onReceive(ctx, intent);
            }
        };
        if(active)
        {
            this.start();
        }
    }

    private void onReceive(Context ctx, Intent intent)
    {
        try
        {
            final int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
            final int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
            final boolean present = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);
            final int health  = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
            final int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            final int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
            final String technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
            final int temperature= intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
            final int voltage= intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
            battery = new Battery(plugged,status,present,health,level,scale,technology,temperature,voltage);
        }
        catch (Exception ex)
        {
            Logger.getLogger(BatteryObservable.class.getName()).log(Level.SEVERE,"getting extra data",ex);
        }
        this.setChanged();
        this.notifyObservers(battery);
    }

    public void start()
    {
        synchronized (lock)
        {
            if(active==false)
            {
                context.registerReceiver(receiver,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                active = true;
            }
        }
    }
    public void stop()
    {
        synchronized (lock)
        {
            if(active==true)
            {
                context.unregisterReceiver(receiver);
                active = false;
            }
        }
    }

    @Override
    protected void finalize() throws Throwable
    {
        this.stop();
        super.finalize();
    }
    public String getPlugged(int plugged)
    {
        int id;
        switch (plugged)
        {
            case 0:
                id = R.string.battery_plugged_battery;
                break;
            case BatteryManager.BATTERY_PLUGGED_AC:
                id = R.string.battery_plugged_ac;
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                id = R.string.battery_plugged_usb;
                break;
            default:
                id = R.string.battery_plugged_plugged;
                break;
        }
        return context.getString(id);
    }
    public String getHealth(int health)
    {
        int id=0;
        switch (health)
        {
            case 0x00000007://BatteryManager.BATTERY_HEALTH_COLD (needs api 14)
                id = R.string.battery_health_cold;
                break;
            case BatteryManager.BATTERY_HEALTH_DEAD:
                id = R.string.battery_health_dead;
                break;
            case BatteryManager.BATTERY_HEALTH_GOOD:
                id = R.string.battery_health_good;
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                id = R.string.battery_health_overheat;
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                id = R.string.battery_health_over_voltage;
                break;
            case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                id = R.string.battery_health_unknown;
                break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                id = R.string.battery_health_unspecified_failure;
                break;
            default:
                return "code="+health;
        }
        return context.getString(id);
    }

    public String getStatus(int status)
    {
        int id=0;
        switch (status)
        {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                id = R.string.battery_status_charging;
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                id = R.string.battery_status_discharging;
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                id = R.string.battery_status_full;
                break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                id = R.string.battery_status_not_charging;
                break;
            case BatteryManager.BATTERY_STATUS_UNKNOWN:
                id = R.string.battery_status_unknown;
                break;
            default:
                return "code="+status;
        }
        return context.getString(id);
    }
    public String getPlugged(Battery battery)
    {
        return getPlugged(battery.getPlugged());
    }
    public String getHealth(Battery battery)
    {
        return getHealth(battery.getHealth());
    }
    public String getStatus(Battery battery)
    {
        return getStatus(battery.getStatus());
    }

    public Battery getBattery()
    {
        return battery;
    }

    public void addObserver(GenericObserver<S, Battery> observer)
    {
        genericObservable.addObserver(observer);
    }

    public int countObservers()
    {
        return genericObservable.countObservers();
    }

    public void deleteObserver(GenericObserver<S, Battery> observer)
    {
        genericObservable.deleteObserver(observer);
    }

    public void deleteObservers()
    {
        genericObservable.deleteObservers();
    }

    public boolean hasChanged()
    {
        return genericObservable.hasChanged();
    }

    public void notifyObservers()
    {
        genericObservable.notifyObservers();
    }

    public void notifyObservers(Object arg)
    {
        genericObservable.notifyObservers(arg);
    }

    void setChanged()
    {
        genericObservable.setChanged();
    }
}
