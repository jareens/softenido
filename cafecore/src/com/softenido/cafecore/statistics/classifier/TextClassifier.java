/*
 * TextClassifier.java
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

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author franci
 */
public class TextClassifier
{
    final Classifier classifier = new NaiveClassifier();

    public void coach(String category, String text)
    {
        coach(category, new Scanner(text));
    }
    public void coach(String category, InputStream text)
    {
        coach(category, new Scanner(text));
    }
    void coach(String category, Scanner sc)
    {
        while(sc.hasNext())
        {
            String word = filter(sc.next());
            if(word!=null)
            {
                classifier.coach(category, word, 1);
            }
        }       
    }

    public Score classify(String ... words)
    {
        return classifier.classify(words);
    }
    Score classify(Scanner sc)
    {
        ArrayList<String> words = new ArrayList<String>();
        while(sc.hasNext())
        {
            String w = filter(sc.next());
            if(w!=null)
            {
                words.add(w);
            }
        }
        return classifier.classify(words.toArray(new String[0]));
    }
    public Score classify(String text)
    {
        return classify(new Scanner(text));
    }
    public Score classify(InputStream text)
    {
        return classify(new Scanner(text));
    }
    static final String FILTER = "[\\p{Punct}\\p{Digit}«»]";
    static String filter(String word)
    {
        // at least a letter
        if(word.length()==0 || word.matches(FILTER+"*"))
        {
            return null;
        }
        
        word = word.toLowerCase();
        while(word.matches(FILTER+".*"))
        {
            word = word.substring(1,word.length());
        }
        while(word.matches(".*"+FILTER))
        {
            word = word.substring(0,word.length()-1);
        }
        return word;
    }

    public void export(OutputStream out) throws UnsupportedEncodingException
    {
        classifier.export(out);
    }
    
}
