/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.trading;

/**
 *
 * @author franci
 */
public enum PositionType
{
    LONG("Long"), SHORT("Short");

    final String name;

    private PositionType(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    static public PositionType get(double limit, double stop, double target)
    {
        if(stop < limit && target > limit)
        {
            return LONG;
        }
        if(stop > limit && target < limit)
        {
            return SHORT;
        }
        return null;
    }
}
