package com.softenido.droiddesk.admob;

import com.google.ads.AdView;

/**
 * Created by IntelliJ IDEA.
 * User: franci
 * Date: 7/11/11
 * Time: 1:02
 * To change this template use File | Settings | File Templates.
 */
public class AdMobBanner extends AdMob
{
    private final AdView adView;

    public AdMobBanner(AdView adView)
    {
        this.adView = adView;
    }

    @Override
    protected void finalize() throws Throwable
    {
        adView.destroy();
        super.finalize();
    }
}
