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
        {"CA","google-terms-of-service-CA.txt.gz"},
        {"ES","google-terms-of-service-ES.txt.gz"},
        {"EU","google-terms-of-service-EU.txt.gz"},
        {"FR","google-terms-of-service-FR.txt.gz"},
        {"GE","google-terms-of-service-GE.txt.gz"},
        {"GL","google-terms-of-service-GL.txt.gz"},
        {"IT","google-terms-of-service-IT.txt.gz"},
        {"EN","google-terms-of-service-UK.txt.gz"},
        {"EN","google-terms-of-service-US.txt.gz"},
        {"NL","google-terms-of-service-NL.txt.gz"},
        {"PT","google-terms-of-service-PT.txt.gz"},
        {"RU","google-terms-of-service-RU.txt.gz"}
    };
    static final String LINE = "^[A-Za-z0-9]+.+$";
    public static final String AMBIGUOUS = ".*(Copyright|Parkway|Google|escrito|EXEMPLARES|Estados *Unidos|vaststelt).*";
    
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
        TextClassifier classifier = new TextClassifier();
        
        for(int i=0;i<LEARN.length;i++)
        {
            String lang = LEARN[i][0];
            InputStream gz = new GZIPInputStream(TextClassifierTest.class.getResourceAsStream(LEARN[i][1]));    
            classifier.coach(lang, gz);
        }
        
        //uncomment this line to get a dictionariy
        classifier.save(new FileOutputStream("dictionary.txt"));
        classifier.saveGZ(new FileOutputStream("dictionary.txt.gz"));

        ByteArrayOutputStream out =  new ByteArrayOutputStream();
        classifier.saveGZ(out);
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        TextClassifier classifier2 = new TextClassifier();
        classifier2.loadGZ(in);
        
        assertEquals(classifier, classifier2);
        
        for(int i=0;i<LEARN.length;i++)
        {
            String lang = LEARN[i][0];
            InputStream gz = new GZIPInputStream(TextClassifierTest.class.getResourceAsStream(LEARN[i][1]));    
            Scanner sc = new Scanner(gz);
            
            while( sc.hasNextLine())
            {
                String line=sc.nextLine().trim().replaceAll(" +"," " );
                if(!line.matches(AMBIGUOUS) && line.length()>20)
                {
                    String lang2= classifier.classify(line).getName();
                    //System.out.println(lang+"-"+lang2+"("+line+")");
                    assertEquals(lang, lang2);
                }
            }
        }
    }
}
