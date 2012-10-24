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


import com.softenido.cafecore.util.Strings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author franci
 */
public class Phrases
{
    private static final Logger logger = Logger.getLogger(Phrases.class.getName());
    //(phrase)+(punct)+(spaces)+(phrases)

    static final String OPEN   = "¿¡({\\[«";
    static final String CLOSE  = "。.:;?!)}\\]…|»";
    static final String NEXT   = "($|(.|\n)+)";
    static final String QUOTES  = "\"“”»«";
    static final String QUOTE  = "'‘’";
    static final String TABSPACE  = " \t";
    static final String ANYSPACE  = "\\s\u00a0";
    static final String ALL  = OPEN+CLOSE+QUOTES;
    

    private static String _(String exp)
    {
        String regex = exp.replace("A", ALL)
                          .replace("a", ALL+QUOTE)
                          .replace("O", OPEN)
                          .replace("C", CLOSE)
                          .replace("c", CLOSE+",")
                          .replace("Q", QUOTES)
                          .replace("q", QUOTE)
                          .replace("N", NEXT)
                          .replace("s",TABSPACE)//spaces in the same line
                          .replace("S",ANYSPACE);//all kind of spaces
        return regex;
    }
    //simple
    static final String[] REGEX =
    {
        "([^A]+[.]+)",//1
        "([OQqS]*[^A]+[cQqS]*([S]|$))",//2 ([open|quote]+text+[close|quote))+(quote+text)
        "([OQqS]*[^A]+[cQqS]*[Qq]([S]|$))",//3 ([open|quote]+text+[close|quote))+(quote+text)
        "([OQqS]*[^A]+[cQqS]*)",//4
        "([OQqS]*?[^A]+?[cQqS]*?)",//5 ([open|quote]+text+[close|quote))+(open+text)
    };
    
    static Pattern[] patterns = null;
    static int[] statistics = null;
    static volatile int failures = 0;
    static Pattern[] getPatterns()
    {
        if(patterns==null)
        {
            patterns = new Pattern[REGEX.length];
            for(int i=0;i<patterns.length;i++)
            {
                patterns[i] = Pattern.compile(_(REGEX[i]));
            }
            statistics = new int[patterns.length];
        }
        return patterns;
    }   
   
    public static List<String> splitByPattern(String paragraph)
    {
        ArrayList<String> phrases = new ArrayList<String>();
        Pattern[] patterns = getPatterns();        
        int index=0;
        int count=0;
        while(paragraph!=null && paragraph.length()>0)
        {
            if(paragraph.length()<=index)
            {
                break;
            }
            
            Matcher matcher=null;
            for(int i=0;i<patterns.length;i++)
            {
                matcher = patterns[i].matcher(paragraph);
                if(matcher.find(index) && matcher.start()==index)
                {
                    statistics[i]++;
//                    System.out.println("regex[i]="+i+"-> "+REGEX[i]);
                    break;
                }
                matcher = null;
            }
            if(matcher != null)
            {
                String match = matcher.group();
                index = matcher.end();
                phrases.add(match);
                logger.log(Level.CONFIG, "phrase.match({0})={1}",new Object[]{count++,match});
                continue;
            }
//            System.out.println("..........");
            String nomatch = paragraph.substring(index);
            phrases.add(nomatch);
            logger.log(Level.CONFIG, "phrase.unmatch({0})={1}",new Object[]{count++,nomatch});
            failures++;
            break;
        }
        if(logger.isLoggable(Level.CONFIG))
        {
            logger.log(Level.CONFIG, Arrays.toString(statistics) );
        }
        System.out.println(Arrays.toString(statistics));
        return phrases;
    }
    public static List<String> splitByPattern(List<String> paragraphs)
    {
        ArrayList<String> phrases = new ArrayList<String>(paragraphs.size());
        for(String item : paragraphs)
        {
            List<String> cuts = splitByPattern(item);
            phrases.addAll(cuts);
        }
        paragraphs = phrases;
        phrases = new ArrayList<String>(paragraphs.size());
        return paragraphs;
    }

    public static String[] split(String paragraph)
    {
        return split(paragraph, 0);
    }
    private static class Phrase
    {
        final String full;
        final String trim;
        final int size;

        public Phrase(String full)
        {
            this.full = full;
            this.trim = Strings.trimWhitespaces(full);
            this.size = this.trim.length();
        }
    }
    
    public static String[] split(String paragraph, int min)
    {
        if(paragraph.length()<min)
        {
            return new String[]{paragraph};
        }
        int count = 0;
        
        List<String> cuts = splitByPattern(Arrays.asList(new String[]{paragraph}));
        ArrayList<Phrase> join = new ArrayList<Phrase>();

        //join possible abbreviations
        while(cuts.size()>0)
        {
            String p0 = cuts.get(0);
            int n0 = p0.trim().length();
            if(n0==0)
            {
                cuts.remove(0);
                continue;
            }
            if(cuts.size()==1)
            {
                join.add(new Phrase(cuts.remove(0)));
                break;
            }
            String p1 = cuts.get(1);
            int n1 = p1.trim().length();
            if(n1==0)
            {
                cuts.remove(1);
                continue;
            }            
            boolean dot = p0.endsWith(".");// the last character is a dot
            boolean dotm = dot && !Character.isWhitespace(p1.codePointAt(0));
            boolean abb  = dot || p0.trim().endsWith("."); // the last non-space character is a dot
            boolean abbm = abb && dotContinued(p1);
            
            if(dotm || abbm)
            {
                String p=p0+p1;
                cuts.set(0, p);
                cuts.remove(1);
                continue;
            }
            join.add(new Phrase(cuts.remove(0)));
        }
                
        List<String> merge = new ArrayList<String>();
        //merge short phrases
        while(join.size()>0)
        {
            int n = join.size();
            Phrase p0 = join.get(0);
            Phrase p1 = (n>1)?join.get(1):null;
            Phrase p2 = (n>2)?join.get(2):null;
            Phrase p3 = (n>2)?join.get(2):null;
            
            //this would be a bug
            if(p0.size==0)
            {
                Logger.getLogger(Phrases.class.getName()).log(Level.WARNING,"p0.size==0 means a bug");
                join.remove(0);
                continue;
            }
            
            // n0+N1+... = n0N1+...
            if(n>1)
            {
                if( p0.size<min || (n==2 && p1.size<min) || (n>=3 && p1.size<min && p0.size+p1.size<p1.size+p2.size) || (n>=4 && p1.size<min && p1.size+p2.size+p3.size<min))
                {
                    join.set(0, new Phrase(p0.full+p1.full));
                    join.remove(1);
                    continue;
                }
            }
            assert (p0.size>=min || merge.isEmpty()):"(p0.size>=min || merge.isEmpty())";
            merge.add(p0.trim);
            join.remove(0);
        }
        return merge.toArray(new String[0]);
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

    public static int getFailures()
    {
        return failures;
    }
}
