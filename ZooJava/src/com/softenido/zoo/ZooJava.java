/*
 *  ZooJava.java
 *
 *  Copyright (C) 2011  Francisco Gómez Carrasco
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
package com.softenido.zoo;

import com.softenido.zoo.classifier.ZooClassifier;
import java.io.IOException;

/**
 *
 * @author franci
 */
public class ZooJava
{
    static final String HELP = "Profilable\n\ncommands:\nclassifier\n";
    static final String sample = "Now, Therefore THE GENERAL ASSEMBLY proclaims THIS UNIVERSAL DECLARATION OF HUMAN RIGHTS as a common standard of achievement for all peoples and all nations, to the end that every individual and every organ of society, keeping this Declaration constantly in mind, shall strive by teaching and education to promote respect for these rights and freedoms and by progressive measures, national and international, to secure their universal and effective recognition and observance, both among the peoples of Member States themselves and among the peoples of territories under their jurisdiction.";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException
    {
        if(args.length==0)
        {
            System.out.printf(HELP);
        }
        else if(args[0].equalsIgnoreCase("classifier"))
        {
            ZooClassifier.main(args);
        }
        else
        {
            System.out.printf(HELP);
        }
        
    }   
    
}
