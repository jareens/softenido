package com.softenido.tradertoolbox;

import android.app.Activity;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Vibrator;
import com.softenido.droiddesk.admob.AdMob;

public class AutoEnvelopeActivity extends Activity
{

    Vibrator vibrator=null;
    WifiManager wm=null;
    WifiManager.WifiLock wLock = null;
    @SuppressWarnings("FieldCanBeLocal")
    private AdMob admob=null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        admob = AdMob.addBanner(this,R.id.mainLayout);
   }

}