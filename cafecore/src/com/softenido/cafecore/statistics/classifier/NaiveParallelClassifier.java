/*
 * NaiveParallelClassifier.java
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
import com.softenido.cafecore.util.SimpleInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author franci
 */
public class NaiveParallelClassifier extends AbstractClassifier
{

    int total = 0;
    int count = 0;//number of category actually used
    final int size;
    final String[] categoryName;
    final int[] categoryCount;
    final HashMap<String, SimpleInteger> categoryIndex = new HashMap<String, SimpleInteger>(64);
    final HashMap<String, int[]> words = new HashMap<String, int[]>(8 * 1024);

    public NaiveParallelClassifier(String[] categories)
    {
        this.size = categories.length;
        this.categoryName = categories.clone();
        this.categoryCount = new int[categories.length];
        for (int i = 0; i < this.size; i++)
        {
            categoryIndex.put(categories[i], new SimpleInteger(i));
            count++;
        }
    }

    public NaiveParallelClassifier(int categories)
    {
        this.size = categories;
        this.categoryName = new String[categories];
        this.categoryCount = new int[categories];
    }

    public void coach(String category, String word, int n)
    {
        int index = getIndex(category);
        int[] w = wordCount(word, true);

        w[index] += n;
        this.categoryCount[index] += n;
        this.total += n;
    }

    int[] wordCount(String word, boolean create)
    {
        int[] w = this.words.get(word);
        if (w == null)
        {
            w = new int[this.size];
            if (create)
            {
                this.words.put(word, w);
            }
        }
        return w;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final NaiveParallelClassifier other = (NaiveParallelClassifier) obj;
        if (this.total != other.total)
            return false;
        if (!Arrays.equals(categoryName, other.categoryName))
            return false;
        if (!Arrays.equals(categoryCount, other.categoryCount))
            return false;
        if (this.words == other.words)
            return true;
        for(Entry<String, int[]> item : this.words.entrySet())
        {
            String key = item.getKey();
            int[] val = item.getValue();
            int[] oth = other.words.get(key);
            if(oth==null || !Arrays.equals(val, oth) )
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "AbstractClassifier{" + "total=" + total + ", categories=" + count + ", words=" + words.size() + '}';
    }

    //w count of evidences given class and word
    //c count of evidences given class
    //k constant to elude divide by 0
    //m multiply to elude negative probability
    //note: no optimizar manualmente (pierde eficiencia), dejarlo a jit hace mejor trabajo
    private static double probability(int w, int c, int k, int m)//full
    {
        //los Ncw+1/Nc+nc son los mejores descatados los demás
        double n = w * k + 1;
        double d = c + k;
        double p = n / d;
        //los que hacen log del valor o un multiplo log(p)*m ó log(p*m) son los mejores
        //los que suman log(p+m) son sólo regulares (sobre todo con los CJK
        //return Math.log(p*m);
        return Math.log(p * m);
    }

    //w count of evidences given class and word
    //c count of evidences given class
    //k constant to elude divide by 0
    //m multiply to elude negative probability
    //note: no optimizar manualmente (pierde eficiencia), dejarlo a jit hace mejor trabajo
    private double[] probability(int[] w, int[] c, int k, int m)//full
    {
        double[] p = new double[size];
        for (int i = 0; i < count; i++)
        {
            p[i] = probability(w[i], c[i], k, m);
        }
        return p;
    }
    // no ordenar, tarda más
    private static boolean cacheable = true;
    private static int threshold = 1024;

    public Score[] classify(Score[] scores, String... words)
    {
        final int k = this.count;
        final int m = this.total;

        final boolean cached = cacheable && (words.length >= threshold);
        ArrayList<Score> sc = new ArrayList<Score>(count);
        //Logger.getLogger(NaiveParallelClassifier.class.getName()).log(Level.INFO,"indexes:{0} words:{1} ",new Object[]{indexes.size(),words.length});

        final HashMap<String, double[]> cache = cached ? new HashMap<String, double[]>(words.length * 2) : null;
        double[] p = new double[size];

        for (String w : words)
        {
            double[] pw;
            if (cached)
            {
                pw = cache.get(w);
                if (pw == null)
                {
                    final int[] wc = wordCount(w, false);
                    pw = probability(wc, categoryCount, k, m);
                    cache.put(w, pw);
                }
            }
            else
            {
                final int[] wc = wordCount(w, false);
                pw = probability(wc, categoryCount, k, m);
            }
            for (int i = 0; i < size; i++)
            {
                p[i] += pw[i];
            }
        }
        if (scores.length < count)
        {
            scores = new Score[count];
        }

        for (int i = 0; i < count; i++)
        {
            scores[i] = new Score(categoryName[i], p[i]);
        }
        return scores;
    }

    private int getIndex(String category)
    {
        SimpleInteger index = this.categoryIndex.get(category);
        if (index == null)
        {
            this.categoryName[count] = category;
            index = new SimpleInteger(count);
            categoryIndex.put(category, index);
            count++;
        }
        return index.get();
    }

    public static boolean isCacheable()
    {
        return cacheable;
    }

    public static void setCacheable(boolean cacheable)
    {
        NaiveParallelClassifier.cacheable = cacheable;
    }

    public static int getThreshold()
    {
        return threshold;
    }

    public static void setThreshold(int threshold)
    {
        NaiveParallelClassifier.threshold = threshold;
    }

    @Override
    int wordCount(String category, String word)
    {
        int[] w = this.wordCount(word, false);
        int index = this.getIndex(category);
        return w[index];
    }

    @Override
    int getTotal()
    {
        return this.total;
    }

    @Override
    String[] getCategories()
    {
        return Arrays6.copyOfRange(this.categoryName, 0, count);
    }

    @Override
    String[] getWords()
    {
        return this.words.keySet().toArray(new String[0]);
    }

    public boolean containsCategory(String category)
    {
        return this.categoryIndex.containsKey(category);
    }
}