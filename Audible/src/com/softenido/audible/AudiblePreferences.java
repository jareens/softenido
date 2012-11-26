/*
 * AudiblePreferences.java
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

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.Editable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: franci
 * Date: 18/09/12
 * Time: 0:06
 * To change this template use File | Settings | File Templates.
 */
public class AudiblePreferences
{
    private static final String READING_LOWERCASE = "reading.lowercase";
    private static final String READING_IGNORE_TITLE = "reading.ignore.title";
    private static final String READING_IGNORE_TITLE_REPEATED = "reading.ignore.title.repeated";
    private static final String READING_IGNORE_PARENTHESES  = "reading.ignore.parentheses";
    private static final String READING_IGNORE_SQUAREBRACKETS  = "reading.ignore.squarebrackets";
    private static final String READING_IGNORE_CURLYBRACKETS  = "reading.ignore.curlybrackets";
    private static final String READING_IGNORE_PIPE = "reading.ignore.pipe";
    private static final String READING_IGNORE_UNDERSCORE = "reading.ignore.underscore";
    private static final String READING_IGNORE_HYPHENS = "reading.ignore.hyphens";
    private static final String READING_IGNORE_ASTERISK = "reading.ignore.asterisk";

    private static final String AUTO  = "auto";
    private static final String AUTO_PLAY = "auto.play";
    private static final String AUTO_EXIT = "auto.exit";

    private static final String LANG_DEFAULT  = "lang.default";
    private static final String LANG_DETECT   = "lang.detect";
    private static final String LANG_UNIT = "lang.unit";

    public static final String AUTO_SCREEN_LOCK = "auto.screen.lock";

    private static final String UI_FONT_TYPEFACE = "ui.font.typeface";
    private static final String UI_FONT_SIZE     = "ui.font.size";
    private static final String UI_FONT_BOLD     = "ui.font.bold";
    private static final String UI_TOASTS        = "ui.toasts";
    private static final String UI_PROGRESS      = "ui.progress";
    private static final String UI_VOLUME        = "ui.volume";
    private static final String UI_JOINLINES     = "ui.joinlines";


    private static final String EARLY_SAVE       = "advanced.ealysave";
    private static final String EARLY_DETECT     = "advanced.ealydetect";
    private static final String KEEP_QUICK_START = "advanced.keep_quick_start";

    private static final String TITLE = "title";
    private static final String BODY  = "body";
    private static final String EXTRA = "extra";
    private static final String LEARN = "learn";
    private static final String THEME = "theme";
    private static final String THEME_DEFAULT = "Dark";

    private static final String PHRASE= "phrase";

    static volatile AudiblePreferences instance=null;
    final SharedPreferences settings;
    volatile SharedPreferences.Editor editor=null;
    volatile boolean modified=true;

