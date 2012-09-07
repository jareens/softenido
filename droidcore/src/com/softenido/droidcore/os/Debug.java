/*
 * Debug.java
 *
 * Copyright (c) 2012 Francisco Gómez Carrasco
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
 *
 * most part of this file has been copied from android source code
 */
package com.softenido.droidcore.os;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: franci
 * Date: 20/07/12
 * Time: 0:09
 * To change this template use File | Settings | File Templates.
 */
public class Debug
{
    public static class ThreadMode
    {
        boolean detectAll;
        boolean diskReads;
        boolean diskWrites;
        boolean network;
        boolean penaltyLog;

        public ThreadMode()
        {
            this(false);
        }
        public ThreadMode(boolean all)
        {
            this.detectAll = all;
            this.diskReads = all;
            this.diskWrites = all;
            this.network = all;
            this.penaltyLog = all;
        }

        public ThreadMode setDetectAll(boolean detectAll)
        {
            this.detectAll = detectAll;
            return this;
        }
        public ThreadMode setDiskReads(boolean diskReads)
        {
            this.diskReads = diskReads;
            return this;
        }
        public ThreadMode setDiskWrites(boolean diskWrites)
        {
            this.diskWrites = diskWrites;
            return this;
        }
        public ThreadMode setNetwork(boolean network)
        {
            this.network = network;
            return this;
        }
        public ThreadMode setPenaltyLog(boolean penaltyLog)
        {
            this.penaltyLog = penaltyLog;
            return this;
        }
    }
    public static class VmMode
    {
        boolean detectLeakedSqlLiteObjects=false;
        boolean detectLeakedClosableObjects=false;
        boolean penaltyLog=false;
        boolean penaltyDeath=false;

        public VmMode()
        {
            this(false);
        }
        public VmMode(boolean all)
        {
            this.detectLeakedSqlLiteObjects = detectLeakedSqlLiteObjects;
            this.detectLeakedClosableObjects = detectLeakedClosableObjects;
            this.penaltyLog = penaltyLog;
            this.penaltyDeath = penaltyDeath;
        }

        public VmMode setDetectLeakedSqlLiteObjects(boolean detectLeakedSqlLiteObjects)
        {
            this.detectLeakedSqlLiteObjects = detectLeakedSqlLiteObjects;
            return this;
        }

        public VmMode setDetectLeakedClosableObjects(boolean detectLeakedClosableObjects)
        {
            this.detectLeakedClosableObjects = detectLeakedClosableObjects;
            return this;
        }

        public VmMode setPenaltyLog(boolean penaltyLog)
        {
            this.penaltyLog = penaltyLog;
            return this;
        }

        public VmMode setPenaltyDeath(boolean penaltyDeath)
        {
            this.penaltyDeath = penaltyDeath;
            return this;
        }
    }

