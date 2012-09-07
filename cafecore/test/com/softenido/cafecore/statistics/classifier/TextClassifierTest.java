/*
 * TextClassifierTest.java
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
public class TextClassifierTest
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
        {"en", "en_UK","google-terms-of-service-en_UK.txt.gz"},//inglés UK
        {"en", "en_US","google-terms-of-service-en_US.txt.gz"},//inglés US
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
        {"pt", "pt_BR","google-terms-of-service-pt_BR.txt.gz"},//Portugues(Brasil)
        {"pt", "pt_PT","google-terms-of-service-pt_PT.txt.gz"},//Portugues(Portugal)
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
    };
    static final String LINE = "^[A-Za-z0-9]+.+$";
    public static final String AMBIGUOUS = ".*(Copyright|Parkway|Google|escrito|EXEMPLARES|Estados *Unidos|vaststelt|Mountain View|Perlindungan Privasi|Laas gewysig|Ultima modifica|našich Službách).*";
    
    public TextClassifierTest()
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
     * Test of classify method, of class TextClassifier.
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
            TextClassifier classifierAll = parallel?TextClassifier.getParallelClassifier(LEARN.length):TextClassifier.getSerialClassifier();
            TextClassifier classifierInc = parallel?TextClassifier.getParallelClassifier(LEARN.length):TextClassifier.getSerialClassifier();

            for(int i=0;i<LEARN.length;i++)
            {
                String locale  = LEARN[i][1];
                String file  = LEARN[i][2];
                InputStream gz;
                gz = new GZIPInputStream(TextClassifierTest.class.getResourceAsStream(file));
                classifierAll.coach(locale, gz);

                //la suma de los ficheros individuales es menor que agrupados en uno solo
                String lang3 = Locales.getISO3Language(LEARN[i][0]);
                TextClassifier classifier = parallel?TextClassifier.getParallelClassifier(LEARN.length):TextClassifier.getSerialClassifier();
                gz = new GZIPInputStream(TextClassifierTest.class.getResourceAsStream(file));
                classifier.coach(lang3, gz);
                String file2 = "lang_"+lang3+".data";
                File   fd = new File(dst,file2.toLowerCase());
                classifier.save(new FileOutputStream(fd),lang3);
            }
            Locale.getISOCountries();
            //uncomment this line to get a dictionariy
            //classifierAll.save(new FileOutputStream("dictionary.txt"));
            //classifier.saveGZ(new FileOutputStream("dictionary.txt.gz"));

            ByteArrayOutputStream out =  new ByteArrayOutputStream();
            classifierAll.save(out);
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            
            String[] categories = ((AbstractClassifier)classifierAll.classifier).getCategories();
            categories = Arrays6.copyOf(categories, LEARN.length);
            
            TextClassifier classifier2 = parallel?TextClassifier.getParallelClassifier(categories):TextClassifier.getSerialClassifier();
            classifier2.load(in);

            assertEquals(classifierAll, classifier2);       

            classifierAll.classify("nou a elecció de google");

            for(int i=0;i<LEARN.length;i++)
            {
                String lang = LEARN[i][1];
                String file = LEARN[i][2];
                classifierInc.coach(lang, new GZIPInputStream(TextClassifierTest.class.getResourceAsStream(file)));

                InputStream gz = new GZIPInputStream(TextClassifierTest.class.getResourceAsStream(file));    
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
                        assertEquals("all"+msg,lang.substring(0,n), langAll.substring(0,n));
                        assertEquals("inc"+msg,lang.substring(0,n), langInc.substring(0,n));
                    }
                }
            }
        }
    }
    
    /**
     * Test of filter method, of class TextClassifier.
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
                                 
        TextClassifier classifier = TextClassifier.getSerialClassifier();
       
        for(int i=0;i<texts.length;i++)
        {
            String[] result = classifier.split(texts[i]);
            assertArrayEquals(expected[i], result);
        }
    }
}
