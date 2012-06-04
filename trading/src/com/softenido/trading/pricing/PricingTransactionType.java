/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.trading.pricing;

import com.softenido.trading.OrderType;
import com.softenido.trading.TransactionType;
import java.math.BigDecimal;

/**
 *
 * @author franci
 */
public class PricingTransactionType implements Pricing
{
    final Pricing buy;
    final Pricing sell;
    final Pricing sshort;
    final Pricing cover;

    public PricingTransactionType(Pricing buy, Pricing sell, Pricing sshort, Pricing cover)
    {
        this.buy = buy;
        this.sell = sell;
        this.sshort = sshort;
        this.cover = cover;
    }
    public PricingTransactionType(Pricing buy, Pricing sell)
    {
        this.buy = buy;
        this.sell = sell;
        this.sshort = sell;
        this.cover = buy;
    }

    public BigDecimal getCost(TransactionType tt, OrderType ot, double price, int shares)
    {
        BigDecimal cost = BigDecimal.ZERO;
        switch(tt)
        {
            case BUY: 
                cost = buy.getCost(tt, ot, price, shares);
                break;
            case SELL: 
                cost = sell.getCost(tt, ot, price, shares);
                break;
            case SHORT: 
                cost = sshort.getCost(tt, ot, price, shares);
                break;
            case COVER: 
                cost = cover.getCost(tt, ot, price, shares);
                break;
        }
        return cost;
    }
}

