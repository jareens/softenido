/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.softenido.findrepe;

/**
 *
 * @author franci
 */
public class EqualsFilter<E>
{
    private final E obj;

    public EqualsFilter(E obj)
    {
        this.obj = obj;
    }

    @Override
    public boolean equals(Object obj)
    {
        return super.equals(obj);
    }

    @Override
    public int hashCode()
    {
        return super.hashCode();
    }
    
}
