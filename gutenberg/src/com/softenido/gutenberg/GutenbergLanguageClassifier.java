/*
 * GutenbergLanguageClassifier.java
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
package com.softenido.gutenberg;

import com.softenido.cafecore.statistics.classifier.AbstractLanguageClassifier;
import com.softenido.cafecore.statistics.classifier.ClassifierFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author franci
 */
public class GutenbergLanguageClassifier extends AbstractLanguageClassifier
{

    public GutenbergLanguageClassifier(String unmatched)
    {
        super(unmatched);
    }

    public GutenbergLanguageClassifier(String unmatched, String[] languages)
    {
        super(unmatched, languages);
    }

    public GutenbergLanguageClassifier(String unmatched, int languages)
    {
        super(unmatched, languages);
    }
    
    protected boolean initialize(String item)
    {
        try
        {
            //los gz son descomprimidos automáticamente por la plataforma
            InputStream in;
            in = Gutenberg.getLanguageDataStream(item);
            if(in!=null)
            {
                this.load(in);
            }
            return true;
        }
        catch (IOException ex)
        {
            Logger.getLogger(GutenbergLanguageClassifier.class.getName()).log(Level.SEVERE,"error reading from dictionary", ex);
        }
        catch (ClassifierFormatException ex)
        {
            Logger.getLogger(GutenbergLanguageClassifier.class.getName()).log(Level.SEVERE,"dictionary has wrong format", ex);
        }
        return false;
    }
}
