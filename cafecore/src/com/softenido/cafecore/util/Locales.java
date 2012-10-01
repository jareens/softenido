/*
 * Locales.java
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
 */
package com.softenido.cafecore.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

//singleton holder (Efective Java 2nd Ed - Joshua Bloch)
class Country
{
    final String iso2;
    final String iso3;
    final String nameDefault;
    final String nameEnglish;

    public Country(String iso2, String iso3, String nameDefault, String nameEnglish)
    {
        this.iso2 = iso2;
        this.iso3 = iso3;
        this.nameDefault = nameDefault;
        this.nameEnglish = nameEnglish;
    }
}
//singleton holder (Efectife Java 2nd Ed - Joshua Bloch)
enum CountryHolder
{
    INSTANCE;
    
    final HashMap<String,Country> map = new HashMap();
    final Locale defaultLocale = Locale.getDefault();
    
    CountryHolder()
    {
        for(String code : Locale.getISOCountries())
        {
            Locale locale = new Locale("", code, "");
            
            String iso2  = locale.getCountry();
            String iso3  = locale.getISO3Country();
            String nameDefault = locale.getDisplayCountry(defaultLocale);
            //String nameNative  = locale.getDisplayCountry(locale);
            String nameEnglish = locale.getDisplayCountry(Locale.US);
            
            Country country = new Country(iso2, iso3, nameDefault, nameEnglish);
            
            map.put(code.toLowerCase(),country);
            map.put(iso3.toLowerCase(),country);
            map.put(nameDefault.toLowerCase(),country);
            if(!nameEnglish.equalsIgnoreCase(nameDefault))
            {
                map.put(nameEnglish.toLowerCase(),country);
            }
        }
    }
    boolean populate(String code)
    {
        return false;
    }
    
    public String iso2(String val)
    {
        Country country = val!=null?map.get(val.toLowerCase()):null;
        return country!=null?country.iso2:null;
    }
    public String iso3(String val)
    {
        Country country = val!=null?map.get(val.toLowerCase()):null;
        return country!=null?country.iso3:null;
    }
    public String name(String val,Locale locale)
    {
        Country country = val!=null?map.get(val.toLowerCase()):null;
        String name = null;
        if(country!=null)
        {
            if(locale==defaultLocale)
                name = country.nameDefault;
            else if(locale==Locale.US)
                name = country.nameEnglish;
            else 
                name = new Locale("",country.iso2,"").getDisplayCountry(locale);
        }
        return name;
    }
    public String name(String val)
    {
        return name(val,Locale.getDefault());
    }
    
}
class Language
{
    final String iso2;
    final String iso3;
    final String nameDefault;
    final String nameNative;
    final String nameEnglish;

    public Language(String iso2, String iso3, String nameDefault, String nameNative, String nameEnglish)
    {
        this.iso2 = iso2;
        this.iso3 = iso3;
        this.nameDefault = nameDefault;
        this.nameNative  = nameNative;
        this.nameEnglish = nameEnglish;
    }
}
//singleton holder (Efectife Java 2nd Ed - Joshua Bloch) addapted copy from CountryHolder
enum LanguageHolder
{
    INSTANCE;
    
    final HashMap<String,Language> map = new HashMap();
    final Locale defaultLocale = Locale.getDefault();
    
    LanguageHolder()
    {
        for(String code : Locale.getISOLanguages())
        {
            populate(code, map, defaultLocale);
        }
    }
    
    static Language populate(String code, Map<String,Language> map, Locale defaultLocale)
    {
        try
        {
            Locale locale = new Locale(code, "", "");

            String iso2  = locale.getLanguage();
            String iso3  = locale.getISO3Language();
            String nameDefault = locale.getDisplayLanguage(defaultLocale);
            String nameNative  = locale.getDisplayLanguage(locale);
            String nameEnglish = locale.getDisplayLanguage(Locale.US);

            //spanish and interligue uses interligua for ia and ie
            if(iso2.equals("ie"))
            {
                nameDefault=nameNative=nameEnglish;
            }

            Language language = new Language(iso2, iso3, nameDefault, nameNative, nameEnglish);

            map.put(code.toLowerCase(),language);
            map.put(iso3.toLowerCase(),language);
            map.put(nameDefault.toLowerCase(),language);
            boolean nativeEqualsDefault = nameNative.equalsIgnoreCase(nameDefault);
            boolean englishEqualsNative = nameEnglish.equalsIgnoreCase(nameNative);
            boolean englishEqualsDefault = (englishEqualsNative&&nativeEqualsDefault) || nameEnglish.equalsIgnoreCase(nameDefault);
            if(!nativeEqualsDefault)
            {
                map.put(nameNative.toLowerCase(),language);
            }
            if(!englishEqualsDefault)
            {
                map.put(nameEnglish.toLowerCase(),language);
            }
            return language;
        }
        catch(Exception ex)
        {
            Logger.getLogger(LanguageHolder.class.getName()).log(Level.CONFIG,"error populating language",ex);
            return null;
        }
    }
    private static final HashMap<String,String> alt = new HashMap()
    {
        {
            //alternative names, java names
            put("he","iw");
            put("yi","ji");
            put("id","in");
            put("alb", "sqi");
            put("arm", "hye");
            put("baq", "eus");
            put("bur", "mya");
            put("chi", "zho");
            put("cze", "ces");
            put("dut", "nld");
            put("fre", "fra");
            put("ger", "deu");
            put("geo", "kat");
            put("gre", "ell");
            put("ice", "isl");
            put("mac", "mkd");
            put("mao", "mri");
            put("may", "msa");
            put("per", "fas");
            put("rum", "ron");
            put("slo", "slk");
            put("tib", "bod");
            put("wel", "cym");
            put("dhivehi", "divehi");
            put("galician","gallegan");
            put("kuanyama","kwanyama");
            put("kalaallisut","greenlandic");
            put("limburgan","limburgish");
            put("romansh","raeto-romance");
            put("sinhala","sinhalese");
        }
    };
    private String altToISO(String key)
    {
        String val = alt.get(key);
        return val!=null?val:key;
    }
    
