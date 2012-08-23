/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.cafecore.util;

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
public class FontsTest
{
    
    public FontsTest()
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
     * Test of isKnown method, of class Fonts.
     */
    @Test
    public void testIsKnown()
    {
        for(String font : Fonts.safe_sans)
        {
            assertEquals(font, true, Fonts.isKnown(font));
        }
        for(String font : Fonts.safe_serif)
        {
            assertEquals(font, true, Fonts.isKnown(font));
        }
        for(String font : Fonts.safe_slab)
        {
            assertEquals(font, true, Fonts.isKnown(font));
        }
        for(String font : Fonts.safe_mono)
        {
            assertEquals(font, true, Fonts.isKnown(font));
        }
        for(String font : Fonts.safe_known)
        {
            assertEquals(font, true, Fonts.isKnown(font));
        }
        for(String font : Fonts.sans)
        {
            assertEquals(font, true, Fonts.isKnown(font));
        }
        for(String font : Fonts.serif)
        {
            assertEquals(font, true, Fonts.isKnown(font));
        }
        for(String font : Fonts.slab)
        {
            assertEquals(font, true, Fonts.isKnown(font));
        }
        for(String font : Fonts.mono)
        {
            assertEquals(font, true, Fonts.isKnown(font));
        }
        for(String font : Fonts.cursive)
        {
            assertEquals(font, true, Fonts.isKnown(font));
        }
        for(String font : Fonts.fantasy)
        {
            assertEquals(font, true, Fonts.isKnown(font));
        }
    }

    /**
     * Test of isSans method, of class Fonts.
     */
    @Test
    public void testIsSans()
    {
        for(String font : Fonts.safe_sans)
        {
            assertEquals(font, true, Fonts.isSans(font));
        }
        for(String font : Fonts.safe_serif)
        {
            assertEquals(font, false, Fonts.isSans(font));
        }
        for(String font : Fonts.serif)
        {
            assertEquals(font, false, Fonts.isSans(font));
        }
    }

    /**
     * Test of isSerif method, of class Fonts.
     */
    @Test
    public void testIsSerif()
    {
        for(String font : Fonts.safe_serif)
        {
            assertEquals(font, true, Fonts.isSerif(font));
        }
        for(String font : Fonts.safe_sans)
        {
            assertEquals(font, false, Fonts.isSerif(font));
        }
        for(String font : Fonts.sans)
        {
            assertEquals(font, false, Fonts.isSerif(font));
        }
    }

    /**
     * Test of isMonospace method, of class Fonts.
     */
    @Test
    public void testIsMonospace()
    {
        for(String font : Fonts.safe_mono)
        {
            assertEquals(font, true, Fonts.isMonospace(font));
        }
    }


