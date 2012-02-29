/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.trading.indicator;

import com.softenido.trading.stock.Stock;
import java.util.Date;

/**
 *
 * @author franci
 */
public class Volume extends AbstractIndicator
{
    public Volume(Stock stock, String[] fields, Date[] dates, double[][] values)
    {
        super("Volume", stock, fields, dates, values);
    }
}
