/*
 *  IDEACipher.java
 *
 *  Copyright (C) 1998-2010 Francisco Gómez Carrasco
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
package com.softenido.cafecore.crypto;

import javax.crypto.*;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;

public class IDEACipher extends CipherSpi
{
    /* algoritmo de encriptacion */
    //void crypt(final short BUFIN[4],short BUFOUT[4],final short Z[52])
    public static void crypt(final byte BUFIN[],byte BUFOUT[],final short Z[])
    {
        crypt(null,(byte[])null,Z);
    }
    public static void crypt(final short BUFIN[],short BUFOUT[],final short Z[])
    {
        short r,x1,x2,x3,x4,kk,t1,t2,a;
        x1=BUFIN[0];
        x2=BUFIN[1];
        x3=BUFIN[2];
        x4=BUFIN[3];
        
        /* 8 rondas de cifrado */
        for(r=0;r<8;r++)
        {
            /* the group operation on 64-bits block */
            x1 = mul(x1,Z[r*6]);
            x4 = mul(x4,Z[r*6+3]);
            x2+= Z[r*6+1];
            x3+= Z[r*6+2];
            
            /* the function of the MA structure */
            kk = mul( Z[r*6+4] , (short)(x1^x3) );
            t1 = mul( Z[r*6+5] , (short)(kk + (x2^x4)) );
            t2 = (short)(kk+t1);
            
            /* the involutary permutation PI */
            x1^= t1;
            x4^= t2;
            a  = (short)(x2^t2);
            x2 = (short)(x3^t1);
            x3 =a;
        }
        
        /* transformaci�n de salida */
        BUFOUT[0] = mul(x1,Z[(8 * 6)]);
        BUFOUT[1] = (short)(x3+Z[8*6+1]);
        BUFOUT[2] = (short)(x2+Z[8*6+2]);
        BUFOUT[3] = mul(x4,Z[8*6+3]);
    }
    
    /* multiplicaci�n tipo idea */