    AudiblePreferences(Context ctx)
    {
        this.settings = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static AudiblePreferences getInstance(Context ctx)
    {
        if(instance==null)
        {
            instance = new AudiblePreferences(ctx);
        }
        return instance;
    }

    private SharedPreferences.Editor edit()
    {
        if(editor==null)
        {
            editor = settings.edit();
        }
        return editor;
    }
    public void commit()
    {
        edit().commit();
        editor=null;
    }
    public boolean isModified()
    {
        return modified;
    }
    public void setModified(boolean modified)
    {
        if(modified)
        {
            languajes = null;
            locales   = null;
        }
        this.modified = modified;
    }

    public String getTitle()
    {
        return settings.getString(TITLE,"");
    }
    public void setTitle(String head)
    {
        edit().putString(TITLE, head);
    }

    public String getBody()
    {
        return settings.getString(BODY,"");
    }
    public void setBody(String body)
    {
        edit().putString(BODY, body);
    }

    public boolean isPhrase()
    {
        return settings.getBoolean(PHRASE, false);
    }
    public void setPhrase(boolean value)
    {
        edit().putBoolean(PHRASE, value);
    }

    public String getTheme()
    {
        return settings.getString(THEME, THEME_DEFAULT);
    }

    public String getLangDefault()
    {
        return settings.getString(LANG_DEFAULT,"");
    }
    public boolean getLangDetect()
    {
        return settings.getBoolean(LANG_DETECT, false);
    }

//    static final String iso3Compressed = "afr,ind,msa,cat,ces,dan,deu,est,eng,spa,epo,eus,fil,fra,glg,hrv,zul,isl,ita,swa,lav,lit,hun,nld,nor,pol,por,ron,slk,slv,fin,swe,vie,tur,ell,bul,rus,srp,ukr,heb,urd,ara,fas,amh,mar,hin,ben,guj,tam,tel,kan,mal,tha,kor,zho,jpn";
    static final String iso3Compressed = "deu,eng,spa,fra,ita,rus,kor,zho,jpn";
    static String[] iso3 = null;
    static String[] iso3Key = null;
    private String[] languajes = null;
    public String[] getLanguages()
    {
        if(iso3==null)
        {
            iso3 = iso3Compressed.split(",");
            iso3Key = new String[iso3.length];
            for(int i=0;i<iso3.length;i++)
            {
                iso3Key[i] = "lang.iso3."+iso3[i];
            }
        }
        if(languajes==null)
        {
            ArrayList<String> langs = new ArrayList<String>(iso3.length);
            for(int i=0;i<iso3.length;i++)
            {
                if(!settings.getBoolean(iso3Key[i],false))
                {
                    continue;
                }
                langs.add(iso3[i]);
            }
            languajes = langs.toArray(new String[0]);
        }
        return languajes;
    }

    static final String countriesCompressed = "eng,spa,fra,por,zho";
    static HashMap<String,String> countryKey = null;
    private String[] locales   = null;
    public String[] getLocales()
    {
        if(countryKey ==null)
        {
            countryKey = new HashMap<String, String>(countriesCompressed.length());
            String[] countries = countriesCompressed.split(",");
            for(int i=0;i<countries.length;i++)
            {
                countryKey.put(countries[i], "lang.iso3." + countries[i] + ".country");
            }
        }
        if(locales==null)
        {
            String[] langs = getLanguages();
            ArrayList<String> locs = new ArrayList<String>(langs.length);
            for(int i=0;i<langs.length;i++)
            {
                String country = countryKey.get(langs[i]);
                String code = (country==null)? langs[i] : settings.getString(country,langs[i]);
                locs.add(code);
            }
            locales = locs.toArray(new String[0]);
        }
        return locales;

    }


    public boolean getReadingLowercase()
    {
        return settings.getBoolean(READING_LOWERCASE, false);
    }
    public boolean getReadingIgnoreTitle()
    {
        return settings.getBoolean(READING_IGNORE_TITLE, false);
    }
    public boolean getReadingIgnoreTitleRepeated()
    {
        return settings.getBoolean(READING_IGNORE_TITLE_REPEATED, false);
    }

    public boolean getReadingIgnoreParentheses()
    {
        return settings.getBoolean(READING_IGNORE_PARENTHESES, false);
    }
    public boolean getReadingIgnoreSquarebrackets()
    {
        return settings.getBoolean(READING_IGNORE_SQUAREBRACKETS, false);
    }

    public boolean getReadingIgnoreCurlybrackets()
    {
        return settings.getBoolean(READING_IGNORE_CURLYBRACKETS, false);
    }

    public boolean getReadingIgnorePipe()
    {
        return settings.getBoolean(READING_IGNORE_PIPE, false);
    }

    public boolean getReadingIgnoreUnderscore()
    {
        return settings.getBoolean(READING_IGNORE_UNDERSCORE, false);
    }

    public boolean getReadingIgnoreHyphens()
    {
        return settings.getBoolean(READING_IGNORE_HYPHENS, false);
    }
    public boolean getReadingIgnoreAsterisk()
    {
        return settings.getBoolean(READING_IGNORE_ASTERISK, false);
    }

    public String getFontTypeFace()
    {
        return settings.getString(UI_FONT_TYPEFACE, "normal");
    }
    public int getFontSize()
    {
        return getInt(UI_FONT_SIZE, 12);
    }

    public boolean getFontBold()
    {
        return settings.getBoolean(UI_FONT_BOLD, false);
    }

    public boolean isAuto()
    {
        return settings.getBoolean(AUTO, false);
    }
    public void setAuto(boolean value)
    {
        edit().putBoolean(AUTO, value);
    }
    public boolean isAutoPlay()
    {
        return settings.getBoolean(AUTO_PLAY, false);
    }
    public boolean isAutoExit()
    {
        return settings.getBoolean(AUTO_EXIT, false);
    }
    public boolean isAutoScreenLock()
    {
        return settings.getBoolean(AUTO_SCREEN_LOCK, false);
    }
    public boolean isToasts()
    {
        return settings.getBoolean(UI_TOASTS, true);
    }
    public boolean isProgress()
    {
        return settings.getBoolean(UI_PROGRESS, true);
    }
    public boolean isEarlySave()
    {
        return settings.getBoolean(EARLY_SAVE, true);
    }
    public boolean isEarlyDetect()
    {
        return settings.getBoolean(EARLY_DETECT, true);
    }


    public int getQuickStart()
    {
        return getInt(KEEP_QUICK_START, 86400);
    }

    public boolean isVolume()
    {
        return settings.getBoolean(UI_VOLUME, true);
    }
    public void setVolume(boolean value)
    {
        edit().putBoolean(UI_VOLUME, value);
    }

    public boolean isJoinLines()
    {
        return settings.getBoolean(UI_JOINLINES, false);
    }

    public int getLangUnit()
    {
        return getInt(LANG_UNIT, 2);
    }
    private int getInt(String key, int defaultValue)
    {
        try
        {
            return Integer.parseInt(settings.getString(key, ""+defaultValue));
        }
        catch(NumberFormatException ex)
        {
            SharedPreferences.Editor edit = settings.edit();
            edit.putInt(key, defaultValue);
            edit.commit();
            return defaultValue;
        }
    }
    private boolean getBoolean(String key, boolean defaultValue)
    {
        try
        {
            return Boolean.parseBoolean(settings.getString(key, ""+defaultValue));
        }
        catch(NumberFormatException ex)
        {
            SharedPreferences.Editor edit = settings.edit();
            edit.putBoolean(key, defaultValue);
            edit.commit();
            return defaultValue;
        }
    }
}
