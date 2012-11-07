/*
 * Execute.java
 *
 * Copyright (c) 2012  Francisco Gómez Carrasco
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Report bugs or new features to: flikxxi@gmail.com
 */

package com.softenido.cafedroid.os;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * Created by IntelliJ IDEA.
 * User: franci
 * Date: 21/01/12
 * Time: 11:43
 * To change this template use File | Settings | File Templates.
 */
public class Execute
{
    static public boolean hasRootAccess()
    {
        Process p;
        try
        {
            p = Runtime.getRuntime().exec("su");

            // Attempt to write a file to a root-only
            Writer os = new PrintWriter(p.getOutputStream());
            os.write("echo 'testing root access'\n");
//            os.write("echo 'testing root access' > /system/sd/TestRootAccess.tmp\n");
            os.write("exit\n");
            os.flush();
            p.waitFor();
            if(p.exitValue() != 255)
            {
                return true;
            }
            return false;
        }
        catch(InterruptedException e)
        {
            return false;
        }
        catch(IOException e)
        {
            return false;
        }
    }

    public static boolean rootExec(String cmd)
    {
        Process p = null;
        try
        {
            p = Runtime.getRuntime().exec("su -c " + cmd);
            p.waitFor();
            // Comprobamos si somos root
            if(p.exitValue() != 255)
            {
                return true;
            }
            return false;
        }
        catch(InterruptedException e)
        {
            return false;
        }
        catch(IOException e)
        {
            return false;
        }
    }

}
