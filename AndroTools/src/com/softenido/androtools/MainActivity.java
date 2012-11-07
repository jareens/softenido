/*
 * MainActivity.java
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
package com.softenido.androtools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.softenido.cafedroid.admob.AdMob;
import com.softenido.cafedroid.util.ui.AboutGPL3Activity;

public class MainActivity extends Activity
{
    private AdMob admob=null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        final Intent secretCodes = new Intent(this,SecretCodesActivity.class);
        final Intent performance = new Intent(this, PerformanceTestsActivity.class);
        final Intent display = new Intent(this, DisplayActivity.class);
        final Intent about = new Intent(this, AboutGPL3Activity.class);

        ListView mainListView = (ListView) findViewById(R.id.secret_codes_list_view);
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView parent, View view, int position, long id)
            {
                switch (position)
                {
                    case 0:
                        startActivity(secretCodes);
                        break;
                    case 1:
                        startActivity(performance);
                        break;
                    case 2:
                        startActivity(display);
                        break;
                    case 3:
                        startActivity(about);
                        break;
                }
            }
        });

        admob = AdMob.addBanner(this, R.id.mainLayout, true);
    }
}
