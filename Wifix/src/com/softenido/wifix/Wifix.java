/*
 * Wifix.java
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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.*;
import com.softenido.cafecore.text.HumanDateFormat;
import com.softenido.cafecore.util.GenericObserver;
import com.softenido.cafecore.util.Locales;
import com.softenido.droidcore.os.Battery;
import com.softenido.droidcore.services.LocalService;
import com.softenido.droidcore.services.LocalServiceConnection;
import com.softenido.droiddesk.admob.AdMob;
import com.softenido.droiddesk.util.ui.AboutGPL3Activity;

import java.util.Date;
import java.util.Locale;

public class Wifix extends Activity implements GenericObserver<KeepWifiService,Battery>
{
    static final long MIN = 60*1000;
    static final long HOUR = 60*MIN;
    static final long DAY = 24*HOUR;
    static final long[] KEEP_MILLIS = {5*MIN, 15*MIN, 30*MIN, 1*HOUR, 2*HOUR, 6*HOUR, 8*HOUR, 12*HOUR, 1*DAY, 7*DAY, 30*DAY, 365*DAY};
    static final int[] KEEP_LEVELS = {10, 20, 30, 40, 50, 60, 70, 80, 90};

    static volatile WifiManager wm=null;
    static volatile Vibrator vibrator =null;

    @SuppressWarnings("FieldCanBeLocal")
    private AdMob admob=null;

    private volatile LocalServiceConnection connection =null;
    private volatile KeepWifiService keep =null;
    final HumanDateFormat hdf = HumanDateFormat.getShortInstance(new Date());
    boolean initialized=false;

    CheckBox cbKeepLock;
    Spinner timeSpinner;
    Spinner batterySpinner;
    TextView textTime;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //LocalService.setToastDebug(true);

        admob = AdMob.addBanner(this,R.id.mainLayout);

        final Button bConnect = (Button) findViewById(R.id.bReconnect);
        final Button bReassign= (Button) findViewById(R.id.bReassign);
        cbKeepLock= (CheckBox) findViewById(R.id.cbKeepLock);
        textTime = (TextView) findViewById(R.id.time_text);
        timeSpinner = (Spinner) findViewById(R.id.time_spinner);
        batterySpinner = (Spinner) findViewById(R.id.battery_spinner);
        final Button bAbout= (Button) findViewById(R.id.bAbout);
        final Button bHide = (Button) findViewById(R.id.bHide);

        // if there is no saved state from a previous instance, let set our preferred one
        if(savedInstanceState==null)
        {
            cbKeepLock.setEnabled(false);
            timeSpinner.setEnabled(false);
            batterySpinner.setEnabled(false);
            timeSpinner.setSelection(3);
            batterySpinner.setSelection(3);
            initialized=true;
        }

        // keep connection is created just once using Application context,
        // and reused in every instance of this Activity
        connection = new LocalServiceConnection(getApplicationContext(),KeepWifiService.class)
        {
            @Override
            public void onConnected(LocalService service)
            {
                keep = (KeepWifiService) service;
                if(service!=null)
                {
                    keep.addObserver(Wifix.this);
                    loadKeep();
                }
            }
        };
        connection.bindService();

        if(vibrator==null)
        {
            vibrator= (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        }
        if(wm==null)
        {
            wm = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        }

        bConnect.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                wm.reconnect();
                vibrator.vibrate(33);
            }
        });

        bReassign.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                wm.reassociate();
                vibrator.vibrate(33);
            }
        });

        cbKeepLock.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                if(keep.isKeep()==false)
                {
                    int timeOption = timeSpinner.getSelectedItemPosition();
                    int batteryOption = batterySpinner.getSelectedItemPosition();
                    keep.adquire(KEEP_MILLIS[timeOption], KEEP_LEVELS[batteryOption]);
                }
                else
                {
                    keep.release();
                }
                vibrator.vibrate(33);
            }
        });
        final Intent about = new Intent(this,AboutGPL3Activity.class);
        bAbout.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                startActivity(about);
            }
        });
        bHide.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                final Intent home = new Intent(Intent.ACTION_MAIN);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivityForResult(home, 0);
                //finish();
            }
        });
    }
    @Override
    protected void onStart()
    {
        if(keep!=null)
        {
            keep.addObserver(this);
            loadKeep();
        }
        super.onStart();
    }

    @Override
    protected void onStop()
    {
        if(keep!=null)
        {
            keep.deleteObserver(this);
        }
        super.onStop();
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    void loadKeep()
    {
        if(keep!=null)
        {
            KeepWifiService.Conf conf = keep.getConf();
            cbKeepLock.setEnabled(true);
            boolean active = keep.isKeep();

            if(cbKeepLock.isChecked() != active)
            {
                cbKeepLock.setChecked(active);
            }
            timeSpinner.setEnabled(!active);
            batterySpinner.setEnabled(!active);

            if(conf!=null && (active || initialized) )
            {
                timeSpinner.setSelection(getMillisOption(conf));
                batterySpinner.setSelection(getBatteryOption(conf));
                initialized = false;
            }
            textTime.setText( active?hdf.format(keep.getConf().getFinishTime()):"");
        }
        else
        {
            cbKeepLock.setEnabled(false);
            timeSpinner.setEnabled(true);
            batterySpinner.setEnabled(true);
            textTime.setText("");
        }
    }

    private int getMillisOption(KeepWifiService.Conf conf)
    {
        for(int i=0;i<KEEP_MILLIS.length;i++)
        {
            if(KEEP_MILLIS[i]>=conf.getDurationMillis())
            {
                return i;
            }
        }
        return 0;
    }
    private int getBatteryOption(KeepWifiService.Conf conf)
    {
        for(int i=0;i<KEEP_LEVELS.length;i++)
        {
            if(KEEP_LEVELS[i]>=conf.getBatteryLevel())
            {
                return  i;
            }
        }
        return 0;
    }


    public void update(KeepWifiService sender, Battery data)
    {
        loadKeep();
    }
}