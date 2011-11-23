/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.trading;

import com.softenido.trading.pricing.NullPricing;
import com.softenido.trading.pricing.Pricing;

/**
 *
 * @author franci
 */
public class Account
{
    String name;
    double equity;
    double tradeRisk;
    double accountRisk;
    Pricing pricing;
    double buyingPower;

    public Account(String name, double equity, double tradeRisk, double accountRisk, Pricing pricing, double buyingPower)
    {
        this.name = name;
        this.equity = equity;
        this.tradeRisk = tradeRisk;
        this.accountRisk = accountRisk;
        this.pricing = pricing;
        this.buyingPower = buyingPower;
    }

    public Account(String name, double equity, double tradeRisk, double accountRisk)
    {
        this.name = name;
        this.equity = equity;
        this.tradeRisk = tradeRisk;
        this.accountRisk = accountRisk;
        this.pricing = new NullPricing();
        this.buyingPower = equity;
    }

    public String getName()
    {
        return name;
    }

    public double getAccountRisk()
    {
        return accountRisk;
    }

    public double getEquity()
    {
        return equity;
    }

    public Pricing getPricing()
    {
        return pricing;
    }

    public double getTradeRisk()
    {
        return tradeRisk;
    }
    
}
