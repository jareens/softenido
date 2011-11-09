package com.softenido.wifix;

import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import java.util.ArrayList;
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

    static volatile String adUnitId = null;
    static final HashSet<String> testDev = new HashSet<String>();
    static volatile boolean firstAd = true;
    static volatile AdRequest request = null;

    public static void init(Activity activity,int id,String[] testDevices)
    {
        testDev.clear();
        testDev.add(AdRequest.TEST_EMULATOR);
        for(String dev: testDevices)
        {
            testDev.add(dev);
        }
        Context ctx;
        ctx = activity.getApplication().getBaseContext();
        adUnitId = ctx.getResources().getString(id);
        request = new AdRequest();
        request.setTestDevices(testDev);
    }

    static AdMob addAdView(Activity activity, AdSize adSize, LinearLayout layout)
    {
        if(adUnitId==null)
        {
            return null;
        }
        try
        {

            AdView adView = firstAd ? new AdView(activity, adSize, adUnitId) : new AdView(activity, adSize, adUnitId);

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
        if(adUnitId!=null)
        {
            return  addBanner(activity, (LinearLayout) activity.findViewById(layoutId));
        }
        return null;
    }

}
