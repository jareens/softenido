/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.cafecore.text;

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
public class ParagraphsTest
{
    
    public ParagraphsTest()
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

    
    static final String[] docs = 
    {
        "a\n\n",
        "\n\na",
        "\ufffc\n\na",
        "a\n\n\ufffc\n\nb",
        " a\n\n\ufffc\n\nb ",
        " a\n\n \ufffc\n\ufffc \n\n\n \n\nb ",
    };
    static final String[][] expDirty = 
    {
        {"a"},
        {"a"},
        {"\ufffc","a"},
        {"a","\ufffc","b"},
        {"a","\ufffc","b"},
        {"a","\ufffc\n\ufffc","b"},
    };
    static final String[][] expClean = 
    {
        {"a"},
        {"a"},
        {"a"},
        {"a","b"},
        {"a","b"},
        {"a","b"},
    };
    /**
     * Test of split method, of class Paragraphs.
     */
    @Test
    public void testSplit_String_boolean()
    {
        String[] result;
        for(int i=0;i<docs.length;i++)
        {
            result = Paragraphs.split(docs[i],false);
            assertArrayEquals(""+i, expDirty[i], result);
            
            result = Paragraphs.split(docs[i],true);
            assertArrayEquals(""+i, expClean[i], result);
        }
    }

    /**
     * Test of split method, of class Paragraphs.
     */
    @Test
    public void testSplit_String()
    {
        String[] result;
        for(int i=0;i<docs.length;i++)
        {
            result = Paragraphs.split(docs[i]);
            assertArrayEquals(""+i, expDirty[i], result);
        }
    }
}
