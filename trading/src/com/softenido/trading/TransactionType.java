/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.trading;

/**
 *
 * @author franci
 */
public enum TransactionType
{
    BUY(PositionType.LONG), SHORT(PositionType.SHORT), SELL(PositionType.LONG), COVER(PositionType.SHORT);
    
    final private PositionType positionType;

    private TransactionType(PositionType positionType)
    {
        this.positionType = positionType;
    }
    public PositionType getPositionType()
    {
        return positionType;
    }
}
