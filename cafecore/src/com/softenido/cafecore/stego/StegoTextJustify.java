/*
 *  StegoTextJustify.java
 *
 *  Copyright (C) 1998-2011  Francisco Gómez Carrasco
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

import com.softenido.cafecore.math.FastMath;
import com.softenido.cafecore.security.SecureBitSet;
import com.softenido.cafecore.util.BitSetReader;
import com.softenido.cafecore.util.BitSetWriter;
import com.softenido.cafecore.util.BitSets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;

public class StegoTextJustify
{
    public static final int MODE_NONE  = 0;
    public static final int MODE_WRAP  = 1;
    public static final int MODE_UNWRAP = 2;
    private static final String PARAGRAPHS = "(\r?\n\r?){2,}";
    private static final String LINES = "(\r?\n\r?)";
    private static final String WORDS = "[ \n\r\t\f]+";
    private static final String HOLES = "[^ ]+";

    private static final SecureRandom random = new SecureRandom();
    private static final StegoPack pk = new StegoPack0();
    

    private final int columns;
    private final boolean splitLines;
    private final boolean mergeLines;
    
    static void encodeNH(int[] padding ,int n, int h, int index, BitSetReader br)
    {
        if(n==0)
        {
            return;
        }
        if(n>h/2)
        {
            encodeNH(padding,h-n,h,index, br);
        }
        else if((n > 1 && n < h) || (n == 1 && h == 2))
        {
            int bit = br.get()?1:0;
            padding[index+bit]++;
            encodeNH(padding,n-1,h-2,index+2, br);
        }
        else
        {
            int bits= FastMath.log2(h, false);
            int pos = br.get(bits);
            padding[index+pos]++;
        }
    }
    static void decodeNH(int[] padding ,int n, int h, int index, BitSetWriter bw)
    {
        if(n==0)
        {
            return;
        }
        if(n>h/2)
        {
            decodeNH(padding,h-n,h,index, bw);
        }
        else if((n > 1 && n < h) || (n == 1 && h == 2))
        {
            boolean bit = (padding[index]<padding[index+1]);
            bw.put(bit);
            decodeNH(padding,n-1,h-2,index+2, bw);
        }
        else
        {
            byte pos = 0;
            for(byte i=0;i<h-1;i++)
            {
                if(padding[index+i]<padding[index+i+1])
                    pos=i;
            }
            bw.put(pos,FastMath.log2(h, false));
        }
    }


    public StegoTextJustify(int columns, int mode)
    {
        this.columns = columns;
        this.splitLines = (mode == StegoTextJustify.MODE_WRAP) ||(mode == StegoTextJustify.MODE_UNWRAP);
        this.mergeLines = (mode == StegoTextJustify.MODE_UNWRAP);
    }
    
    String[][][] splitParagraphs(String src)
    {
        String[] paragraphs;
        String[][][] words;

        paragraphs = src.split(PARAGRAPHS);
        words      = new String[paragraphs.length][][];
        for(int i=0;i<paragraphs.length;i++)
        {
            words[i] = splitLines(paragraphs[i]);
        }
        return words;
    }
    String[][] splitLines(String src)
    {
        ArrayList<String[]> list = new ArrayList<String[]>();
        String[] lines = (splitLines&&mergeLines)? (new String[]{src}) : src.split(LINES);
        for(int i =0;i<lines.length;i++)
        {
            String[][] words = splitWords(lines[i]);
            Collections.addAll(list, words);
        }
        return list.toArray(new String[list.size()][]);
    }
    String[][] splitWords(String src)
    {
        String[] words = src.split(WORDS);

        if(!splitLines)
        {
            return new String[][]{words};
        }

        ArrayList<String[]> list = new ArrayList<String[]>();
        ArrayList<String> line = new ArrayList<String>();

        int cols=-1;

        for(int i=0;i<words.length;)
        {
            String w = words[i++];
            int ws = w.length();
            if( cols+ws >= this.columns && cols>0 )
            {
                list.add(line.toArray(new String[line.size()]));
                line.clear();
                cols=-1;
            }

            line.add(w);
            cols += ws+1;
        }
        if(!line.isEmpty())
        {
            list.add(line.toArray(new String[line.size()]));
        }
        return list.toArray(new String[list.size()][]);
    }

    public String justify(String txt) throws NoSuchAlgorithmException
    {
        byte[] msg;
        int    msgsize;
        byte[] pass = new byte[16];
               
        msgsize = 1024;
        msg = new byte[msgsize+1024];
        random.nextBytes(msg);
        msgsize = 1024;
        
        random.nextBytes(pass);
        
        return pack(txt,msg,pass);
    }
    public String pack(final String txt,final byte[] msg,final byte[] pass) throws NoSuchAlgorithmException
    {
        byte[] msgpk = pk.pack(msg);
        if(pass != null && pass.length != 0)
        {
            StegoCipher.encrypt(msgpk,msgpk,msg.length,pass,pass.length);
        }
        
        String[][][] words= splitParagraphs(txt);
        
        StringBuilder dst = new StringBuilder();
        BitSetReader bqMsg = BitSetReader.build(BitSets.copy(new SecureBitSet(),msg));

        for(int i=0;i<words.length;i++)
        {
            if (i > 0)
            {
                dst.append('\n');
            }
            for(int j=0;j<words[i].length;j++)
            {
                String line = mergeLine(words[i][j], bqMsg,(j+1==words[i].length));
                dst.append(line);
                dst.append('\n');
            }
        }
        return dst.toString();
    }
    static int lineSize(String[] words)
    {
        int size = 0;
        for (int i = 0; i < words.length; i++)
        {
            size += words[i].length();
        }
        return size;
    }
    
    String mergeLine(String[] words,BitSetReader br,boolean tail)
    {
        if (words.length == 0)
        {
            return "";
        }
        if (words.length == 1)
        {
            return words[0];
        }
        StringBuilder line = new StringBuilder();
        
        int[] padding = NHPadding(words,br,tail);
        line.append(words[0]);
        for(int i=1;i<words.length;i++)
        {
            for(int j=0;j<padding[i-1];j++)
            {
                line.append(' ');
            }
            line.append(words[i]);
        }
        return line.toString();
    }
    int[] NHPadding(String[] words,BitSetReader bits, boolean tail)
    {
        int size = lineSize(words);
        int h = words.length-1; //numero de huecos que dejan las palabras
        int cols = tail?Math.min(columns,size+h+h/2):columns;
        int n=(cols-size)%h; //numero de espacios necesarios para completar las columnas
        int p=(cols-size)/h;
        if(n>h/2)
        {
            n=h-n;
        }
        int[] padding = new int[h];
        Arrays.fill(padding,p);

        encodeNH(padding,n,h,0,bits);
        return padding;
    }

    public byte[] unpack(String stg,final byte[] pass) throws NoSuchAlgorithmException
    {
        int h;
        int msgpklen;
        byte msg[] = new byte[1024];
        BitSet bs = new BitSet();
        BitSetWriter bw= BitSetWriter.build(bs);

        String[] lines = stg.split(LINES);

        for(int i=0;i<lines.length;i++)
        {
            decodeLine(lines[i],bw);
        }
        msgpklen = bs.size()/8;
        if (pass != null && pass.length != 0)
        {
            StegoCipher.decrypt(msg, msg, msgpklen, pass, pass.length);
        }

        msg = pk.unpack(msg);
        return msg;
    }
    private static void decodeLine(String line, BitSetWriter bits)
    {
        String[] items= line.trim().split(HOLES);
        int[] holes = new int[items.length];
        int h = 0;

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        
        for(int i=0;i<items.length;i++)
        {
            if(items[i].length()>0)
            {
                int val = items[i].length();
                holes[h++]=val;
                min = Math.min(min,val);
                max = Math.max(max,val);
            }
        }
        if (min == max || max-min>1)
        {
            return;
        }
        boolean[] nh = new boolean[h];
        int n = 0;
        for(int i=0;i<h;i++)
        {
            nh[i] = (holes[i]==max);
            if (nh[i])
            {
                n++;
            }
        }
        decodeNH(holes, n, h,0, bits);
    }
}