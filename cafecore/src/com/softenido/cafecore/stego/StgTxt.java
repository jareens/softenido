/*
 *  StgTxt.java
 *
 *  Copyright (C) 1998-2011 Francisco Gómez Carrasco
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
package com.softenido.cafecore.stego;

import com.softenido.cafecore.security.SecureBitSet;
import java.util.BitSet;

public class StgTxt
{
    // steg		linea de texto resultante
    // slen		longitud del texto resultante
    // txt	  texto contenedor
    // tlen		longitud del texto contenedor
    // msg		mensaje a ocultar
    // mlen		longitud del mensaje a ocultar
   public static int stegotxt(byte[] steg,int slen,   final byte[] txt, int tlen,    final byte[] msg, int mlen)
    {
        int i;    // txt index
        int j;    //
        int k;    //
        byte c,cc;
        int nbits;
        
        BitSet bs = null;//new SecureBitSet(msg);
        
        nbits = mlen*8;
        for( cc=0,i=j=k=0 ; i<tlen && j<slen; i++,cc=c )
        {
            c = txt[i];
            if(c != 32)
                steg[j++]=c;
            else if(cc!=32)
            {
                // si quedan bits y esta activo se a�ade un espacio
                if( k<nbits && bs.get(k++) )
                    steg[j++] = 32;
                steg[j++] = c;
            }
        }
        if( (k+7)/8 < mlen)
            return -1;
        return j;
    }
    // steg		linea de texto resultante
    // slen		longitud del texto resultante
    // txt	  texto contenedor
    // tlen		longitud del texto contenedor
    // msg		mensaje a ocultar
    // mlen		longitud del mensaje a ocultar
    public static int unstegotxt(final byte[] steg,int slen,byte[] msg,int mlen)
    {
        int i;    // txt index
        int j;    //
        int k;
        
        SecureBitSet bs = null;//aki new SecureBitSet(msg);
        
        for( i=j=0 ; i<slen && (j/8)<mlen; i++ )
        {
            for(k=0;i<slen && steg[i] == 32 ;k++)
                i++;
            if(k!=0)
                bs.set(j++,k>1);
            
        }
        //aki bs.get(msg);
        return j/8;
    }


}