    private Language getLanguage(String code)
    {
        if(code!=null)
        {
            String iso = altToISO(code.toLowerCase());
            Language lang = map.get(iso);
            return lang!=null? lang : populate(iso,map,defaultLocale);
        }
        return null;
    }
    
    public String iso2(String val)
    {
        Language language = getLanguage(val);
        return language!=null?language.iso2:null;
    }
    public String iso3(String val)
    {
        Language language = getLanguage(val);
        return language!=null?language.iso3:null;
    }
    public String name(String val,Locale locale)
    {
        Language language = getLanguage(val);
        String name = null;
        if(language!=null)
        {
            if(locale==defaultLocale)
                name = language.nameDefault;
            else if(locale==Locale.US)
                name = language.nameEnglish;
            else if(language.iso2.equals(locale.getLanguage()))
                name = language.nameNative;
            else 
                name = new Locale(language.iso2,"","").getDisplayLanguage(locale);
        }
        return name;
    }
    public String name(String val)
    {
        return name(val,Locale.getDefault());
    }
}

/**
 *
 * @author franci
 */
public class Locales
{
    public static String getISO2Country(String val)
    {
        return CountryHolder.INSTANCE.iso2(val);
    }
    public static String getISO2Language(String val)
    {
        return LanguageHolder.INSTANCE.iso2(val);
    }
    public static String getISO3Country(String val)
    {
        return CountryHolder.INSTANCE.iso3(val);
    }
    public static String getISO3Language(String val)
    {
        return LanguageHolder.INSTANCE.iso3(val);
    }
    public static String getDisplayCountry(String val)
    {
        return CountryHolder.INSTANCE.name(val);
    }
    public static String getDisplayCountry(String val, Locale locale)
    {
        return CountryHolder.INSTANCE.name(val, locale);
    }
    public static String getDisplayCountry(String val, String language)
    {
        String iso2 = getISO2Language(language);
        return iso2!=null?getDisplayCountry(val,new Locale(iso2,"","")):null;
    }
    public static String getDisplayLanguage(String val)
    {
        return LanguageHolder.INSTANCE.name(val);
    }
    public static String[] getDisplayLanguage(String[] val)
    {
        String[] langs = new String[val.length];
        for(int i=0;i<langs.length;i++)
        {
            langs[i] = getDisplayLanguage(val[i]);
        }
        return langs;
    }
    public static String getDisplayLanguage(String val, Locale locale)
    {
        return LanguageHolder.INSTANCE.name(val, locale);
    }
    public static String[] getDisplayLanguage(String[] val, Locale locale)
    {
        String[] langs = new String[val.length];
        for(int i=0;i<langs.length;i++)
        {
            langs[i] = getDisplayLanguage(val[i], locale);
        }
        return langs;
    }
    public static String getDisplayLanguage(String val, String language)
    {
        String iso2 = getISO2Language(language);
        return iso2!=null?getDisplayLanguage(val,new Locale(iso2,"","")):null;
    }
    public static String[] getDisplayLanguage(String[] val, String language)
    {
        String[] langs = new String[val.length];
        for(int i=0;i<langs.length;i++)
        {
            langs[i] = getDisplayLanguage(val[i], language);
        }
        return langs;
    }
    public static String getDisplayLanguageNative(String val)
    {
        return getDisplayLanguage(val,val);
    }
    public static String[] getDisplayLanguageNative(String[] val)
    {
        String[] langs = new String[val.length];
        for(int i=0;i<langs.length;i++)
        {
            langs[i] = getDisplayLanguageNative(val[i]);
        }
        return langs;
    }

    public static String[] getDisplayLanguageCountry(Locale[] val)
    {
        return getDisplayLanguageCountry(val, null);
    }
    public static String[] getDisplayLanguageCountry(Locale[] val, Locale locale)
    {
        String[] display = new String[val.length];
        for(int i=0;i<display.length;i++)
        {
            display[i] = getDisplayLanguageCountry(val[i], locale);
        }
        return display;
    }
    public static String getDisplayLanguageCountry(Locale val)
    {
        return getDisplayLanguageCountry(val, null);
    }
    public static String getDisplayLanguageCountry(Locale val, Locale locale)
    {
        if(val==null)
        {
            return "";
        }
        if(locale==null)
        {
            locale = val;
        }
        String lang = val.getDisplayLanguage(locale);
        String country = val.getDisplayCountry(locale);
        
        if(lang.length()>0 && country.length()>0)
        {
            return lang + " (" + country +")";
        }
        if(lang.length()>0)
        {
            return lang;
        }
        return "("+country+")";
    }
    private enum DefaultHolder
    {
        INSTANCE;
        private final Locale initial = Locale.getDefault();
        void setDefault(Locale value)
        {
            Locale.setDefault(value==null?initial:value);
        }
    }
    // sets a locale or the original one if null is passed as parameter
    public static void setDefault(Locale value)
    {
        DefaultHolder.INSTANCE.setDefault(value);
    }
}
