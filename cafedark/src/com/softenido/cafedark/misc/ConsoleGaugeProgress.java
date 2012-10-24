/*
 *  ConsoleGaugeProgress.java
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
package com.softenido.cafedark.misc;

import com.softenido.cafecore.misc.AbstractGaugeProgress;
import java.io.Console;

/**
 *
 * @author franci
 */
public class ConsoleGaugeProgress extends AbstractGaugeProgress
{

    private int lastLen = 0;
    private boolean debug = true;
    Console con = System.console();
    private boolean prefixBreak = true;
    private boolean newLine = false;

    public ConsoleGaugeProgress()
    {
        super();
    }

    public void paint(double done, String txt)
    {
        StringBuilder buf = new StringBuilder("\r");
        buf.append(txt);
        for (int i = txt.length(); i < lastLen; i++)
        {
            buf.append(" ");
        }
        if (newLine)
        {
            buf.append("\n");
        }

        if (con == null && debug)
        {
            System.out.print(buf.toString());
            System.out.flush();
        }
        else
        {
            con.printf("%s", buf.toString());
            con.flush();
        }
        lastLen = txt.length();
    }

    @Override
    public void setPrefix(String prefix)
    {
        if(prefixBreak)
        {
            newLine=true;
            paint(getDone(),getPrefix());
            newLine=false;
        }
        super.setPrefix(prefix);
        paint(getDone(),prefix);
    }
}
