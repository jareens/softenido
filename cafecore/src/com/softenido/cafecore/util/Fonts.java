/*
 * Fonts.java
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

import java.util.Arrays;
import java.util.HashSet;


/**
 *
 * @author franci
 */
public class Fonts
{
    //sans-serif
    static final String Arial       = "Arial";
    static final String Helvetica   = "Helvetica";
    static final String Verdana     = "Verdana";
    static final String Tahoma      = "Tahoma";
    static final String ArialBlack  = "Arial Black";   
    static final String ComicSansMS       = "Comic Sans MS";
    static final String Impact      = "Impact";
    
    //serif
    static final String Times           = "Times";
    static final String TimesNewRoman   = "Times New Roman";
    static final String Georgia         = "Georgia";
    static final String Palatino        = "Palatino";
    static final String Garamond        = "Garamond";
    
    //monospace
    static final String Courier        = "Courier";
    static final String CourierNew     = "Courier New";
    static final String LucidaConsole  = "Lucida Console";
    static final String Monaco         = "Monaco";

    //slab serif
//    static final String Alexandria = "Alexandria";
//    static final String Apex = "Apex";
//    static final String Archer = "Archer";
//    static final String Athens = "Athens";
//    static final String City = "City";
//    static final String ChollaSlab = "Cholla Slab";
//    static final String Clarendon = "Clarendon";
//    static final String ConcreteRoman = "Concrete Roman";
    static final String Egyptienne = "Egyptienne";
//    static final String GuardianEgyptian = "Guardian Egyptian";
//    static final String Lexia = "Lexia";
//    static final String MuseoSlab = "Museo Slab";
    static final String Rockwell = "Rockwell";
//    static final String Nilland = "Nilland";
//    static final String SkeletonAntique = "Skeleton Antique";
//    static final String Tower = "Tower";
    
    
//    //sans-serif
//    static final String Gadget      = "Gadget";    
//    static final String Geneva      = "Geneva";
//    static final String LucidaSans = "Lucida Sans";
//    static final String LucidaSansUnicode = "Lucida Sans Unicode";
//    static final String MSSansSerif = "MS Sans Serif";
//    static final String Papyrus     = "Papyrus";
//    static final String Trebuchet   = "Trebuchet";
//    static final String TrebuchetMS = "Trebuchet MS";
//    //serif
//    static final String BookAntiqua     = "Book Antiqua";
//    static final String NewYork         = "New York";
//    static final String MSSerif         = "MS Serif";
//    static final String PalatinoLinotype= "Palatino Linotype";
//    //fantasy
//    static final String Copperplate = "Copperplate";
//    static final String Desdemona   = "Desdemona";
//    static final String Charcoal    = "Charcoal";
//    static final String Kino        = "Kino";
//    //script
//    static final String Lucida_Handwriting  = "Lucida Handwriting";
//    static final String ZapfChancery        = "Zapf Chancery";

    //no generic fonts
//    static final String Symbol = "Symbol";
    static final String Webdings="Webdings";
//    static final String Wingdings="Wingdings";
//    static final String ZapfDingbats="Zapf Dingbats";
    
    
    private static HashSet<String> lowSet(String ... data)
    {
        final HashSet<String> set = new HashSet<String>(data.length);
        for(String item : data)
        {
            set.add(item.toLowerCase());
        }
        return set;
    }
    
    static final HashSet<String> safe_sans = lowSet(Arial, Helvetica);
    static final HashSet<String> safe_serif= lowSet(TimesNewRoman, Times);
    static final HashSet<String> safe_slab = lowSet(Courier);
    static final HashSet<String> safe_mono = lowSet(CourierNew, Courier);
    static final HashSet<String> safe_known= lowSet(Arial, Helvetica, TimesNewRoman, Times, CourierNew, Courier);
    
    static final HashSet<String> sans = lowSet(Arial, Helvetica, Verdana, Tahoma, ArialBlack, ComicSansMS, Impact);
    static final HashSet<String> serif= lowSet(TimesNewRoman, Times, Georgia, Palatino, Garamond);
    static final HashSet<String> slab = lowSet(Egyptienne, Rockwell, Courier);
    static final HashSet<String> mono = lowSet(CourierNew, Courier, LucidaConsole, Monaco);
    static final HashSet<String> cursive = lowSet(ComicSansMS);
    static final HashSet<String> fantasy = lowSet(Webdings);
    static final HashSet<String> known = lowSet(Arial, Helvetica, Verdana, Tahoma, ArialBlack, ComicSansMS, Impact, TimesNewRoman, Times, Georgia, Palatino, Garamond, CourierNew, Courier, LucidaConsole, Monaco, Webdings, Egyptienne, Rockwell);
    
    public static boolean isSafeKnown(String name)
    {
        return safe_known.contains(name.toLowerCase());
    }
    public static boolean isSafeSans(String name)
    {
        return safe_sans.contains(name.toLowerCase());
    }
    public static boolean isSafeSerif(String name)
    {
        return safe_serif.contains(name.toLowerCase());
    }
    public static boolean isSafeSlab(String name)
    {
        return safe_slab.contains(name.toLowerCase());
    }
    public static boolean isSafeMonospace(String name)
    {
        return safe_mono.contains(name.toLowerCase());
    }

    public static boolean isKnown(String name)
    {
        return known.contains(name.toLowerCase());
    }
    public static boolean isSans(String name)
    {
        return sans.contains(name.toLowerCase());
    }
    public static boolean isSerif(String name)
    {
        return serif.contains(name.toLowerCase());
    }
    public static boolean isSlab(String name)
    {
        return slab.contains(name.toLowerCase());
    }
    public static boolean isMonospace(String name)
    {
        return mono.contains(name.toLowerCase());
    }
    static boolean isCursive(String name)
    {
        return cursive.contains(name.toLowerCase());
    }
    static boolean isFantasy(String name)
    {
        return fantasy.contains(name.toLowerCase());
    }
    
    public static String getGenericFamily(String name)
    {
        StringBuilder family=new StringBuilder();
        String comma="";
        if(sans.contains(name))
        {
            family.append(comma).append("sans-serif");
            comma = ", ";
        }
        if(serif.contains(name))
        {
            family.append(comma).append("serif");
            comma = ", ";
        }
        if(mono.contains(name))
        {
            family.append(comma).append("monospace");
            comma = ", ";
        }
        if(cursive.contains(name))
        {
            family.append(comma).append("cursive");
            comma = ", ";
        }
        if(fantasy.contains(name))
        {
            family.append(comma).append("fantasy");
        }
        return family.toString();
    }    
}
