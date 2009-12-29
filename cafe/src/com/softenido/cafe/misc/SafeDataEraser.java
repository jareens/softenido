/*
 *  SafeDataEraser.java
 *
 *  Copyright (C) 2007  Francisco Gómez Carrasco
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
package com.softenido.cafe.misc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Obtains safe Erarser data for magnetic memory
 * @author franci
 */
public class SafeDataEraser
{
    private static Random rand = new Random();
    
    private final byte[][] data=
    {
        null,
        null,
        null,
        null,
        {(byte)0x55,(byte)0x55,(byte)0x55},
        {(byte)0xAA,(byte)0xAA,(byte)0xAA},
        {(byte)0x92,(byte)0x49,(byte)0x24},
        {(byte)0x49,(byte)0x24,(byte)0x92},
        {(byte)0x24,(byte)0x92,(byte)0x49},
        {(byte)0x00,(byte)0x00,(byte)0x00},
        {(byte)0x11,(byte)0x11,(byte)0x11},
        {(byte)0x22,(byte)0x22,(byte)0x22},
        {(byte)0x33,(byte)0x33,(byte)0x33},
        {(byte)0x44,(byte)0x44,(byte)0x44},
        {(byte)0x55,(byte)0x55,(byte)0x55},
        {(byte)0x66,(byte)0x66,(byte)0x66},
        {(byte)0x77,(byte)0x77,(byte)0x77},
        {(byte)0x88,(byte)0x88,(byte)0x88},
        {(byte)0x99,(byte)0x99,(byte)0x99},
        {(byte)0xAA,(byte)0xAA,(byte)0xAA},
        {(byte)0xBB,(byte)0xBB,(byte)0xBB},
        {(byte)0xCC,(byte)0xCC,(byte)0xCC},
        {(byte)0xDD,(byte)0xDD,(byte)0xDD},
        {(byte)0xEE,(byte)0xEE,(byte)0xEE},
        {(byte)0xFF,(byte)0xFF,(byte)0xFF},
        {(byte)0x92,(byte)0x49,(byte)0x24},
        {(byte)0x49,(byte)0x24,(byte)0x92},
        {(byte)0x24,(byte)0x92,(byte)0x49},
        {(byte)0x6D,(byte)0xB6,(byte)0xDB},
        {(byte)0xB6,(byte)0xDB,(byte)0x6D},
        {(byte)0xDB,(byte)0x6D,(byte)0xB6},
        null,
        null,
        null,
        null,
    };
    
    /**
     * Creates a new instance of SafeDataEraser
     */
    public SafeDataEraser()
    {
    }
    /**
     *
     * @param index
     * @param buf
     */
    public void getPattern(int index,byte[] buf)
    {
        byte[] pattern;
        
        pattern = data[index%35];
        
        if(pattern==null)
        {
            pattern = new byte[3];
            rand.nextBytes(pattern);
        }
        
        for(int i=0;i<buf.length;i++)
            buf[i] = pattern[i%3];
    }
    
    public void wipeFile(String strFile) throws FileNotFoundException, IOException
    {
        long size;                  // size of the file to wipe
        File f = null;              // file descriptor
        FileOutputStream fos = null;// Stream used to write to the file
        byte[] pattern = null;      // buffer for the pattern
        
        try
        {
            f = new File(strFile);
            size = f.length();
            if(size==0)
            {
                f = null;
                return;
            }
            pattern = new byte[3*1024];
            
            for(int i=0;i<data.length;i++)
            {
                fos = new FileOutputStream(f);
                rand.nextBytes(pattern);

                for( int j=0 ; j<size ; j+=pattern.length )
                    fos.write(pattern);
                
                fos.flush();
                if(fos!=null)
                    fos.close();
                fos = null;
            }
        }
        finally
        {
            if(pattern!=null)
                pattern = null;
            if(fos!=null)
            {
                fos.close();
                fos = null;
            }
        }
    }
}
