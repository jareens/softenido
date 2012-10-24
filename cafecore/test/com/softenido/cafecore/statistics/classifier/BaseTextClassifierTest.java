/*
 * BaseTextClassifierTest.java
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
package com.softenido.cafecore.statistics.classifier;

import com.softenido.cafecore.io.Files;
import com.softenido.cafecore.util.Arrays6;
import com.softenido.cafecore.util.Locales;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author franci
 */
public class BaseTextClassifierTest
{
    static final String [][] LEARN= 
    {
        {"af", "af","google-terms-of-service-af.txt.gz"},//Afrikaans
        {"am", "am","google-terms-of-service-am.txt.gz"},//Amharic
        {"ar", "ar","google-terms-of-service-ar.txt.gz"},//Arabic-Árabe
        {"bg", "bg","google-terms-of-service-bg.txt.gz"},//Bulgarian-Búlgaro
        {"bn", "bn","google-terms-of-service-bn.txt.gz"},//Bengali
        {"ca", "ca","google-terms-of-service-ca.txt.gz"},//catalan
        {"cs", "cs","google-terms-of-service-cs.txt.gz"},//Czech-Checo
        {"da", "da","google-terms-of-service-da.txt.gz"},//Danish-Danés
        {"de", "de","google-terms-of-service-de.txt.gz"},//German-Alemán
        {"el", "el","google-terms-of-service-el.txt.gz"},//Modern Greek-Griego
        {"en", "en","google-terms-of-service-en_UK.txt.gz"},//inglés UK
        {"en", "en","google-terms-of-service-en_US.txt.gz"},//inglés US
        {"eo", "eo","Rakontoj-(Jakub Arbes)-eo.txt.gz"},//Esperanto
        {"es", "es","google-terms-of-service-es.txt.gz"},//Spanish-español
        {"es", "es","google-terms-of-service-es-419.txt.gz"},//Spanish-Español Latinoamerica
        {"et", "et","google-terms-of-service-et.txt.gz"},//Estonian-Estonio-Eesti
        {"eu", "eu","google-terms-of-service-eu.txt.gz"},//euskera
        {"fa", "fa","google-terms-of-service-fa.txt.gz"},//Persian-Persa-Farsi
        {"fi", "fi","google-terms-of-service-fi.txt.gz"},//Finnish-Finés-Suomi
        {"fil","fil","google-terms-of-service-fil.txt.gz"},//Filipino
        {"fr", "fr","google-terms-of-service-fr.txt.gz"},//francés
        {"fr", "fr","google-terms-of-service-fr_CA.txt.gz"},//francés
        {"gl", "gl","google-terms-of-service-gl.txt.gz"},//Galician-gallego-galego    
        {"gu", "gu","google-terms-of-service-gu.txt.gz"},//Gujarati
        {"heb","heb","google-terms-of-service-heb.txt.gz"},//(iw)Hebrew-Hebreo
        {"hu", "hu","google-terms-of-service-hu.txt.gz"},//Hungarian-Húngaro-Magyar
        {"hi", "hi","google-terms-of-service-hi.txt.gz"},//hindi
        {"hr", "hr","google-terms-of-service-hr.txt.gz"},//Croatian-Croata-Hrvatski
        {"id", "id","google-terms-of-service-id.txt.gz"},//Indonesian-Indonesio
        {"is", "is","google-terms-of-service-is.txt.gz"},//Icelandic-Islandés-Íslenska
        {"it", "it","google-terms-of-service-it.txt.gz"},//italiano
        {"ja", "ja","google-terms-of-service-ja.txt.gz"},//japones
        {"ko", "ko","google-terms-of-service-ko.txt.gz"},//coreano
        {"kn", "kn","google-terms-of-service-kn.txt.gz"},//Kannada
        {"lt", "lt","google-terms-of-service-lt.txt.gz"},//Lithuanian-Lituano
        {"lv", "lv","google-terms-of-service-lv.txt.gz"},//Latvian-Letón
        {"ml", "ml","google-terms-of-service-ml.txt.gz"},//Malayalam
        {"mr", "mr","google-terms-of-service-mr.txt.gz"},//Marathi
        {"ms", "ms","google-terms-of-service-ms.txt.gz"},//Malay-Malayo
        {"nl", "nl","google-terms-of-service-nl.txt.gz"},//Dutch-Neerlandes(holandes)-Nederlands
        {"no", "no","google-terms-of-service-no.txt.gz"},//Norwegian-Bokmal Noruego-Norsk
        {"pl", "pl","google-terms-of-service-pl.txt.gz"},//Polish-Polaco
        {"pt", "pt","google-terms-of-service-pt_BR.txt.gz"},//Portugues(Brasil)
        {"pt", "pt","google-terms-of-service-pt_PT.txt.gz"},//Portugues(Portugal)
        {"ro", "ro","google-terms-of-service-ro.txt.gz"},//Romanian-Rumano
        {"ru", "ru","google-terms-of-service-ru.txt.gz"},//ruso
        {"sk", "sk","google-terms-of-service-sk.txt.gz"},//Slovak-Eslovaco
        {"sl", "sl","google-terms-of-service-sl.txt.gz"},//Sinhala-Esloveno
        {"sr", "sr","google-terms-of-service-sr.txt.gz"},//Serbian-Serbio
        {"sv", "sv","google-terms-of-service-sv.txt.gz"},//Swedish-Sueco
        {"sw", "sw","google-terms-of-service-sw.txt.gz"},//Swahili-Swahili-Kiswahili
        {"ta", "ta","google-terms-of-service-ta.txt.gz"},//Tamil
        {"th", "th","google-terms-of-service-th.txt.gz"},//Thai-Tailandés
        {"te", "te","google-terms-of-service-te.txt.gz"},//Telugu
        {"tr", "tr","google-terms-of-service-tr.txt.gz"},//Turkish-Turco
        {"uk", "uk","google-terms-of-service-uk.txt.gz"},//Ukrainian-Ucraniano
        {"ur", "ur","google-terms-of-service-ur.txt.gz"},//Urdu
        {"vi", "vi","google-terms-of-service-vi.txt.gz"},//Vietnamese-Vietnamita
        {"zh", "zh_cn","google-terms-of-service-zh_CN.txt.gz"},//chino china
        {"zh", "zh_hk","google-terms-of-service-zh_HK.txt.gz"},//chino hongkong
        {"zh", "zh_tw","google-terms-of-service-zh_TW.txt.gz"},//chino taiwan
        {"zu", "zu","google-terms-of-service-zu.txt.gz"},//Zulu-Isizulu
        {"de", "de","udhr-deu.txt.gz"},//inglés
        {"en", "en","udhr-eng.txt.gz"},//inglés
        {"fr", "fr","udhr-fra.txt.gz"},//frances
        {"it", "it","udhr-ita.txt.gz"},//italiano
        {"ja", "ja","udhr-jpn.txt.gz"},//japones
        {"ko", "ko","udhr-kor.txt.gz"},//koreano
        {"ru", "ru","udhr-rus.txt.gz"},//ruso
        {"es", "es","udhr-spa.txt.gz"},//español
        {"zh", "zh_cn","udhr-zho.txt.gz"},//chino mandarín
        {"en", "en","google-privacy-en.txt.gz"},//ingles
        {"es", "es","google-privacy-es.txt.gz"},//español
        {"es", "es","google-privacy-es-419.txt.gz"},//español (latinoamerica)
        {"fr", "fr","google-privacy-fr.txt.gz"},//francés
        {"en", "en","google-privacy-en_UK.txt.gz"},//inglés UK
        {"en", "en","google-privacy-en_US.txt.gz"},//inglés US
        {"it", "it","google-privacy-it.txt.gz"},//italiano
        {"ja", "ja","google-privacy-ja.txt.gz"},//japones
        {"ko", "ko","google-privacy-ko.txt.gz"},//koreano
        {"ru", "ru","google-privacy-ru.txt.gz"},//ruso
        {"zh", "zh_CN","google-privacy-zh_CN.txt.gz"},//chino simplificado
        {"zh", "zh_TW","google-privacy-zh_TW.txt.gz"},//chino tradicional
        {"zh", "zh_HK","google-privacy-zh_HK.txt.gz"},//chino HongKong
        {"de", "de","google-play-tos-de.txt.gz"},//alemán
        {"en", "en","google-play-tos-en.txt.gz"},//inglés US
        {"fr", "fr","google-play-tos-fr.txt.gz"},//francés
        {"ko", "ko","google-play-tos-ko.txt.gz"},//koreano
        {"it", "it","google-play-tos-it.txt.gz"},//italia
        {"zh", "zh_TW","google-play-tos-zh_TW.txt.gz"},//chino taiwan
        {"es", "es","google-play-tos-es.txt.gz"},//español España
        {"zh", "zh_HK","google-privacy-zh_HK.txt.gz"},//chino HongKong
        {"ja", "ja","google-play-tos-ja.txt.gz"},//japonés
        {"es", "es","La_obscenidad_y_el_símbolo_-_sobre_la_acción_política_del_SAT.txt.gz"},//español
        {"en", "en","TheGreatBoerWar-ArthurConanDoyle.txt.gz"},//inglés
        {"es", "es","Historia_de_la_vida_del_Buscón-Francisco_de_Quevedo.txt.gz"},//español
        {"de", "de","Macchiavellis_Buch_vom_Fürsten-Niccolò_Machiavelli.txt.gz"},//alemán
        {"fr", "fr","LesTroisMousquetaires-AlexandreDumas.fr.txt.gz"},//francés
    };    
    static final String LINE = "^[A-Za-z0-9]+.+$";
    public static final String AMBIGUOUS = ".*(Copyright|Parkway|Google|escrito|EXEMPLARES|Estados *Unidos|vaststelt|Mountain View|Perlindungan Privasi|Laas gewysig|Ultima modifica|našich Službách|PUNITIVE DAMAGES|sublime au ridicule|London, _sir,|_Thank you, be easy._).*";
    
