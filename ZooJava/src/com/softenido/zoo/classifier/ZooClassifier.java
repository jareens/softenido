/*
 *  ZooJava.java
 *
 *  Copyright (C) 2012  Francisco Gómez Carrasco
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  Report bugs or new features to: flikxxi@gmail.com
 *
 */
package com.softenido.zoo.classifier;

import com.softenido.cafecore.profile.ProfileRecord;
import com.softenido.cafecore.profile.Profiler;
import com.softenido.cafecore.statistics.classifier.BaseTextClassifier;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author franci
 */
public class ZooClassifier
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
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException
    {
        Profiler.setActive(true);
        Profiler profiler;
        ProfileRecord track;
        for(int par=0;par<10;par++)
        {
            boolean parallel= (par%2==0);
            BaseTextClassifier classifierAll = parallel?new BaseTextClassifier(null, LEARN.length):new BaseTextClassifier(null);

            profiler = Profiler.getProfiler(ZooClassifier.class.getName()+"-"+parallel+".coach");
            track = profiler.open();
            for(int i=0;i<LEARN.length;i++)
            {
                String lang = LEARN[i][0];
                InputStream gz = new GZIPInputStream(ZooClassifier.class.getResourceAsStream(LEARN[i][1]));
                classifierAll.coach(lang, gz);
            }
            profiler.close(track);            

            
            profiler = Profiler.getProfiler(ZooClassifier.class.getName()+"-"+parallel+".classify.short");
            track = profiler.open();
            for(int i=0;i<LEARN.length;i++)
            {
                String lang = LEARN[i][0].substring(0,2);
                InputStream gz = new GZIPInputStream(ZooClassifier.class.getResourceAsStream(LEARN[i][1]));    
                Scanner sc = new Scanner(gz);
                int count=0;
                while( sc.hasNextLine())
                {
                    
                    count++;
                    String line=sc.nextLine().trim().replaceAll(" +"," " );
                    if(!line.matches(AMBIGUOUS) && line.length()>20)
                    {
                        String lang2= classifierAll.classify(line).getName().substring(0,2);
                        if(!lang2.equals(lang))
                        {
                            System.err.println("error parallel="+parallel+" "+lang+" != "+lang2+"line="+line);
                        }
                    }
                }
            }
            profiler.close(track);

            
            profiler = Profiler.getProfiler(ZooClassifier.class.getName()+"-"+parallel+".classify.long");
            track = profiler.open();
            for(int i=0;i<LEARN.length;i++)
            {
                String lang = LEARN[i][0].substring(0,2);
                InputStream gz = new GZIPInputStream(ZooClassifier.class.getResourceAsStream(LEARN[i][1]));    
                String lang2= classifierAll.classify(gz).getName().substring(0,2);
                if(!lang2.equals(lang))
                {
                    System.err.println("error parallel="+parallel+" "+lang+" != "+lang2);
                }
            }
            profiler.close(track);
            
            profiler = Profiler.getProfiler(ZooClassifier.class.getName()+"-"+parallel+".coach.es-en");
            classifierAll = parallel?new BaseTextClassifier(null, LEARN.length):new BaseTextClassifier(null);
            track = profiler.open();
            for(int i=10;i<14;i++)
            {
                String lang = LEARN[i][0];
                InputStream gz = new GZIPInputStream(ZooClassifier.class.getResourceAsStream(LEARN[i][1]));
                classifierAll.coach(lang, gz);
            }
            profiler.close(track);            

            profiler = Profiler.getProfiler(ZooClassifier.class.getName()+"-"+parallel+".classify.es-en");
            String lang = LEARN[12][0].substring(0,2);
            InputStream gz = new GZIPInputStream(ZooClassifier.class.getResourceAsStream(LEARN[12][1]));    
            String lang2= classifierAll.classify(gz).getName().substring(0,2);
            if(!lang2.equals(lang))
            {
                System.err.println("error parallel="+parallel+" "+lang+" != "+lang2);
            }
            profiler.close(track);
        
        
        }
        Profiler.print(System.out);
    }
        
}
