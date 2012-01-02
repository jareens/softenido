/*
 *  EuroCheckSum.java
 *
 *  Copyright (C) 2007-2011  Francisco Gómez Carrasco
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
package com.softenido.cafecore.misc;

/**
 *
 * @author franci
 */
public class EuroCheckSum
{
    private static final String letter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String number = "23456789123456789123456789";
    
    private static final String countries[][] =
    {
        {"Z","Bélgica"},
        {"Y","Grecia"},
        {"X","Alemánia"},
        {"W","Dinamarca"},//
        {"V","España"},
        {"U","Francia"},
        {"T","Irlanda"},
        {"S","Italia"},
        {"R","Luxemburgo"},
        {"Q","No se usa"},
        {"P","Holanda"},
        {"O","No se usa"},
        {"N","Austria"},
        {"M","Portugal"},
        {"L","Finlandia"},
        {"K","Suiza"},
        {"J","Reino Unido"}
    };
    private EuroCheckSum()
    {
    }
    public static boolean verify(String serial)
    {
        int i;
        int sum;
        
        if (serial.length() != 12)
        {
            return false;
        }
        
        for(sum=i=0;i<serial.length();i++)
        {
            char c = serial.charAt(i);
            
            if( c < '0' || c >'9' )
            {
                c = number.charAt(letter.indexOf(Character.toUpperCase(c)));
            }
            
            sum += ( c - '0' );
            
        }
        return ((sum % 9) == 0);
    }
}