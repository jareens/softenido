package com.softenido.tradertoolbox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import com.softenido.droiddesk.admob.AdMob;
import com.softenido.droiddesk.util.ui.AboutGPL3Activity;

public class AutoEnvelopeActivity extends Activity
{

    Vibrator vibrator=null;
    WifiManager wm=null;
    WifiManager.WifiLock wLock = null;
    private AdMob admob=null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);

        admob = AdMob.addBanner(this,R.id.mainLayout);

        Context c;
        c = this.getApplication().getBaseContext();
        vibrator = (Vibrator) c.getSystemService(Context.VIBRATOR_SERVICE);
        wm = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);

        final Button bRiskManager = (Button) findViewById(R.id.bRiskManager);
        final Button bAutoEnvelope= (Button) findViewById(R.id.bAutoEnvelope);
        final Button bAbout= (Button) findViewById(R.id.bAbout);
        final Button bHide = (Button) findViewById(R.id.bHide);

        final Intent riskManager = new Intent(this,RiskManagerActivity.class);
        final Intent autoenvelope = new Intent(this,AutoEnvelopeActivity.class);
        final Intent about = new Intent(this,AboutGPL3Activity.class);

        bAbout.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                startActivity(riskManager);
            }
        });

        bAbout.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                startActivity(autoenvelope);
            }
        });
        bAbout.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                startActivity(about);
            }
        });
        bHide.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                final Intent home = new Intent(Intent.ACTION_MAIN);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
            }
        });
    }

}