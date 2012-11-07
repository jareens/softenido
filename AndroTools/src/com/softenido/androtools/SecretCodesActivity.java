/*
 * SecretCodesActivity.java
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
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.softenido.cafedroid.admob.AdMob;

public class SecretCodesActivity extends Activity
{
    private AdMob admob=null;

    String[] codes =
            {
                    "*#06#", //IMEI
                    "*#*#4636#*#*", //Phone Setting
                    "*#*#7262626#*#*", //FieldTest
                    "*#*#2846579#*#*" // Testing Settings (Huawei)
            };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secret_codes);

        ListView mainListView = (ListView) findViewById(R.id.secret_codes_list_view);
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView parent, View view, int position, long id)
            {
                switch (position)
                {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        //Intent intent = new Intent();
                        //intent.setAction(Intent.ACTION_DIAL,Uri.parse("tel:" + Uri.encode(codes[position])));
                        //intent.setData(Uri.parse("tel:" + Uri.encode(codes[position]));
//                        startActivity(intent);
                        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(codes[position]))));
                        break;
                }
            }
        });

        admob = AdMob.addBanner(this, R.id.mainLayout, true);

        //Uri uri = Uri.parse("*#*#2846579#*#*");
        //Uri uri = Uri.parse("tel:"+Uri.encode("*#*#4636#*#*"));
        //Intent intent = new Intent(Intent.ACTION_DIAL,uri);
        //startActivity(intent);


    }
}
