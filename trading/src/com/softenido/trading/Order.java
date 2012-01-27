/*
 * Order.java
 *
 * Copyright (c) 2011-2012  Francisco Gómez Carrasco
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
package com.softenido.trading;

import java.util.Date;

/**
 *
 * @author franci
 */
public class Order
{
    final TransactionType type;
    final String symbol;
    final int quantity;
    final OrderType orderType;
    final double limit;
    final double stop;
    double price;
    final Date duration;
    final boolean allOrNone;
    final OrderStatus status;
    volatile double averagePrice=0;
    
    public Order(TransactionType type, String symbol, int quantity, OrderType orderType, double limit, double stop, Date duration, boolean allOrNone,OrderStatus status)
    {
        this.type = type;
        this.symbol = symbol;
        this.quantity = quantity;
        this.orderType = orderType;
        this.limit = limit;
        this.stop = stop;
        this.duration = duration;
        this.allOrNone = allOrNone;
        this.status = status;   
    }

    public Order(TransactionType type, String symbol, int quantity, OrderType orderType, double limit, double stop)
    {
        this.type = type;
        this.symbol = symbol;
        this.quantity = quantity;
        this.orderType = orderType;
        this.limit = limit;
        this.stop = stop;
        this.duration = null;
        this.allOrNone= false;
        this.status = null;
    }
}
