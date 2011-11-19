package com.softenido.droiddesk.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by IntelliJ IDEA.
 * User: franci
 * Date: 15/11/11
 * Time: 14:53
 * To change this template use File | Settings | File Templates.
 */
public class MetaData
{
    static public Bundle getBundle(Context ctx)
    {
        Bundle bundle = null;
        ApplicationInfo ai = null;
        try
        {
            ai = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
            return ai.metaData;
        }
        catch (PackageManager.NameNotFoundException ex)
        {
            Log.e(MetaData.class.getName(),"Failed to load meta-data",ex);
        }
        return null;
    }
}
