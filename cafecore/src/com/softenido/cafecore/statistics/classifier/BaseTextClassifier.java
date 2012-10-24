/*
 * BaseTextClassifier.java
 *
 * Copyright (c) 2012 Francisco Gómez Carrasco
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
package com.softenido.cafecore.statistics.classifier;

import com.softenido.cafecore.util.CounterSet;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.Character.UnicodeBlock;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author franci
 */
public class BaseTextClassifier implements TextClassifier
{
    final Classifier classifier;

    BaseTextClassifier(Classifier classifier)
    {
        this.classifier = classifier;
    }
    
    public BaseTextClassifier(String unmatched)
    {
        this(new NaiveSerialClassifier(unmatched));
    }
    public BaseTextClassifier(String unmatched, String[] categories)
    {
        this(new NaiveParallelClassifier(unmatched, categories));
    }
    public BaseTextClassifier(String unmatched, int categories)
    {
        this(new NaiveParallelClassifier(unmatched, categories));
    }

    public void coach(String category, String text)
    {
        this.coach(category, new Scanner(text));
    }

    public void coach(String category, InputStream text)
    {
        this.coach(category, new Scanner(text));
    }
    static final String DELIMITER = "([\\p{javaWhitespace},:;，、；：。])+";
    static boolean group = true;//indica si se agrupan las palabras en coach 
    private void coach(String category, Scanner sc)
    {
        sc.useDelimiter(DELIMITER);
        if (group)
        {
            this.coachGroup(category, sc);
        }
        this.coachSingle(category, sc);
    }

    private void coachSingle(String category, Scanner sc)
    {
        while (sc.hasNext())
        {
            String[] words = this.split(sc.next());
            for(String w : words)
            {
                w = filter(w);
                if(w != null)
                {
                    this.classifier.coach(category, w, 1);
                }
            }
        }
    }
    // los 3 valores siguientes son arbitrarios
    static final int SUCCESS = 2;//
    static final int VERIFY_SIZE = 1024;//1k
    static final int BLOCK_SIZE = 65536;//64k

    private void coachGroup(String category, Scanner sc)
    {       
        CounterSet<String> set = new CounterSet<String>(BLOCK_SIZE);
        while(sc.hasNext())
        {
            String[] words = this.split(sc.next());
            for(int i=0;i<words.length;i++)
            {
                String w = filter(words[i]);
                if(w != null)
                {
                    set.add(w);
                }
                if (i % VERIFY_SIZE == 0 && i > VERIFY_SIZE)
                {
                    if (set.getSuccess() < SUCCESS)
                    {
                        break;
                    }
                    if (set.size() > BLOCK_SIZE)
                    {
                        this.coachSet(category, set);
                        i = 0;
                    }
                }
            }
        }
        this.coachSet(category, set);
    }

    private void coachSet(String category, CounterSet<String> set)
    {
        String[] w = new String[set.size()];
        int[] c = new int[set.size()];
        set.toArray(w, c);
        classifier.coach(category, w, c);
        set.clear();
    }
    private static int sampleLimit = Integer.MAX_VALUE;

    Score classify(Scanner sc)
    {
        boolean ok = false;
        ArrayList<String> samples = new ArrayList<String>();
        for (int i = 0; i < this.sampleLimit && sc.hasNext(); i++)
        {
            ok=true;
            String[] words = split(sc.next());
            for(String w : words)
            {
                w = filter(w);
                if(w != null)
                {
                    samples.add(w);
                }
            }
        }
        if(!ok)
        {
            System.out.println(sc.nextLine());
        }
        return classifier.classify(samples.toArray(new String[0]));
    }

    public Score classify(String text)
    {
        return this.classify(new Scanner(text));
    }

    public Score classify(InputStream text)
    {
        return this.classify(new Scanner(text));
    }
//    static final String FILTER = "[\\p{Punct}\\p{Digit}«»“”„?’|]";
//    static final Pattern noletter = Pattern.compile(FILTER + "*");
//    static final Pattern noletterbegin = Pattern.compile(FILTER + ".*");
//    static final Pattern noletterend = Pattern.compile(".*" + FILTER);
//    static final Pattern alphanumeric = Pattern.compile("\\w+");
//    static final Pattern numeric = Pattern.compile("[0-9,.]+");
    
