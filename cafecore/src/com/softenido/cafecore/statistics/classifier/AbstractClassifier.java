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

import com.softenido.cafecore.util.Pair;       
import com.softenido.cafecore.util.SimpleInteger;
import com.softenido.cafecore.util.Sorts;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;       

/**
 *
 * @author franci
 */
public abstract class AbstractClassifier implements Classifier
{
    int total = 0;
    final HashMap<String,SimpleInteger> categories = new HashMap<String,SimpleInteger>(64);
    final HashMap<String,SimpleInteger> words = new HashMap<String,SimpleInteger>(8*1024);
    final HashMap<Pair,SimpleInteger> cells = new HashMap<Pair,SimpleInteger>(16*1024);
    
    void coach(String category, String word, double probability, double weigh)
    {
        coach(category, word, probability * weigh);
    }
    void coach(String category, String word, double probability)
    {
        int count = (int) Math.round(probability * (Integer.MAX_VALUE/1024) );
        coach(category, word, count);
    }
    public void coach(String category, String word, int n)
    {
        final Pair cell = new Pair(category,word);

        SimpleInteger c = this.categories.get(category);
        SimpleInteger w = this.words.get(word);
        SimpleInteger o = this.cells.get(cell);

        if(c==null)
        {
            c = new SimpleInteger();
            this.categories.put(category, c);
        }
        if(w==null)
        {
            w = new SimpleInteger();
            this.words.put(word, w);
        }
        if(o==null)
        {
            o = new SimpleInteger();
            this.cells.put(cell,o);
        }
        total+=n;
        c.add(n);
        w.add(n);
        o.add(n);            
    }
    public void coach(String category, String[] word, int[] n)
    {
        for(int i=0;i<word.length;i++)
        {
            this.coach(category, word[i], n[i]);
        }
    }
    
    // no optimizar los if, jit mejora en esta disposición
    int categoryCount(String category)
    {
        SimpleInteger counter = this.categories.get(category);
        return (counter!=null)?counter.get():0;
    }
    int wordCount(String category,String word)
    {
        SimpleInteger counter = this.cells.get(new Pair(category,word));
        return (counter!=null)?counter.get():0;
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
    static boolean optimize = false;
    public void save(OutputStream out) throws UnsupportedEncodingException, NoSuchAlgorithmException
    {
        PrintStream ps = new PrintStream(out);
        
        String[] cats = this.categories.keySet().toArray(new String[0]);
        String[] word = this.words.keySet().toArray(new String[0]);
        
        if(optimize)
        {
            int[] nums = new int[cats.length];
            for(int i=0;i<cats.length;i++)
            {
                for(int j=0;j<word.length;j++)
                {
                    if(wordCount(cats[i], word[j])>0)
                        nums[i]++;
                }
            }
            Sorts.sort(nums, cats, true);
        }
        else
        {
            Arrays.sort(cats);    
        }
        
        Arrays.sort(word);

        ps.println("Classifier:"+this.total);
        ps.print("word");
        for(String  c : cats)
        {
            ps.print("|"+c);
        }
        ps.println();            
        
        int count = 0;
        for(String  w : word)
        {
            StringBuilder line = new StringBuilder(w);
            String sep = "";
                    
            for(String  c : cats)
            {
                SimpleInteger counter = this.cells.get(new Pair(c, w));
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
        load(in,null);
    }    
    
    public void load(InputStream in, String[] allowedCategories) throws ClassifierFormatException, NoSuchAlgorithmException
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
        
        final boolean[] allowed = new boolean[cats.length];
        if(allowedCategories==null)
        {
            Arrays.fill(allowed, true);
        }
        else
        {
            HashSet<String> set = new HashSet<String>(Arrays.asList(allowedCategories));
            for(int i=1;i<allowed.length;i++)
            {
                allowed[i] = set.contains(cats[i]);
            }
        }
        
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
                if(allowed[i] && counters[i].length()>0)
                {
                    this.coach(cats[i],word,Integer.valueOf(counters[i]));
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
        this.save(new GZIPOutputStream(out));
    }
    public void loadGZ(InputStream in) throws IOException, ClassifierFormatException, NoSuchAlgorithmException
    {
        this.load(new GZIPInputStream(in));
    }
    public void loadGZ(InputStream in, String[] allowedCategories) throws IOException, ClassifierFormatException, NoSuchAlgorithmException
    {
        this.load(new GZIPInputStream(in), allowedCategories);
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

    @Override
    public String toString()
    {
        return "AbstractClassifier{" + "total=" + total + ", categories=" + categories.size() + ", words=" + words.size() + ", cells=" + cells.size() + '}';
    }

    public Classifier synchronizedClassifier()
    {
        return synchronizedClassifier(this);
    }
    static Classifier synchronizedClassifier(final Classifier classifier)
    {
        return new Classifier() 
        {
            private final Object lock = new Object();
            public Score[] classify(Score[] scores, String... words)
            {
                synchronized(lock)
                {
                    return classifier.classify(scores, words);    
                }
            }
            public Score classify(String... words)
            {
                synchronized(lock)
                {
                    return classifier.classify(words);
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
            public void save(OutputStream out) throws UnsupportedEncodingException, NoSuchAlgorithmException
            {
                synchronized(lock)
                {
                    classifier.save(out);
                }
            }
            public void load(InputStream in) throws ClassifierFormatException, NoSuchAlgorithmException
            {
                synchronized(lock)
                {
                    classifier.load(in);
                }
            }
            public void load(InputStream in, String[] allowedCategories) throws ClassifierFormatException, NoSuchAlgorithmException
            {
                synchronized(lock)
                {
                    classifier.load(in, allowedCategories);
                }
            }
            public void saveGZ(OutputStream out) throws UnsupportedEncodingException, IOException, NoSuchAlgorithmException
            {
                synchronized(lock)
                {
                    classifier.saveGZ(out);
                }
            }
            public void loadGZ(InputStream in) throws ClassifierFormatException, IOException, NoSuchAlgorithmException
            {
                synchronized(lock)
                {
                    classifier.loadGZ(in);
                }
            }
            public void loadGZ(InputStream in, String[] allowedCategories) throws ClassifierFormatException, IOException, NoSuchAlgorithmException
            {
                synchronized(lock)
                {
                    classifier.loadGZ(in, allowedCategories);
                }
            }

            @Override
            public String toString()
            {
                return classifier.toString();
            }
       };
    }
}