    //return true when package is signed with debug key or android:debuggable=true
    //return false only when signed with production key and android:debuggable=false
    public static boolean isDebuggable(Context context)
    {
        PackageManager pm = context.getPackageManager();
        try
        {
            ApplicationInfo info = pm.getApplicationInfo(context.getPackageName(), 0);
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        }
        catch (PackageManager.NameNotFoundException ex)
        {
            Logger.getLogger(Debug.class.getName()).log(Level.SEVERE, "Error getting if running in debug mode", ex);
        }
        return true;
    }
    public static boolean isDebuggerConnected()
    {
        return android.os.Debug.isDebuggerConnected();
    }
    public static boolean setStrictMode(ThreadMode mode)
    {
        // ThreadPolicy class need Api 9 (Android 2.3), so reflection is used for compatibility
        if(AndroidVersion.os.SDK>=9)
        {

            try
            {
                Class strictModeClass = Class.forName("android.os.StrictMode");
                Class strictMode$ThreadPolicy$BuilderClass = Class.forName("android.os.StrictMode$ThreadPolicy$Builder");
                Object builder = strictMode$ThreadPolicy$BuilderClass.newInstance();

                builder = mode.detectAll? builder.getClass().getMethod("detectAll").invoke(builder) : builder;
                builder = mode.diskReads? builder.getClass().getMethod("detectDiskReads").invoke(builder) : builder;
                builder = mode.diskWrites? builder.getClass().getMethod("detectDiskWrites").invoke(builder) : builder;
                builder = mode.network? builder.getClass().getMethod("detectNetwork").invoke(builder) : builder;
                builder = mode.penaltyLog? builder.getClass().getMethod("penaltyLog").invoke(builder) : builder;

                Object policy = builder.getClass().getMethod("build").invoke(builder);

                Method setThreadPolicy =  strictModeClass.getMethod("setThreadPolicy", policy.getClass());

                setThreadPolicy.invoke(null,policy);

                // code above is API 8 compatible version of the following API 9 compatible code
                //ThreadPolicy.ThreadPolicy.Builder builder = new ThreadPolicy.ThreadPolicy.Builder();
                //builder = detectAll?builder.detectDiskReads():builder;
                //builder = diskReads?builder.detectDiskReads():builder;
                //builder = diskWrites?builder.detectDiskWrites():builder;
                //builder = network?builder.detectNetwork():builder;
                //builder = penaltyLog?builder.penaltyLog():builder;
                //ThreadPolicy.ThreadPolicy policy = x?builder.build();
                //ThreadPolicy.setThreadPolicy(policy);

                return true;
            }
            catch(Exception ex)
            {
                Logger.getLogger(Debug.class.getName()).log(Level.WARNING, "setting StrictMode.StrictThreadPolicy", ex);
            }
        }
        return false;
    }

    public static boolean setVmPolicy(VmMode mode)
    {
        // ThreadPolicy class need Api 9 (Android 2.3), so reflection is used for compatibility
        if(AndroidVersion.os.SDK>=9)
        {

            boolean detectLeakedClosableObjects = mode.detectLeakedClosableObjects && (AndroidVersion.os.SDK>=11);
            try
            {
                Class strictModeClass = Class.forName("android.os.StrictMode");
                Class strictMode$VmPolicy$BuilderClass = Class.forName("android.os.StrictMode$VmPolicy$Builder");
                Object builder = strictMode$VmPolicy$BuilderClass.newInstance();

                builder = mode.detectLeakedSqlLiteObjects? builder.getClass().getMethod("detectLeakedSqlLiteObjects").invoke(builder) : builder;
                builder = detectLeakedClosableObjects? builder.getClass().getMethod("detectLeakedClosableObjects").invoke(builder) : builder;
                builder = mode.penaltyLog? builder.getClass().getMethod("penaltyLog").invoke(builder) : builder;
                builder = mode.penaltyDeath? builder.getClass().getMethod("penaltyDeath").invoke(builder) : builder;
                builder = mode.penaltyLog? builder.getClass().getMethod("penaltyLog").invoke(builder) : builder;

                Object policy = builder.getClass().getMethod("build").invoke(builder);

                Method setVmPolicy =  strictModeClass.getMethod("setVmPolicy", policy.getClass());

                setVmPolicy.invoke(null,policy);

                // code above is API 8 compatible version of the following API 9 compatible code
                //ThreadPolicy.ThreadPolicy.Builder builder = new ThreadPolicy.ThreadPolicy.Builder();
                //builder = detectLeakedSqlLiteObjects?builder.detectLeakedSqlLiteObjects():builder;
                //builder = detectLeakedClosableObjects?builder.detectLeakedClosableObjects():builder;
                //builder = penaltyLog?builder.penaltyLog():builder;
                //builder = penaltyDeath?builder.penaltyDeath():builder;
                //ThreadPolicy.VmPolicy policy = x?builder.build();
                //ThreadPolicy.setVmPolicy(policy);

                return true;
            }
            catch(Exception ex)
            {
                Logger.getLogger(Debug.class.getName()).log(Level.WARNING, "setting StrictMode.VmPolicy", ex);
            }
        }
        return false;
    }
}
