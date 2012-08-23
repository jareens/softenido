/*
 *  SafeDataEraser.java
 *
 *  Copyright (C) 2007-2012 Francisco Gómez Carrasco
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
package com.softenido.cafecore.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Obtains safe Erarser data for magnetic memory
 *
 * based on <b>Secure Deletion of Data from Magnetic and Solid-State Memory<b>
 * by <b>Peter Gutmann<b>
 *
 * @author franci
 */
public class SafeDataEraser
{
    private static final Random rand = new SecureRandom();
    private static final byte[][] patterns=
    {
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
        {(byte)0xDB,(byte)0x6D,(byte)0xB6}
    };

    private final byte[][] shuffle;
    
    /**
     * Creates a new instance of SafeDataEraser
     */
    public SafeDataEraser()
    {
        byte[][] pat = new byte[patterns.length][];
        for(int i = 0;i<pat.length;i++)
        {
            pat[i] = patterns[i];
        }

        Collections.shuffle(Arrays.asList(pat), rand);

        shuffle = new byte[35][];
        for(int i = 0;i<4;i++)
        {
            byte[] item = new byte[3];
            rand.nextBytes(item);
            shuffle[i]= item;
        }
        for(int i = 0;i<pat.length;i++)
        {
            shuffle[i+4]= pat[i];
        }
        for(int i = 31;i<35;i++)
        {
            byte[] item = new byte[3];
            rand.nextBytes(item);
            shuffle[i]= item;
        }
    }

    public void getPattern(int index, byte[] buf)
    {
        byte[] pattern = shuffle[index % shuffle.length];
        for(int i=0;i<buf.length;i++)
        {
            buf[i] = pattern[ i % 3];
        }
    }
    
    public void wipeFile(File file) throws FileNotFoundException, IOException
    {
        final long size = file.length();
        if (size == 0)
        {
            return;
        }

        byte[] buf = new byte[(int) Math.min(30 * 1024, size)];
        for (int i = 0; i < patterns.length; i++)
        {
            RandomAccessFile raf = new RandomAccessFile(file,"rwd");
            try
            {
                getPattern(i, buf);
                for (long j = 0; j < size; j += buf.length)
                {
                    raf.write(buf, 0, (int) Math.min(buf.length, size - j));
                }
            }
            finally
            {
                raf.close();
            }
        }
        file.delete();
    }
}
