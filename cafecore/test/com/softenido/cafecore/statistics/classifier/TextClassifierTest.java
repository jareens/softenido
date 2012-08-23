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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
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
        {"af","google-terms-of-service-af.txt.gz"},//Afrikaans
        {"am","google-terms-of-service-am.txt.gz"},//Amharic
        {"ar","google-terms-of-service-ar.txt.gz"},//Arabic-Árabe
        {"bg","google-terms-of-service-bg.txt.gz"},//Bulgarian-Búlgaro
        {"bn","google-terms-of-service-bn.txt.gz"},//Bengali
        {"ca","google-terms-of-service-ca.txt.gz"},//catalan
        {"cs","google-terms-of-service-cs.txt.gz"},//Czech-Checo
        {"da","google-terms-of-service-da.txt.gz"},//Danish-Danés
        {"de","google-terms-of-service-de.txt.gz"},//German-Alemán
        {"el","google-terms-of-service-el.txt.gz"},//Modern Greek-Griego
        {"en_UK","google-terms-of-service-en_UK.txt.gz"},//inglés UK
        {"en_US","google-terms-of-service-en_US.txt.gz"},//inglés US
        {"es","google-terms-of-service-es.txt.gz"},//Spanish-español
        {"es","google-terms-of-service-es-419.txt.gz"},//Spanish-Español Latinoamerica
        {"et","google-terms-of-service-et.txt.gz"},//Estonian-Estonio-Eesti
        {"eu","google-terms-of-service-eu.txt.gz"},//euskera
        {"fa","google-terms-of-service-fa.txt.gz"},//Persian-Persa-Farsi
        {"fi","google-terms-of-service-fi.txt.gz"},//Finnish-Finés-Suomi
        {"fil","google-terms-of-service-fil.txt.gz"},//Filipino
        {"fr","google-terms-of-service-fr.txt.gz"},//francés
        {"fr","google-terms-of-service-fr_CA.txt.gz"},//francés
        {"gl","google-terms-of-service-gl.txt.gz"},//Galician-gallego-galego    
        {"gu","google-terms-of-service-gu.txt.gz"},//Gujarati
        {"heb","google-terms-of-service-heb.txt.gz"},//(iw)Hebrew-Hebreo
        {"hu","google-terms-of-service-hu.txt.gz"},//Hungarian-Húngaro-Magyar
        {"hi","google-terms-of-service-hi.txt.gz"},//hindi
        {"hr","google-terms-of-service-hr.txt.gz"},//Croatian-Croata-Hrvatski
        {"id","google-terms-of-service-id.txt.gz"},//Indonesian-Indonesio
        {"is","google-terms-of-service-is.txt.gz"},//Icelandic-Islandés-Íslenska
        {"it","google-terms-of-service-it.txt.gz"},//italiano
        {"ja","google-terms-of-service-ja.txt.gz"},//japones
        {"ko","google-terms-of-service-ko.txt.gz"},//coreano
        {"kn","google-terms-of-service-kn.txt.gz"},//Kannada
        {"lt","google-terms-of-service-lt.txt.gz"},//Lithuanian-Lituano
        {"lv","google-terms-of-service-lv.txt.gz"},//Latvian-Letón
        {"ml","google-terms-of-service-ml.txt.gz"},//Malayalam
        {"mr","google-terms-of-service-mr.txt.gz"},//Marathi
        {"ms","google-terms-of-service-ms.txt.gz"},//Malay-Malayo
        {"nl","google-terms-of-service-nl.txt.gz"},//Dutch-Neerlandes(holandes)-Nederlands
        {"no","google-terms-of-service-no.txt.gz"},//Norwegian-Bokmal Noruego-Norsk
        {"pl","google-terms-of-service-pl.txt.gz"},//Polish-Polaco
        {"pt_BR","google-terms-of-service-pt_BR.txt.gz"},//Portugues(Brasil)
        {"pt_PT","google-terms-of-service-pt_PT.txt.gz"},//Portugues(Portugal)
        {"ro","google-terms-of-service-ro.txt.gz"},//Romanian-Rumano
        {"ru","google-terms-of-service-ru.txt.gz"},//ruso
        {"sk","google-terms-of-service-sk.txt.gz"},//Slovak-Eslovaco
        {"sl","google-terms-of-service-sl.txt.gz"},//Sinhala-Esloveno
        {"sr","google-terms-of-service-sr.txt.gz"},//Serbian-Serbio
        {"sv","google-terms-of-service-sv.txt.gz"},//Swedish-Sueco
        {"sw","google-terms-of-service-sw.txt.gz"},//Swahili-Swahili-Kiswahili
        {"ta","google-terms-of-service-ta.txt.gz"},//Tamil
        {"th","google-terms-of-service-th.txt.gz"},//Thai-Tailandés
        {"te","google-terms-of-service-te.txt.gz"},//Telugu
        {"tr","google-terms-of-service-tr.txt.gz"},//Turkish-Turco
        {"uk","google-terms-of-service-uk.txt.gz"},//Ukrainian-Ucraniano
        {"ur","google-terms-of-service-ur.txt.gz"},//Urdu
        {"vi","google-terms-of-service-vi.txt.gz"},//Vietnamese-Vietnamita
        {"zh_cn","google-terms-of-service-zh_CN.txt.gz"},//chino china
        {"zh_hk","google-terms-of-service-zh_HK.txt.gz"},//chino hongkong
        {"zh_tw","google-terms-of-service-zh_TW.txt.gz"},//chino taiwan
        {"zu","google-terms-of-service-zu.txt.gz"},//Zulu-Isizulu
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
        TextClassifier classifierAll = new TextClassifier();
        TextClassifier classifierInc = new TextClassifier();
        
        for(int i=0;i<LEARN.length;i++)
        {
            String lang = LEARN[i][0];
            InputStream gz = new GZIPInputStream(TextClassifierTest.class.getResourceAsStream(LEARN[i][1]));
            classifierAll.coach(lang, gz);
        }
        
        //uncomment this line to get a dictionariy
        classifierAll.save(new FileOutputStream("dictionary.txt"));
        //classifier.saveGZ(new FileOutputStream("dictionary.txt.gz"));

        ByteArrayOutputStream out =  new ByteArrayOutputStream();
        classifierAll.save(out);
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        TextClassifier classifier2 = new TextClassifier();
        classifier2.load(in);
        
        assertEquals(classifierAll, classifier2);
        
        classifierAll.classify("nou a elecció de google");
        
        for(int i=0;i<LEARN.length;i++)
        {
            String lang = LEARN[i][0];
            classifierInc.coach(lang, new GZIPInputStream(TextClassifierTest.class.getResourceAsStream(LEARN[i][1])));
            
            InputStream gz = new GZIPInputStream(TextClassifierTest.class.getResourceAsStream(LEARN[i][1]));    
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
                                 
        TextClassifier classifier = new TextClassifier();
        for(int i=0;i<texts.length;i++)
        {
            String[] result = classifier.split(texts[i]);
            assertArrayEquals(expected[i], result);
        }
    }

}