    public BaseTextClassifierTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of classify method, of class BaseTextClassifier.
     */
    @Test
    public void testClassify_InputStream() throws IOException, ClassifierFormatException, UnsupportedEncodingException, NoSuchAlgorithmException
    {
        File home = Files.getHomeFile();
        File dst  = new File(home,"cafecore-data");
        dst.mkdir();
        
        final boolean[] PARALLEL = {false, true};
                
        for(boolean parallel: PARALLEL)
        {
            BaseTextClassifier classifierAll = parallel?new BaseTextClassifier("",LEARN.length):new BaseTextClassifier("");
            BaseTextClassifier classifierInc = parallel?new BaseTextClassifier("",LEARN.length):new BaseTextClassifier("");
            BaseTextClassifier classifierISO3 = parallel?new BaseTextClassifier("",LEARN.length):new BaseTextClassifier("");

            for(int i=0;i<LEARN.length;i++)
            {
                String locale  = LEARN[i][1];
                String file  = LEARN[i][2];
                InputStream gz;
                gz = new GZIPInputStream(BaseTextClassifierTest.class.getResourceAsStream(file));
                classifierAll.coach(locale, gz);

                //la suma de los ficheros individuales es menor que agrupados en uno solo
                String lang3 = Locales.getISO3Language(LEARN[i][0]);
                lang3 = lang3!=null? lang3:LEARN[i][0];
                BaseTextClassifier classifier = parallel?new BaseTextClassifier("",LEARN.length):new BaseTextClassifier("");
                gz = new GZIPInputStream(BaseTextClassifierTest.class.getResourceAsStream(file));
                classifier.coach(lang3, gz);
                
                gz = new GZIPInputStream(BaseTextClassifierTest.class.getResourceAsStream(file));
                classifierISO3.coach(lang3, gz);
            }
            
            Locale.getISOCountries();
            //uncomment this line to get a dictionariy
            //classifierAll.save(new FileOutputStream("dictionary.txt"));
            //classifier.saveGZ(new FileOutputStream("dictionary.txt.gz"));

            ByteArrayOutputStream out =  new ByteArrayOutputStream();
            classifierAll.save(out, 0, Integer.MAX_VALUE);
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            
            String[] categories = ((AbstractClassifier)classifierAll.classifier).getCategories();
            categories = Arrays6.copyOf(categories, LEARN.length);
            
            BaseTextClassifier classifier2 = parallel?new BaseTextClassifier("", categories):new BaseTextClassifier("");
            classifier2.load(in, true);

            assertEquals(classifierAll, classifier2);       

            classifierAll.classify("nou a elecció de google");

            for(int i=0;i<LEARN.length;i++)
            {
                String lang = LEARN[i][1];
                String file = LEARN[i][2];
                classifierInc.coach(lang, new GZIPInputStream(BaseTextClassifierTest.class.getResourceAsStream(file)));

                InputStream gz = new GZIPInputStream(BaseTextClassifierTest.class.getResourceAsStream(file));    
                Scanner sc = new Scanner(gz);
                int count=0;
                while( sc.hasNextLine())
                {
                    count++;
                    String line=sc.nextLine().trim().replaceAll(" +"," " );
                    if(!line.matches(AMBIGUOUS) && line.length()>20)
                    {
                        String langAll= classifierAll.classify(line).getName();
                        String langInc= classifierInc.classify(line).getName();
                        //System.out.println(lang+"-"+lang2+"("+line+")");
                        String msg = "lang="+lang+" text="+line;
                        int n   =Math.min(3,lang.length());
                        int nAll=Math.min(3,langAll.length());
                        int nInc=Math.min(3,langInc.length());
                        // por ahora confunde los chinos entre sí cuando hay pocas ocurrencias.
                        assertEquals(file+" all"+msg,lang.substring(0,n), langAll.substring(0,n));
                        assertEquals(file+" inc"+msg,lang.substring(0,n), langInc.substring(0,n));
                    }
                }
            }
            
            if(parallel)
            {
                final String[] export = {"deu", "eng",  "fra",  "ita",  "jpn",  "kor",  "por",  "rus",  "spa",  "zho"};
                final int[] threshold = {4,     13,      11,      0,      0,      0,      0,      2,      4,      0};
                                
                for(int i=0;i<export.length;i++)
                {
                    String fileHiFreq = "lang_"+export[i]+".data.hi";
                    String fileLoFreq = "lang_"+export[i]+".data.lo";
                    File   fdhi = new File(dst,fileHiFreq.toLowerCase());
                    File   fdlo = new File(dst,fileLoFreq.toLowerCase());
                    classifierISO3.save(new FileOutputStream(fdhi),threshold[i], Integer.MAX_VALUE,export[i]);
                    classifierISO3.save(new FileOutputStream(fdlo),0, threshold[i]-1,export[i]);
                }
            }

            // found empty category where no other category matches
            assertEquals("", classifierISO3.classify("abcdefghijklmnñopqrstuvwxyz").getName());
        }
    }
    
