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
import com.softenido.droiddesk.admob.AdMob;
import com.softenido.droiddesk.util.ui.AboutGPL3Activity;

public class Wifix extends Activity
{

    private AdMob admob=null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        admob = AdMob.addBanner(this,R.id.mainLayout);

        final Button bConnect = (Button) findViewById(R.id.bReconnect);
        final Button bReassign= (Button) findViewById(R.id.bReassign);
        final CheckBox cbKeepLock= (CheckBox) findViewById(R.id.checkBox1);
        final Button bAbout= (Button) findViewById(R.id.bAbout);
        final Button bHide = (Button) findViewById(R.id.bHide);

        bConnect.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                getWifiManager().reconnect();
                vibrate(33);
            }
        });
        bReassign.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                getWifiManager().reassociate();
                vibrate(33);
            }
        });
        cbKeepLock.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                if(cbKeepLock.isChecked())
                {
                    getWifiLock().acquire();
                }
                else
                {
                    getWifiLock().release();

                }
                vibrate(33);
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
                startActivity(home);
                //finish();
            }
        });
    }

    static volatile Vibrator vibrator =null;

    void vibrate(int time)
    {
        if(vibrator ==null)
        {
            Context c = this.getApplication().getBaseContext();
            vibrator = (Vibrator) c.getSystemService(Context.VIBRATOR_SERVICE);
        }
        vibrator.vibrate(time);
    }

    static volatile WifiManager.WifiLock wLock = null;
    WifiManager.WifiLock getWifiLock()
    {
        if(wLock==null)
        {
            WifiManager wm = getWifiManager();
            wLock = wm.createWifiLock("WifiLock");
        }
        return wLock;
    }

   static volatile WifiManager wm=null;
   WifiManager getWifiManager()
   {
         if(wm==null)
         {
             Context c = this.getApplication().getBaseContext();
             wm = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
         }
         return wm;
    }






}