/*
 * AdMob.java
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

package com.softenido.droiddesk.admob;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.softenido.droid.R;
import com.softenido.droidcore.os.MetaData;

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
    static volatile AdRequest request = null;
    private static final String CONF_LOG_MSG = "add to AndroidManifest.xml <meta-data android:name=\"ADMOB_PUBLISHER_ID\" android:value=\"YOUR AdMob ID\"/>";

    private static void init(Context ctx)
    {
        if(initialized==false)
        {
            Bundle bundle = MetaData.getBundle(ctx);
            if(id==null)
            {
                // get AdMob Publisher Id from AndroidManisfest metadata
                id = (bundle!=null)?bundle.getString(ADMOB_PUBLISHER_ID):null;
                if(id==null)
                {
                    Logger.getLogger(AdMob.class.getName()).log(Level.SEVERE,CONF_LOG_MSG);
                }
            }
            // get Test Devices from AndroidManisfest metadata
            String list = (bundle!=null)?bundle.getString(TEST_DEVICES):null;
            String devs[]=(list!=null)  ?list.split(","):new String[0];
            for(String item:devs)
            {
                testDevices.add(item);
            }
            request = new AdRequest();
            request.addTestDevice(AdRequest.TEST_EMULATOR);
            for(String item:testDevices)
            {
                request.addTestDevice(item);
            }
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

    static AdMob addAdView(Activity activity, AdSize adSize, LinearLayout parent, boolean top)
    {
        init(activity);
        if(id==null || id.equals(NO_ADS_AT_ALL))
        {
            return null;
        }
        try
        {
            AdView adView = new AdView(activity, adSize, id);
            if(top)
            {
                parent.addView(adView, 0);
            }
            else
            {
                parent.addView(adView);
            }
            adView.loadAd(request);
            return  new AdMobBanner(adView);
        }
        catch (Exception ex)
        {
            Logger.getLogger(AdMob.class.getName()).log(Level.SEVERE,"addAdView",ex);
            return null;
        }
    }

    public static AdMob addBanner(Activity activity, LinearLayout parent, boolean top)
    {
        return  addAdView(activity,AdSize.BANNER,parent, top);
    }
    public static AdMob addBanner(Activity activity, LinearLayout parent)
    {
        return  addBanner(activity,parent,false);
    }
    public static AdMob addBanner(Activity activity, int parentLayoutId, boolean top)
    {
        LinearLayout layout = (LinearLayout) activity.findViewById(parentLayoutId);
        return  addBanner(activity, layout, top);
    }
    public static AdMob addBanner(Activity activity, int parentLayoutId)
    {
        return  addBanner(activity, parentLayoutId, false);
    }
    public static AdMob addBanner(ListActivity activity, boolean top)
    {
        activity.setContentView(R.layout.admob_listactivity_listview);
        return  addBanner(activity, R.id.admob_listactivity_linearlayout, top);
    }

}
