/*
 *  OptionParser.java
 *
 *  Copyright (C) 2009  Francisco Gómez Carrasco
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
package com.softenido.cafe.util.options;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author franci
 */
public class OptionParser
{

    private static final String ONE_HYPHEN = "-";
    private static final String TWO_HYPHEN = "--";
    private boolean oneHyphen = true;
    private boolean twoHyphen = true;
    private boolean oneHyphenMode = true;
    private boolean alteLongMode = true;
    private boolean posixly = false;
    List<Option> optionList = new ArrayList<Option>();
    private final Option oneHyphenOption = new BooleanOption(ONE_HYPHEN);
    private final Option twoHyphenOption = new BooleanOption(TWO_HYPHEN);

    public OptionParser()
    {
    }

    public <T extends Option> T add(T item)
    {
        optionList.add(item);
        return item;
    }

    public String[] parse(String[] args)
    {
        Option[] rules = optionList.toArray(new Option[0]);
        String remainder[] = new String[args.length];
        int remainderSize = 0;
argloop:for (int i = 0; i < args.length; i++)
        {
            if (oneHyphen && args[i].equals(ONE_HYPHEN))
            {
                // opcion - input by stdin
                oneHyphenOption.addCount();
                continue;
            }
            else if (twoHyphen && args[i].equals(TWO_HYPHEN))
            {
                // option -- no more options
                twoHyphenOption.addCount();
                continue;
            }
            else
            {
                // long options
                for (int j = 0; j < rules.length; j++)
                {
                    int size = rules[j].parseLong(i, args);
                    if (size > 0)
                    {
                        i += (size - 1);
                        continue argloop;
                    }
                }
                // sort options
                if (args[i].startsWith(ONE_HYPHEN))
                {
                    int size = parseShort(i, args, rules);
                    if (size > 0)
                    {
                        i += (size - 1);
                        continue argloop;
                    }
                }
                if (posixly)
                {
                    for(;i < args.length; i++)
                    {
                        remainder[remainderSize++] = args[i];
                    }
                    break;
                }
                remainder[remainderSize++] = args[i];
            }
        }
        return Arrays.copyOf(remainder, remainderSize);
    }

    private int parseShort(int index, String[] args, Option[] rules)
    {
        int size = 0;
        return 0;
    }

    public boolean isPosixly()
    {
        return posixly;
    }

    public void setPosixly(boolean posixly)
    {
        this.posixly = posixly;
    }
}
