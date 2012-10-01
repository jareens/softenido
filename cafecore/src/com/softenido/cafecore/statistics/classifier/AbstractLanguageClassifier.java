/*
 * AbstractLanguageClassifier.java
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
import java.util.Arrays;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author franci
 */
public abstract class AbstractLanguageClassifier extends TextClassifier
{
    private final Object lock = new Object();    
    final Queue<String> langs = new LinkedBlockingQueue<String>();

    public AbstractLanguageClassifier(String unmatched)
    {
        super(unmatched);
    }

    public AbstractLanguageClassifier(String unmatched, String[] languages)
    {
        super(unmatched, languages.length);//use number to let the initialize do it work properly
        synchronized(lock)
        {
            langs.addAll(Arrays.asList(languages));
        }
    }

    public AbstractLanguageClassifier(String unmatched, int languages)
    {
        super(unmatched, languages);
    }
    public void add(final String... languages)
    {
        synchronized(lock)
        {
            langs.addAll(Arrays.asList(languages));
        }
    }
    public final void prepare()
    {
        new Thread(new Runnable() 
        {
            public void run()
            {
                initialize();
            }
        }).start();
    }
    public void initialize()
    {
        synchronized (lock)
        {
            for(String item=langs.poll();item!=null;item=langs.poll())
            {
                if(!this.containsCategory(item))
                {
                    initialize(item);
                }
            }
        }
    }
    protected abstract boolean initialize(String item);    
    
    @Override
    Score classify(Scanner sc)
    {
        initialize();
        return super.classify(sc);
    }

    @Override
    public Score classify(String text)
    {
        initialize();
        return super.classify(text);
    }

    @Override
    public Score classify(InputStream text)
    {
        initialize();
        return super.classify(text);
    }

    @Override
    public void save(OutputStream out, String... allowedCategories) throws UnsupportedEncodingException
    {
        initialize();
        super.save(out, allowedCategories);
    }

    @Override
    public void saveGZ(OutputStream out, String... allowedCategories) throws UnsupportedEncodingException, IOException, NoSuchAlgorithmException
    {
        initialize();
        super.saveGZ(out, allowedCategories);
    }
}
