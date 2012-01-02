/*
 * AboutGPL3Activity.java
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

package com.softenido.droiddesk.util.ui;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.softenido.droidcore.R;
import com.softenido.droidcore.os.MetaData;
import com.softenido.droiddesk.admob.AdMob;

import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;

public class AboutGPL3Activity extends Activity
{
    static final String METADATA_COPYRIGHT = "com.softenido.about.copyright";
    static final String METADATA_URL1 = "com.softenido.about.url1";
    static final String METADATA_URL2 = "com.softenido.about.url2";

    @SuppressWarnings("FieldCanBeLocal")
    private AdMob admob=null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_gpl3);

        admob = AdMob.addBanner(this, R.id.aboutLayout);

        CharSequence label       = "aplication android:label";
        CharSequence description = "aplication android:description";
        CharSequence versionName = "manifest android:versionName";
        String copyRight         = "meta-data "+METADATA_COPYRIGHT;
        String url1              = "meta-data "+METADATA_URL1;
        String url2              = "meta-data "+METADATA_URL2;

        try
        {
            PackageInfo packageInfo;
            Context context = this.getApplicationContext();
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
            label       = packageInfo.applicationInfo.loadLabel(context.getPackageManager());
            description = packageInfo.applicationInfo.loadDescription(context.getPackageManager());
            Bundle bundle= MetaData.getBundle(context);
            copyRight   = bundle.getString(METADATA_COPYRIGHT);
            url1   = bundle.getString(METADATA_URL1);
            url2   = bundle.getString(METADATA_URL2);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            Logger.getLogger(AboutGPL3Activity.class.getName()).log(SEVERE,"Package not found");
        }

        TextView AboutAppLabel = (TextView) findViewById(R.id.AboutAppLabel);
        AboutAppLabel.setText(label);

        TextView AboutAppVersionName = (TextView) findViewById(R.id.AboutAppVersionName);
        AboutAppVersionName.setText("v"+versionName);

        TextView AboutAppDescription = (TextView) findViewById(R.id.AboutAppDescription);
        AboutAppDescription.setText(description);

        TextView AboutCopyRight = (TextView) findViewById(R.id.AboutCopyRight);
        AboutCopyRight.setText(copyRight);

        TextView webUrl1Text = (TextView) findViewById(R.id.AboutWebUrl1);
        webUrl1Text.setText(url1);
        TextView webUrl2Text = (TextView) findViewById(R.id.AboutWebUrl2);
        webUrl2Text.setText(url2);

        final Button bAccept= (Button) findViewById(R.id.bAboutAccept);

        bAccept.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                finish();
            }
        });
   }
}