    static final Set<UnicodeBlock> asianCharsType = new HashSet<UnicodeBlock>() 
    {
        {
            add(UnicodeBlock.CJK_COMPATIBILITY);
            add(UnicodeBlock.CJK_COMPATIBILITY_FORMS);
            add(UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS);
            add(UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT);
            add(UnicodeBlock.CJK_RADICALS_SUPPLEMENT);
            add(UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION);
            add(UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS);
            add(UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A);
            add(UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B);
            add(UnicodeBlock.KANGXI_RADICALS);
            add(UnicodeBlock.IDEOGRAPHIC_DESCRIPTION_CHARACTERS);
            add(UnicodeBlock.HIRAGANA);
            add(UnicodeBlock.KATAKANA);
            add(UnicodeBlock.HANGUL_SYLLABLES);
        }
    };
    static final int[] letterables = {'’'};
    private static boolean isLetterOrDigit(int codepoint)
    {
        boolean found = Character.isLetterOrDigit(codepoint);
        for(int i=0;!found && i<letterables.length;i++)
        {
            found = (codepoint==letterables[i]);
        }
        return found;
    }
    private static boolean isAsian(int codepoint)
    {
        return asianCharsType.contains(UnicodeBlock.of(codepoint));
    }
    static String[] split(String text)
    {
        if(text.length()==0)
        {
            return new String[0];
        }
        if(text.length()<=1)
        {
            return new String[]{text};
        }
        
        ArrayList<String> list = new ArrayList<String>();

        // split into tokens
        int lenth = text.codePointCount(0, text.length());
        StringBuilder sb = new StringBuilder();
        boolean data =false;
        for(int i = 0;i<lenth;i++)
        {
            int c = text.codePointAt(i);
            boolean letter = isLetterOrDigit(c);
            boolean asian  = isAsian(c);
            
            if(!letter||asian)
            {
                if(data)
                {
                    list.add(sb.toString());
                    sb=new StringBuilder();
                    data=false;
                }
                if(letter&&asian)
                {
                    list.add(new StringBuilder().appendCodePoint(c).toString());
                }
                continue;
            }
            sb.appendCodePoint(c);
            data=true;
        }
        if(data)
        {
            list.add(sb.toString());
        }
        return list.toArray(new String[0]);
    }

    String filter(String word)
    {
        // at least a letter
        if (word.length() == 0)
        {
            return null;
        }
        //we don't care about case
        word = word.toLowerCase();
        
        final int len = word.codePointCount(0, word.length());
        //look for the first letter
        int first;
        for(first=0;first<len;first++)
        {
            if(Character.isLetter(word.codePointAt(first)))
                break;
        }
        //look for the last letter or digit
        int last;
        for(last=len-1;last>first;last--)
        {
            if(Character.isLetterOrDigit(word.codePointAt(last)))
                break;
        }
        //full string needs no change
        if(first==0&&last==len-1)
            return word;
        //at least a letter
        if(last-first<1)
            return null;
        
        StringBuilder sb = new StringBuilder();
        for(int i=first;i<=last;i++)
            sb.appendCodePoint(word.codePointAt(i));
        
        return sb.toString();
        
//        // ascii words needs no change
//        if (alphanumeric.matcher(word).matches())
//        {
//            return word;
//        }
//        //garbage is not a word
//        if (noletter.matcher(word).matches())
//        {
//            return null;
//        }
//        //cut garbage before the word
//        while (noletterbegin.matcher(word).matches())
//        {
//            word = word.substring(1, word.length());
//        }
//        //cut garbage after the word
//        while (noletterend.matcher(word).matches())
//        {
//            word = word.substring(0, word.length() - 1);
//        }
//        return word;
    }
    