    /**
     * Test of getGenericFamily method, of class Fonts.
     */
    @Test
    public void testGetGenericFamily()
    {
        for(String font : Fonts.known)
        {
            //00000
            if(!Fonts.isSans(font) && !Fonts.isSerif(font) && !Fonts.isCursive(font) && !Fonts.isFantasy(font) && !Fonts.isMonospace(font) )
                assertEquals("",Fonts.getGenericFamily(font));
            //00001
            else if(!Fonts.isSans(font) && !Fonts.isSerif(font) && !Fonts.isCursive(font) && !Fonts.isFantasy(font) && Fonts.isMonospace(font) )
                assertEquals(font,"monospace",Fonts.getGenericFamily(font));
            //00010
            else if(!Fonts.isSans(font) && !Fonts.isSerif(font) && !Fonts.isCursive(font) && Fonts.isFantasy(font) && !Fonts.isMonospace(font) )
                assertEquals(font,"fantasy",Fonts.getGenericFamily(font));
            //00011
            else if(!Fonts.isSans(font) && !Fonts.isSerif(font) && !Fonts.isCursive(font) && Fonts.isFantasy(font) && Fonts.isMonospace(font) )
                assertEquals(font,"fantasy, monospace",Fonts.getGenericFamily(font));
            //00100
            else if(!Fonts.isSans(font) && !Fonts.isSerif(font) && Fonts.isCursive(font) && !Fonts.isFantasy(font) && !Fonts.isMonospace(font) )
                assertEquals(font,"cursive",Fonts.getGenericFamily(font));
            //00101
            else if(!Fonts.isSans(font) && !Fonts.isSerif(font) && Fonts.isCursive(font) && !Fonts.isFantasy(font) && Fonts.isMonospace(font) )
                assertEquals(font,"cursive, monospace",Fonts.getGenericFamily(font));
            //00110
            else if(!Fonts.isSans(font) && !Fonts.isSerif(font) && Fonts.isCursive(font) && Fonts.isFantasy(font) && !Fonts.isMonospace(font) )
                assertEquals(font,"sans-serif, cursive, fantasy, monospace",Fonts.getGenericFamily(font));
            //00111
            else if(!Fonts.isSans(font) && !Fonts.isSerif(font) && Fonts.isCursive(font) && Fonts.isFantasy(font) && Fonts.isMonospace(font) )
                assertEquals(font,"cursive, fantasy, monospace",Fonts.getGenericFamily(font));
            //01000
            else if(!Fonts.isSans(font) && Fonts.isSerif(font) && !Fonts.isCursive(font) && !Fonts.isFantasy(font) && !Fonts.isMonospace(font) )
                assertEquals(font,"serif",Fonts.getGenericFamily(font));
            //01001
            else if(!Fonts.isSans(font) && Fonts.isSerif(font) && !Fonts.isCursive(font) && !Fonts.isFantasy(font) && Fonts.isMonospace(font) )
                assertEquals(font,"sans-serif, monospace",Fonts.getGenericFamily(font));
            //10000
            else if(Fonts.isSans(font) && !Fonts.isSerif(font) && !Fonts.isCursive(font) && !Fonts.isFantasy(font) && !Fonts.isMonospace(font) )
                assertEquals(font,"sans-serif",Fonts.getGenericFamily(font));
            //10001
            else if(Fonts.isSans(font) && !Fonts.isSerif(font) && !Fonts.isCursive(font) && !Fonts.isFantasy(font) && Fonts.isMonospace(font) )
                assertEquals(font,"sans-serif, monospace",Fonts.getGenericFamily(font));
            //10010
            else if(!Fonts.isSans(font) && !Fonts.isSerif(font) && !Fonts.isCursive(font) && !Fonts.isFantasy(font) && !Fonts.isMonospace(font) )
                assertEquals(font,"sans-serif, fantasy",Fonts.getGenericFamily(font));
            //10011
            else if(Fonts.isSans(font) && !Fonts.isSerif(font) && !Fonts.isCursive(font) && Fonts.isFantasy(font) && Fonts.isMonospace(font) )
                assertEquals(font,"sans-serif, fantasy, monospace",Fonts.getGenericFamily(font));
            //10100
            else if(Fonts.isSans(font) && !Fonts.isSerif(font) && Fonts.isCursive(font) && !Fonts.isFantasy(font) && !Fonts.isMonospace(font) )
                assertEquals(font,"sans-serif, cursive",Fonts.getGenericFamily(font));
            //10101
            else if(!Fonts.isSans(font) && !Fonts.isSerif(font) && !Fonts.isCursive(font) && !Fonts.isFantasy(font) && !Fonts.isMonospace(font) )
                assertEquals(font,"sans-serif, cursive, monospace",Fonts.getGenericFamily(font));
            //10110
            else if(!Fonts.isSans(font) && !Fonts.isSerif(font) && !Fonts.isCursive(font) && !Fonts.isFantasy(font) && !Fonts.isMonospace(font) )
                assertEquals(font,"sans-serif, cursive, fantasy",Fonts.getGenericFamily(font));
            //10111
            else if(!Fonts.isSans(font) && !Fonts.isSerif(font) && !Fonts.isCursive(font) && !Fonts.isFantasy(font) && !Fonts.isMonospace(font) )
                assertEquals(font,"sans-serif, cursive, fantasy, monospace",Fonts.getGenericFamily(font));
            //11000
            else if(!Fonts.isSans(font) && !Fonts.isSerif(font) && !Fonts.isCursive(font) && !Fonts.isFantasy(font) && !Fonts.isMonospace(font) )
                assertEquals(font,"sans-serif, serif, cursive, fantasy, monospace",Fonts.getGenericFamily(font));
            //11001
            else if(!Fonts.isSans(font) && !Fonts.isSerif(font) && !Fonts.isCursive(font) && !Fonts.isFantasy(font) && !Fonts.isMonospace(font) )
                assertEquals(font,"sans-serif, serif, cursive, fantasy, monospace",Fonts.getGenericFamily(font));
            else
                assertEquals(font,"",Fonts.getGenericFamily(font));
        }
        
        assertEquals("",Fonts.getGenericFamily("dummy font name"));
    }

}
