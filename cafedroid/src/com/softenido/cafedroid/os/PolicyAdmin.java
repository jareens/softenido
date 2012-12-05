/*
 * DeviceAdmin.java
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

package com.softenido.cafedroid.os;

import android.app.Activity;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created with IntelliJ IDEA.
 * User: franci
 * Date: 7/10/12
 * Time: 0:47
 * To change this template use File | Settings | File Templates.
 */
public class PolicyAdmin
{
    public static final int RESULT_ENABLE = 62120256;

    private final Activity activity;
    private final ComponentName componentName;
    private final DevicePolicyManager dpm;

    public PolicyAdmin(Activity activity)
    {
        this.activity = activity;
        this.componentName = new ComponentName(activity, PolicyAdmin.Receiver.class);
        this.dpm = (DevicePolicyManager) activity.getSystemService(Context.DEVICE_POLICY_SERVICE);
    }

    public void setActiveAdmin(int resId, Object... formatArgs)
    {
        String explanation = activity.getString(resId, formatArgs);
        setActiveAdmin(explanation);
    }
    public void setActiveAdmin(String explanation)
    {
        // Launch the activity to have the user enable our admin.
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,  explanation);
        activity.startActivityForResult(intent, PolicyAdmin.RESULT_ENABLE);
    }

    public boolean isAdminActive()
    {
        return this.dpm.isAdminActive(componentName);
    }

    public boolean lockNow()
    {
        try
        {
            this.dpm.lockNow();
            return true;
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    public void  removeActiveAdmin()
    {
        this.dpm.removeActiveAdmin(componentName);
    }

    static public class Receiver extends DeviceAdminReceiver
    {
        static SharedPreferences getSamplePreferences(Context context)
        {
            return context.getSharedPreferences(DeviceAdminReceiver.class.getName(), 0);
        }
    }

}
