/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fjtk.se.security;

import junit.framework.TestCase;

public class BlurBooleanTest extends TestCase
{
    public BlurBooleanTest(String testName)
    {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    /**
     * Test of getValue method, of class BlurBoolean.
     */
    public void testGetValue()
    {
        System.out.println("getValue");
        BlurBoolean instance = null;
        Boolean expResult = null;
        Boolean result = instance.getValue();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
