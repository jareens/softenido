/*
 * ListViewPureXmlActivity.java
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.softenido.droiddesk.admob.AdMob;

public class ListViewPureXmlActivity extends Activity
{
    private AdMob admob=null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_pure_xml);

        ListView mainListView = (ListView) findViewById(R.id.main_list_view);
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView parent, View view, int position, long id)
            {
                Toast.makeText(getApplicationContext(), "Click item "+position,Toast.LENGTH_SHORT).show();
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
