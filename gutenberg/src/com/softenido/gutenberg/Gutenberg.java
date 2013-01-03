/*
 * Gutenberg.java
 *
 * Copyright (c) 2012-2013 Francisco Gómez Carrasco
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
package com.softenido.gutenberg;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author franci
 */
public class Gutenberg
{
    private static enum Holder
    {
        INSTANCE;
        private final String ISO3_LANGUAGES =
                    "ara,deu,eng,fra,ita,jpn,kor,nld,por,rus,spa,tur,zho,";
//                  "afr,amh,ARA,ben,bul,cat,ces,dan,deu,ell,"
//                + "ENG,epo,est,eus,fas,fil,fin,FRA,glg,guj,"
//                + "heb,hin,hrv,hun,ind,isl,ITA,JPN,kan,KOR,"
//                + "lav,lit,mal,mar,msa,NLD,nor,pol,POR,ron,"
//                + "RUS,slk,slv,SPA,srp,swa,swe,tam,tel,tha,"
//                + "TUR,ukr,urd,vie,ZHO,zul,";
        final String[] iso3Languages;
        Set<String> iso3Set;
        Holder()
        {
            iso3Languages = new String[ISO3_LANGUAGES.length()/4];
            iso3Set=new HashSet<String>(iso3Languages.length);
            for (int i = 0; i < iso3Languages.length; i++)
            {
                String lang = ISO3_LANGUAGES.substring((i*4),  (i * 4)+3);
                iso3Languages[i] = lang;
                iso3Set.add(lang);
            }
        }
    }
    
    HashMap<String, String> map;

    public static String[] getISO3Languages()
    {
        return Holder.INSTANCE.iso3Languages.clone();
    }
    public static boolean gz = false;
    public static InputStream getLanguageDataStream(String iso3, boolean low) throws IOException
    {
        if(iso3!=null && iso3.length()>0 && Holder.INSTANCE.iso3Set.contains(iso3))
        {
            String fileName = "lang_"+iso3+".data."+(low?"lo":"hi");
            InputStream is = Gutenberg.class.getResourceAsStream(fileName);
            return gz ? new GZIPInputStream(is) : is; 
        }
        return null;
    }
}

