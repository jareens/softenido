/*
 * NaiveParallelClassifierTest.java
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
package com.softenido.cafecore.statistics.classifier;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author franci
 */
public class NaiveParallelClassifierTest
{
    String [][] LEARN= 
    {
        {"ca","google-terms-of-service-ca.txt.gz"},
        {"es","google-terms-of-service-es.txt.gz"},
        {"eu","google-terms-of-service-eu.txt.gz"},
        {"fr","google-terms-of-service-fr.txt.gz"},
        {"de","google-terms-of-service-de.txt.gz"},
        {"gl","google-terms-of-service-gl.txt.gz"},
        {"it","google-terms-of-service-it.txt.gz"},
        {"en","google-terms-of-service-en_UK.txt.gz"},
        {"en","google-terms-of-service-en_US.txt.gz"},
        {"nl","google-terms-of-service-nl.txt.gz"},
        {"pt","google-terms-of-service-pt_BR.txt.gz"},
        {"pt","google-terms-of-service-pt_PT.txt.gz"},
        {"RU","google-terms-of-service-ru.txt.gz"}
    };
    
    public NaiveParallelClassifierTest()
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
     * Test of probability method, of class NaiveSerialClassifier.
     */
    @Test
    public void testProbability()
    {
        String[] categories = {"es","ca","en"};
        String[] words = {"por","franci"};

        NaiveParallelClassifier instance = new NaiveParallelClassifier("",categories);
        
        instance.coach("es","por", 1);
        instance.coach("es","franci", 1);
        instance.coach("ca","per", 1);
        instance.coach("ca","franci", 1);
        instance.coach("en","by", 1);
        instance.coach("en","franci", 1);
        
        assertEquals("es", instance.classify(words).getName());
        
        instance = new NaiveParallelClassifier("",3);
        
        instance.coach("es","por", 1);
        instance.coach("es","franci", 1);
        instance.coach("ca","per", 50);
        instance.coach("ca","franci", 50);
        instance.coach("en","by", 20);
        instance.coach("en","franci", 20);
        
        assertEquals("es", instance.classify(words).getName());

    }
    
    /**
     * Test of classify method, of class Classifier.
     */
    @Test
    public void testClassify_StringArr() throws IOException
    {
        NaiveParallelClassifier classifier = new NaiveParallelClassifier("",LEARN.length);
        
        for(int i=0;i<LEARN.length;i++)
        {
            String lang = LEARN[i][0];
            InputStream gz = new GZIPInputStream(NaiveParallelClassifierTest.class.getResourceAsStream(LEARN[i][1]));    
            Scanner sc = new Scanner(gz);
            
            while( sc.hasNext())
            {
                String word=sc.next().toLowerCase();
                classifier.coach(lang, word, 1);
            }
        }
        assertEquals("es", classifier.classify(new String[]{"debes","seguir","las","políticas","disponibles","a","través","de","los","servicios"}).getName());
        assertEquals("fr", classifier.classify(new String[]{"vous","devez","respecter","les","règles","applicables","aux","services","que","vous","utilisez"}).getName());
        assertEquals("de", classifier.classify(new String[]{"sie","sind","zur","einhaltung","der","richtlinien","verpflichtet","die","für","unsere","dienstegelten"}).getName());
        assertEquals("it", classifier.classify(new String[]{"è","necessario","seguire","tutte","le","norme","messe","a","disposizione","dell’utente","all’interno","dei","servizi"}).getName());
        assertEquals("en", classifier.classify(new String[]{"you","must","follow","any","policies","made","available","to","you","within","the","services","don't"}).getName());
        assertEquals("en", classifier.classify(new String[]{"you","must","follow","any","policies","made","available","to","you","within","the","services","do","not"}).getName());
        assertEquals("en", classifier.classify(new String[]{"you","xyz","no"}).getName());      
    }    
}
