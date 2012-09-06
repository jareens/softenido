/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
                  "afr,amh,ara,ben,bul,cat,ces,dan,deu,ell,"
                + "eng,epo,est,eus,fas,fil,fin,fra,glg,guj,"
                + "heb,hin,hrv,hun,ind,isl,ita,jpn,kan,kor,"
                + "lav,lit,mal,mar,msa,nld,nor,pol,por,ron,"
                + "rus,slk,slv,spa,srp,swa,swe,tam,tel,tha,"
                + "tur,ukr,urd,vie,zho,zul,";
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

    public static InputStream getLanguageDataStream(String iso3) throws IOException
    {
        if(iso3!=null && iso3.length()>0 && Holder.INSTANCE.iso3Set.contains(iso3))
        {
             String fileName = "lang_"+iso3+".data.gz";
            return new GZIPInputStream(Gutenberg.class.getResourceAsStream(fileName));
        }
        return null;
    }
}
