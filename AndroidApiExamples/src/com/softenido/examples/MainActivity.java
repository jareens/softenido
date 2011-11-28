/*
 * MainActivity.java
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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.softenido.droiddesk.admob.AdMob;
import com.softenido.droiddesk.util.ui.AboutGPL3Activity;

public class MainActivity extends Activity
{
    private AdMob admob=null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final Intent listViewPureXml = new Intent(this,ListViewPureXmlActivity.class);
        final Intent listViewCodeXml = new Intent(this,ListViewCodeAndXmlActivity.class);
        final Intent tasksList = new Intent(this,TasksListViewActivity.class);
        final Intent batteryList = new Intent(this,BatteryDisplayActivity.class);
        final Intent speech = new Intent(this, SpeechActivity.class);
        final Intent about = new Intent(this, AboutGPL3Activity.class);

        ListView mainListView = (ListView) findViewById(R.id.main_list_view);
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView parent, View view, int position, long id)
            {
                switch (position)
                {
                    case 0:
                        startActivity(listViewPureXml);
                        break;
                    case 1:
                        startActivity(listViewCodeXml);
                        break;
                    case 2:
                        startActivity(tasksList);
                        break;
                    case 3:
                        startActivity(batteryList);
                        break;
                    case 4:
                        startActivity(speech);
                        break;
                    case 5:
                        startActivity(about);
                        break;
                }
            }
        });

        mainListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                Toast.makeText(getApplicationContext(), "Long Click item "+position,Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        admob = AdMob.addBanner(this, R.id.mainLayout, true);

    }
}
