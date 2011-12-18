/*
 *  NormalizeFile.java
 *
 *  Copyright (C) 2009-2011 Francisco Gómez Carrasco
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
package com.softenido.findrepe;

import com.softenido.cafedark.io.Files;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 *
 * @author franci
 */
public class NormalizeFile
{
    private final File fd;
    private String message;
    private int value = 0;
    
    public NormalizeFile(File fd)
    {
        this.fd = fd;
    }

    public String getErrorMessage()
    {
        return message;
    }

    public int getValue()
    {
        return value;
    }

    public String normalize() throws IOException, InterruptedException
    {
            value=0;

            String name = fd.getCanonicalPath();
            String escape = Files.escape(name);
            String normal = Files.normalize(escape);
            escape = Files.wildcard(escape);

            String[] cmd = new String[]{"mv",escape,normal};
            Process child = Runtime.getRuntime().exec(cmd);
            InputStream err = child.getErrorStream();
            value=child.waitFor();
            byte[] buf = new byte[16*1024];
            int r = err.read(buf);
            message = new String(Arrays.copyOfRange(buf, 0, r));
            return normal;
    }
}
