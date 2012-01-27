/*
 * Strings.java
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

package com.softenido.cafecore.util;

/**
 * Created by IntelliJ IDEA.
 * User: franci
 * Date: 21/01/12
 * Time: 20:50
 * To change this template use File | Settings | File Templates.
 */
public abstract class Strings
{
    public static String repeat(char c,int count)
    {
        char[] tmp = new char[count];
        java.util.Arrays.fill(tmp,c);
        return new String(tmp,0,count);
    }
    public static String repeat(String s,int count)
    {
        StringBuilder builder = new StringBuilder(s.length()*count);
        for(int i=0;i<count;i++)
        {
            builder.append(s);
        }
        return builder.toString();
    }
    
    public static StringBuilder fill(StringBuilder builder,char c, int size, boolean insert)
    {
        int count = Math.max(size-builder.length(), 0);
        String cc = repeat(c,count);
        return insert?builder.insert(0, cc):builder.append(cc);
    }
    public static String fill(CharSequence cs,char c, int size, boolean insert)
    {
        return fill(new StringBuilder(cs),c,size,insert).toString();
    }
    public static StringBuilder fill(StringBuilder builder,char c, int size)
    {
        return fill(builder,c,size,false);
    }
    public static String fill(CharSequence cs,char c, int size)
    {
        return fill(new StringBuilder(cs),c,size,false).toString();
    }
}
