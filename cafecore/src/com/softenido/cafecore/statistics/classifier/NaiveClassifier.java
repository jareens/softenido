/*
 * NaiveClassifier.java
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

import java.util.ArrayList;

/**
 *
 * @author franci
 */
public class NaiveClassifier extends AbstractClassifier
{
    public double probability(String word, String category)
    {
        double n = count(category, word) + 1;
        double d = count(null,     word) + this.categories.size();
        double p = n / d;
        return p;
    }
    
    public Score[] classify(Score[] scores,String ... words)
    {
        ArrayList<Score> sc = new ArrayList<Score>(categories.size());
        
        for(String c : categories.keySet())
        {
            double p=0;
            if(words.length>0)
            {
                for(String w : words)
                {
                    p+= probability(w,c) * Math.log(1+w.length());
                }
            }
            sc.add(new Score(c,p));
        }
        
        return sc.toArray(scores);
    }
}