    public void load(InputStream in, boolean strict, String... allowedCategories) throws ClassifierFormatException
    {
        classifier.load(in, strict, allowedCategories);
    }
    public void save(OutputStream out, int min, int max, String... allowedCategories) throws UnsupportedEncodingException
    {
        classifier.save(out, min, max, allowedCategories);
    }
    public void loadGZ(InputStream in, boolean strict, String... allowedCategories) throws ClassifierFormatException, IOException, NoSuchAlgorithmException
    {
        classifier.loadGZ(in, strict, allowedCategories);
    }
    public void saveGZ(OutputStream out, int min, int max, String... allowedCategories) throws UnsupportedEncodingException, IOException, NoSuchAlgorithmException
    {
        classifier.saveGZ(out, min, max, allowedCategories);
    }

    public int hashCode()
    {
        return classifier.hashCode();
    }

    public boolean equals(Object obj)
    {
        if (obj == this)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final BaseTextClassifier other = (BaseTextClassifier) obj;
        return classifier.equals(other.classifier);
    }

    @Override
    public String toString()
    {
        return "TextClassifier{" + "classifier=" + classifier + '}';
    }

    public static int getSampleLimit()
    {
        return sampleLimit;
    }

    public static void setSampleLimit(int sampleLimit)
    {
        BaseTextClassifier.sampleLimit = sampleLimit;
    }

    public boolean containsCategory(String category)
    {
        return classifier.containsCategory(category);
    }

    public BaseTextClassifier synchronizedClassifier()
    {
        return synchronizedClassifier(this);
    }
    static BaseTextClassifier synchronizedClassifier(BaseTextClassifier classifier)
    {
        return new BaseTextClassifier(classifier.classifier)
        {
            final Object lock = new Object();
            @Override
            public void coach(String category, String text)
            {
                synchronized(lock)
                {
                    super.coach(category, text);
                }
            }

            @Override
            public void coach(String category, InputStream text)
            {
                synchronized(lock)
                {
                    super.coach(category, text);
                }
            }

            @Override
            Score classify(Scanner sc)
            {
                synchronized(lock)
                {
                    return super.classify(sc);
                }
            }

            @Override
            public Score classify(String text)
            {
                synchronized(lock)
                {
                    return super.classify(text);
                }
            }

            @Override
            public Score classify(InputStream text)
            {
                synchronized(lock)
                {
                    return super.classify(text);
                }
            }

            @Override
            public boolean containsCategory(String category)
            {
                synchronized(lock)
                {
                    return super.containsCategory(category);
                }
            }

            @Override
            public void load(InputStream in, boolean strict, String... allowedCategories) throws ClassifierFormatException
            {
                synchronized(lock)
                {
                    super.load(in, strict, allowedCategories);
                }
            }
            
            @Override
            public void save(OutputStream out, int min, int max, String... allowedCategories) throws UnsupportedEncodingException
            {
                synchronized(lock)
                {
                    super.save(out, min, max, allowedCategories);
                }
            }

            @Override
            public void loadGZ(InputStream in, boolean strict, String... allowedCategories) throws ClassifierFormatException, IOException, NoSuchAlgorithmException
            {
                synchronized(lock)
                {
                    super.loadGZ(in, strict, allowedCategories);
                }
            }
            @Override
            public void saveGZ(OutputStream out, int min, int max, String... allowedCategories) throws UnsupportedEncodingException, IOException, NoSuchAlgorithmException
            {
                synchronized(lock)
                {
                    super.saveGZ(out, min, max, allowedCategories);
                }
            }

            @Override
            public String getUnmatched()
            {
                synchronized(lock)
                {
                    return super.getUnmatched();
                }
            }
            @Override
            public void setUnmatched(String unmatched)
            {
                synchronized(lock)
                {
                    super.setUnmatched(unmatched);
                }
            }
        };
    }

    public static boolean isGroup()
    {
        return group;
    }

    public static void setGroup(boolean group)
    {
        BaseTextClassifier.group = group;
    }

    public void setUnmatched(String unmatched)
    {
        classifier.setUnmatched(unmatched);
    }
    public String getUnmatched()
    {
        return classifier.getUnmatched();
    }
}
