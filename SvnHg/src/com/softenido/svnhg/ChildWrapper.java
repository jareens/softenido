/*
 *  ChildWrapper.java
 *
 *  Copyright (C) 2010-2011 Francisco Gómez Carrasco
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
package com.softenido.svnhg;

import com.softenido.cafedark.io.Files;
import com.softenido.cafedark.io.VerboseInputStream;
import com.softenido.cafedark.io.VerboseOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 *
 * @author franci
 */
public class ChildWrapper
{
    private final PrintStream verboseCmd;
    private final PrintStream verboseIn;
    private final PrintStream verboseOut;
    private final PrintStream verboseErr;
    
    private Process proc = null;
    protected InputStream in=null;
    protected OutputStream out=null;
    protected InputStream err=null;

    private String obfuscated =null;

    public void setObfuscated(String val)
    {
        this.obfuscated = val;
    }
    
    protected ChildWrapper(PrintStream verboseCmd, PrintStream verboseIn, PrintStream verboseOut, PrintStream verboseErr)
    {
        this.verboseCmd = verboseCmd;
        this.verboseIn = verboseIn;
        this.verboseOut = verboseOut;
        this.verboseErr = verboseErr;
    }

    protected Process execute(String cmd) throws IOException
    {
        if (verboseCmd != null)
        {
            verboseCmd.println(cmd);
        }
        proc = Runtime.getRuntime().exec(cmd);
        in  = verboseIn  == null ? proc.getInputStream()  : new VerboseInputStream(proc.getInputStream(),   verboseIn);
        out = verboseOut == null ? proc.getOutputStream() : new VerboseOutputStream(proc.getOutputStream(), verboseOut);
        err = verboseErr  == null ? proc.getErrorStream()  : new VerboseInputStream(proc.getErrorStream(),   verboseErr);
        return proc;
    }
    protected Process execute(String ... cmd) throws IOException
    {
        if (verboseCmd != null)
        {
            String line = "$";
            for(int i =0;i<cmd.length;i++)
            {
                String sep = (i>0?" ":"");
                line += sep+ (cmd[i].equals(obfuscated)?"********":Files.escape(cmd[i]));
            }
            verboseCmd.println(line);
        }
        proc = Runtime.getRuntime().exec(cmd);
        in  = verboseIn  == null ? proc.getInputStream()  : new VerboseInputStream(proc.getInputStream(),   verboseIn);
        out = verboseOut == null ? proc.getOutputStream() : new VerboseOutputStream(proc.getOutputStream(), verboseOut);
        err = verboseErr  == null ? proc.getErrorStream()  : new VerboseInputStream(proc.getErrorStream(),   verboseErr);
        return proc;
    }

}