    /**
     * Test of filter method, of class BaseTextClassifier.
     */
    @Test
    public void testSplit()
    {
        final String[] texts = 
        {
            "don quijote de la mancha.",//0
            "don,quijote,de,la,mancha.",//1
            "don-quijote-de-la-mancha.",//2
            "don+quijote+de+la+mancha.",//3
            "don quijote\tde la\tmancha.",//4
            "¿Donde estás?", //5
            "Hello World!",//6
            "查詢促進民間參與公共建設法.",//7
            "查詢促進民間參與公共建設法（210bot法）.",//8
            "查詢促進民間參與公共建設法（210ＢＯＴ法）."//9
        };
        final String[][] expected = 
        { 
            {"don", "quijote", "de", "la", "mancha"},//0
            {"don", "quijote", "de", "la", "mancha"},//1
            {"don", "quijote", "de", "la", "mancha"},//2
            {"don", "quijote", "de", "la", "mancha"},//3
            {"don", "quijote", "de", "la", "mancha"},//4
            {"Donde", "estás"},//5
            {"Hello", "World"},//6
            {"查", "詢", "促", "進", "民", "間", "參", "與", "公", "共", "建", "設", "法"},//7
            {"查", "詢", "促", "進", "民", "間", "參", "與", "公", "共", "建", "設", "法", "210bot", "法"},//8
            {"查", "詢", "促", "進", "民", "間", "參", "與", "公", "共", "建", "設", "法", "210ＢＯＴ", "法"} //9
        };
                                 
        BaseTextClassifier classifier = new BaseTextClassifier("");
       
        for(int i=0;i<texts.length;i++)
        {
            String[] result = BaseTextClassifier.split(texts[i]);
            assertArrayEquals(expected[i], result);
        }
    }
}
