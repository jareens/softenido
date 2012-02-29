/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.trading.stock;

/**
 *
 * @author franci
 */
public class Stock
{
    final String name;
    final String ticket;
    final String market;
    final String coin;

    public Stock(String name, String ticket, String market, String coin)
    {
        this.name = name;
        this.ticket = ticket;
        this.market = market;
        this.coin = coin;
    }

    public String getCoin()
    {
        return coin;
    }

    public String getMarket()
    {
        return market;
    }

    public String getName()
    {
        return name;
    }

    public String getTicket()
    {
        return ticket;
    }
    
}
