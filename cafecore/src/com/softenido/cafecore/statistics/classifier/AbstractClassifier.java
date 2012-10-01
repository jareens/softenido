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

import com.softenido.cafecore.util.Sorts;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
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
    String unmatched;

    public AbstractClassifier(String unmatched)
    {
        this.unmatched = unmatched;
    }
    
    final void coach(String category, String word, double probability, double weigh)
    {
        coach(category, word, probability * weigh);
    }
    final void coach(String category, String word, double probability)
    {
        int count = (int) Math.round(probability * (Integer.MAX_VALUE/1024) );
        coach(category, word, count);
    }
    public abstract void coach(String category, String word, int n);
    
    final public void coach(String category, String[] word, int[] n)
    {
        for(int i=0;i<word.length;i++)
        {
            this.coach(category, word[i], n[i]);
        }
    }
    
    abstract int wordCount(String category,String word);
    abstract int getTotal();
    abstract String[] getCategories();
    abstract String[] getWords();

    public final Score classify(String[] words)
    {
        Score max = null;
        Score[] scores = classify(new Score[0],words);
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

    abstract public Score[] classify(Score[] scores, String[] words);
    
    public final void load(InputStream in, String... allowedCategories) throws ClassifierFormatException
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
        boolean[] allowed = getAllowed(cats, allowedCategories);
        
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
    
    static boolean optimize = false;
    public final void save(OutputStream out, String... allowedCategories) throws UnsupportedEncodingException
    {
        PrintStream ps = new PrintStream(out);
        
        String[] cats = this.getCategories();
        String[] word = this.getWords();
        
        if(optimize && (allowedCategories==null || allowedCategories.length>1) )
        {
            boolean[] allowed = getAllowed(cats, allowedCategories);
            int[] nums = new int[cats.length];
            for(int i=0;i<cats.length;i++)
            {
                if(allowed[i])
                {
                    for(int j=0;j<word.length;j++)
                    {
                        if(wordCount(cats[i], word[j])>0)
                            nums[i]++;
                    }
                }
            }
            Sorts.sort(nums, cats, true);
        }
        else
        {
            Arrays.sort(cats);    
        }
        
        boolean[] allowed = getAllowed(cats, allowedCategories);
        Arrays.sort(word);

        ps.println("Classifier:"+this.getTotal());
        ps.print("word");
        for(int i=0;i<cats.length;i++)
        {
            if(allowed[i])
                ps.print("|"+cats[i]);
        }
        ps.println();            
        
        int count = 0;
        for(String  w : word)
        {
            StringBuilder line = new StringBuilder(w);
            String sep = "";
            boolean skip=true;
                    
            for(int i=0;i<cats.length;i++)
            {
                if(allowed[i])
                {
                    int n = this.wordCount(cats[i], w);
                    sep += "|";
                    if(n>0)
                    {
                        line.append(sep).append(n);
                        sep = "";
                        skip=false;
                    }
                }
            }
            if(!skip)
            {
                ps.println(line.toString());
                count++;
            }
        }
        
        ps.println("words="+count);
        ps.flush();
    }
    public final void loadGZ(InputStream in, String... allowedCategories) throws IOException, ClassifierFormatException, NoSuchAlgorithmException
    {
        this.load(new GZIPInputStream(in), allowedCategories);
    }
    public final void saveGZ(OutputStream out, String... allowedCategories) throws UnsupportedEncodingException, IOException, NoSuchAlgorithmException
    {
        this.save(new GZIPOutputStream(out), allowedCategories);
    }

    public final Classifier synchronizedClassifier()
    {
        return synchronizedClassifier(this);
    }
    static Classifier synchronizedClassifier(final Classifier classifier)
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
            public void load(InputStream in, String... allowedCategories) throws ClassifierFormatException
            {
                synchronized(lock)
                {
                    classifier.load(in, allowedCategories);
                }
            }
            public void loadGZ(InputStream in, String... allowedCategories) throws ClassifierFormatException, IOException, NoSuchAlgorithmException
            {
                synchronized(lock)
                {
                    classifier.loadGZ(in, allowedCategories);
                }
            }

            public void save(OutputStream out, String... allowedCategories) throws UnsupportedEncodingException
            {
                synchronized(lock)
                {
                    classifier.save(out, allowedCategories);
                }
            }

            public void saveGZ(OutputStream out, String... allowedCategories) throws UnsupportedEncodingException, IOException, NoSuchAlgorithmException
            {
                synchronized(lock)
                {
                    classifier.saveGZ(out, allowedCategories);
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

    private static boolean[] getAllowed(String[] cats, String[] allowedCategories)
    {
        final boolean[] allowed = new boolean[cats.length];
        if(allowedCategories==null || allowedCategories.length==0)
        {
            Arrays.fill(allowed, true);
        }
        else
        {
            HashSet<String> set = new HashSet<String>(Arrays.asList(allowedCategories));
            for(int i=0;i<allowed.length;i++)
            {
                allowed[i] = set.contains(cats[i]);
            }
        }
        return allowed;
    }
    
    //w count of evidences given class and word
    //c count of evidences given class
    //k constant to elude divide by 0
    //m multiply to elude negative probability
    //note: no optimizar manualmente (pierde eficiencia), dejarlo a jit hace mejor trabajo
    static double probability(int w, int c, int k, int m)//full
    {
        //los Ncw+1/Nc+nc son los mejores descatados los demás
        double n = w * k + 1;
        //double d = (w>0)? (c + k) : (m + k);
        double d = w>0? c + k : k + m;
        double p = n / d;
        //los que hacen log del valor o un multiplo log(p)*m ó log(p*m) son los mejores
        //los que suman log(p+m) son sólo regulares (sobre todo con los CJK
        return Math.log(p*m+1);
    }

    public String getUnmatched()
    {
        return unmatched;
    }

    public void setUnmatched(String unmatched)
    {
        this.unmatched = unmatched;
    }

    
}