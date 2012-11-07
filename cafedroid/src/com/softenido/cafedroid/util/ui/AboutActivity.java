/*
 * AboutActivity.java
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

package com.softenido.cafedroid.util.ui;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.softenido.cafedroid.os.AndroidVersion;
import com.softenido.cafedroid.os.MetaData;
import com.softenido.cafedroid.R;
import com.softenido.cafedroid.admob.AdMob;
import com.softenido.cafedroid.app.SendEmail;
import com.softenido.cafedroid.app.SendEmailBuilder;

import java.util.Locale;
import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;

public class AboutActivity extends Activity
{
    static final String METADATA_COPYRIGHT = "com.softenido.about.copyright";
    static final String METADATA_URL1 = "com.softenido.about.url1";
    static final String METADATA_URL2 = "com.softenido.about.url2";
    static final String METADATA_URL3 = "com.softenido.about.url3";
    static final String METADATA_EMAIL = "com.softenido.about.email";

    int layout = R.layout.about_generic;
    int bannerLayoutId=R.id.aboutLayout;

    CharSequence label       = "aplication android:label";
    CharSequence description = "aplication android:description";
    CharSequence versionName = "manifest android:versionName";
    String copyRight         = "meta-data "+METADATA_COPYRIGHT;
    String url1              = "meta-data "+METADATA_URL1;
    String url2              = "meta-data "+METADATA_URL2;
    String url3              = "meta-data "+METADATA_URL3;
    String email             = "meta-data "+METADATA_EMAIL;

    @SuppressWarnings("FieldCanBeLocal")
    private AdMob admob=null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(layout);

        admob = AdMob.addBanner(this, bannerLayoutId);

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
            url3   = bundle.getString(METADATA_URL3);
            email  = bundle.getString(METADATA_EMAIL);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            Logger.getLogger(AboutActivity.class.getName()).log(SEVERE,"Package not found");
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
        TextView webUrl2Text = (TextView) findViewById(R.id.AboutWebUrl2);
        TextView webUrl3Text = (TextView) findViewById(R.id.AboutWebUrl3);
        webUrl1Text.setText(url1);
        webUrl2Text.setText(url2);
        webUrl3Text.setText(url3);

        MarketLinkify.addLinks(webUrl1Text);
        MarketLinkify.addLinks(webUrl2Text);
        MarketLinkify.addLinks(webUrl3Text);

        TextView about_locale = (TextView) findViewById(R.id.about_locale);
        final String os_locale = AndroidVersion.os.NAME+" "+AndroidVersion.os.RELEASE+" ("+Locale.getDefault()+")";
        about_locale.setText(os_locale);

        final Button bAccept= (Button) findViewById(R.id.bAboutAccept);

        bAccept.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                finish();
            }
        });
        final Button bFeedback= (Button) findViewById(R.id.bAboutFeedback);
        if(email==null || email.trim().length()==0)
        {
            Log.w(AboutActivity.class.getName(),"metadata "+METADATA_EMAIL+" is "+(email==null?"null":"empty"));
            bFeedback.setEnabled(false);
        }
        bFeedback.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                String title = getResources().getString(R.string.about_feedback_title);
                String subject = "["+label+"]["+versionName+"] "+bFeedback.getText();
                SendEmail se = new SendEmailBuilder(AboutActivity.this,title).addTo(email).setSubject(subject).build();
                se.send();
            }
        });
   }
}