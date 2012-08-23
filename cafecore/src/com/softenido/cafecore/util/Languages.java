/*
 * Languages.java
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

/**
 *
 * @author franci
 */
public class Languages
{
    static final String[][] ISO_369=
    {
        //639-1,639-2B,639-3, Language Name
        {"aa", "aar", "aar", "Afar"},
        {"ab", "abk", "abk", "Abkhazian"},        
        {"ae", "ave", "ave", "Avestan"},        
        {"af", "afr", "afr", "Afrikaans"},        
        {"ak", "aka", "aka", "Akan"},        
        {"am", "amh", "amh", "Amharic"},        
        {"an", "arg", "arg", "Aragonese"},        
        {"ar", "ara", "ara", "Arabic"},        
        {"as", "asm", "asm", "Assamese"},        
        {"av", "ava", "ava", "Avaric"},        
        {"ay", "aym", "aym", "Aymara"},        
        {"az", "aze", "aze", "Azerbaijani"},        
        {"ba", "bak", "bak", "Bashkir"},        
        {"be", "bel", "bel", "Belarusian"},        
        {"bg", "bul", "bul", "Bulgarian"},        
        {"bi", "bis", "bis", "Bislama"},
        {"bm", "bam", "bam", "Bambara"},
        {"bn", "ben", "ben", "Bengali"},
        {"bo", "tib", "bod", "Tibetan"},
        {"br", "bre", "bre", "Breton"},
        {"bs", "bos", "bos", "Bosnian"},
        {"ca", "cat", "cat", "Catalan"},
        {"ce", "che", "che", "Chechen"},
        {"ch", "cha", "cha", "Chamorro"},
        {"co", "cos", "cos", "Corsican"},
        {"cr", "cre", "cre", "Cree"},
        {"cs", "cze", "ces", "Czech"},
        {"cu", "chu", "chu", "Church Slavic"},
        {"cv", "chv", "chv", "Chuvash"},
        {"cy", "wel", "cym", "Welsh"},
        {"da", "dan", "dan", "Danish"},
        {"de", "ger", "deu", "German"},
        {"dv", "div", "div", "Dhivehi"},
        {"dz", "dzo", "dzo", "Dzongkha"},
        {"ee", "ewe", "ewe", "Ewe"},
        {"el", "gre", "ell", "Modern Greek"},// (1453-)
        {"en", "eng", "eng", "English"},
        {"eo", "epo", "epo", "Esperanto"},
        {"es", "spa", "spa", "Spanish"},
        {"et", "est", "est", "Estonian"},
        {"eu", "baq", "eus", "Basque"},
        {"fa", "per", "fas", "Persian"},
        {"ff", "ful", "ful", "Fulah"},
        {"fi", "fin", "fin", "Finnish"},
        {"fj", "fij", "fij", "Fijian"},
        {"fo", "fao", "fao", "Faroese"},
        {"fr", "fre", "fra", "French"},
        {"fy", "fry", "fry", "Western Frisian"},
        {"ga", "gle", "gle", "Irish"},
        {"gd", "gla", "gla", "Scottish Gaelic"},
        {"gl", "glg", "glg", "Galician"},
        {"gn", "grn", "grn", "Guarani"},
        {"gu", "guj", "guj", "Gujarati"},
        {"gv", "glv", "glv", "Manx"},
        {"ha", "hau", "hau", "Hausa"},
        {"he", "heb", "heb", "Hebrew"},
        {"hi", "hin", "hin", "Hindi"},
        {"ho", "hmo", "hmo", "Hiri Motu"},
        {"hr", "hrv", "hrv", "Croatian"},
        {"ht", "hat", "hat", "Haitian"},
        {"hu", "hun", "hun", "Hungarian"},
        {"hy", "arm", "hye", "Armenian"},
        {"hz", "her", "her", "Herero"},
        {"ia", "ina", "ina", "Interlingua"},
        {"id", "ind", "ind", "Indonesian"},
        {"ie", "ile", "ile", "Interlingue"},
        {"ig", "ibo", "ibo", "Igbo"},
        {"ii", "iii", "iii", "Sichuan Yi"},
        {"ik", "ipk", "ipk", "Inupiaq"},
        {"io", "ido", "ido", "Ido"},
        {"is", "ice", "isl", "Icelandic"},
        {"it", "ita", "ita", "Italian"},
        {"iu", "iku", "iku", "Inuktitut"},
        {"ja", "jpn", "jpn", "Japanese"},
        {"jv", "jav", "jav", "Javanese"},
        {"ka", "geo", "kat", "Georgian"},
        {"kg", "kon", "kon", "Kongo"},
        {"ki", "kik", "kik", "Kikuyu"},
        {"kj", "kua", "kua", "Kuanyama"},
        {"kk", "kaz", "kaz", "Kazakh"},
        {"kl", "kal", "kal", "Kalaallisut"},
        {"km", "khm", "khm", "Central Khmer"},
        {"kn", "kan", "kan", "Kannada"},
        {"ko", "kor", "kor", "Korean"},
        {"kr", "kau", "kau", "Kanuri"},
        {"ks", "kas", "kas", "Kashmiri"},
        {"ku", "kur", "kur", "Kurdish"},
        {"kv", "kom", "kom", "Komi"},
        {"kw", "cor", "cor", "Cornish"},
        {"ky", "kir", "kir", "Kirghiz"},
        {"la", "lat", "lat", "Latin"},
        {"lb", "ltz", "ltz", "Luxembourgish"},
        {"lg", "lug", "lug", "Ganda"},
        {"li", "lim", "lim", "Limburgan"},
        {"ln", "lin", "lin", "Lingala"},
        {"lo", "lao", "lao", "Lao"},
        {"lt", "lit", "lit", "Lithuanian"},
        {"lu", "lub", "lub", "Luba-Katanga"},
        {"lv", "lav", "lav", "Latvian"},
        {"mg", "mlg", "mlg", "Malagasy"},
        {"mh", "mah", "mah", "Marshallese"},
        {"mi", "mao", "mri", "Maori"},
        {"mk", "mac", "mkd", "Macedonian"},
        {"ml", "mal", "mal", "Malayalam"},
        {"mn", "mon", "mon", "Mongolian"},
        {"mr", "mar", "mar", "Marathi"},
        {"ms", "may", "msa", "Malay"},
        {"mt", "mlt", "mlt", "Maltese"},
        {"my", "bur", "mya", "Burmese"},
        {"na", "nau", "nau", "Nauru"},
        {"nb", "nob", "nob", "Norwegian Bokmål"},
        {"nd", "nde", "nde", "North Ndebele"},
        {"ne", "nep", "nep", "Nepali"},
        {"ng", "ndo", "ndo", "Ndonga"},
        {"nl", "dut", "nld", "Dutch"},
        {"nn", "nno", "nno", "Norwegian Nynorsk"},
        {"no", "nor", "nor", "Norwegian"},
        {"nr", "nbl", "nbl", "South Ndebele"},
        {"nv", "nav", "nav", "Navajo"},
        {"ny", "nya", "nya", "Nyanja"},
        {"oc", "oci", "oci", "Occitan"},// (post 1500)
        {"oj", "oji", "oji", "Ojibwa"},
        {"om", "orm", "orm", "Oromo"},
        {"or", "ori", "ori", "Oriya"},
        {"os", "oss", "oss", "Ossetian"},
        {"pa", "pan", "pan", "Panjabi"},
        {"pi", "pli", "pli", "Pali"},
        {"pl", "pol", "pol", "Polish"},
        {"ps", "pus", "pus", "Pushto"},
        {"pt", "por", "por", "Portuguese"},
        {"qu", "que", "que", "Quechua"},
        {"rm", "roh", "roh", "Romansh"},
        {"rn", "run", "run", "Rundi"},
        {"ro", "rum", "ron", "Romanian"},
        {"ru", "rus", "rus", "Russian"},
        {"rw", "kin", "kin", "Kinyarwanda"},
        {"sa", "san", "san", "Sanskrit"},
        {"sc", "srd", "srd", "Sardinian"},
        {"sd", "snd", "snd", "Sindhi"},
        {"se", "sme", "sme", "Northern Sami"},
        {"sg", "sag", "sag", "Sango"},
        {"si", "sin", "sin", "Sinhala"},
        {"sk", "slo", "slk", "Slovak"},
        {"sl", "slv", "slv", "Slovenian"},
        {"sm", "smo", "smo", "Samoan"},
        {"sn", "sna", "sna", "Shona"},
        {"so", "som", "som", "Somali"},
        {"sq", "alb", "sqi", "Albanian"},
        {"sr", "srp", "srp", "Serbian"},
        {"ss", "ssw", "ssw", "Swati"},
        {"st", "sot", "sot", "Southern Sotho"},
        {"su", "sun", "sun", "Sundanese"},
        {"sv", "swe", "swe", "Swedish"},
        {"sw", "swa", "swa", "Swahili"},
        {"ta", "tam", "tam", "Tamil"},
        {"te", "tel", "tel", "Telugu"},
        {"tg", "tgk", "tgk", "Tajik"},
        {"th", "tha", "tha", "Thai"},
        {"ti", "tir", "tir", "Tigrinya"},
        {"tk", "tuk", "tuk", "Turkmen"},
        {"tl", "tgl", "tgl", "Tagalog"},
        {"tn", "tsn", "tsn", "Tswana"},
        {"to", "ton", "ton", "Tonga"},
        {"tr", "tur", "tur", "Turkish"},
        {"ts", "tso", "tso", "Tsonga"},
        {"tt", "tat", "tat", "Tatar"},
        {"tw", "twi", "twi", "Twi"},
        {"ty", "tah", "tah", "Tahitian"},
        {"ug", "uig", "uig", "Uighur"},
        {"uk", "ukr", "ukr", "Ukrainian"},
        {"ur", "urd", "urd", "Urdu"},
        {"uz", "uzb", "uzb", "Uzbek"},
        {"ve", "ven", "ven", "Venda"},
        {"vi", "vie", "vie", "Vietnamese"},
        {"vo", "vol", "vol", "Volapük"},
        {"wa", "wln", "wln", "Walloon"},
        {"wo", "wol", "wol", "Wolof"},
        {"xh", "xho", "xho", "Xhosa"},
        {"yi", "yid", "yid", "Yiddish"},
        {"yo", "yor", "yor", "Yoruba"},
        {"za", "zha", "zha", "Zhuang"},
        {"zh", "chi", "zho", "Chinese"},
        {"zu", "zul", "zul", "Zulu"}
    };
    static final HashMap<String,String> map_iso2 = new HashMap();
    static final HashMap<String,String> map_iso3 = new HashMap();
    static final HashMap<String,String> map_alias = new HashMap();
    static final HashMap<String,String> map_name = new HashMap();
    static
    {
        for(String[] lang : ISO_369)
        {
            String iso2  = lang[0];
            String alias = lang[1];
            String iso3  = lang[2];
            String _Name  = lang[3];
            String name  = _Name.toLowerCase();
            
            alias = alias.equals(iso3)?null:alias;
            
            map_iso2.put(iso2,iso2);
            map_iso2.put(iso3,iso2);
            if(alias!=null) map_iso2.put(alias,iso2);
            map_iso2.put(name,iso2);

            map_iso3.put(iso2,iso3);
            map_iso3.put(iso3,iso3);
            if(alias!=null) map_iso3.put(alias,iso3);
            map_iso3.put(name,iso3);

            if(alias!=null)
            {
                map_alias.put(iso2,alias);
                map_alias.put(iso3,alias);
                map_alias.put(alias,alias);
                map_alias.put(name,alias);
            }
            else
            {
                map_alias.put(iso2,iso3);
                map_alias.put(iso3,iso3);
                map_alias.put(name,iso3);
            }
            
            map_name.put(iso2,_Name);
            map_name.put(iso3,_Name);
            if(alias!=null) map_name.put(alias,_Name);
            map_name.put(name,_Name);
        }
    };
    public static String iso2(String val)
    {
        return map_iso2.get(val!=null?val.toLowerCase():val);
    }
    public static String iso3(String val)
    {
        return map_iso3.get(val!=null?val.toLowerCase():val);
    }
    public static String alias(String val)
    {
        return map_alias.get(val!=null?val.toLowerCase():val);
    }
    public static String name(String val)
    {
        return map_name.get(val!=null?val.toLowerCase():val);
    }
}
