/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.softenido.svnhg;

import javax.print.attribute.SetOfIntegerSyntax;

/**
 *
 * @author franci
 */

public class RevisionRange extends SetOfIntegerSyntax
{

    public RevisionRange(int lowerBound, int upperBound)
    {
        super(lowerBound, upperBound);
    }

    public RevisionRange(int member)
    {
        super(member);
    }

    public RevisionRange(int[][] members)
    {
        super(members);
    }

    public RevisionRange(String members)
    {
        super(members);
    }   
}
