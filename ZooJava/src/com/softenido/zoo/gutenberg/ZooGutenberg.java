/*
 *  ZooJava.java
 *
 *  Copyright (C) 2012  Francisco Gómez Carrasco
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  Report bugs or new features to: flikxxi@gmail.com
 *
 */
package com.softenido.zoo.gutenberg;

import com.softenido.cafecore.profile.ProfileRecord;
import com.softenido.cafecore.profile.Profiler;
import com.softenido.cafecore.statistics.classifier.AbstractClassifier;
import com.softenido.gutenberg.Gutenberg;
import com.softenido.gutenberg.GutenbergLanguageClassifier;
import java.io.IOException;

/**
 *
 * @author franci
 */
public class ZooGutenberg
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException
    {
        Profiler.setActive(true);
        Profiler profilerSlowGZ = Profiler.getProfiler("gutenberg.slow.gz");
        Profiler profilerFastGZ = Profiler.getProfiler("gutenberg.fast.gz");
        Profiler profilerSlow = Profiler.getProfiler("gutenberg.slow");
        Profiler profilerFast = Profiler.getProfiler("gutenberg.fast");
        GutenbergLanguageClassifier classifier;
        
        for(int par=0;par<10;par++)
        {
            Gutenberg.gz = !Gutenberg.gz;
            classifier = new GutenbergLanguageClassifier("");
            classifier.add("eng","spa","deu");
            classifier.classify("hola mundo");
        }
        
        
        ProfileRecord track;
        for(int par=0;par<200;par++)
        {
            
            Gutenberg.gz = true;
            track = profilerFastGZ.open();
            classifier = new GutenbergLanguageClassifier("");
            classifier.add("eng","spa","deu");
            classifier.classify("hola mundo");
            profilerFastGZ.close(track);

            Gutenberg.gz = false;
            track = profilerFast.open();
            classifier = new GutenbergLanguageClassifier("");
            classifier.add("eng","spa","deu");
            classifier.classify("hola mundo");
            profilerFast.close(track);
            
            Profiler.print(System.out);
        }
        
    }
        
}
