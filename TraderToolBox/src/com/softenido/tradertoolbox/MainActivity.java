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

package com.softenido.tradertoolbox;

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
    @SuppressWarnings("FieldCanBeLocal")
    private AdMob admob=null;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final Intent equityRiskManager = new Intent(this,EquityRiskActivity.class);
        final Intent tradeRiskManager = new Intent(this,TradeRiskActivity.class);
        final Intent autoEnvelope = new Intent(this,AutoEnvelopeActivity.class);
        final Intent about = new Intent(this, AboutGPL3Activity.class);

        ListView mainListView = (ListView) findViewById(R.id.main_list_view);
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView parent, View view, int position, long id)
            {
                switch (position)
                {
                    case 0:
                        startActivity(equityRiskManager);
                        break;
                    case 1:
                        startActivity(tradeRiskManager);
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this,R.string.not_yet_implemented,Toast.LENGTH_LONG).show();
//                        startActivity(autoEnvelope);
                        break;
                    case 3:
                        Toast.makeText(MainActivity.this,R.string.not_yet_implemented,Toast.LENGTH_LONG).show();
//                        startActivity(accounts);
                        break;
                    case 4:
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