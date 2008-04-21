/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fjtk.se.security;

/**
 *
 * @author franci
 */
public abstract class BlurObject<T>
{
    protected final T x;
    protected final T y;

    protected BlurObject(T x, T y)
    {
        this.x = x;
        this.y = y;
    }

    public abstract T getValue();

}
   