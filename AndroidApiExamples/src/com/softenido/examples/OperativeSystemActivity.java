/*
 * OperativeSystemActivity.java
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

package com.softenido.examples;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import com.softenido.cafecore.os.OSName;
import com.softenido.droidcore.os.AndroidVersion;
import com.softenido.droidcore.os.Execute;
import com.softenido.droiddesk.admob.AdMob;

public class OperativeSystemActivity extends Activity {
    private AdMob admob = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.os_display);
        admob = AdMob.addBanner(this, R.id.mainLayout, true);

        final Handler handler = new Handler();

        final TextView osName = (TextView) findViewById(R.id.os_name);
        final TextView osVersion = (TextView) findViewById(R.id.os_version);
        final TextView arch = (TextView) findViewById(R.id.os_arch);
        final TextView jvmVersion = (TextView) findViewById(R.id.os_jvm_version);
        final TextView androidVersion = (TextView) findViewById(R.id.os_android_version);
        final TextView api = (TextView) findViewById(R.id.os_android_api);
        final TextView root = (TextView) findViewById(R.id.os_root_access);

        osName.setText(OSName.os.getName());
        arch.setText(System.getProperty("os.arch"));
        osVersion.setText(System.getProperty("os.version"));
        jvmVersion.setText(System.getProperty("java.vm.version"));
        androidVersion.setText(AndroidVersion.os.RELEASE + " " + AndroidVersion.os.NAME);
        api.setText("" + AndroidVersion.os.SDK);
        root.setText("" + Execute.hasRootAccess());
    }
}
