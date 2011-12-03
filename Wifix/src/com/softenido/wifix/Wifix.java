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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import com.softenido.droidcore.services.LocalService;
import com.softenido.droidcore.services.LocalServiceConnection;
import com.softenido.droiddesk.admob.AdMob;
import com.softenido.droiddesk.util.ui.AboutGPL3Activity;
import com.softenido.hardcore.text.HumanDateFormat;

import java.util.Date;

public class Wifix extends Activity
{
    static final int M = 60*1000;
    static final int H = 3600*1000;
    static final int[] TIMES = {5*M, 15*M, 30*M, 1*M, 2*H,6*H, 8*H, 12*H, 24*H, 0};

    static volatile WifiManager wm=null;
    static volatile Vibrator vibrator =null;


    private AdMob admob=null;

    private volatile LocalServiceConnection connection =null;
    private volatile KeepWifiService keep =null;
    final HumanDateFormat hdf = HumanDateFormat.getShortInstance(new Date());

    CheckBox cbKeepLock;
    Spinner spinnerTime;
    TextView textTime;
    Runnable update=null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        LocalService.setToastDebug(true);

        admob = AdMob.addBanner(this,R.id.mainLayout);

        final Button bConnect = (Button) findViewById(R.id.bReconnect);
        final Button bReassign= (Button) findViewById(R.id.bReassign);
        cbKeepLock= (CheckBox) findViewById(R.id.cbKeepLock);
        spinnerTime = (Spinner) findViewById(R.id.time_spinner);
        textTime = (TextView) findViewById(R.id.time_text);
        final Button bAbout= (Button) findViewById(R.id.bAbout);
        final Button bHide = (Button) findViewById(R.id.bHide);

        if(vibrator==null)
        {
            vibrator= (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        }
        if(wm==null)
        {
            wm = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        }
        update = new Runnable()
        {
            public void run()
            {
                loadKeep();
            }
        };



        loadKeep();

        // keep connection is created just once using Application context,
        // and reused in every instance of this Activity
        connection = new LocalServiceConnection(getApplicationContext(),KeepWifiService.class)
        {
            @Override
            public void onConnected(LocalService service)
            {
                Wifix.this.keep = (KeepWifiService) service;
                if(service!=null)
                {
                    loadKeep();
                }
            }
        };
        connection.bindService();

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
                    int option = spinnerTime.getSelectedItemPosition();
                    keep.adquire(TIMES[option], update);
                    loadKeep();
                }
                else
                {
                    keep.release();
                    loadKeep();
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
    protected void onDestroy()
    {
        super.onDestroy();
    }

    void loadKeep()
    {
        if(keep!=null)
        {
            cbKeepLock.setEnabled(true);
            if(keep.isKeep())
            {
                if(!cbKeepLock.isChecked())
                {
                    cbKeepLock.setChecked(true);
                }
                spinnerTime.setEnabled(false);
                textTime.setText(hdf.format(keep.getFinishTime()));
            }
            else
            {
                if(cbKeepLock.isChecked())
                {
                    cbKeepLock.setChecked(false);
                }
                spinnerTime.setEnabled(true);
                textTime.setText("");
            }
        }
        else
        {
            cbKeepLock.setEnabled(false);
            spinnerTime.setEnabled(true);
            textTime.setText("");
        }
    }

    @Override
    protected void onResume()
    {
        if(keep!=null)
        {
            keep.setUpdate(update);
        }
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        if(keep!=null)
        {
            keep.setUpdate(null);
        }
        super.onPause();
    }
}