/*
 * GutenbergLanguageClassifierTest.java
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
package com.softenido.gutenberg;

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
public class GutenbergLanguageClassifierTest
{
    final static String[][] data =
    {
        {"spa", "¿Qué es poesía? Dices mientras clavas en mi pupila tu pupila azul; ¿Qué es poesía...? ¿Y tú me lo preguntas? ¡Poesía... eres tú!"},//Qué es poesía? (GustavoAdolfoBécquer)
        {"spa", "Con diez cañones por banda, viento en popa a toda vela, no corta el mar, sino vuela un velero bergantín; bajel pirata que llaman, por su bravura, el Temido, en todo mar conocido del uno al otro confín. La luna en el mar riela, en la lona gime el viento y alza en blando..."},//La canción del pirata (José Espronceda).
        {"eng", "As I was going over the Cork and Kerry Mountains I saw Captain Farrell and his money he was countin' I first produced my pistol and then produced my rapier I said 'Stand and deliver or the devil he may take ya' I took all of his money and it was a pretty penny I took all of his money yeah and I brought it home to Molly She swore that she loved me no never would she leave me But the devil take that woman, yeah, for you know she tricked me easy "}, //Wisky in a jar
        {"eng", "Tonight We are young So let's set the world on fire We can burn brighter than the sun"}, //Fun - We are young
        {"eng", "Geek and Gamer girls We’re unbelievable Comic books And Manga in stock Hasbro toys So rare We got the mail-away Ooooooh oh ooooooh Geek and Gamer girls We’re undefeatable FPS Achievement unlocked Jedis represent Now put your sabers up Oooooooh oh ooooooh"},//Geek & Gamer Girls
        {"fra", "Voyage, voyage Plus loin que la nuit et le jour, (voyage voyage) Voyage (voyage) Dans l'espace inouï de l'amour. Voyage, voyage Sur l'eau sacrée d'un fleuve indien, (voyage voyage) Voyage (voyage) Et jamais ne revient."},
        {"por", "Grândola, vila morena Terra da fraternidade O povo é quem mais ordena Dentro de ti, ó cidade Dentro de ti, ó cidade O povo é quem mais ordena Terra da fraternidade Grândola, vila morena"},
        {"deu", "Komm in mein Boot Ein Sturm kommt auf Und es wird Nacht Wo willst du hin So ganz allein Treibst du davon Wer hält deine Hand Wenn es dich Nach unten zieht"},
        {"deu", "verkündet die Generalversammlung"},
        {"eng", "The General Assembly"},
        {"fra", "L'Assemblée générale"},
        {"ita", "L'ASSEMBLEA GENERALE"},
        {"jpn", "よって、ここに、国連総会は、"},
        {"kor", "국제연합총회는,"},
        {"rus", "Генеральная Ассамблея,"},
        {"spa", "La Asamblea General"},
        {"zho", "大 会, 发 布 这 一 世 界 人 权 宣 言 ,"},
        {"deu", "Generalversammlung"},
    };

    
    public GutenbergLanguageClassifierTest()
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

    @Test
    public void testSomeMethod()
    {
        String[] langs = Gutenberg.getISO3Languages();
        GutenbergLanguageClassifier classifier = new GutenbergLanguageClassifier("(null)", langs);
        
        for(String[] item:data)
        {
            assertEquals(item[0], classifier.classify(item[1]).getName());
        }
    }
}
