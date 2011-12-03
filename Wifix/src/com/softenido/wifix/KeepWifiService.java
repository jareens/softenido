/*
 * KeepWifiService.java
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

package com.softenido.wifix;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;
import com.softenido.droidcore.services.LocalService;
import com.softenido.droiddesk.app.NotificationBuilder;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: franci
 * Date: 29/11/11
 * Time: 10:17
 */
public class KeepWifiService extends LocalService
{
    static final int NOTIFICATION_ID = 123456;
    static final long TICK_MILLIS = 5*60*1000;//every five minutes

    private final Object lock = new Object();
    private volatile boolean keep = false;
    WifiManager.WifiLock wLock=null;
    private Intent intent;

    private long finishMillis=0;
    private Date finishTime= null;
    private volatile CountDownTimer countDown=null;

    private Runnable update;

    @Override
    public void onCreate()
    {
        super.onCreate();
        intent = new Intent(this,KeepWifiService.class);
        WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wLock = wm.createWifiLock("Wifix.Lock");
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
        super.onDestroy();
    }

    public void adquire(long millis, Runnable update)
    {
        synchronized (lock)
        {
            if(keep==false)
            {
                wLock.acquire();
                keep=true;
                startCountDown(millis);
                startService(intent);
            }
            notifyStatus(true);
            setUpdate(update);
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
                if(countDown!=null)
                {
                    countDown.cancel();
                }
            }
            notifyStatus(false);
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

    private void startCountDown(long finishMillis)
    {
        if(finishMillis>0)
        {
            countDown = new CountDownTimer(finishMillis, TICK_MILLIS)
            {
                @Override
                public void onFinish()
                {
                    release();
                    countDown=null;
                    runUpdate();
                }
                @Override
                public void onTick(long l)
                {
                    notifyStatus(keep);
                }
            }.start();
            finishTime = new Date(System.currentTimeMillis()+finishMillis);
        }
        else
        {
            countDown = null;
            finishTime= null;
        }
    }

    public Date getFinishTime()
    {
        return finishTime;
    }

    public void setUpdate(Runnable update)
    {
        synchronized(lock)
        {
            this.update = update;
        }
    }
    public void runUpdate()
    {
        synchronized(lock)
        {
            if(update!=null)
            {
                update.run();
            }
        }
    }
}
