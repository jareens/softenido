/*
 * Account.java
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
