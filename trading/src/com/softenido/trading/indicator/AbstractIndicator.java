/*
 * AbstractIndicator.java
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
class AbstractIndicator implements Indicator
{
    static final double[][] EMPTY_VALUES = new double[0][];
    static final Date[] EMPTY_DATES = new Date[0];
    
    final String name;
    final Stock stock;
    final String fields[];
    final Date[] dates;
    final double[][] values;

    public AbstractIndicator(String name, Stock stock, String[] fields, Date[] dates, double[][] values)
    {
        this.name = name;
        this.stock = stock;
        this.fields = fields;
        this.dates = dates;
        this.values = values;
    }

    public String getName()
    {
        return name;
    }

    public String[] getFields()
    {
        return fields;
    }

    public Stock getStock()
    {
        return stock;
    }

    public Date[] getDates()
    {
        return dates;
    }

    public double[][] getValues()
    {
        return values;
    }



}
