/*
 * Classifiers.java
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
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 *
 * @author franci
 */
public abstract class Classifiers
{
    TextClassifier synchronizedClassifier(TextClassifier classifier)
    {
        return null;
    }

    public static Classifier synchronizedClassifier(final Classifier classifier)
    {
        return new Classifier() 
        {
            private final Object lock = new Object();
            public Score[] classify(Score[] scores, String[] words)
            {
                synchronized(lock)
                {
                    return classifier.classify(scores, words);    
                }
            }
            public Score classify(String[] words)
            {
                synchronized(lock)
                {
                    return classifier.classify(words);
                }
            }
            public boolean containsCategory(String category)
            {
                synchronized(lock)
                {
                    return classifier.containsCategory(category);
                }
            }
            public void coach(String category, String word, int n)
            {
                synchronized(lock)
                {
                    classifier.coach(category, word, n);
                }
            }
            public void coach(String category, String[] word, int[] n)
            {
                synchronized(lock)
                {
                    classifier.coach(category, word, n);
                }
            }
            public void load(InputStream in, boolean strict, String... allowedCategories) throws ClassifierFormatException
            {
                synchronized(lock)
                {
                    classifier.load(in, strict, allowedCategories);
                }
            }
            public void loadGZ(InputStream in, boolean strict, String... allowedCategories) throws ClassifierFormatException, IOException, NoSuchAlgorithmException
            {
                synchronized(lock)
                {
                    classifier.loadGZ(in, strict, allowedCategories);
                }
            }

            public void save(OutputStream out, int min, int max, String... allowedCategories) throws UnsupportedEncodingException
            {
                synchronized(lock)
                {
                    classifier.save(out, min, max, allowedCategories);
                }
            }

            public void saveGZ(OutputStream out, int min, int max, String... allowedCategories) throws UnsupportedEncodingException, IOException, NoSuchAlgorithmException
            {
                synchronized(lock)
                {
                    classifier.saveGZ(out, min, max, allowedCategories);
                }
            }

            public void setUnmatched(String unmatched)
            {
                synchronized(lock)
                {
                    classifier.setUnmatched(unmatched);
                }
            }

            public String getUnmatched()
            {
                synchronized(lock)
                {
                    return classifier.getUnmatched();
                }
            }
            @Override
            public String toString()
            {
                return classifier.toString();
            }

       };
    }
    public static BaseTextClassifier synchronizedClassifier(BaseTextClassifier classifier)
    {
        return new BaseTextClassifier(classifier.classifier)
        {
            final Object lock = new Object();
            @Override
            public void coach(String category, String text)
            {
                synchronized(lock)
                {
                    super.coach(category, text);
                }
            }

            @Override
            public void coach(String category, InputStream text)
            {
                synchronized(lock)
                {
                    super.coach(category, text);
                }
            }

            @Override
            Score classify(Scanner sc)
            {
                synchronized(lock)
                {
                    return super.classify(sc);
                }
            }

            @Override
            public Score classify(String text)
            {
                synchronized(lock)
                {
                    return super.classify(text);
                }
            }

            @Override
            public Score classify(InputStream text)
            {
                synchronized(lock)
                {
                    return super.classify(text);
                }
            }

            @Override
            public boolean containsCategory(String category)
            {
                synchronized(lock)
                {
                    return super.containsCategory(category);
                }
            }

            @Override
            public void load(InputStream in, boolean strict, String... allowedCategories) throws ClassifierFormatException
            {
                synchronized(lock)
                {
                    super.load(in, strict, allowedCategories);
                }
            }
            
            @Override
            public void save(OutputStream out, int min, int max, String... allowedCategories) throws UnsupportedEncodingException
            {
                synchronized(lock)
                {
                    super.save(out, min, max, allowedCategories);
                }
            }

            @Override
            public void loadGZ(InputStream in, boolean strict, String... allowedCategories) throws ClassifierFormatException, IOException, NoSuchAlgorithmException
            {
                synchronized(lock)
                {
                    super.loadGZ(in, strict, allowedCategories);
                }
            }
            @Override
            public void saveGZ(OutputStream out, int min, int max, String... allowedCategories) throws UnsupportedEncodingException, IOException, NoSuchAlgorithmException
            {
                synchronized(lock)
                {
                    super.saveGZ(out, min, max, allowedCategories);
                }
            }

            @Override
            public String getUnmatched()
            {
                synchronized(lock)
                {
                    return super.getUnmatched();
                }
            }
            @Override
            public void setUnmatched(String unmatched)
            {
                synchronized(lock)
                {
                    super.setUnmatched(unmatched);
                }
            }
        };
    }
    
}
