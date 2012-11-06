/*
 * GutenbergLanguageClassifier.java
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
package com.softenido.gutenberg;

import com.softenido.cafecore.statistics.classifier.AbstractLanguageClassifier;
import com.softenido.cafecore.statistics.classifier.ClassifierFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author franci
 */
public class GutenbergLanguageClassifier extends AbstractLanguageClassifier
{
    private static final String HI = ".hi";
    private static final String LO = ".lo";
    private final HashSet<String> categoryLock = new HashSet<String>();
    private volatile boolean twoPasses = false;
    private final Queue<String> langsSecondPass = new ConcurrentLinkedQueue<String>();
    
    public GutenbergLanguageClassifier(String unmatched)
    {
        super(unmatched);
    }

    public GutenbergLanguageClassifier(String unmatched, String[] languages)
    {
        super(unmatched, languages);
    }

    public GutenbergLanguageClassifier(String unmatched, int languages)
    {
        super(unmatched, languages);
    }
    public boolean add(final String... languages)
    {
        if(twoPasses)
        {
            String[] langs = new String[languages.length];
            for(int i=0;i<languages.length;i++)
            {
                langs[i] = languages[i]+HI;
                langsSecondPass.add(languages[i]+LO);
            }
            return super.add(langs);
        }
        else
        {
            return super.add(languages);
        }
    }

    protected boolean initialize(String item)
    {
        boolean hi = item.endsWith(HI) || !item.endsWith(LO);
        boolean lo = item.endsWith(LO) || !item.endsWith(HI);
        String lang = (hi!=lo)? item.substring(0, item.length()-3) : item;
        try
        {
            InputStream in;
            if(hi && !categoryLock.contains(lang+HI))
            {
                categoryLock.add(lang+HI);//early lock form new attemps of initialization
                in = Gutenberg.getLanguageDataStream(lang, false);
                if(in!=null)
                {
                    this.load(in, false);
                }
            }
            if(lo && !categoryLock.contains(lang+LO))
            {
                categoryLock.add(lang+LO);//early lock form new attemps of initialization
                in = Gutenberg.getLanguageDataStream(lang, true);
                if(in!=null)
                {
                    this.load(in, false);
                }
            }
            return true;
        }
        catch (IOException ex)
        {
            Logger.getLogger(GutenbergLanguageClassifier.class.getName()).log(Level.SEVERE,"error reading from dictionary", ex);
        }
        catch (ClassifierFormatException ex)
        {
            Logger.getLogger(GutenbergLanguageClassifier.class.getName()).log(Level.SEVERE,"dictionary has wrong format", ex);
        }
        return false;
    }

    @Override
    public boolean containsCategory(String category)
    {
        return categoryLock.contains(category);
    }

    public boolean isTwoPasses()
    {
        return twoPasses;
    }

    public void setTwoPasses(boolean twoPasses)
    {
        this.twoPasses = twoPasses;
    }

    public boolean firstPass()
    {
        return initialize();
    }
    public boolean secondPass()
    {
        //second pass is not mandatory to be complete atomically
        boolean ret = false;
        for(String item:langsSecondPass)
        {
            super.add(item);
            ret = initializeLoop() | ret;
        }
        return ret;
    }
}
