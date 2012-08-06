/*
 * AbstractClassifier.java
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

import com.softenido.cafecore.util.Pair;
import com.sun.org.apache.xml.internal.serializer.WriterToUTF8;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

class CellKey extends Pair<String,String>
{
    public CellKey(String category, String word)
    {
        super(category, word);
    }

    public String getCategory()
    {
        return getKey();
    }

    public String getWord()
    {
        return getVal();
    }

    @Override
    public String toString()
    {
        return "["+getKey()+","+getVal()+"]";
    }
}

/**
 *
 * @author franci
 */
public abstract class AbstractClassifier implements Classifier
{
    final Object lock = new Object();
    int total = 0;
    final HashMap<String,AtomicInteger> categories = new HashMap<String,AtomicInteger>();
    final HashMap<String,AtomicInteger> words = new HashMap<String,AtomicInteger>();
    final HashMap<CellKey,AtomicInteger> cells = new HashMap<CellKey,AtomicInteger>();
    
    public void coach(String category, String word, double probability, double weigh)
    {
        coach(category, word, probability * weigh);
    }
    public void coach(String category, String word, double probability)
    {
        int count = (int) Math.round(probability * (Integer.MAX_VALUE/1024) );
        coach(category, word, count);
    }
    public void coach(String category, String word, int n)
    {
        synchronized(lock)
        {
            final CellKey cell = new CellKey(category,word);
            
            AtomicInteger c = this.categories.get(category);
            AtomicInteger w = this.words.get(word);
            AtomicInteger o = this.cells.get(cell);
            
            if(c==null)
            {
                c = new AtomicInteger();
                this.categories.put(category, c);
            }
            if(w==null)
            {
                w = new AtomicInteger();
                this.words.put(word, w);
            }
            if(o==null)
            {
                o = new AtomicInteger();
                this.cells.put(cell,o);
            }
            total+=n;
            c.addAndGet(n);
            w.addAndGet(n);
            o.addAndGet(n);            
        }
    }
    int count(String category, String word)
    {
        synchronized(lock)
        {
            AtomicInteger counter;
            if(category==null && word==null)
            {
                return this.total;
            }
            else if(category!=null && word==null)
            {
                counter = this.categories.get(category);
            }
            else if(category==null && word!=null)
            {
                counter = this.words.get(word);
            }
            else
            {
                CellKey key = new CellKey(category,word);
                counter = this.cells.get(key);
            }
            return (counter!=null)?counter.get():0;
        }
    }
    public Score classify(String ... words)
    {
        Score[] scores = classify(new Score[0],words);
        Score max = null;
        if(scores.length>0)
        {
            max = scores[0];
            for(int i = 1;i<scores.length;i++)
            {
                if(scores[i].getValue()>max.getValue())
                {
                    max = scores[i];
                }
            }
        }
        return max;
    }

    abstract public Score[] classify(Score[] scores, String... words);
    
    public void export(OutputStream out) throws UnsupportedEncodingException
    {
        PrintStream ps = new PrintStream(out);
        String[] cats = this.categories.keySet().toArray(new String[0]);
        String[] words = this.words.keySet().toArray(new String[0]);
        
        ps.print("word");
        for(String  c : cats)
        {
            ps.print("|"+c);
        }
        ps.println();
            
        for(String  w : words)
        {
            ps.print(w);
            for(String  c : cats)
            {
                AtomicInteger counter = this.cells.get(new CellKey(c, w));
                int n = (counter!=null)? counter.get() : 0;
                ps.print("|"+(n==0?"":n));
            }
            ps.println();
        }
    }
    
}