/* se multiplican y se le resta el numero que queda
fuera del redondeo. teniendo en cuenta que el 0 es un 0xffff
 */
    private static short mul(short a,short b)
    {
        long p;
        long q;
        
        if(a==0)
        {
            p=0x10001-b;
        }
        else if(b==0)
        {
            p=0x10001-a;
        }
        else
        {
            q=(long)a*(long)b;
            p=(q & 0xffff) - (q>>16);
            if(p<=0)
                p+=0x10001;
        }
        return (short)(p&0xffff);
    }
    
    /* calcula el inverso de num para la multiplicaci�n idea*/
    private static short inv(short num)
    {
        long n1,n2,q,r,b1,b2,t;
        if(num==0)
        {
            b2=0;
        }
        else
        {
            n1=0x10001;
            n2=num;
            b2=1;
            b1=0;
            do
            {
                r = (n1 % n2);
                q = (n1-r)/n2;
                if( r==0 && b2<0 )
                {
                    b2+=0x10001;
                }
                else
                {
                    n1=n2;
                    n2=r;
                    t=b2;
                    b2=b1-q*b2;
                    b1=t;
                }
            }while(r!=0);
        }
        return (short)b2;
    }
    
    /* genera la subclaves Z de cifrado */
    // solo funciona en litle endian
    //public void idea_key(const uint16 key[8], uint16 Z[52])
    public static void key(final byte key[], short Z[])
    {
        int i;
        
        /* se copian las 8 primeras tal cual	*/
        for(i=0;i<8;i++)
            Z[i]=(short)((key[i*2]<<8)&key[i*2+1]);
        
        /* se copian desplazando 25 bits cada ronda */
        for(i=8;i<52;i++)
        {
            if(i%8==6)
            {
                /* para Z[14],Z[22],... */
                Z[i] = (short)((Z[i-7]<<9) ^ (Z[i-14]>>7));
                i++;
                /* para Z[15],Z[23],... */
                Z[i] = (short)((Z[i-15]<<9) ^ (Z[i-14]>>7));
            }
            else
            {
                Z[i] = (short)((Z[i-7]<<9) ^ (Z[i-6]>>7));
            }
        }
    }
    
    /* genera la subclaves DK de cifrado */
    //public void idea_dekey(short Z[52],short DK[52])
    public static void dekey(short Z[],short DK[])
    {
        int i;
        for(i=0;i<48;i+=6)
        {
            DK[i]	= inv(Z[48-i]);
            DK[i+1]	= (short)-Z[50-i];
            DK[i+2]     = (short)-Z[49-i];
            DK[i+3]	= inv(Z[51-i]);
            DK[i+4]	= Z[46-i];
            DK[i+5]     = Z[47-i];
        }
        DK[48]	= inv(Z[0]);
        DK[49]	= (short)-Z[1];
        DK[50]	= (short)-Z[2];
        DK[51]	= inv(Z[3]);
        
        DK[1]	= (short)-Z[49];
        DK[2]	= (short)-Z[50];
    }
    
    //7.17 // se encriptan una serie de n bytes en modo CFB-8
    //void idea_encrypt_cfb8(byte[] c,final byte[] x,int num,byte iv[8],final short Z[52])
    public static void encrypt_cfb8(byte[] c,final byte[] x,int num,byte iv[],final short Z[])
    {
        int i;
        byte[] O=new byte[16];
        
        for(i=0;i<num;i++)
        {
            crypt(iv,O,Z);
            c[i] = (byte)(x[i] ^ O[0]);
            System.arraycopy(iv, 1, iv, 0, 7);// memmove(iv,iv+1,7);
            iv[7]=c[i];
        }
    }
    //7.17 // se desencriptan una serie de n bytes en modo CFB-8
    //void idea_decrypt_cfb8(byte[] x,final byte[]c,int num,byte iv[8],final short Z[52])
    public static void decrypt_cfb8(byte[] x,final byte[]c,int num,byte iv[],final short Z[])
    {
        int i;
        byte[] O=new byte[16];
        
        for(i=0;i<num;i++)
        {
            crypt(iv,O,Z);
            System.arraycopy( iv, 1, iv, 0, 7 );
            iv[7]=c[i];
            x[i] = (byte)(c[i] ^ O[0]);
        }
    }
    //void idea_encode_cfb8(byte[] c,final byte[] x,int num,byte iv[8],final short key[8])
    public static void encode_cfb8(byte[] c,final byte[] x,int num,byte iv[],final byte[] key)
    {
        short Z[]=new short[52];
        key(key,Z);
        encrypt_cfb8(c,x,num,iv,Z);
    }
    //void idea_decode_cfb8(byte[] x,final byte[] c,int num,byte iv[8],final short key[8])
    public static void decode_cfb8(byte[] x,final byte[] c,int num,byte iv[],final byte key[])
    {
        short Z[]=new short[52];
        key(key,Z);
        decrypt_cfb8(x,c,num,iv,Z);
    }

    @Override
    protected void engineSetMode(String string) throws NoSuchAlgorithmException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void engineSetPadding(String string) throws NoSuchPaddingException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected int engineGetBlockSize()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected int engineGetOutputSize(int i)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected byte[] engineGetIV()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected AlgorithmParameters engineGetParameters()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void engineInit(int i, Key key, SecureRandom sr) throws InvalidKeyException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void engineInit(int i, Key key, AlgorithmParameterSpec aps, SecureRandom sr) throws InvalidKeyException, InvalidAlgorithmParameterException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void engineInit(int i, Key key, AlgorithmParameters ap, SecureRandom sr) throws InvalidKeyException, InvalidAlgorithmParameterException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected byte[] engineUpdate(byte[] bytes, int i, int i1)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected int engineUpdate(byte[] bytes, int i, int i1, byte[] bytes1, int i2) throws ShortBufferException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected byte[] engineDoFinal(byte[] bytes, int i, int i1) throws IllegalBlockSizeException, BadPaddingException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected int engineDoFinal(byte[] bytes, int i, int i1, byte[] bytes1, int i2) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}