/*
 *  StegoPack.java
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

public interface StegoPack
{
//     not a public structure
//    typedef struct
//    {
//        unsigned id0:1;
//        unsigned parity:1;
//        unsigned len:6;
//        char data[63];
//    }stgpck0;
//    typedef struct
//    {
//        unsigned id0:2;
//        unsigned parity:4;
//        unsigned len:10;
//        char data[63];
//    }stgpck1;

    
    byte[] pack(byte[] src);
    byte[] unpack(byte[] src);
       
}