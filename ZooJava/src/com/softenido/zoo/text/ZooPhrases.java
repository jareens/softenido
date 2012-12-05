/*
 *  ZooPhrases.java
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
package com.softenido.zoo.text;

import com.softenido.cafecore.profile.ProfileRecord;
import com.softenido.cafecore.profile.Profiler;
import com.softenido.cafecore.text.Phrases;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author franci
 */
public class ZooPhrases
{
    public static void main(String[] args) throws IOException
    {
        Profiler.setActive(true);
        Profiler profiler;
        ProfileRecord track;
        
        profiler = Profiler.getProfiler(ZooPhrases.class.getName());
        for(int i=0;i<2;i++)
        {
            track = profiler.open();
            InputStream gz = new GZIPInputStream(ZooPhrases.class.getResourceAsStream("101.txt.gz"));

            StringBuilder sb = new StringBuilder();
            byte[] buffer = new byte[65000];
            int r;
            while( (r=gz.read(buffer))>0)
                    sb.append(new String(buffer,0,r));
            String text = sb.toString();

            String[] phrases = Phrases.split(text, 5);
            System.out.println("phrases="+phrases.length);
            profiler.close(track);
        }
        Profiler.print(System.out);
    }
        
    
}
