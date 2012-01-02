/*
 *  StegoPack0.java
 *
 *  Copyright (C) 2011 Francisco Gómez Carrasco
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

public class StegoPack0 implements StegoPack
{
//     not a public structure
//    typedef struct
//    {
//        unsigned id0:1;
//        unsigned parity:1;
//        unsigned len:6;
//        char data[63];
//    }stgpck0;
    public byte[] pack(byte[] src)
    {
        int num,numblocks;
        int numbytes = (src.length/63)*64 + ( (src.length%63)!=0 ? (src.length%63+1) : 1);
        
        numblocks = src.length!=0 ? (src.length+62)/63:1;
        int nsrc = src.length;
        byte[] dst = new byte[numbytes];

        for(int i=0; i<numblocks ; i++)
        {
            num = Math.min(63,nsrc);
            
            dst[i*64] = (byte) (num<<2);
            System.arraycopy(src, (i*64), dst, (i*64)+1, num);
            int parity = Cardinality.getByteCardinality(dst,i*64+i,num+1) % 2;
            dst[i*64] = (byte) ((num << 2) + (parity << 1));
        }
        return dst;
    }
    public byte[] unpack(byte[] src)
    {
        int i,num,numblocks;
        int ndst=0;//numero de bytes de salida
        // se calcula el numero maximo de bloques
        numblocks = (src.length+63)/64;
        byte dst[] = new byte[numblocks*63];
        
        for( ndst=i=0,num=63 ; i<numblocks && num==63 ; i++, ndst+=num )
        {
            //se verifica el tipo de empaquetado
            if( (src[i*64]&1) != 0 )
                return null;

            num = (src[i*64]>>>2);
            // se verifica que la longitud este dentro del tama\uFFFDo del paquete
            if((i*64+num+1)>src.length)
                return null;
            if( (Cardinality.getByteCardinality(src,i*64,num+1)%2) !=0)
                return null;
            //memcpy(dst+numbytes,src[i].data,num);
            System.arraycopy(src, (i*64)+1, dst, ndst, num);
        }
        byte[] dst2 = new byte[ndst];
        System.arraycopy(dst, 0, dst2, 0, ndst);
        return dst2;
    }

       
}