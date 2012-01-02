/*
 *  StegoCipher.java
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StegoCipher
{
    // not a public structure
    public static void encrypt(byte[] dst,final byte[] src,int num,final byte[] pass,int npass) throws NoSuchAlgorithmException 
    {
      byte[] iv = new byte[16];
      byte[] key= new byte[16];
      
      MessageDigest md5;
      md5 =  MessageDigest.getInstance("MD5");
      md5.update(pass,0,npass);
      md5.digest(key);
      
      md5.reset();
      md5.update(key,0,16);
      md5.digest(iv);
      //aki IDEA.encode_cfb8(dst,src,num,iv,key);
    }

    public static void decrypt(byte[] dst,final byte[] src,int num,final byte[] pass,int npass) throws NoSuchAlgorithmException 
    {
      byte[] iv = new byte[16];
      byte[] key= new byte[16];

      MessageDigest md5;
      md5 =  MessageDigest.getInstance("MD5");
      md5.update(pass,0,npass);
      md5.digest(key);
      
      md5.reset();
      md5.update(key,0,16);
      md5.digest(iv);
      //aki IDEA.decode_cfb8(dst,src,num,iv,key);
    }
}
