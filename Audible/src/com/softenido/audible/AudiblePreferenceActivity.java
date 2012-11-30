/*
 * ConfigurationActivity.java
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

package com.softenido.audible;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.widget.ListView;
import com.softenido.cafedroid.os.PolicyAdmin;
import com.softenido.cafedroid.preference.SummaryPrefereceActivity;

/**
 * Created with IntelliJ IDEA.
 * User: franci
 * Date: 3/09/12
 * Time: 23:46
 * To change this template use File | Settings | File Templates.
 */
public class AudiblePreferenceActivity extends SummaryPrefereceActivity
{
    AudiblePreferences preferences=null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        this.setResult(AudibleMain.CODE_PREFERENCES);
        preferences = AudiblePreferences.getInstance(this);

//        ListView listView = getListView();
//        Preference pf = this.findPreference("lang.avaliables");
//        listView.smoothScrollToPosition(9);

    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        if(key.equals(AudiblePreferences.AUTO_SCREEN_LOCK))
        {
            boolean active = sharedPreferences.getBoolean(key,false);
            if(active && !AudibleMain.policyAdmin.isAdminActive())
            {
                String explanation = this.getString(R.string.locknow_explanation);
                AudibleMain.policyAdmin.setActiveAdmin(explanation);
            }
            else if (!active && AudibleMain.policyAdmin.isAdminActive())
            {
                AudibleMain.policyAdmin.removeActiveAdmin();
            }
        }
        super.onSharedPreferenceChanged(sharedPreferences, key);
        preferences.setModified(true);
    }

}
