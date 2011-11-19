package com.softenido.droiddesk.admob;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.softenido.droiddesk.util.MetaData;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

/**
 * Created by IntelliJ IDEA.
 * User: franci
 * Date: 7/11/11
 * Time: 0:55
 * To change this template use File | Settings | File Templates.
 */
public class AdMob
{
    public static final String ADMOB_PUBLISHER_ID = "ADMOB_PUBLISHER_ID";
    public static final String TEST_DEVICES       = "TEST_DEVICES";
    public static final String NO_ADS_AT_ALL      = "NO_ADS_AT_ALL";

    static volatile boolean initialized = false;
    static volatile String id = null;
    static final HashSet<String> testDevices = new HashSet<String>();
    {
        testDevices.add(AdRequest.TEST_EMULATOR);
    }
    static volatile AdRequest request = null;

    private static void init(Context ctx)
    {
        if(initialized==false)
        {
            Bundle bundle = MetaData.getBundle(ctx);
            if(id==null)
            {
                // get AdMob Publisher Id from AndroidManisfest metadata
                id = (bundle!=null)?bundle.getString(ADMOB_PUBLISHER_ID):null;
            }
            // get Test Devices from AndroidManisfest metadata
            String list = (bundle!=null)?bundle.getString(TEST_DEVICES):null;
            String devs[]=(list!=null)  ?list.split(","):new String[0];
            for(String item:devs)
            {
                testDevices.add(item);
            }
            request = new AdRequest();
            request.setTestDevices(AdMob.testDevices);
            initialized = true;
        }
    }

    private static void init(Activity activity)
    {
        init(activity.getApplication().getBaseContext());
    }

    private static void setId(String val)
    {
        id = val;
    }

    public static void addTestDevice(String device)
    {
        testDevices.add(device);
    }

    static AdMob addAdView(Activity activity, AdSize adSize, LinearLayout layout)
    {
        init(activity);
        if(id.equals(NO_ADS_AT_ALL))
        {
            return null;
        }
        try
        {
            AdView adView = new AdView(activity, adSize, id);
            layout.addView(adView);
            adView.loadAd(request);
            return  new AdMobBanner(adView);
        }
        catch (Exception ex)
        {
            Logger.getLogger(AdMob.class.getName()).log(Level.SEVERE,"addAdView",ex);
            return null;
        }
    }
    public static AdMob addBanner(Activity activity, LinearLayout layout)
    {
        return  addAdView(activity,AdSize.BANNER,layout);
    }
    public static AdMob addBanner(Activity activity, int layoutId)
    {
        LinearLayout layout = (LinearLayout) activity.findViewById(layoutId);
        return  addBanner(activity, layout);
    }

}
