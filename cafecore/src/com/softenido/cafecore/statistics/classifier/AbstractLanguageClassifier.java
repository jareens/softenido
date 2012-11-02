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

import com.softenido.cafecore.logging.NullStatusNotifier;
import com.softenido.cafecore.logging.StatusNotifier;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

/**
 *
 * @author franci
 */
public abstract class AbstractLanguageClassifier extends BaseTextClassifier implements LanguageClassifier
{
    private volatile boolean initialized = false;
    private final Object lock = new Object();    
    private final Queue<String> langs = new ConcurrentLinkedQueue<String>();
    private volatile StatusNotifier statusNotifier = new NullStatusNotifier();

    public AbstractLanguageClassifier(String unmatched)
    {
        super(unmatched);
        initialized=false;
    }
    public AbstractLanguageClassifier(String unmatched, String[] languages)
    {
        super(unmatched, languages.length);//use number to let the initialize do it work properly
        synchronized(lock)
        {
            langs.addAll(Arrays.asList(languages));
        }
        initialized=false;
    }
    public AbstractLanguageClassifier(String unmatched, int languages)
    {
        super(unmatched, languages);
    }
    public boolean add(final String... languages)
    {
        if(langs.addAll(Arrays.asList(languages)))
        {
            initialized=false;
            return true;
        }
        return false;
    }

    public boolean initialize()
    {
        synchronized (lock)
        {
            if(initialized)
            {
                return false;
            }
            return initializeLoop();
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
    public void save(OutputStream out, int min, int max, String... allowedCategories) throws UnsupportedEncodingException
    {
        initialize();
        super.save(out, min, max,  allowedCategories);
    }

    @Override
    public void saveGZ(OutputStream out, int min, int max, String... allowedCategories) throws UnsupportedEncodingException, IOException, NoSuchAlgorithmException
    {
        initialize();
        super.saveGZ(out, min, max, allowedCategories);
    }

    protected boolean initializeLoop()
    {
        boolean ret = false;
        while(langs.size()>0)
        {
            int i=0;
            for(String item=langs.poll();item!=null;item=langs.poll())
            {
                ret=true;
                if(!this.containsCategory(item))
                {
                    long started = System.nanoTime();
                    initialize(item);
                    long finalized = System.nanoTime();
                    long dif = (finalized-started)/1000000;
                    statusNotifier.log(Level.INFO, "[{0}]={1}ms",item,dif);
                }
            }
        }
        initialized=true;
        return ret;
    }

    public void setStatusNotifier(StatusNotifier statusNotifier)
    {
        this.statusNotifier = statusNotifier!=null?statusNotifier:new NullStatusNotifier();
    }
}
