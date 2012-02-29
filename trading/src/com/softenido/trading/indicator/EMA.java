/*
 * EMA.java
 *
 * Copyright (c) 2012  Francisco Gómez Carrasco
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Report bugs or new features to: flikxxi@gmail.com
 */

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
public class EMA extends AbstractIndicator
{
    final int periods;
    final double k;

    public EMA(int periods, double k, Stock stock, String[] fields, Date[] dates, double[][] values)
    {
        super("EMA", stock, fields, dates, values);
        this.periods = periods;
        this.k = k;
    }
    private double getEMA(double last, double price)
    {
        return price * k + last * (1-k);
    }
    static EMA build(int period, int field, boolean force, Prices prices)
    {
        return null;
    }
}
