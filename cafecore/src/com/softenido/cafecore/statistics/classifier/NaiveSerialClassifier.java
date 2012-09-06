/*
 * NaiveSerialClassifier.java
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author franci
 */
public class NaiveSerialClassifier extends AbstractClassifier
{
    int total = 0;
    final HashMap<String,SimpleInteger> categories = new HashMap<String,SimpleInteger>(64);
    final HashMap<String,SimpleInteger> words = new HashMap<String,SimpleInteger>(8*1024);
    final HashMap<Pair,SimpleInteger> cells = new HashMap<Pair,SimpleInteger>(16*1024);

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
        final NaiveSerialClassifier other = (NaiveSerialClassifier) obj;
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

    private boolean[] getAllowed(String[] cats, String[] allowedCategories)
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

    @Override
    String[] getCategories()
    {
        return this.categories.keySet().toArray(new String[0]);
    }
    @Override
    String[] getWords()
    {
        return this.words.keySet().toArray(new String[0]);
    }
    int getCategoryCount()
    {
        return this.categories.size();
    }
    @Override
    int getTotal()
    {
        return this.total;
    }

    //w count of evidences given class and word
    //c count of evidences given class
    //k constant to elude divide by 0
    //m multiply to elude negative probability
    //note: no optimizar manualmente (pierde eficiencia), dejarlo a jit hace mejor trabajo
    public static double probability(int w, int c, int k, int m)//full
    {
         //los Ncw+1/Nc+nc son los mejores descatados los demás
        double n = w*k + 1;
        double d = c + k;
        double p = n / d;
        //los que hacen log del valor o un multiplo log(p)*m ó log(p*m) son los mejores
        //los que suman log(p+m) son sólo regulares (sobre todo con los CJK
        //return Math.log(p*m);
        return Math.log(p*m);
    }
    // no ordenar, tarda más
    private static boolean cacheable = true;
    private static int     threshold = 1024;
    
    public Score[] classify(Score[] scores,String ... words)
    {
        final int k=this.categories.size();
        final int m=this.total;
        
        final boolean cached = cacheable && (words.length>=threshold);
        ArrayList<Score> sc = new ArrayList<Score>(categories.size());
        //Logger.getLogger(NaiveSerialClassifier.class.getName()).log(Level.INFO,"categories:{0} words:{1} ",new Object[]{categories.size(),words.length});

        for(String c : categories.keySet())
        {
            final HashMap<String,Double> cache = cached?new HashMap<String,Double>(words.length*2):null;
            double p=0;
            final int cc=categoryCount(c);
            
            if(words.length>0)
            {
                int n=0;
                for(String w : words)
                {
                    double pd;
                    if(cached)
                    {
                        Double pc = cache.get(w);
                        if(pc==null)
                        {
                            final int wc = wordCount(c,w);
                            pd = probability(wc,cc, k, m);
                            cache.put(w, pd);
                        }
                        else
                        {
                            pd = pc.doubleValue();
                        }
                    }
                    else
                    {
                        final int wc = wordCount(c,w);
                        pd = probability(wc,cc, k, m);
                    }
                    p+= pd;
                    n++;
                }
            }
            sc.add(new Score(c,p));
        }
        return sc.toArray(scores);
    }

    public static boolean isCacheable()
    {
        return cacheable;
    }

    public static void setCacheable(boolean cacheable)
    {
        NaiveSerialClassifier.cacheable = cacheable;
    }

    public static int getThreshold()
    {
        return threshold;
    }

    public static void setThreshold(int threshold)
    {
        NaiveSerialClassifier.threshold = threshold;
    }

    public boolean containsCategory(String category)
    {
        return this.categories.containsKey(category);
    }
    
}