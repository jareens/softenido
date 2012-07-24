/*
 *  MailToTest.java
 *
 *  Copyright (C) 2012  Francisco Gómez Carrasco
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
package com.softenido.cafedesk.net;

import java.net.URI;
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
public class MailToTest
{
    
    public MailToTest()
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
     * Test of to method, of class MailTo.
     */
    @Test
    public void testTo()
    {
        MailTo builder = new MailTo();
        String mailto = builder.to("example@example.com").buildString();
        assertEquals("mailto:example@example.com",mailto);
    }

    /**
     * Test of cc method, of class MailTo.
     */
    @Test
    public void testCc()
    {
        MailTo builder = new MailTo();
        String mailto = builder.cc("example@example.com").buildString();
        assertEquals("mailto:?cc=example@example.com",mailto);
    }

    /**
     * Test of subject method, of class MailTo.
     */
    @Test
    public void testSubject()
    {
        MailTo builder = new MailTo();
        String mailto = builder.subject("this is the subject").buildString();
        assertEquals("mailto:?subject=this%20is%20the%20subject",mailto);
    }

    /**
     * Test of body method, of class MailTo.
     */
    @Test
    public void testBody()
    {
        MailTo builder = new MailTo();
        String mailto = builder.body("this is the body").buildString();
        assertEquals("mailto:?body=this%20is%20the%20body",mailto);
    }

    /**
     * Test of header method, of class MailTo.
     */
    @Test
    public void testHeader()
    {
        MailTo builder = new MailTo();
        String mailto = builder.header("Message-Id","aba.123@mail.example.com").buildString();
        assertEquals("mailto:?Message-Id=aba.123@mail.example.com",mailto);
    }

    /**
     * Test of buildString method, of class MailTo.
     */
    @Test
    public void testBuildString()
    {
        MailTo builder = new MailTo();
        builder.to("example@example.com");
        builder.cc("carboncopy@example.com");
        builder.subject("this is the subject");
        builder.body("this is the body %?&+");
        builder.header("x-mailer", "test");
        String mailto = builder.buildString();
        assertEquals("mailto:example@example.com?cc=carboncopy@example.com&subject=this%20is%20the%20subject&body=this%20is%20the%20body%20%25%3F%26%2B&x-mailer=test",mailto);
    }

    /**
     * Test of buildURI method, of class MailTo.
     */
    @Test
    public void testBuildURI() throws Exception
    {
        MailTo builder = new MailTo();
        String mailto = builder.header("Message-Id","aba.123@mail.example.com").buildString();
        assertEquals("mailto:?Message-Id=aba.123@mail.example.com",mailto);
    }
}
