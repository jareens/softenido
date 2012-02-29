/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.trading.stock;

/**
 *
 * @author franci
 */
public class StockData
{
    final Stock stock;
    final double[][] values;

    public StockData(Stock stock, double[][] values)
    {
        this.stock = stock;
        this.values = values;
    }
}
