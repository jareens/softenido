/*
 * AbstractClassifier.java
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

import com.softenido.cafecore.util.Arrays6;
import com.softenido.cafecore.util.Pair;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;       


class CmpAtomicInteger extends AtomicInteger
{
    @Override
    public int hashCode()
    {
        return this.get();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final CmpAtomicInteger other = (CmpAtomicInteger) obj;
        if (this != other && this.get()!=other.get())
            return false;
        return true;
    }
    
}

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
                c = new CmpAtomicInteger();
                this.categories.put(category, c);
            }
            if(w==null)
            {
                w = new CmpAtomicInteger();
                this.words.put(word, w);
            }
            if(o==null)
            {
                o = new CmpAtomicInteger();
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
    
    static final String MD5 = "MD5";
    public void save(OutputStream out) throws UnsupportedEncodingException, NoSuchAlgorithmException
    {
        PrintStream ps = new PrintStream(out);
        
        String[] cats = this.categories.keySet().toArray(new String[0]);
        String[] words = this.words.keySet().toArray(new String[0]);
        
        Arrays.sort(cats);
        Arrays.sort(words);

        ps.println("Classifier:"+this.total);
        ps.print("word");
        for(String  c : cats)
        {
            ps.print("|"+c);
        }
        ps.println();            
        
        int count = 0;
        for(String  w : words)
        {
            StringBuilder line = new StringBuilder(w);
            String sep = "";
                    
            for(String  c : cats)
            {
                AtomicInteger counter = this.cells.get(new CellKey(c, w));
                int n = (counter!=null)? counter.get() : 0;
                sep += "|";
                if(n>0)
                {
                    line.append(sep).append(n);
                    sep = "";
                }
            }
            ps.println(line.toString());
            count++;
        }
        
        ps.println("words="+count);
        ps.flush();
    }
    public void load(InputStream in) throws ClassifierFormatException, NoSuchAlgorithmException
    {
        Scanner sc = new Scanner(in);
        if( !sc.hasNextLine() || !sc.nextLine().startsWith("Classifier:") || !sc.hasNextLine() )
        {
            throw new ClassifierFormatException("wron't format");
        }
        String[] cats = sc.nextLine().split("\\|");
        if(!cats[0].equals("word"))
        {
            throw new ClassifierFormatException("wron't format");
        }
        cats = Arrays6.copyOfRange(cats, 1, cats.length);
        
        int count=0;
        String line=null;
        while(sc.hasNextLine())
        {
            line = sc.nextLine();
            if(!line.contains("|"))
            {
                break;
            }
            String[] counters = line.split("\\|");
            String word = counters[0];
            
            for(int i=1;i<counters.length;i++)
            {
                if(counters[i].length()>0)
                {
                    this.coach(cats[i-1],word,Integer.valueOf(counters[i]));
                }
            }
            count++;
        }
        if(line==null || !line.startsWith("words="+count))
        {
            throw new ClassifierFormatException("readed words="+count+" expected "+line);
        }
    }
    
    public void saveGZ(OutputStream out) throws UnsupportedEncodingException, IOException, NoSuchAlgorithmException
    {
        save(new GZIPOutputStream(out));
    }
    public void loadGZ(InputStream in) throws IOException, ClassifierFormatException, NoSuchAlgorithmException
    {
        load(new GZIPInputStream(in));
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 31 * hash + this.total;
        hash = 31 * hash + this.categories.size();
        hash = 31 * hash + this.words.size();
        hash = 31 * hash + this.cells.size();
        return hash;
    }


    @Override
    public boolean equals(Object obj)
    {
        if(obj == this)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final AbstractClassifier other = (AbstractClassifier) obj;
        if (this.total != other.total)
            return false;
        if (this.categories != other.categories && (this.categories == null || !this.categories.equals(other.categories)))
            return false;
        if (this.words != other.words && (this.words == null || !this.words.equals(other.words)))
            return false;
        if (this.cells != other.cells && (this.cells == null || !this.cells.equals(other.cells)))
            return false;
        return true;
    }
}
