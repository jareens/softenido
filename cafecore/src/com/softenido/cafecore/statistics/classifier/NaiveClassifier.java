/*
 * NaiveClassifier.java
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

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author franci
 */
public class NaiveClassifier extends AbstractClassifier
{
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
        //Logger.getLogger(NaiveClassifier.class.getName()).log(Level.INFO,"categories:{0} words:{1} ",new Object[]{categories.size(),words.length});

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
        NaiveClassifier.cacheable = cacheable;
    }

    public static int getThreshold()
    {
        return threshold;
    }

    public static void setThreshold(int threshold)
    {
        NaiveClassifier.threshold = threshold;
    }
}
