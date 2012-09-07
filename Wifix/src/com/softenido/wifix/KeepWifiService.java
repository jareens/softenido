/*
 * KeepWifiService.java
 *
 * Copyright (c) 2011-2012 Francisco Gómez Carrasco
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

package com.softenido.wifix;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;
import android.widget.Toast;
import com.softenido.droidcore.os.Battery;
import com.softenido.droidcore.os.BatteryObservable;
import com.softenido.droidcore.services.LocalService;
import com.softenido.droiddesk.app.NotificationBuilder;
import com.softenido.cafecore.util.GenericObservable;
import com.softenido.cafecore.util.GenericObserver;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: franci
 * Date: 29/11/11
 * Time: 10:17
 */
public class KeepWifiService extends LocalService implements GenericObserver<KeepWifiService,Battery>
{
    static public class Conf
    {
        Conf(long durationMillis, int batteryLevel)
        {
            this.durationMillis = durationMillis;
            this.batteryLevel = batteryLevel;
            this.finishTime = new Date(System.currentTimeMillis()+durationMillis);
        }
        final long durationMillis;
        final Date finishTime;
        final int batteryLevel;

        public int getBatteryLevel()
        {
            return batteryLevel;
        }
        public long getDurationMillis()
        {
            return durationMillis;
        }
        public Date getFinishTime()
        {
            return finishTime;
        }
    }
    static final int NOTIFICATION_ID = 123456;
    static final long TICK_MILLIS = 5*60*1000;//every five minutes

    private final Object lock = new Object();
    private volatile boolean keep = false;
    WifiManager.WifiLock wLock=null;
    private Intent intent;

    private volatile CountDownTimer countDown=null;

    final private GenericObservable<KeepWifiService,Battery> observable = new GenericObservable<KeepWifiService,Battery>(this);
    final private BatteryObservable<KeepWifiService> batteryObservable = new BatteryObservable<KeepWifiService>(this,this,false);
    private Conf conf=null;

    @Override
    public void onCreate()
    {
        super.onCreate();
        intent = new Intent(this,KeepWifiService.class);
        WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wLock = wm.createWifiLock("Wifix.Lock");
        batteryObservable.addObserver(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        if(wLock.isHeld())
        {
            wLock.release();
        }
        super.onDestroy();
    }

    public void adquire(long millis, int batteryLevel)
    {
        synchronized (lock)
        {
            if(keep==false)
            {
                conf = new Conf(millis,batteryLevel);
                wLock.acquire();
                keep=true;
                startService(intent);
                startCountDown();
                batteryObservable.start();
                notifyStatus(true);
                observable.setChanged();
                observable.notifyObservers();
            }
            setSticky(keep);
        }
    }

    public void release()
    {
        synchronized(lock)
        {
            if(keep==true)
            {
                wLock.release();
                keep=false;
                stopService(intent);
                stopCountDown();
                batteryObservable.stop();
                notifyStatus(false);
                observable.setChanged();
                observable.notifyObservers();
            }
            setSticky(keep);
        }
    }

    public boolean isKeep()
    {
        return keep;
    }

    void notifyStatus(boolean active)
    {
        NotificationManager mgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(active)
        {

            NotificationBuilder nb = new NotificationBuilder(this);

            String ticker = getString(R.string.app_name)+" - "+getString(R.string.keepLock);

            nb.setSmallIcon(R.drawable.wifix64)
              .setTicker(ticker)
              .setContentTitle(ticker)
              .setContentText(getString(R.string.notify_keep_text))
              .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, Wifix.class), 0))
              .setAutoCancel(false);

            android.app.Notification n = nb.getNotification();

            mgr.notify(NOTIFICATION_ID, n);
        }
        else
        {
            mgr.cancel(NOTIFICATION_ID);
        }
    }

    private void startCountDown()
    {
        if(conf!=null)
        {
            countDown = new CountDownTimer(conf.durationMillis, TICK_MILLIS)
            {
                @Override
                public void onFinish()
                {
                    countDown=null;
                    release();
                }
                @Override
                public void onTick(long l)
                {
                    notifyStatus(keep);
                }
            }.start();
        }
        else
        {
            countDown = null;
        }
    }
    private void stopCountDown()
    {
        if(countDown!=null)
        {
            countDown.cancel();
            countDown=null;
        }
    }

    public Conf getConf()
    {
        return conf;
    }
    

    public void addObserver(GenericObserver<KeepWifiService, Battery> keepWifiServiceBatteryGenericObserver)
    {
        observable.addObserver(keepWifiServiceBatteryGenericObserver);
    }

    public void deleteObserver(GenericObserver<KeepWifiService, Battery> keepWifiServiceBatteryGenericObserver)
    {
        observable.deleteObserver(keepWifiServiceBatteryGenericObserver);
    }

    public void update(KeepWifiService sender, Battery battery)
    {
        if(conf!=null && battery.getLevel()<conf.batteryLevel)
        {
            Toast.makeText(this,R.string.not_enough_battery,Toast.LENGTH_LONG).show();
            release();
        }
    }

}
