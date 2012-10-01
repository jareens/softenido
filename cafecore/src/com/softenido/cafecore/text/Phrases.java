/*
 * Phrases.java
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.cafecore.text;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author franci
 */
public class Phrases
{
    //(phrase)+(punct)+(spaces)+(phrases)

    static final String OPEN   = "¿¡({\\[";
    static final String CLOSE  = "。.:;?!)}\\]";
    static final String ALL  = ".:;¿?¡!。\"“”()\\[\\]{}";
    static final String NEXT   = "($|(.|\n)+)";
    static final String QUOTE  = "\"“”";

    private static String _(String exp)
    {
        String regex = exp.replace("A", ALL)
                          .replace("a", ALL+"'")
                          .replace("O", OPEN)
                          .replace("C", CLOSE)
                          .replace("c", CLOSE+",")
                          .replace("Q", QUOTE)
                          .replace("q", "'")
                          .replace("N", NEXT)
                          .replace("S","\\s\u00a0")//all kind of spaces
                          .replace("s"," \t");//spaces in the same line;
        return regex;
    }
    //simple
    static final String[] REGEX =
    {
        "([S]*[q]+[^a]+[C]*[s]*[q]+[s]*[c]*)([S]N)",
        "([^a]+[S]+)([q]N)",
        "([S]*[O]+[^A]+[C]+)([S]N)",
        
        "([^A]+)($)",
        "([O]*[^A]+[C]+[s]*[,]*)(N)",
        "([^A]+[S]+)([Q]N)",
        "([^A]+[s]+)([O]N)",

        "([S]*[Q]+[^A]+[C]*[s]*[Q]+[s]*[c]*)([S]N)",
        
        "([S]*[O]+[s]*[Q]+[^A]+[C]*[s]*[Q]+[s]*[C]+)([ ]N)",
//        "([ ]*[Q]+[ ]*[O]+[^P]+[C]+[ ]*[Q]+[ ]*[C]*)([ ]N)",
        "([S]*[O]+[s]*[Q]+[^A]+[C]+)([ ]N)",

//        "( *[B]+[Q]+[^P]+[Q]+[b]+)( N)",
        
//        "([^P]+? +)([“]N)",        

        
//        "( *[^P]+ +)([Q]+N)",
//        "( *[^P]+[Q]+)N",

//        "( *[Q]+[^P]+)($)",
//        "( *[Q]+[^P]+[C]+)N",
//        "( *[¿]+[^P]+[?]+)N",
//        "( *[¡]+[^P]+[!]+)N",
//        "( *[“][^P]+[”]+)N",
//        "( *[(]+[^P]+[)]+)N",
//        "( *[¿]+[^P]+[?]+)N",
//        "( *[¡]+[^P]+[!]+)N",
//        "( *[“][^P]+)N",
//        "( *[(]+[^P]+)N",
//        "( *[¿]+[^P]+)N",
//        "( *[¡]+[^P]+)N",
        
    };
    static Pattern[] patterns = null;
    static Pattern[] getPatterns()
    {
        if(patterns==null)
        {
            patterns = new Pattern[REGEX.length];
            for(int i=0;i<patterns.length;i++)
            {
                patterns[i] = Pattern.compile(_(REGEX[i]));
            }
        }
        return patterns;
    }
    
    public static List<String> splitByPattern(String paragraph)
    {
        ArrayList<String> phrases = new ArrayList<String>();
        
        Pattern[] patterns = getPatterns();
                
        while(paragraph!=null && paragraph.length()>0)
        {
            if(paragraph.trim().length()==0)
            {
                break;
            }
            
            Matcher matcher=null;
            
            for(int i=0;i<patterns.length;i++)
            {
                matcher = patterns[i].matcher(paragraph);
                if(matcher.matches())
                {
//                    System.out.println("----------");
//                    System.out.println(REGEX[i]+" -> "+matcher.pattern().pattern());
//                    System.out.println("----------");
                    break;
                }
                matcher = null;
            }
            if(matcher != null)
            {
                String match = matcher.group(1);
                String next  = matcher.group(2);
                System.out.println("++++++++++");
                System.out.println(match);
                phrases.add(match);
                paragraph = next;
                continue;
            }
            System.out.println("++++++++++");
            System.out.println(paragraph);
            System.out.println("++++++++++");
            System.out.println("++++++++++");
            System.out.println("-fin-");
            phrases.add(paragraph);
            break;
        }
        return phrases;
    }
    public static String[] split(String paragraph)
    {
        return split(paragraph, 0);
    }
    
    public static String[] split(String paragraph, int minSize)
    {
        if(paragraph.length()<minSize)
        {
            return new String[]{paragraph};
        }

        List<String> cuts = splitByPattern(paragraph);
        ArrayList<String> join = new ArrayList<String>();

        //join possible abbreviations
        while(cuts.size()>0)
        {
            String p0 = cuts.get(0);
            if(p0.length()==0 || p0.trim().length()==0)
            {
                cuts.remove(0);
                continue;
            }
            if(cuts.size()==1)
            {
                join.add(cuts.remove(0).trim());                
                break;
            }
            String p1 = cuts.get(1);
            if(p1.length()==0)
            {
                cuts.remove(1);
                continue;
            }
            boolean merge = ( p0.endsWith(".") && ( !Character.isSpaceChar(p1.codePointAt(0)) || dotContinued(p1) ) ) ||
                            ( p0.endsWith(".") && ( !Character.isSpaceChar(p1.codePointAt(0)) || dotContinued(p1) ) ) ;
            if(merge)
            {
                String p=p0+p1;
                cuts.set(0, p.trim());
                cuts.remove(1);
                continue;
            }
            join.add(cuts.remove(0).trim());
        }
        return join.toArray(new String[0]);
    }
    private static boolean endsWithLatinAbbreviation(String phrase)
    {
        if(phrase.length()==0)
        {
            return false;
        }
        int i = phrase.codePointCount(0, phrase.length())-1;
        //dot
        int codepoint = phrase.codePointAt(i--);
        if(codepoint!='.')
        {
            return false;
        }
        codepoint = phrase.codePointAt(i--);
        while(i>=0 && Character.isLowerCase(codepoint))
        {
            codepoint = phrase.codePointAt(i--);
        }
        return Character.isUpperCase(codepoint);
    }
    private static boolean dotContinued(String phrase)
    {
        if(phrase.length()==0)
        {
            return false;
        }
        int count = phrase.codePointCount(0, phrase.length());
        int i=0;
        //spaces
        for(;i<count;i++)
        {
            int codepoint = phrase.codePointAt(i);
            if(!Character.isSpaceChar(codepoint))
                break;
        }
        if(i>=count)
        {
            return false;
        }
        int codepoint = phrase.codePointAt(i);
        return Character.isLowerCase(codepoint);
    }
    
    
}
