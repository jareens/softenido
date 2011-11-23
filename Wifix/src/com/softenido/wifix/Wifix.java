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

    Vibrator vibrator=null;
    WifiManager wm=null;
    WifiManager.WifiLock wLock = null;
    private AdMob admob=null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        admob = AdMob.addBanner(this,R.id.mainLayout);

        Context c;
        c = this.getApplication().getBaseContext();
        vibrator = (Vibrator) c.getSystemService(Context.VIBRATOR_SERVICE);
        wm = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);

        final Button bConnect = (Button) findViewById(R.id.bReconnect);
        final Button bReassign= (Button) findViewById(R.id.bReassign);
        final CheckBox cbKeepLock= (CheckBox) findViewById(R.id.checkBox1);
        final Button bAbout= (Button) findViewById(R.id.bAbout);
        final Button bHide = (Button) findViewById(R.id.bHide);

        wLock = wm.createWifiLock("WifiLock");

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
                if(cbKeepLock.isChecked())
                {
                    wLock.acquire();
                }
                else
                {
                    wLock.release();

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
                startActivity(home);
                //finish();
            }
        });


//        new Thread(new Runnable()
//        {
//            public void run()
//            {
//                while(true)
//                {
//                    int state = wm.getWifiState();
//                    if(WifiManager.WIFI_STATE_ENABLED == state)
//                    {
//                        for(int i=0;i<state;i++)
//                        {
//                            vibrator.vibrate(50);
//                            try
//                            {
//                                Thread.sleep(50);
//                            } catch (InterruptedException e)
//                            {
//                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                            }
//                        }
//                    }
//                    try
//                    {
//                        Thread.sleep(15000);
//                    } catch (InterruptedException e)
//                    {
//                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                    }
//                }
//
//            }
//        }).start();
    }

}