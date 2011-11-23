/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.trading;

/**
 *
 * @author franci
 */
public enum OrderType
{
    MARKET("Market"), 
    LIMIT("Limit"), 
    STOP("Stop"), 
    STOP_LIMIT("Stop Limit"), 
    TRAILING_STOP_AMOUNT("Trailing Stop($)"), 
    TRAILING_STOP_PERCENT("Trailing Stop(%)");
    final String name;

    private OrderType(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
}
