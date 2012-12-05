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

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceScreen;
import android.widget.ListView;
import android.widget.Toast;
import com.softenido.cafedroid.admob.AdMob;
import com.softenido.cafedroid.preference.SummaryPrefereceActivity;
import com.softenido.cafedroid.util.ui.Notifier;
import com.softenido.cafedroid.util.ui.ViewsHandler;

/**
 * Created with IntelliJ IDEA.
 * User: franci
 * Date: 3/09/12
 * Time: 23:46
 * To change this template use File | Settings | File Templates.
 */
public class AudiblePreferenceActivity extends SummaryPrefereceActivity
{
    @SuppressWarnings("FieldCanBeLocal")
    private AdMob admob=null;

    static final String LANG_AVALIABLES_KEY = "lang.avaliables";

    AudiblePreferences preferences=null;
    Notifier notifier;
    ViewsHandler vh;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        this.setResult(AudibleMain.CODE_PREFERENCES);
        preferences = AudiblePreferences.getInstance(this);

        Handler handler = new Handler();
        vh = new ViewsHandler(handler);
        notifier = Notifier.build(this,handler,true);

        Intent intent = getIntent();
        if(intent!=null && LANG_AVALIABLES_KEY.equals(intent.getAction()))
        {
            ListView listView = getListView();
            PreferenceScreen pf = (PreferenceScreen)this.findPreference(LANG_AVALIABLES_KEY);
            this.setPreferenceScreen(pf);
        }
    }

    @Override
    public void setPreferenceScreen(PreferenceScreen preferenceScreen) {
        super.setPreferenceScreen(preferenceScreen);    //To change body of overridden methods use File | Settings | File Templates.
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
        else if(key.equals(AudiblePreferences.LANG_DETECT))
        {
            boolean active = sharedPreferences.getBoolean(key,false);
            if(active && preferences.getLanguages().length==0)
            {
                String noLang =getString(R.string.no_enabled_languages_title);
                String optionName = getString(R.string.preferences_language_avaliable_title);
                String msg = getString(R.string.no_enabled_languages_toast, noLang, optionName);
                CheckBoxPreference cp = (CheckBoxPreference)findPreference(key);
                if(cp!=null)
                {
                    notifier.w(msg);
                    vh.setChecked(cp,false);
                }
            }
        }
        super.onSharedPreferenceChanged(sharedPreferences, key);
        preferences.setModified(true);
    }


}
