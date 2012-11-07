/*
 * TextClassifierService.java
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

package com.softenido.audible;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import com.softenido.cafecore.profile.ProfileRecord;
import com.softenido.cafecore.profile.Profiler;
import com.softenido.cafedroid.app.NotificationBuilder;
import com.softenido.cafedroid.services.LocalService;
import com.softenido.cafedroid.services.LocalServiceConnection;
import com.softenido.gutenberg.GutenbergLanguageClassifier;

/**
 * Created with IntelliJ IDEA.
 * User: franci
 * Date: 15/10/12
 * Time: 19:34
 * To change this template use File | Settings | File Templates.
 */
public class KeepAudibleLoadService extends LocalService
{
    static final int MAX_LANGS = 10;
    static final String profilerName = KeepAudibleLoadService.class.getName();
    final static GutenbergLanguageClassifier classifier = new GutenbergLanguageClassifier("", MAX_LANGS)
    {
        @Override
        protected boolean initialize(String item)
        {
            Profiler profiler = Profiler.getProfiler(profilerName);
            ProfileRecord rec = profiler.open();
            boolean ret = super.initialize(item);
            profiler.close(rec);
            Profiler.print(System.out);
            return ret;
        }
    };
    static volatile AudiblePreferences settings = null;

    private Intent intent;
    @Override
    public void onCreate()
    {
        super.onCreate();
        intent = new Intent(this, KeepAudibleLoadService.class);
        startCountDown();
    }

    public static GutenbergLanguageClassifier getClassifier()
    {
        return classifier;
    }
    public static AudiblePreferences getPreferences(Context context)
    {
        if(settings==null)
        {
            settings = AudiblePreferences.getInstance(context);
        }
        return settings;
    }

    public Notification buidNotification()
    {
        NotificationBuilder nb = new NotificationBuilder(this);

        String ticker = getString(R.string.app_name)+" - "+getString(R.string.notify_keep_ticker);

        nb.setSmallIcon(R.drawable.ic_notification)
                .setTicker(ticker)
                .setContentTitle(ticker)
                .setContentText(getString(R.string.notify_keep_text))
                .setContentIntent(PendingIntent.getActivity(this, 0, intent, 0))
                .setAutoCancel(false);

        return nb.getNotification();
    }

    private static int NOTIFICATION_ID = 1234567;
    private static volatile LocalServiceConnection connection =null;
    private static volatile KeepAudibleLoadService keep =null;
    private static volatile NotificationManager mgr = null;

    static void connect(Activity activity)
    {
        final Context context = activity.getApplicationContext();
        connection = new LocalServiceConnection(context,KeepAudibleLoadService.class)
        {
            @Override
            public void onConnected(LocalService service)
            {
                mgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                keep = (KeepAudibleLoadService) service;

                Notification n = keep.buidNotification();
                //mgr.notify(NOTIFICATION_ID, n);
                keep.startForeground(NOTIFICATION_ID, n);
            }
        };
        connection.bindService();
        setActive(true, Integer.MAX_VALUE);
    }
    private static void disconnect()
    {
        if(keep!=null)
        {
            //mgr.cancel(NOTIFICATION_ID);
            keep.stopForeground(true);
        }
    }

    static volatile boolean active = true;
    static volatile int countdown= Integer.MAX_VALUE;

    void startCountDown()
    {
        new Thread(new Runnable()
        {
            public void run()
            {
                while(active || countdown>0)
                {
                    countdown--;
                    try
                    {
                        Log.d(KeepAudibleLoadService.class.getSimpleName(),"countdown="+(countdown)+"min");
                        Thread.sleep(60*1000);
                    }
                    catch (InterruptedException ex)
                    {
                        Log.e(KeepAudibleLoadService.class.getSimpleName(),"countdown="+(countdown)+"min", ex);
                    }
                }
                Log.d(KeepAudibleLoadService.class.getSimpleName(),"stopForeground");
                stopForeground(true);
            }
        }).start();
    }

    static public void setActive(boolean active, int seconds)
    {
        KeepAudibleLoadService.active = active;
        KeepAudibleLoadService.countdown = seconds/60;
    }
}