/*
 * LocalesTest.java
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

import java.util.Locale;
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
public class LocalesTest
{
    
    public LocalesTest()
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
     * Test of getISO2Country method, of class Locales.
     */
    @Test
    public void testGetISO2Country()
    {
        assertNull(Locales.getISO2Country(null));
        assertNull(Locales.getISO2Country("garbaje not a country"));
        
        assertEquals("ES", Locales.getISO2Country("es"));
        assertEquals("ES", Locales.getISO2Country("ES"));
        assertEquals("ES", Locales.getISO2Country("esp"));
        assertEquals("ES", Locales.getISO2Country("ESP"));
        assertEquals("ES", Locales.getISO2Country("spain"));
        assertEquals("ES", Locales.getISO2Country("Spain"));
        assertEquals("ES", Locales.getISO2Country("SPAIN"));
        
        //Esto sólo funciona si el locale es español
        if(Locale.getDefault().getLanguage().equalsIgnoreCase("es"))
        {
            assertEquals("ES", Locales.getISO2Country("españa"));
            assertEquals("ES", Locales.getISO2Country("España"));
            assertEquals("ES", Locales.getISO2Country("ESPAÑA"));
        }

        Locale es = new Locale("es");
        
        for(int i=0;i<COUNTRIES.length;i++)
        {
            String name = COUNTRIES[i][0];
            String iso2 = COUNTRIES[i][1];
            String iso3 = COUNTRIES[i][2];
            String num3 = COUNTRIES[i][3];
            
            assertEquals(iso2, Locales.getISO2Country(iso2));
            assertEquals(iso2, Locales.getISO2Country(iso3));
            assertEquals(iso2, Locales.getISO2Country(name));
            assertEquals(iso2, Locales.getISO2Country(name.toLowerCase()));
            assertEquals(iso2, Locales.getISO2Country(name.toUpperCase()));
        }
    }

    /**
     * Test of getISO2Language method, of class Locales.
     */
    @Test
    public void testGetISO2Language()
    {
        assertNull(Locales.getISO2Language(null));
        assertNull(Locales.getISO2Language("garbaje not a language"));
        
        assertEquals("es", Locales.getISO2Language("es"));
        assertEquals("es", Locales.getISO2Language("spa"));
        assertEquals("es", Locales.getISO2Language("spanish"));
        assertEquals("es", Locales.getISO2Language("Spanish"));
        assertEquals("es", Locales.getISO2Language("SPANISH"));

        assertEquals("bo", Locales.getISO2Language("bo"));
        assertEquals("bo", Locales.getISO2Language("tib"));
        assertEquals("bo", Locales.getISO2Language("bod"));
        assertEquals("bo", Locales.getISO2Language("tibetan"));
        assertEquals("bo", Locales.getISO2Language("Tibetan"));
        assertEquals("bo", Locales.getISO2Language("TIBETAN"));
        
        //Esto sólo funciona si el locale es español
        if(Locale.getDefault().getLanguage().equalsIgnoreCase("es"))
        {
            assertEquals("es", Locales.getISO2Language("Español"));
            assertEquals("bo", Locales.getISO2Language("Tibetano"));
        }        
        
        for(int i=0;i<ISO_369.length;i++)
        {
            String old2 = ISO_369[i][0];
            String iso2 = ISO_369[i][1];
            String old3 = ISO_369[i][2];
            String iso3 = ISO_369[i][3];
            String name = ISO_369[i][4];
            
            assertEquals(name,iso2, Locales.getISO2Language(iso2));
            assertEquals(name,iso2, Locales.getISO2Language(iso3));
            if(!old2.equals(iso2))
            {
                assertEquals(name,iso2, Locales.getISO2Language(old2));
            }
            if(!old3.equals(iso3))
            {
                assertEquals(name,iso2, Locales.getISO2Language(old3));
            }
            
            assertEquals(name,iso2, Locales.getISO2Language(name));
            assertEquals(name,iso2, Locales.getISO2Language(name.toLowerCase()));
            assertEquals(name,iso2, Locales.getISO2Language(name.toUpperCase()));
        }
    }

    /**
     * Test of getISO3Country method, of class Locales.
     */
    @Test
    public void testGetISO3Country()
    {
        assertNull(Locales.getISO3Country(null));
        assertNull(Locales.getISO3Country("garbaje not a country"));
        
        assertEquals("ESP", Locales.getISO3Country("ES"));
        assertEquals("ESP", Locales.getISO3Country("es"));
        assertEquals("ESP", Locales.getISO3Country("spain"));
        assertEquals("ESP", Locales.getISO3Country("Spain"));
        assertEquals("ESP", Locales.getISO3Country("SPAIN"));
        
        for(int i=0;i<COUNTRIES.length;i++)
        {
            String name = COUNTRIES[i][0].toUpperCase();
            String iso2 = COUNTRIES[i][1].toUpperCase();
            String iso3 = COUNTRIES[i][2].toUpperCase();
            String num3 = COUNTRIES[i][3];
            
            assertEquals(iso3, Locales.getISO3Country(iso2));
            assertEquals(iso3, Locales.getISO3Country(iso3));
            assertEquals(iso3, Locales.getISO3Country(name));
            assertEquals(iso3, Locales.getISO3Country(name.toLowerCase()));
            assertEquals(iso3, Locales.getISO3Country(name.toUpperCase()));
        }
        
    }

    /**
     * Test of getISO3Language method, of class Locales.
     */
    @Test
    public void testGetISO3Language()
    {
        assertNull(Locales.getISO3Language(null));
        assertNull(Locales.getISO3Language("garbaje not a language"));
        
        assertEquals("spa", Locales.getISO3Language("es"));
        assertEquals("spa", Locales.getISO3Language("spa"));
        assertEquals("spa", Locales.getISO3Language("spanish"));
        assertEquals("spa", Locales.getISO3Language("Spanish"));

        assertEquals("bod", Locales.getISO3Language("bo"));
        assertEquals("bod", Locales.getISO3Language("tib"));
        assertEquals("bod", Locales.getISO3Language("bod"));
        assertEquals("bod", Locales.getISO3Language("Tibetan"));
        assertEquals("bod", Locales.getISO3Language("tibetan"));

        for(int i=0;i<ISO_369.length;i++)
        {
            String old2 = ISO_369[i][0];
            String iso2 = ISO_369[i][1];
            String old3 = ISO_369[i][3];
            String iso3 = ISO_369[i][3];
            String name = ISO_369[i][0];
            
            assertEquals(name,iso3, Locales.getISO3Language(old2));
            assertEquals(name,iso3, Locales.getISO3Language(iso2));
            assertEquals(name,iso3, Locales.getISO3Language(iso3));
            assertEquals(name,iso3, Locales.getISO3Language(old3));
            assertEquals(name,iso3, Locales.getISO3Language(name));
            assertEquals(name,iso3, Locales.getISO3Language(name.toLowerCase()));
            assertEquals(name,iso3, Locales.getISO3Language(name.toUpperCase()));
        }
    }

    /**
     * Test of getDisplayCountry method, of class Locales.
     */
    @Test
    public void testGetDisplayCountry_String_Locale()
    {
        assertNull(Locales.getDisplayCountry(null,Locale.US));
        assertNull(Locales.getDisplayCountry("garbaje not a country",Locale.US));
        
        Locale loc = new Locale("","ES","");
        assertEquals("España", Locales.getDisplayCountry("es",loc));
        assertEquals("España", Locales.getDisplayCountry("ES",loc));
        assertEquals("España", Locales.getDisplayCountry("spain",loc));
        assertEquals("España", Locales.getDisplayCountry("Spain",loc));
        assertEquals("España", Locales.getDisplayCountry("SPAIN",loc));
        assertEquals("España", Locales.getDisplayCountry("españa",loc));
        assertEquals("España", Locales.getDisplayCountry("España",loc));
        assertEquals("España", Locales.getDisplayCountry("ESPAÑA",loc));
        loc = Locale.US;
        assertEquals("Spain", Locales.getDisplayCountry("es",loc));
        assertEquals("Spain", Locales.getDisplayCountry("ES",loc));
        assertEquals("Spain", Locales.getDisplayCountry("spain",loc));
        assertEquals("Spain", Locales.getDisplayCountry("Spain",loc));
        assertEquals("Spain", Locales.getDisplayCountry("SPAIN",loc));
        assertEquals("Spain", Locales.getDisplayCountry("españa",loc));
        assertEquals("Spain", Locales.getDisplayCountry("España",loc));
        assertEquals("Spain", Locales.getDisplayCountry("ESPAÑA",loc));
        loc = Locale.ITALY;
        assertEquals("Spagna", Locales.getDisplayCountry("es",loc));
        assertEquals("Spagna", Locales.getDisplayCountry("ES",loc));
        assertEquals("Spagna", Locales.getDisplayCountry("spain",loc));
        assertEquals("Spagna", Locales.getDisplayCountry("Spain",loc));
        assertEquals("Spagna", Locales.getDisplayCountry("SPAIN",loc));
        assertEquals("Spagna", Locales.getDisplayCountry("españa",loc));
        assertEquals("Spagna", Locales.getDisplayCountry("España",loc));
        assertEquals("Spagna", Locales.getDisplayCountry("ESPAÑA",loc));
    }

    /**
     * Test of getDisplayCountry method, of class Locales.
     */
    @Test
    public void testGetDisplayCountry_String_String()
    {
        String loc = "es";
        assertEquals("España", Locales.getDisplayCountry("es",loc));
        assertEquals("España", Locales.getDisplayCountry("ES",loc));
        assertEquals("España", Locales.getDisplayCountry("spain",loc));
        assertEquals("España", Locales.getDisplayCountry("Spain",loc));
        assertEquals("España", Locales.getDisplayCountry("SPAIN",loc));
        assertEquals("España", Locales.getDisplayCountry("españa",loc));
        assertEquals("España", Locales.getDisplayCountry("España",loc));
        assertEquals("España", Locales.getDisplayCountry("ESPAÑA",loc));
        loc = Locale.US.getLanguage();
        assertEquals("Spain", Locales.getDisplayCountry("es",loc));
        assertEquals("Spain", Locales.getDisplayCountry("ES",loc));
        assertEquals("Spain", Locales.getDisplayCountry("spain",loc));
        assertEquals("Spain", Locales.getDisplayCountry("Spain",loc));
        assertEquals("Spain", Locales.getDisplayCountry("SPAIN",loc));
        assertEquals("Spain", Locales.getDisplayCountry("españa",loc));
        assertEquals("Spain", Locales.getDisplayCountry("España",loc));
        assertEquals("Spain", Locales.getDisplayCountry("ESPAÑA",loc));
        loc = Locale.ITALY.getLanguage();
        assertEquals("Spagna", Locales.getDisplayCountry("es",loc));
        assertEquals("Spagna", Locales.getDisplayCountry("ES",loc));
        assertEquals("Spagna", Locales.getDisplayCountry("spain",loc));
        assertEquals("Spagna", Locales.getDisplayCountry("Spain",loc));
        assertEquals("Spagna", Locales.getDisplayCountry("SPAIN",loc));
        assertEquals("Spagna", Locales.getDisplayCountry("españa",loc));
        assertEquals("Spagna", Locales.getDisplayCountry("España",loc));
        assertEquals("Spagna", Locales.getDisplayCountry("ESPAÑA",loc));
    }

    /**
     * Test of getDisplayLanguage method, of class Locales.
     */
    @Test
    public void testGetDisplayLanguage_String_Locale()
    {
        assertNull(Locales.getDisplayLanguage((String)null,Locale.US));
        assertNull(Locales.getDisplayLanguage("garbaje not a country",Locale.US));
        
        Locale loc = new Locale("ES","","");
        assertEquals("español", Locales.getDisplayLanguage("es",loc).toLowerCase());
        assertEquals("español", Locales.getDisplayLanguage("ES",loc).toLowerCase());
        assertEquals("español", Locales.getDisplayLanguage("spanish",loc).toLowerCase());
        assertEquals("español", Locales.getDisplayLanguage("Spanish",loc).toLowerCase());
        assertEquals("español", Locales.getDisplayLanguage("SPANISH",loc).toLowerCase());
        assertEquals("español", Locales.getDisplayLanguage("español",loc).toLowerCase());
        assertEquals("español", Locales.getDisplayLanguage("Español",loc).toLowerCase());
        assertEquals("español", Locales.getDisplayLanguage("ESPAÑOL",loc).toLowerCase());
        loc = Locale.US;
        assertEquals("spanish", Locales.getDisplayLanguage("es",loc).toLowerCase());
        assertEquals("spanish", Locales.getDisplayLanguage("ES",loc).toLowerCase());
        assertEquals("spanish", Locales.getDisplayLanguage("spanish",loc).toLowerCase());
        assertEquals("spanish", Locales.getDisplayLanguage("Spanish",loc).toLowerCase());
        assertEquals("spanish", Locales.getDisplayLanguage("SPANISH",loc).toLowerCase());
        assertEquals("spanish", Locales.getDisplayLanguage("español",loc).toLowerCase());
        assertEquals("spanish", Locales.getDisplayLanguage("Español",loc).toLowerCase());
        assertEquals("spanish", Locales.getDisplayLanguage("ESPAÑOL",loc).toLowerCase());
        loc = Locale.ITALY;
        assertEquals("spagnolo", Locales.getDisplayLanguage("es",loc).toLowerCase());
        assertEquals("spagnolo", Locales.getDisplayLanguage("ES",loc).toLowerCase());
        assertEquals("spagnolo", Locales.getDisplayLanguage("spanish",loc).toLowerCase());
        assertEquals("spagnolo", Locales.getDisplayLanguage("Spanish",loc).toLowerCase());
        assertEquals("spagnolo", Locales.getDisplayLanguage("SPANISH",loc).toLowerCase());
        assertEquals("spagnolo", Locales.getDisplayLanguage("español",loc).toLowerCase());
        assertEquals("spagnolo", Locales.getDisplayLanguage("Español",loc).toLowerCase());
        assertEquals("spagnolo", Locales.getDisplayLanguage("ESPAÑOL",loc).toLowerCase());
    }

    /**
     * Test of getDisplayLanguage method, of class Locales.
     */
    @Test
    public void testGetDisplayLanguage_String_String()
    {
        Locale loc = new Locale("","ES","");
        assertEquals("español", Locales.getDisplayLanguage("es",loc).toLowerCase());
        assertEquals("español", Locales.getDisplayLanguage("ES",loc).toLowerCase());
        assertEquals("español", Locales.getDisplayLanguage("spanish",loc).toLowerCase());
        assertEquals("español", Locales.getDisplayLanguage("Spanish",loc).toLowerCase());
        assertEquals("español", Locales.getDisplayLanguage("SPANISH",loc).toLowerCase());
        assertEquals("español", Locales.getDisplayLanguage("español",loc).toLowerCase());
        assertEquals("español", Locales.getDisplayLanguage("Español",loc).toLowerCase());
        assertEquals("español", Locales.getDisplayLanguage("ESPAÑOL",loc).toLowerCase());
        loc = Locale.US;
        assertEquals("spanish", Locales.getDisplayLanguage("es",loc).toLowerCase());
        assertEquals("spanish", Locales.getDisplayLanguage("ES",loc).toLowerCase());
        assertEquals("spanish", Locales.getDisplayLanguage("spanish",loc).toLowerCase());
        assertEquals("spanish", Locales.getDisplayLanguage("Spanish",loc).toLowerCase());
        assertEquals("spanish", Locales.getDisplayLanguage("SPANISH",loc).toLowerCase());
        assertEquals("spanish", Locales.getDisplayLanguage("español",loc).toLowerCase());
        assertEquals("spanish", Locales.getDisplayLanguage("Español",loc).toLowerCase());
        assertEquals("spanish", Locales.getDisplayLanguage("ESPAÑOL",loc).toLowerCase());
        loc = Locale.ITALY;
        assertEquals("spagnolo", Locales.getDisplayLanguage("es",loc).toLowerCase());
        assertEquals("spagnolo", Locales.getDisplayLanguage("ES",loc).toLowerCase());
        assertEquals("spagnolo", Locales.getDisplayLanguage("spanish",loc).toLowerCase());
        assertEquals("spagnolo", Locales.getDisplayLanguage("Spanish",loc).toLowerCase());
        assertEquals("spagnolo", Locales.getDisplayLanguage("SPANISH",loc).toLowerCase());
        assertEquals("spagnolo", Locales.getDisplayLanguage("español",loc).toLowerCase());
        assertEquals("spagnolo", Locales.getDisplayLanguage("Español",loc).toLowerCase());
        assertEquals("spagnolo", Locales.getDisplayLanguage("ESPAÑOL",loc).toLowerCase());
    }

    static final String[][] COUNTRIES=
    {
        //only non problematic countries
        //Country, A2, A3, Number
        {"AFGHANISTAN","AF","AFG","004"},
        {"ALBANIA","AL", "ALB", "008"},
        {"ALGERIA","DZ", "DZA", "012"},
        {"AMERICAN SAMOA", "AS", "ASM", "016"},
        {"ANDORRA","AD", "AND", "020"},
        {"ANGOLA","AO", "AGO", "024"},
        {"ANGUILLA","AI", "AIA", "660"},
        {"ANTARCTICA","AQ", "ATA", "010"},
        {"ANTIGUA AND BARBUDA","AG", "ATG", "028"},
        {"ARGENTINA","AR", "ARG", "032"},
        {"ARMENIA","AM", "ARM", "051 "},
        {"ARUBA","AW", "ABW", "533"},
        {"AUSTRALIA","AU", "AUS", "036"},
        {"AUSTRIA","AT", "AUT", "040"},
        {"AZERBAIJAN","AZ", "AZE", "031 "},
        {"BAHAMAS","BS", "BHS", "044"},
        {"BAHRAIN","BH", "BHR", "048"},
        {"BANGLADESH","BD", "BGD", "050"},
        {"BARBADOS","BB", "BRB", "052"},
        {"BELARUS","BY", "BLR", "112 "},
        {"BELGIUM","BE", "BEL", "056"},
        {"BELIZE","BZ", "BLZ", "084"},
        {"BENIN","BJ", "BEN", "204"},
        {"BERMUDA","BM", "BMU", "060"},
        {"BHUTAN","BT", "BTN", "064"},
        {"BOLIVIA","BO", "BOL", "068"},
        {"BOTSWANA","BW", "BWA", "072"},
        {"BOUVET ISLAND","BV", "BVT", "074"},
        {"BRAZIL","BR", "BRA", "076"},
        {"BRITISH INDIAN OCEAN TERRITORY","IO", "IOT", "086"},
        {"BULGARIA","BG", "BGR", "100"},
        {"BURKINA FASO","BF", "BFA", "854"},
        {"BURUNDI","BI", "BDI", "108"},
        {"CAMBODIA","KH", "KHM", "116"},
        {"CAMEROON","CM", "CMR", "120"},
        {"CANADA","CA", "CAN", "124"},
        {"CAPE VERDE","CV", "CPV", "132"},
        {"CAYMAN ISLANDS","KY", "CYM", "136"},
        {"CENTRAL AFRICAN REPUBLIC","CF", "CAF", "140"},
        {"CHAD","TD", "TCD", "148"},
        {"CHILE","CL", "CHL", "152"},
        {"CHINA","CN", "CHN", "156"},
        {"CHRISTMAS ISLAND","CX", "CXR", "162"},
        {"COLOMBIA","CO", "COL", "170"},
        {"COMOROS","KM", "COM", "174"},
        {"CONGO","CG", "COG", "178"},
        {"COOK ISLANDS","CK", "COK", "184"},
        {"COSTA RICA","CR", "CRI", "188"},
        {"CUBA","CU", "CUB", "192"},
        {"CYPRUS","CY", "CYP", "196"},
        {"CZECH REPUBLIC","CZ", "CZE", "203 "},
        {"DENMARK","DK", "DNK", "208"},
        {"DJIBOUTI","DJ", "DJI", "262"},
        {"DOMINICA","DM", "DMA", "212"},
        {"DOMINICAN REPUBLIC","DO", "DOM", "214"},
        {"ECUADOR","EC", "ECU", "218"},
        {"EGYPT","EG", "EGY", "818"},
        {"EL SALVADOR","SV", "SLV", "222"},
        {"EQUATORIAL GUINEA","GQ", "GNQ", "226"},
        {"ERITREA","ER", "ERI", "232"},
        {"ESTONIA","EE", "EST", "233 "},
        {"ETHIOPIA","ET", "ETH", "231"},
        {"FAROE ISLANDS","FO", "FRO", "234"},
        {"FIJI","FJ", "FJI", "242"},
        {"FINLAND","FI", "FIN", "246"},
        {"FRANCE","FR", "FRA", "250"},
        {"FRENCH GUIANA","GF", "GUF", "254"},
        {"FRENCH POLYNESIA","PF", "PYF", "258"},
        {"FRENCH SOUTHERN TERRITORIES","TF", "ATF", "260"},
        {"GABON","GA", "GAB", "266"},
        {"GAMBIA","GM", "GMB", "270"},
        {"GEORGIA","GE", "GEO", "268 "},
        {"GERMANY","DE", "DEU", "276"},
        {"GHANA","GH", "GHA", "288"},
        {"GIBRALTAR","GI", "GIB", "292"},
        {"GREECE","GR", "GRC", "300"},
        {"GREENLAND","GL", "GRL", "304"},
        {"GRENADA","GD", "GRD", "308"},
        {"GUADELOUPE","GP", "GLP", "312"},
        {"GUAM","GU", "GUM", "316"},
        {"GUATEMALA","GT", "GTM", "320"},
        {"GUINEA","GN", "GIN", "324"},
        {"GUINEA-BISSAU","GW", "GNB", "624"},
        {"GUYANA","GY", "GUY", "328"},
        {"HAITI","HT", "HTI", "332"},
        {"HONDURAS","HN", "HND", "340"},
        {"HONG KONG","HK", "HKG", "344"},
        {"HUNGARY","HU", "HUN", "348"},
        {"ICELAND","IS", "ISL", "352"},
        {"INDIA","IN", "IND", "356"},
        {"INDONESIA","ID", "IDN", "360"},
        {"IRAQ","IQ", "IRQ", "368"},
        {"IRELAND","IE", "IRL", "372"},
        {"ISRAEL","IL", "ISR", "376"},
        {"ITALY","IT", "ITA", "380"},
        {"JAMAICA","JM", "JAM", "388"},
        {"JAPAN","JP", "JPN", "392"},
        {"JORDAN","JO", "JOR", "400"},
        {"KAZAKHSTAN","KZ", "KAZ", "398  "},
        {"KENYA","KE", "KEN", "404"},
        {"KIRIBATI","KI", "KIR", "296"},
        {"KUWAIT","KW", "KWT", "414"},
        {"KYRGYZSTAN","KG", "KGZ", "417 "},
        {"LATVIA","LV", "LVA", "428 "},
        {"LEBANON","LB", "LBN", "422"},
        {"LESOTHO","LS", "LSO", "426"},
        {"LIBERIA","LR", "LBR", "430"},
        {"LIECHTENSTEIN","LI", "LIE", "438"},
        {"LITHUANIA","LT", "LTU", "440 "},
        {"MADAGASCAR","MG", "MDG", "450"},
        {"MALAWI","MW", "MWI", "454"},
        {"MALAYSIA","MY", "MYS", "458"},
        {"MALDIVES","MV", "MDV", "462"},
        {"MALI","ML", "MLI", "466"},
        {"MALTA","MT", "MLT", "470"},
        {"MARSHALL ISLANDS","MH", "MHL", "584"},
        {"MARTINIQUE","MQ", "MTQ", "474"},
        {"MAURITANIA","MR", "MRT", "478"},
        {"MAURITIUS","MU", "MUS", "480"},
        {"MAYOTTE","YT", "MYT", "175 "},
        {"MEXICO","MX", "MEX", "484"},
        {"MONACO","MC", "MCO", "492"},
        {"MONGOLIA","MN", "MNG", "496"},
        {"MONTSERRAT","MS", "MSR", "500"},
        {"MOROCCO","MA", "MAR", "504"},
        {"MOZAMBIQUE","MZ", "MOZ", "508"},
        {"MYANMAR","MM", "MMR", "104"},
        {"NAMIBIA","NA", "NAM", "516"},
        {"NAURU","NR", "NRU", "520"},
        {"NEPAL","NP", "NPL", "524"},
        {"NETHERLANDS","NL", "NLD", "528"},
        {"NETHERLANDS ANTILLES","AN", "ANT", "530"},
        {"NEW CALEDONIA","NC", "NCL", "540"},
        {"NEW ZEALAND","NZ", "NZL", "554"},
        {"NICARAGUA","NI", "NIC", "558"},
        {"NIGER","NE", "NER", "562"},
        {"NIGERIA","NG", "NGA", "566"},
        {"NIUE","NU", "NIU", "570"},
        {"NORFOLK ISLAND","NF", "NFK", "574"},
        {"NORTHERN MARIANA ISLANDS","MP", "MNP", "580"},
        {"NORWAY","NO", "NOR", "578"},
        {"OMAN","OM", "OMN", "512"},
        {"PAKISTAN","PK", "PAK", "586"},
        {"PALAU","PW", "PLW", "585"},
        {"PANAMA","PA", "PAN", "591"},
        {"PAPUA NEW GUINEA","PG", "PNG", "598"},
        {"PARAGUAY","PY", "PRY", "600"},
        {"PERU","PE", "PER", "604"},
        {"PHILIPPINES","PH", "PHL", "608"},
        {"PITCAIRN","PN", "PCN", "612"},
        {"POLAND","PL", "POL", "616"},
        {"PORTUGAL","PT", "PRT", "620"},
        {"PUERTO RICO","PR", "PRI", "630"},
        {"QATAR","QA", "QAT", "634"},
        {"REUNION","RE", "REU", "638"},
        {"RWANDA","RW", "RWA", "646"},
        {"SAINT KITTS AND NEVIS","KN", "KNA", "659"},
        {"SAINT LUCIA","LC", "LCA", "662"},
        {"SAINT VINCENT AND THE GRENADINES","VC", "VCT", "670"},
        {"SAMOA","WS", "WSM", "882"},
        {"SAN MARINO","SM", "SMR", "674"},
        {"SAO TOME AND PRINCIPE","ST", "STP", "678"},
        {"SAUDI ARABIA","SA", "SAU", "682"},
        {"SENEGAL","SN", "SEN", "686"},
        {"SEYCHELLES","SC", "SYC", "690"},
        {"SIERRA LEONE","SL", "SLE", "694"},
        {"SINGAPORE","SG", "SGP", "702"},
        {"SLOVENIA","SI", "SVN", "705 "},
        {"SOLOMON ISLANDS","SB", "SLB", "090"},
        {"SOMALIA","SO", "SOM", "706"},
        {"SOUTH AFRICA","ZA", "ZAF", "710"},
        {"SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS","GS", "SGS", "239"},
        {"SPAIN","ES", "ESP", "724"},
        {"SRI LANKA","LK", "LKA", "144"},
        {"SUDAN","SD", "SDN", "736"},
        {"SURINAME","SR", "SUR", "740"},
        {"SWAZILAND","SZ", "SWZ", "748"},
        {"SWEDEN","SE", "SWE", "752"},
        {"SWITZERLAND","CH", "CHE", "756"},
        {"TAJIKISTAN","TJ", "TJK", "762 "},
        {"THAILAND","TH", "THA", "764"},
        {"TOGO","TG", "TGO", "768"},
        {"TOKELAU","TK", "TKL", "772"},
        {"TONGA","TO", "TON", "776"},
        {"TRINIDAD AND TOBAGO","TT", "TTO", "780"},
        {"TUNISIA","TN", "TUN", "788"},
        {"TURKEY","TR", "TUR", "792"},
        {"TURKMENISTAN","TM", "TKM", "795 "},
        {"TURKS AND CAICOS ISLANDS","TC", "TCA", "796"},
        {"TUVALU","TV", "TUV", "798"},
        {"UGANDA","UG", "UGA", "800"},
        {"UKRAINE","UA", "UKR", "804"},
        {"UNITED ARAB EMIRATES","AE", "ARE", "784"},
        {"UNITED KINGDOM","GB", "GBR", "826"},
        {"UNITED STATES","US", "USA", "840"},
        {"UNITED STATES MINOR OUTLYING ISLANDS","UM", "UMI", "581"},
        {"URUGUAY","UY", "URY", "858"},
        {"UZBEKISTAN","UZ", "UZB", "860 "},
        {"VANUATU","VU", "VUT", "548"},
        {"VENEZUELA","VE", "VEN", "862"},
        {"WESTERN SAHARA","EH", "ESH", "732"},
        {"YEMEN","YE", "YEM", "887"},
        {"ZAMBIA","ZM", "ZMB", "894"},
        {"ZIMBABWE","ZW", "ZWE", "716"},
    };
    static final String[][] ISO_369=
    {
        //old639-1,old639-1,639-2B,639-3, Language Name (English)
        {"aa", "aa", "aar", "aar", "Afar"},
        {"ab", "ab", "abk", "abk", "Abkhazian"},        
        {"ae", "ae", "ave", "ave", "Avestan"},        
        {"af", "af", "afr", "afr", "Afrikaans"},        
        {"ak", "ak", "aka", "aka", "Akan"},        
        {"am", "am", "amh", "amh", "Amharic"},        
        {"an", "an", "arg", "arg", "Aragonese"},        
        {"ar", "ar", "ara", "ara", "Arabic"},        
        {"as", "as", "asm", "asm", "Assamese"},        
        {"av", "av", "ava", "ava", "Avaric"},        
        {"ay", "ay", "aym", "aym", "Aymara"},        
        {"az", "az", "aze", "aze", "Azerbaijani"},        
        {"ba", "ba", "bak", "bak", "Bashkir"},        
        {"be", "be", "bel", "bel", "Belarusian"},        
        {"bg", "bg", "bul", "bul", "Bulgarian"},        
        {"bi", "bi", "bis", "bis", "Bislama"},
        {"bm", "bm", "bam", "bam", "Bambara"},
        {"bn", "bn", "ben", "ben", "Bengali"},
        {"bo", "bo", "tib", "bod", "Tibetan"},
        {"br", "br", "bre", "bre", "Breton"},
        {"bs", "bs", "bos", "bos", "Bosnian"},
        {"ca", "ca", "cat", "cat", "Catalan"},
        {"ce", "ce", "che", "che", "Chechen"},
        {"ch", "ch", "cha", "cha", "Chamorro"},
        {"co", "co", "cos", "cos", "Corsican"},
        {"cr", "cr", "cre", "cre", "Cree"},
        {"cs", "cs", "cze", "ces", "Czech"},
        {"cu", "cu", "chu", "chu", "Church Slavic"},
        {"cv", "cv", "chv", "chv", "Chuvash"},
        {"cy", "cy", "wel", "cym", "Welsh"},
        {"da", "da", "dan", "dan", "Danish"},
        {"de", "de", "ger", "deu", "German"},
        {"dv", "dv", "div", "div", "Divehi"},
        {"dv", "dv", "div", "div", "Dhivehi"},
        {"dz", "dz", "dzo", "dzo", "Dzongkha"},
        {"ee", "ee", "ewe", "ewe", "Ewe"},
        {"el", "el", "gre", "ell", "Greek"},// (1453-)
        {"en", "en", "eng", "eng", "English"},
        {"eo", "eo", "epo", "epo", "Esperanto"},
        {"es", "es", "spa", "spa", "Spanish"},
        {"et", "et", "est", "est", "Estonian"},
        {"eu", "eu", "baq", "eus", "Basque"},
        {"fa", "fa", "per", "fas", "Persian"},
        {"ff", "ff", "ful", "ful", "Fulah"},
        {"fi", "fi", "fin", "fin", "Finnish"},
        {"fj", "fj", "fij", "fij", "Fijian"},
        {"fo", "fo", "fao", "fao", "Faroese"},
        {"fr", "fr", "fre", "fra", "French"},
        {"fr", "fr", "fre", "fra", "français"},
        {"fy", "fy", "fry", "fry", "Frisian"},
        {"ga", "ga", "gle", "gle", "Irish"},
        {"gd", "gd", "gla", "gla", "Scottish Gaelic"},
        {"gl", "gl", "glg", "glg", "Galician"},
        {"gl", "gl", "glg", "glg", "Gallegan"},
        {"gn", "gn", "grn", "grn", "Guarani"},
        {"gu", "gu", "guj", "guj", "Gujarati"},
        {"gv", "gv", "glv", "glv", "Manx"},
        {"ha", "ha", "hau", "hau", "Hausa"},

        {"he", "iw", "heb", "heb", "Hebrew"},
        {"iw", "iw", "heb", "heb", "Hebrew"},
        
        {"hi", "hi", "hin", "hin", "Hindi"},
        {"ho", "ho", "hmo", "hmo", "Hiri Motu"},
        {"hr", "hr", "hrv", "hrv", "Croatian"},
        {"ht", "ht", "hat", "hat", "Haitian"},
        {"hu", "hu", "hun", "hun", "Hungarian"},
        {"hy", "hy", "arm", "hye", "Armenian"},
        {"hz", "hz", "her", "her", "Herero"},
        {"ia", "ia", "ina", "ina", "Interlingua"},
        
        {"id", "in", "ind", "ind", "Indonesian"},
        {"in", "in", "ind", "ind", "Indonesian"},
        
        {"ie", "ie", "ile", "ile", "Interlingue"},
        {"ig", "ig", "ibo", "ibo", "Igbo"},
        {"ii", "ii", "iii", "iii", "Sichuan Yi"},
        {"ik", "ik", "ipk", "ipk", "Inupiaq"},
        {"io", "io", "ido", "ido", "Ido"},
        {"is", "is", "ice", "isl", "Icelandic"},
        {"it", "it", "ita", "ita", "Italian"},
        {"iu", "iu", "iku", "iku", "Inuktitut"},
        {"ja", "ja", "jpn", "jpn", "Japanese"},
        {"jv", "jv", "jav", "jav", "Javanese"},
        {"ka", "ka", "geo", "kat", "Georgian"},
        {"kg", "kg", "kon", "kon", "Kongo"},
        {"ki", "ki", "kik", "kik", "Kikuyu"},
        {"kj", "kj", "kua", "kua", "Kuanyama"},
        {"kj", "kj", "kua", "kua", "Kwanyama"},
        {"kk", "kk", "kaz", "kaz", "Kazakh"},
        {"kl", "kl", "kal", "kal", "Kalaallisut"},
        {"km", "km", "khm", "khm", "Khmer"},
        {"kn", "kn", "kan", "kan", "Kannada"},
        {"ko", "ko", "kor", "kor", "Korean"},
        {"kr", "kr", "kau", "kau", "Kanuri"},
        {"ks", "ks", "kas", "kas", "Kashmiri"},
        {"ku", "ku", "kur", "kur", "Kurdish"},
        {"kv", "kv", "kom", "kom", "Komi"},
        {"kw", "kw", "cor", "cor", "Cornish"},
        {"ky", "ky", "kir", "kir", "Kirghiz"},
        {"la", "la", "lat", "lat", "Latin"},
        {"lb", "lb", "ltz", "ltz", "Luxembourgish"},
        {"lg", "lg", "lug", "lug", "Ganda"},
        {"li", "li", "lim", "lim", "Limburgan"},
        {"ln", "ln", "lin", "lin", "Lingala"},
        {"lo", "lo", "lao", "lao", "Lao"},
        {"lt", "lt", "lit", "lit", "Lithuanian"},
        {"lu", "lu", "lub", "lub", "Luba-Katanga"},
        {"lv", "lv", "lav", "lav", "Latvian"},
        {"mg", "mg", "mlg", "mlg", "Malagasy"},
        {"mh", "mh", "mah", "mah", "Marshallese"},
        {"mi", "mi", "mao", "mri", "Maori"},
        {"mk", "mk", "mac", "mkd", "Macedonian"},
        {"ml", "ml", "mal", "mal", "Malayalam"},
        {"mn", "mn", "mon", "mon", "Mongolian"},
        {"mr", "mr", "mar", "mar", "Marathi"},
        {"ms", "ms", "may", "msa", "Malay"},
        {"mt", "mt", "mlt", "mlt", "Maltese"},
        {"my", "my", "bur", "mya", "Burmese"},
        {"na", "na", "nau", "nau", "Nauru"},
        {"nb", "nb", "nob", "nob", "Norwegian Bokmål"},
        {"nd", "nd", "nde", "nde", "North Ndebele"},
        {"ne", "ne", "nep", "nep", "Nepali"},
        {"ng", "ng", "ndo", "ndo", "Ndonga"},
        {"nl", "nl", "dut", "nld", "Dutch"},
        {"nn", "nn", "nno", "nno", "Norwegian Nynorsk"},
        {"no", "no", "nor", "nor", "Norwegian"},
        {"nr", "nr", "nbl", "nbl", "South Ndebele"},
        {"nv", "nv", "nav", "nav", "Navajo"},
        {"ny", "ny", "nya", "nya", "Nyanja"},
        {"oc", "oc", "oci", "oci", "Occitan"},// (post 1500)
        {"oj", "oj", "oji", "oji", "Ojibwa"},
        {"om", "om", "orm", "orm", "Oromo"},
        {"or", "or", "ori", "ori", "Oriya"},
        {"os", "os", "oss", "oss", "Ossetian"},
        {"pa", "pa", "pan", "pan", "Panjabi"},
        {"pi", "pi", "pli", "pli", "Pali"},
        {"pl", "pl", "pol", "pol", "Polish"},
        {"ps", "ps", "pus", "pus", "Pushto"},
        {"pt", "pt", "por", "por", "Portuguese"},
        {"qu", "qu", "que", "que", "Quechua"},
        {"rm", "rm", "roh", "roh", "Romansh"},
        {"rn", "rn", "run", "run", "Rundi"},
        {"ro", "ro", "rum", "ron", "Romanian"},
        {"ru", "ru", "rus", "rus", "Russian"},
        {"rw", "rw", "kin", "kin", "Kinyarwanda"},
        {"sa", "sa", "san", "san", "Sanskrit"},
        {"sc", "sc", "srd", "srd", "Sardinian"},
        {"sd", "sd", "snd", "snd", "Sindhi"},
        {"se", "se", "sme", "sme", "Northern Sami"},
        {"sg", "sg", "sag", "sag", "Sango"},
        {"si", "si", "sin", "sin", "Sinhala"},
        {"sk", "sk", "slo", "slk", "Slovak"},
        {"sl", "sl", "slv", "slv", "Slovenian"},
        {"sm", "sm", "smo", "smo", "Samoan"},
        {"sn", "sn", "sna", "sna", "Shona"},
        {"so", "so", "som", "som", "Somali"},
        {"sq", "sq", "alb", "sqi", "Albanian"},
        {"sr", "sr", "srp", "srp", "Serbian"},
        {"ss", "ss", "ssw", "ssw", "Swati"},
        {"st", "st", "sot", "sot", "Southern Sotho"},
        {"su", "su", "sun", "sun", "Sundanese"},
        {"sv", "sv", "swe", "swe", "Swedish"},
        {"sw", "sw", "swa", "swa", "Swahili"},
        {"ta", "ta", "tam", "tam", "Tamil"},
        {"te", "te", "tel", "tel", "Telugu"},
        {"tg", "tg", "tgk", "tgk", "Tajik"},
        {"th", "th", "tha", "tha", "Thai"},
        {"ti", "ti", "tir", "tir", "Tigrinya"},
        {"tk", "tk", "tuk", "tuk", "Turkmen"},
        {"tl", "tl", "tgl", "tgl", "Tagalog"},
        {"tn", "tn", "tsn", "tsn", "Tswana"},
        {"to", "to", "ton", "ton", "Tonga"},
        {"tr", "tr", "tur", "tur", "Turkish"},
        {"ts", "ts", "tso", "tso", "Tsonga"},
        {"tt", "tt", "tat", "tat", "Tatar"},
        {"tw", "tw", "twi", "twi", "Twi"},
        {"ty", "ty", "tah", "tah", "Tahitian"},
        {"ug", "ug", "uig", "uig", "Uighur"},
        {"uk", "uk", "ukr", "ukr", "Ukrainian"},
        {"ur", "ur", "urd", "urd", "Urdu"},
        {"uz", "uz", "uzb", "uzb", "Uzbek"},
        {"ve", "ve", "ven", "ven", "Venda"},
        {"vi", "vi", "vie", "vie", "Vietnamese"},
        {"vo", "vo", "vol", "vol", "Volapük"},
        {"wa", "wa", "wln", "wln", "Walloon"},
        {"wo", "wo", "wol", "wol", "Wolof"},
        {"xh", "xh", "xho", "xho", "Xhosa"},
        
        {"ji", "ji", "yid", "yid", "Yiddish"},
        {"yi", "ji", "yid", "yid", "Yiddish"},
        
        {"yo", "yo", "yor", "yor", "Yoruba"},
        {"za", "za", "zha", "zha", "Zhuang"},
        {"zh", "zh", "chi", "zho", "Chinese"},
        {"zu", "zu", "zul", "zul", "Zulu"}
    };

    /**
     * Test of getDisplayLanguageCountry method, of class Locales.
     */
    @Test
    public void testGetDisplayLanguageCountry_Locale_Locale()
    {
        Locale es = new Locale("es");
        Locale en = Locale.ENGLISH;

        Locale es_ES = new Locale("es","ES");
        Locale es_AR = new Locale("es","AR");
        Locale en_US = Locale.US;
        Locale en_GB = Locale.UK;

        assertEquals("español", Locales.getDisplayLanguageCountry(es, null).toLowerCase());
        assertEquals("español", Locales.getDisplayLanguageCountry(es, es).toLowerCase());
        assertEquals("spanish", Locales.getDisplayLanguageCountry(es, en).toLowerCase());

        assertEquals("english", Locales.getDisplayLanguageCountry(en, null).toLowerCase());
        assertEquals("english", Locales.getDisplayLanguageCountry(en, en).toLowerCase());
        assertEquals("inglés", Locales.getDisplayLanguageCountry(en, es).toLowerCase());
        
        assertEquals("español (españa)", Locales.getDisplayLanguageCountry(es_ES, null).toLowerCase());
        assertEquals("español (españa)", Locales.getDisplayLanguageCountry(es_ES, es).toLowerCase());
        assertEquals("spanish (spain)", Locales.getDisplayLanguageCountry(es_ES, en).toLowerCase());
        
        assertEquals("español (argentina)", Locales.getDisplayLanguageCountry(es_AR, null).toLowerCase());
        assertEquals("español (argentina)", Locales.getDisplayLanguageCountry(es_AR, es).toLowerCase());
        assertEquals("spanish (argentina)", Locales.getDisplayLanguageCountry(es_AR, en).toLowerCase());
        
        assertEquals("english (united states)", Locales.getDisplayLanguageCountry(en_US, null).toLowerCase());
        assertEquals("english (united states)", Locales.getDisplayLanguageCountry(en_US, en).toLowerCase());
        assertEquals("inglés (estados unidos)", Locales.getDisplayLanguageCountry(en_US, es).toLowerCase());
        
        assertEquals("english (united kingdom)", Locales.getDisplayLanguageCountry(en_GB, null).toLowerCase());
        assertEquals("english (united kingdom)", Locales.getDisplayLanguageCountry(en_GB, en).toLowerCase());
        assertEquals("inglés (reino unido)", Locales.getDisplayLanguageCountry(en_GB, es).toLowerCase());
    }

    /**
     * Test of getDisplayLanguageCountry method, of class Locales.
     */
    @Test
    public void testGetDisplayLanguageCountry_LocaleArr()
    {
        Locale[] locales = { new Locale("es"), Locale.ENGLISH, new Locale("es","ES"), new Locale("es","AR"), Locale.US, Locale.UK};
        String[] expected= {"español", "english", "español (españa)", "español (argentina)", "english (united states)", "english (united kingdom)"};
        String[] results = Locales.getDisplayLanguageCountry(locales);
        
        assertEquals(expected.length,results.length);
        
        for(int i=0;i<expected.length;i++)
        {
            assertEquals(expected[i], results[i].toLowerCase());
        }
    }

    /**
     * Test of getDisplayLanguageCountry method, of class Locales.
     */
    @Test
    public void testGetDisplayLanguageCountry_LocaleArr_Locale()
    {
        Locale[] locales = { new Locale("es"), Locale.ENGLISH, new Locale("es","ES"), new Locale("es","AR"), Locale.US, Locale.UK};
        String[] expected= {"spanish", "english", "spanish (spain)", "spanish (argentina)", "english (united states)", "english (united kingdom)"};
        String[] results = Locales.getDisplayLanguageCountry(locales, Locale.US);
        
        assertEquals(expected.length,results.length);
        
        for(int i=0;i<expected.length;i++)
        {
            assertEquals(expected[i], results[i].toLowerCase());
        }
    }

    /**
     * Test of getDisplayLanguageCountry method, of class Locales.
     */
    @Test
    public void testGetDisplayLanguageCountry_Locale()
    {
        Locale es = new Locale("es");
        Locale en = Locale.ENGLISH;

        Locale es_ES = new Locale("es","ES");
        Locale es_AR = new Locale("es","AR");
        Locale en_US = Locale.US;
        Locale en_GB = Locale.UK;

        assertEquals("español", Locales.getDisplayLanguageCountry(es).toLowerCase());
        assertEquals("english", Locales.getDisplayLanguageCountry(en).toLowerCase());
        assertEquals("español (españa)", Locales.getDisplayLanguageCountry(es_ES).toLowerCase());
        assertEquals("español (argentina)", Locales.getDisplayLanguageCountry(es_AR).toLowerCase());
        assertEquals("english (united states)", Locales.getDisplayLanguageCountry(en_US).toLowerCase());
        assertEquals("english (united kingdom)", Locales.getDisplayLanguageCountry(en_GB).toLowerCase());
    }

    /**
     * Test of setDefault method, of class Locales.
     */
    @Test
    public void testSetDefault()
    {
        final Locale original = Locale.getDefault();
        final Locale tested1  = original==Locale.US?Locale.UK:Locale.US;
        final Locale tested2  = original==Locale.FRANCE?Locale.UK:Locale.FRANCE;
        
        Locales.setDefault(tested1);
        assertEquals(tested1, Locale.getDefault());

        Locales.setDefault(tested2);
        assertEquals(tested2, Locale.getDefault());

        Locales.setDefault(null);
        assertEquals(original, Locale.getDefault());
        
    }

    /**
     * Test of getDisplayLanguageNative method, of class Locales.
     */
    @Test
    public void testGetDisplayLanguageNative_String()
    {
        String[] iso2 = Locale.getISOLanguages();
        for(String code : iso2)
        {
            String expected = Locales.getDisplayLanguage(code,code);
            String result = Locales.getDisplayLanguageNative(code);
            assertEquals(expected, result);    
        }
    }

    /**
     * Test of getDisplayLanguageNative method, of class Locales.
     */
    @Test
    public void testGetDisplayLanguageNative_StringArr()
    {
        String[] iso2 = Locale.getISOLanguages();
        String[] expected = new String[iso2.length];
        for(int i=0;i<iso2.length;i++)
        {
            expected[i] = Locales.getDisplayLanguageNative(iso2[i]);
        }
        String[] result = Locales.getDisplayLanguageNative(iso2);
        assertArrayEquals(expected, result);
    }
}
