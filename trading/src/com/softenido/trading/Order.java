/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
