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
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import com.softenido.droidcore.os.Battery;
import com.softenido.droidcore.os.BatteryObservable;
import com.softenido.droiddesk.admob.AdMob;
import com.softenido.cafecore.util.GenericObserver;

public class BatteryDisplayActivity extends Activity
{
    private AdMob admob=null;

    final BatteryObservable<BatteryDisplayActivity> bc= new BatteryObservable<BatteryDisplayActivity>(this,this,false);

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

        bc.addObserver(new GenericObserver<BatteryDisplayActivity, Battery>()
        {
            public void update(BatteryDisplayActivity sender, Battery battery)
            {
                plugged.setText(bc.getPlugged(battery));
                status.setText(bc.getStatus(battery));
                present.setText(battery.isPresent()?"true":"false");
                health.setText(bc.getHealth(battery));
                level.setText(""+battery.getLevel()+"/"+battery.getScale());
                technology.setText(battery.getTechnology());
                String temp = String.format("%.1fºC",battery.getTemperature()/10.0);
                temperature.setText(temp);
                voltage.setText(""+battery.getVoltage()+"mV");
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        bc.start();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        bc.stop();
    }

    @Override
    protected void onDestroy()
    {
        bc.stop();
        super.onDestroy();
    }

}
