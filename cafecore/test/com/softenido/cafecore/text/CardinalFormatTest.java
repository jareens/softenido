/*
 *  CardinalFormatTest.java
 *
 *  Copyright (C) 2011-2012  Francisco Gómez Carrasco
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
package com.softenido.cafecore.text;

import java.util.Locale;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;
/**
 *
 * @author franci
 */
public class CardinalFormatTest
{
    static class T
    {
        final long num;
        final String text;

        public T(long num, String text)
        {
            this.num = num;
            this.text = text;
        }
    }
    static private T _(long num, String text)
    {
        return new T(num,text);
    }
    // some test have been tested through these webs
    //http://www.tools4noobs.com/online_tools/number_spell_words/
    //http://www.mathcats.com/explore/reallybignumbers.html
    //http://en.wikipedia.org/wiki/List_of_numbers
    static private T[] es_ES = 
    {
        _(0, "cero"),
        _(1, "uno"),
        _(2, "dos"),
        _(3, "tres"),
        _(4 , "cuatro"),
        _(5, "cinco"),
        _(6, "seis"),
        _(7, "siete"),
        _(8, "ocho"),
        _(9, "nueve"),
        _(10, "diez"),
        _(11, "once"),
        _(12, "doce"),
        _(13, "trece"),
        _(14, "catorce"),
        _(15, "quince"),
        _(16, "dieciseis"),
        _(17, "diecisiete"),
        _(18, "dieciocho"),
        _(19, "diecinueve"),
        _(20, "veinte"),
        _(30, "treinta"),
        _(40, "cuarenta"),
        _(50, "cincuenta"),
        _(60, "sesenta"),
        _(70, "setenta"),
        _(80, "ochenta"),
        _(90, "noventa"),
        _(29, "veintinueve"),
        _(38, "treinta y ocho"),
        _(47, "cuarenta y siete"),
        _(56, "cincuenta y seis"),
        _(65, "sesenta y cinco"),
        _(74, "setenta y cuatro"),
        _(83, "ochenta y tres"),
        _(92, "noventa y dos"),
        _(100, "cien"),
        _(200, "doscientos"),
        _(300, "trescientos"),
        _(400, "cuatrocientos"),
        _(500, "quinientos"),
        _(600, "seiscientos"),
        _(700, "setecientos"),
        _(800, "ochocientos"),
        _(900, "novecientos"),
        _(101, "ciento uno"),
        _(212, "doscientos doce"),
        _(323, "trescientos veintitres"),
        _(434, "cuatrocientos treinta y cuatro"),
        _(545, "quinientos cuarenta y cinco"),
        _(656, "seiscientos cincuenta y seis"),
        _(767, "setecientos sesenta y siete"),
        _(878, "ochocientos setenta y ocho"),
        _(989, "novecientos ochenta y nueve"),
        _(1000, "mil"),
        _(1001, "mil uno"),
        _(1012, "mil doce"),
        _(1123, "mil ciento veintitres"),
        _(1234, "mil doscientos treinta y cuatro"),
        _(2345, "dos mil trescientos cuarenta y cinco"),
        _(3456, "tres mil cuatrocientos cincuenta y seis"),
        _(4567, "cuatro mil quinientos sesenta y siete"),
        _(5678, "cinco mil seiscientos setenta y ocho"),
        _(6789, "seis mil setecientos ochenta y nueve"),
        _(7890, "siete mil ochocientos noventa"),
        _(8901, "ocho mil novecientos uno"),
        _(9012, "nueve mil doce"),
        _(10000, "diez mil"),
        _(10221, "diez mil doscientos veintiuno"),
        _(100000,  "cien mil"),
        _(102321, "ciento dos mil trescientos veintiuno"),
        _(1000000, "un millón"),
        _(1023321, "un millón veintitres mil trescientos veintiuno"),
        _(10000000, "diez millones"),
        _(10234321, "diez millones doscientos treinta y cuatro mil trescientos veintiuno"),
        _(100000000, "cien millones"),
        _(102344321, "ciento dos millones trescientos cuarenta y cuatro mil trescientos veintiuno"),
        _(1000000000,"mil millones"),
        _(1023454321, "mil veintitres millones cuatrocientos cincuenta y cuatro mil trescientos veintiuno"),
        _(10000000000L, "diez mil millones"),
        _(10234554321L, "diez mil doscientos treinta y cuatro millones quinientos cincuenta y cuatro mil trescientos veintiuno"),
        _(100000000000L, "cien mil millones"),
        _(102345654321L, "ciento dos mil trescientos cuarenta y cinco millones seiscientos cincuenta y cuatro mil trescientos veintiuno"),
        _(1000000000000L, "un billón"), 
        _(1023456654321L, "un billón veintitres mil cuatrocientos cincuenta y seis millones seiscientos cincuenta y cuatro mil trescientos veintiuno"),
        _(10000000000000L, "diez billones"),
        _(10234567654321L, "diez billones doscientos treinta y cuatro mil quinientos sesenta y siete millones seiscientos cincuenta y cuatro mil trescientos veintiuno"),
        _(100000000000000L, "cien billones"),
        _(102345677654321L, "ciento dos billones trescientos cuarenta y cinco mil seiscientos setenta y siete millones seiscientos cincuenta y cuatro mil trescientos veintiuno"),
        _(1000000000000000L, "mil billones"),
        _(1023456787654321L, "mil veintitres billones cuatrocientos cincuenta y seis mil setecientos ochenta y siete millones seiscientos cincuenta y cuatro mil trescientos veintiuno"),
        _(10000000000000000L, "diez mil billones"),
        _(10234567887654321L, "diez mil doscientos treinta y cuatro billones quinientos sesenta y siete mil ochocientos ochenta y siete millones seiscientos cincuenta y cuatro mil trescientos veintiuno"),
        _(100000000000000000L, "cien mil billones"),
        _(102345678987654321L, "ciento dos mil trescientos cuarenta y cinco billones seiscientos setenta y ocho mil novecientos ochenta y siete millones seiscientos cincuenta y cuatro mil trescientos veintiuno"),
        _(1000000000000000000L, "un trillón"),
        _(1023456789987654321L, "un trillón veintitres mil cuatrocientos cincuenta y seis billones setecientos ochenta y nueve mil novecientos ochenta y siete millones seiscientos cincuenta y cuatro mil trescientos veintiuno"),
        _(9223372036854775807L ,"nueve trillones doscientos veintitres mil trescientos setenta y dos billones treinta y seis mil ochocientos cincuenta y cuatro millones setecientos setenta y cinco mil ochocientos siete")
    };
    static private T[] en_US = 
    {
        _(0, "zero"),
        _(1, "one"),
        _(2, "two"),
        _(3, "three"),
        _(4, "four"),
        _(5, "five"),
        _(6, "six"),
        _(7, "seven"),
        _(8, "eight"),
        _(9, "nine"),
        _(10, "ten"),
        _(11, "eleven"),
        _(12, "twelve"),
        _(13, "thirteen"),
        _(14, "fourteen"),
        _(15, "fifteen"),
        _(16, "sixteen"),
        _(17, "seventeen"),
        _(18, "eighteen"),
        _(19, "nineteen"),
        _(20, "twenty"),
        _(30, "thirty"),
        _(40, "forty"),
        _(50, "fifty"),
        _(60, "sixty"),
        _(70, "seventy"),
        _(80, "eighty"),
        _(90, "ninety"),
        _(29, "twenty-nine"),
        _(38, "thirty-eight"),
        _(47, "forty-seven"),
        _(56, "fifty-six"),
        _(65, "sixty-five"),
        _(74, "seventy-four"),
        _(83, "eighty-three"),
        _(92, "ninety-two"),
        _(100, "one hundred"),
        _(200, "two hundred"),
        _(300, "three hundred"),
        _(400, "four hundred"),
        _(500, "five hundred"),
        _(600, "six hundred"),
        _(700, "seven hundred"),
        _(800, "eight hundred"),
        _(900, "nine hundred"),
        _(101, "one hundred one"),
        _(212, "two hundred twelve"),
        _(323, "three hundred twenty-three"),
        _(434, "four hundred thirty-four"),
        _(545, "five hundred forty-five"),
        _(656, "six hundred fifty-six"),
        _(767, "seven hundred sixty-seven"),
        _(878, "eight hundred seventy-eight"),
        _(989, "nine hundred eighty-nine"),
        _(1000, "one thousand"),
        _(1001, "one thousand one"),
        _(1012, "one thousand twelve"),
        _(1123, "one thousand one hundred twenty-three"),
        _(1234, "one thousand two hundred thirty-four"),
        _(2345, "two thousand three hundred forty-five"),
        _(3456, "three thousand four hundred fifty-six"),
        _(4567, "four thousand five hundred sixty-seven"),
        _(5678, "five thousand six hundred seventy-eight"),
        _(6789, "six thousand seven hundred eighty-nine"),
        _(7890, "seven thousand eight hundred ninety"),
        _(8901, "eight thousand nine hundred one"),
        _(9012, "nine thousand twelve"),
        _(10000, "ten thousand"),
        _(10221, "ten thousand two hundred twenty-one"),
        _(100000, "one hundred thousand"),
        _(102321, "one hundred two thousand three hundred twenty-one"),
        _(1000000, "one million"),
        _(1023321, "one million twenty-three thousand three hundred twenty-one"),
        _(10000000, "ten million"),
        _(10234321, "ten million two hundred thirty-four thousand three hundred twenty-one"),
        _(100000000, "one hundred million"),
        _(102344321, "one hundred two million three hundred forty-four thousand three hundred twenty-one"),
        _(1000000000, "one billion"),
        _(1023454321, "one billion twenty-three million four hundred fifty-four thousand three hundred twenty-one"),
        _(10000000000L, "ten billion"),
        _(10234554321L, "ten billion two hundred thirty-four million five hundred fifty-four thousand three hundred twenty-one"),
        _(100000000000L, "one hundred billion"),
        _(102345654321L, "one hundred two billion three hundred forty-five million six hundred fifty-four thousand three hundred twenty-one"),
        _(1000000000000L, "one trillion"), 
        _(1023456654321L, "one trillion twenty-three billion four hundred fifty-six million six hundred fifty-four thousand three hundred twenty-one"),
        _(10000000000000L, "ten trillion"),
        _(10234567654321L, "ten trillion two hundred thirty-four billion five hundred sixty-seven million six hundred fifty-four thousand three hundred twenty-one"),
        _(100000000000000L, "one hundred trillion"),
        _(102345677654321L, "one hundred two trillion three hundred forty-five billion six hundred seventy-seven million six hundred fifty-four thousand three hundred twenty-one"),
        _(1000000000000000L, "one quadrillion"),
        _(1023456787654321L, "one quadrillion twenty-three trillion four hundred fifty-six billion seven hundred eighty-seven million six hundred fifty-four thousand three hundred twenty-one"),
        _(10000000000000000L, "ten quadrillion"),
        _(10234567887654321L, "ten quadrillion two hundred thirty-four trillion five hundred sixty-seven billion eight hundred eighty-seven million six hundred fifty-four thousand three hundred twenty-one"),
        _(100000000000000000L, "one hundred quadrillion"),
        _(102345678987654321L, "one hundred two quadrillion three hundred forty-five trillion six hundred seventy-eight billion nine hundred eighty-seven million six hundred fifty-four thousand three hundred twenty-one"),
        _(1000000000000000000L, "one quintillion"),
        _(1023456789987654321L, "one quintillion twenty-three quadrillion four hundred fifty-six trillion seven hundred eighty-nine billion nine hundred eighty-seven million six hundred fifty-four thousand three hundred twenty-one"),
        _(9223372036854775807L ,"nine quintillion two hundred twenty-three quadrillion three hundred seventy-two trillion thirty-six billion eight hundred fifty-four million seven hundred seventy-five thousand eight hundred seven")
    };
    static private T[] en_GB =
    {
        _(101, "one hundred and one"),
        _(201, "two hundred and one"),
        _(212, "two hundred and twelve"),
        _(323, "three hundred and twenty-three"),
        _(434, "four hundred and thirty-four"),
        _(545, "five hundred and forty-five"),
        _(656, "six hundred and fifty-six"),
        _(767, "seven hundred and sixty-seven"),
        _(878, "eight hundred and seventy-eight"),
        _(989, "nine hundred and eighty-nine"),
        _(1001, "one thousand and one"),
        _(1012, "one thousand and twelve"),
        _(1123, "one thousand and one hundred and twenty-three"),
        _(1234, "one thousand and two hundred and thirty-four")
    };
            
    public CardinalFormatTest()
    {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        
    }

    @AfterClass
    public static void tearDownClass() throws Exception
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
     * Test of format method, of class CardinalFormat.
     */
    @Test
    public void testFormat()
    {
        CardinalFormat instance;
        
        instance = CardinalFormat.getInstance(new Locale("",""));
        for(int i=0;i<999;i++)
        {
            Assert.assertEquals(Long.toString(i),Long.toString(i),instance.format(i));
        }
        instance = CardinalFormat.getInstance(new Locale("es","ES"));
        for(T e: es_ES)
        {
            Assert.assertEquals(Long.toString(e.num),e.text, instance.format(e.num));
        }
        instance = CardinalFormat.getInstance(Locale.US);
        for(T e: en_US)
        {
            Assert.assertEquals(Long.toString(e.num),e.text, instance.format(e.num));
        }
        instance = CardinalFormat.getInstance(Locale.UK,CardinalFormat.LONG);
        for(T e: en_GB)
        {
            Assert.assertEquals(Long.toString(e.num),e.text, instance.format(e.num));
        }
    }
}
