/*
 * BatteryLevelActivity.java
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

package com.softenido.examples;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import com.softenido.droidcore.util.Battery;
import com.softenido.droidcore.util.BatteryChanged;
import com.softenido.droidcore.util.BatteryChangedListener;
import com.softenido.droiddesk.admob.AdMob;

public class BatteryDisplayActivity extends Activity
{
    private AdMob admob=null;
    private BatteryChanged bc=null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(com.softenido.examples.R.layout.battery_display);
        admob = AdMob.addBanner(this, com.softenido.examples.R.id.mainLayout,true);

        final Handler handler= new Handler();

        final TextView plugged = (TextView) findViewById(R.id.battery_display_plugged);
        final TextView status = (TextView) findViewById(R.id.battery_display_status);
        final TextView present = (TextView) findViewById(R.id.battery_display_present);
        final TextView health = (TextView) findViewById(R.id.battery_display_health);
        final TextView level = (TextView) findViewById(R.id.battery_display_level);
        final TextView technology = (TextView) findViewById(R.id.battery_display_technology);
        final TextView temperature = (TextView) findViewById(R.id.battery_display_temperature);
        final TextView voltage = (TextView) findViewById(R.id.battery_display_voltage);

        bc = new BatteryChanged(this.getApplicationContext(),false)
        {
            @Override
            public void onReceive(Battery battery)
            {
                plugged.setText(getPlugged(battery));
                status.setText(getStatus(battery));
                present.setText(battery.isPresent()?"true":"false");
                health.setText(getHealth(battery));
                level.setText(""+battery.getLevel()+"/"+battery.getScale());
                technology.setText(battery.getTechnology());
                String temp = String.format("%.1fºC",battery.getTemperature()/10.0);
                temperature.setText(temp);
                voltage.setText(""+battery.getVoltage()+"mV");
            }
        };
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        bc.open();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        bc.close();
    }

    @Override
    protected void onDestroy()
    {
        bc.close();
        super.onDestroy();
    }
}
