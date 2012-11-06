/*
 *  ConsoleGaugeView.java
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
package com.softenido.cafedark.gauge;

import com.softenido.cafecore.gauge.GaugeView;
import java.io.Console;
import java.io.PrintStream;

/**
 *
 * @author franci
 */
public class ConsoleGaugeView implements GaugeView
{
    private int lastLen = 0;
    private boolean debug = true;
    private boolean prefixBreak = true;
    private String prefix = null;

    private final Console con;
    private final PrintStream out;
    
    public ConsoleGaugeView()
    {
        super();
        this.con = System.console();
        this.out = System.out;
    }
    public ConsoleGaugeView(PrintStream out)
    {
        super();
        this.con = null;
        this.out = out;
        this.debug=true;
    }

    @Override
    public void paint(boolean started, int max, int val, String prefix, double done, String msg)
    {
        boolean newLine = (prefixBreak && this.prefix.equals(prefix));
        this.prefix=prefix;
        
        StringBuilder buf = new StringBuilder("\r");
        buf.append(msg);
        for (int i = msg.length(); i < lastLen; i++)
        {
            buf.append(" ");
        }
        if (newLine)
        {
            buf.append("\n");
        }

        if (con == null && debug)
        {
            this.out.print(buf.toString());
            this.out.flush();
        }
        else
        {
            con.printf("%s", buf.toString());
            con.flush();
        }
        lastLen = msg.length();
    }

    public boolean isDebug()
    {
        return debug;
    }

    public void setDebug(boolean debug)
    {
        this.debug = debug;
    }
}
