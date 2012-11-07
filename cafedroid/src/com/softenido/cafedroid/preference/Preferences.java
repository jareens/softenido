/*
 * Views.java
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

package com.softenido.cafedroid.preference;

import android.content.SharedPreferences;
import android.preference.*;
import android.util.Log;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: franci
 * Date: 21/09/12
 * Time: 0:20
 * To change this template use File | Settings | File Templates.
 */
public class Preferences
{
    final HashMap<Preference,String> map = new HashMap<Preference,String>();
    final PreferenceActivity pa;

    public Preferences(PreferenceActivity pa)
    {
        this.pa = pa;
    }

    public void updateSummaryAll()
    {
        PreferenceScreen ps = pa.getPreferenceScreen();
        updateSummaryAll(ps);
    }
    public void updateSummaryAll(Preference preference)
    {
        updateSummary(preference);
        if(preference instanceof PreferenceGroup)
        {
            PreferenceGroup group = (PreferenceGroup)preference;
            int count = group.getPreferenceCount();
            for(int i=0;i<count;i++)
            {
                updateSummaryAll(group.getPreference(i));
            }
        }
    }

    public void updateSummary(String key)
    {
        Preference preference = pa.findPreference(key);
        if(preference!=null)
        {
            updateSummary(preference);
        }
    }

    void updateSummary(Preference preference)
    {
        String summary = map.get(preference);

        if(summary!=null)
        {
            formatSummary(preference, summary);
            return;
        }

        CharSequence cs = preference.getSummary();
        if(cs==null)
        {
            return;
        }
        summary = cs.toString();

        if(summary.contains("%s") || summary.contains("%1$s"))
        {
            map.put(preference, summary);
            formatSummary(preference, summary);
            return;
        }

        if(preference instanceof ListPreference)
        {
            pa.getListView().invalidateViews();
        }
    }

    SharedPreferences sharedPreferences=null;//need late initialization in order to wait preference resource has been loaded
    void formatSummary(Preference preference, String summary)
    {
        if(preference instanceof CheckBoxPreference)
        {
            boolean entry = ((CheckBoxPreference) preference).isChecked();
            preference.setSummary(String.format(summary,entry));
        }
        else if(preference instanceof ListPreference)
        {
            CharSequence entry = ((ListPreference) preference).getEntry();
            if(entry!=null)
            {
                preference.setSummary(String.format(summary,entry.toString()));
            }
        }
        else
        {
            //EditTextPreference, MultiSelectListPreference, PreferenceCategory, PreferenceScreen, SwitchPreference
            Log.d(Preferences.class.getName(),"formatSummary: key="+preference.getKey());
        }
    }
}
