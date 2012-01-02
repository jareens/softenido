/*
 * PricingBuilder.java
 *
 * Copyright (c) 2011  Francisco Gómez Carrasco
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

package com.softenido.trading.pricing;

import com.softenido.trading.OrderType;
import com.softenido.trading.TransactionType;

class SimplePricing implements Pricing
{
    final String name;
    final double flatCost;
    final double ratioCost;
    final double shareCost;
    final double minCost;
    final double maxCost;

    SimplePricing(String name)
    {
        this.name      = name;
        this.flatCost  = 0;
        this.ratioCost = 0;
        this.shareCost = 0;
        this.minCost   = 0;
        this.maxCost   = Double.MAX_VALUE;
    }

    SimplePricing(String name, double amount, double ratio, double share, double min, double max)
    {
        this.name      = name;
        this.flatCost  = amount;
        this.ratioCost = ratio;
        this.shareCost = share;
        this.minCost = min;
        this.maxCost = max;
    }

    public double getCost(TransactionType tt, OrderType ot, double price, int shares)
    {
        double cost = flatCost;

        cost += (price * shares) * ratioCost;
        cost += shareCost * shares;
        cost = Math.max(cost, minCost);
        cost = Math.min(cost, maxCost);

        return cost;
    }

    SimplePricing setFlatCost(double val)
    {
        return new SimplePricing(name, val, ratioCost, shareCost, minCost, maxCost);
    }

    SimplePricing setRatioCost(double val)
    {
        return new SimplePricing(name, flatCost, val, shareCost, minCost, maxCost);
    }

    SimplePricing setShareCost(double val)
    {
        return new SimplePricing(name, flatCost, ratioCost, val, minCost, maxCost);
    }

    SimplePricing setMinCost(double val)
    {
        return new SimplePricing(name, flatCost, ratioCost, shareCost, val, maxCost);
    }

    SimplePricing setMaxCost(double val)
    {
        return new SimplePricing(name, flatCost, ratioCost, shareCost, minCost, val);
    }
}

class TransactionPricing implements Pricing
{
    final String name;
    final SimplePricing buyCost;
    final SimplePricing sellCost;
    final SimplePricing shortCost;
    final SimplePricing coverCost;

    public TransactionPricing(String name)
    {
        this.name      = name;
        this.buyCost   = new SimplePricing(name+".buy");
        this.sellCost  = new SimplePricing(name+".sell");
        this.shortCost = new SimplePricing(name+".short");
        this.coverCost = new SimplePricing(name+".conver");
    }

    public TransactionPricing(String name, SimplePricing buy, SimplePricing sell, SimplePricing sellShort, SimplePricing cover)
    {
        this.name      = name;
        this.buyCost   = buy;
        this.sellCost  = sell;
        this.shortCost = sellShort;
        this.coverCost = cover;
    }

    public double getCost(TransactionType tt, OrderType ot, double price, int shares)
    {
        switch(tt)
        {
            case BUY:
                return buyCost.getCost(tt, ot, price, shares);
            case SELL:
                return sellCost.getCost(tt, ot, price, shares);
            case SHORT:
                return shortCost.getCost(tt, ot, price, shares);
            case COVER:
                return coverCost.getCost(tt, ot, price, shares);
            default:
                return 0;
        }
    }

    TransactionPricing setFlatCost(double val)
    {
        return new TransactionPricing(name, buyCost.setFlatCost(val), sellCost.setFlatCost(val), shortCost.setFlatCost(val), coverCost.setFlatCost(val));
    }

    TransactionPricing setSellFlatCost(double val)
    {
        return new TransactionPricing(name, buyCost, sellCost.setFlatCost(val), shortCost, coverCost);
    }

    TransactionPricing setSellAndShortFlatCost(double val)
    {
        return new TransactionPricing(name, buyCost, sellCost.setFlatCost(val), shortCost.setFlatCost(val), coverCost);
    }
}

class OrderPricing implements Pricing
{
    final String name;
    final TransactionPricing commision;
    final TransactionPricing fee;

    OrderPricing(String name)
    {
        this.name      = name;
        this.commision = new TransactionPricing(name+".commision");
        this.fee       = new TransactionPricing(name+".fee");
    }

    OrderPricing(String name, TransactionPricing commision, TransactionPricing fee)
    {
        this.name      = name;
        this.commision = commision;
        this.fee = fee;
    }

    public double getCost(TransactionType tt, OrderType ot, double price, int shares)
    {
        return commision.getCost(tt, ot, price, shares) + fee.getCost(tt, ot, price, shares);
    }

    OrderPricing setFlatCommisionCost(double val)
    {
        return new OrderPricing(name, commision.setFlatCost(val), fee);
    }

    OrderPricing setSellAndShortFlatFeeCost(double val)
    {
        return new OrderPricing(name, commision, fee.setSellAndShortFlatCost(val));
    }
}

/**
 *
 * @author franci
 */
public class PricingBuilder
{
    final String name;
    OrderPricing pricing;

    public PricingBuilder(String name)
    {
        this.name = name;
        this.pricing = new OrderPricing(name);
    }

    public PricingBuilder setFlatCommisionCost(double val)
    {
        pricing = pricing.setFlatCommisionCost(val);
        return this;
    }

    public PricingBuilder setSellAndShortFlatFeeCost(double val)
    {
        pricing = pricing.setSellAndShortFlatFeeCost(val);
        return this;
    }

    public Pricing build()
    {
        return pricing;
    }
    public Pricing build(String name)
    {
        return new OrderPricing(name,pricing.commision,pricing.fee);
    }
}
