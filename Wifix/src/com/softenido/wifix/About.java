package com.softenido.wifix;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.softenido.droiddesk.admob.AdMob;
import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;

public class About extends Activity
{
    private AdMob admob=null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        admob = AdMob.addBanner(this,R.id.aboutLayout);

        String versionName = "xx.xx.xx";

        try
        {
            PackageInfo packageInfo;
            Context context = this.getApplicationContext();
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            Logger.getLogger(About.class.getName()).log(SEVERE,"Package not found");
        }

        TextView versionText = (TextView) findViewById(R.id.VersionText);
        String versionFormat = versionText.getText().toString();
        versionText.setText(String.format(versionFormat, versionName));

        final Button bOk= (Button) findViewById(R.id.bOk);

        bOk.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                finish();
            }
        });


    }
}