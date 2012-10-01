/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.cafecore.text;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author franci
 */
public class PhrasesTest
{
    
    public PhrasesTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of split method, of class Phrases.
     */
    @Test
    public void testSplit_String()
    {
        String[][][] samples =
        {
            {
                {"Because of its widespread influence, Don Quixote also helped cement the modern Spanish language. The opening sentence of the book created a classic Spanish cliché with the phrase \"de cuyo nombre no quiero acordarme\" (\"whose name I do not wish to recall\"): \"En un lugar de la Mancha, de cuyo nombre no quiero acordarme, no hace mucho tiempo que vivía un hidalgo de los de lanza en astillero, adarga antigua, rocín flaco y galgo corredor. \" (\"In a village of La Mancha, whose name I do not wish to recall, there lived, not very long ago, one of those gentlemen with a lance in the lance-rack, an ancient shield, a skinny old horse, and a fast greyhound. The novel's farcical elements make use of punning and similar verbal playfulness. Character-naming in Don Quixote makes ample figural use of contradiction, inversion, and irony, such as the names Rocinante (a reversal) and Dulcinea (an allusion to illusion), and the word quixote itself, possibly a pun on quijada (jaw) but certainly cuixot (Catalan: thighs), a reference to a horse's rump. As a military term, the word quijote refers to cuisses, part of a full suit of plate armour protecting the thighs. The Spanish suffix -ote denotes the augmentative—for example, grande means large, but grandote means extra large. Following this example, Quixote would suggest 'The Great Quijano', a play on words that makes much sense in light of the character's delusions of grandeur. La Mancha is a region of Spain, but mancha (Spanish word) means spot, mark, stain, region and word are not etymologically related (from Arab origin the former, from Latin macula the latter), so \"de La Mancha\" (lit. both Stained Quixote and Quixote from La Mancha) is a hilarious name for a spotless knight."},
                {
                    "Because of its widespread influence, Don Quixote also helped cement the modern Spanish language.",
                    "The opening sentence of the book created a classic Spanish cliché with the phrase",
                    "\"de cuyo nombre no quiero acordarme\"",
                    "(\"whose name I do not wish to recall\"):",
                    "\"En un lugar de la Mancha, de cuyo nombre no quiero acordarme, no hace mucho tiempo que vivía un hidalgo de los de lanza en astillero, adarga antigua, rocín flaco y galgo corredor.\"",
                    "(\"In a village of La Mancha, whose name I do not wish to recall, there lived, not very long ago, one of those gentlemen with a lance in the lance-rack, an ancient shield, a skinny old horse, and a fast greyhound.",
                    "The novel's farcical elements make use of punning and similar verbal playfulness.",
                    "Character-naming in Don Quixote makes ample figural use of contradiction, inversion, and irony, such as the names Rocinante",
                    "(a reversal)",
                    "and Dulcinea ",
                    "(an allusion to illusion),",
                    "and the word quixote itself,",
                    "possibly a pun on quijada",
                    "(jaw)",
                    "but certainly cuixot",
                    "(Catalan: thighs),",
                    "a reference to a horse's rump.",
                    "As a military term, the word quijote refers to cuisses, part of a full suit of plate armour protecting the thighs.",
                    "The Spanish suffix -ote denotes the augmentative—for example, grande means large, but grandote means extra large.",
                    "Following this example, Quixote would suggest",
                    "'The Great Quijano',",
                    "a play on words that makes much sense in light of the character's delusions of grandeur.",
                    "La Mancha is a region of Spain, but mancha",
                    "(Spanish word)",
                    "means spot, mark, stain, region and word are not etymologically related",
                    "(from Arab origin the former, from Latin macula the latter),",
                    "so",
                    "\"de La Mancha\"",
                    "(lit. both Stained Quixote and Quixote from La Mancha)",
                    "is a hilarious name for a spotless knight."
                }
            },
            {
                {"e=m*c²; a=PI*r²;"},
                {"e=m*c²;","a=PI*r²;"}
            },
            {
                {"{uno, dos, tres} pollito inglés."},
                {"{uno, dos, tres}","pollito inglés."}
            },
            {
                {"De repente, miré a la derecha y los vi juntos."},
                {"De repente, miré a la derecha y los vi juntos."}
            },
            {
                {"De repente, miré a la dcha. y los vi juntos."},
                {"De repente, miré a la dcha. y los vi juntos."}
            },
            {
                {"De repente, miré a la dcha. y los vi juntos. "},
                {"De repente, miré a la dcha. y los vi juntos."}
            },
            {
                {"Tengo $10.00 de ayer, ... no lo gasté todo."},
                {"Tengo $10.00 de ayer, ... no lo gasté todo."}
            },
            {
                {"De repente, miré a la dcha. y los vi juntos. Veni, vidi, vinci."},
                {"De repente, miré a la dcha. y los vi juntos.","Veni, vidi, vinci."}
            },
            {
                {"How are you doing today? Are you ok?"},
                {"How are you doing today?","Are you ok?"}
            },
            {
                {"¿Cómo estas Hoy? ¡Te veo estupendo!"},
                {"¿Cómo estas Hoy?","¡Te veo estupendo!"}
            },
            {
                {"静脉，看到的，傻瓜。"},
                {"静脉，看到的，傻瓜。"}
            },
            {
                {"静脉，看到的，傻瓜。静脉，看到的，傻瓜。"},
                {"静脉，看到的，傻瓜。","静脉，看到的，傻瓜。"}
            },
            {
                {"静脉, 看到的, 傻瓜."},
                {"静脉, 看到的, 傻瓜."}
            },
            {
                {"静脉，看到的，傻瓜。 静脉，看到的，傻瓜。"},
                {"静脉，看到的，傻瓜。","静脉，看到的，傻瓜。"}
            },
        };
        
        for(int i=0;i<samples.length;i++)
        {
            String[] result = Phrases.split(samples[i][0][0]);
            assertArrayEquals(samples[i][0][0],samples[i][1], result);
        }
    }

    /**
     * Test of split method, of class Phrases.
     */
    @Test
    public void testSplit_String_int()
    {
        String[][][] samples =
        {
            {
                {"Un caballo. Un león. Un mono."},
                {"Un caballo. Un león. Un mono."}
            },
            {
                {"Un caballo camina por el prado. Un león camina por la sabana. Una mono trepa por el arbol."},
                {"Un caballo camina por el prado.", "Un león camina por la sabana.", "Una mono trepa por el arbol."},
            },
        };
        
        for(int i=0;i<samples.length;i++)
        {
            String[] result = Phrases.split(samples[i][0][0],25);
            assertArrayEquals(samples[i][0][0],samples[i][1], result);
        }
    }

    /**
     * Test of split method, of class Phrases.
     */
    @Test
    public void testBug1() throws IOException
    {
        InputStream fis = new FileInputStream("/home/franci/paragraph.txt");
        byte[] buf = new byte[64000];
        int r = fis.read(buf);
        String parrafo = new String(buf,0,r);
        String[] result = Phrases.split(parrafo,25);
    }

}
