/*
 * PricingScale.java
 *
 * Copyright (c) 2012 Francisco Gómez Carrasco
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

import com.softenido.cafecore.util.Arrays6;
import com.softenido.trading.OrderType;
import com.softenido.trading.TransactionType;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author franci
 */
public class PricingScale implements Pricing
{
    static final BigDecimal[] empty = new BigDecimal[0];
    static final RoundingMode HALF_UP = RoundingMode.HALF_UP;
    final String name;
    final int cScale; 
    final int rScale;
    
    final int steps;
    // conditions
    final double[] money;
    final int[] shares;
    // configuration
    final BigDecimal[] flatCost;
    final BigDecimal[] ratioCost;
    final BigDecimal[] shareCost;
    final BigDecimal[] minCost;
    final BigDecimal[] maxCost;

    public PricingScale(String name, int cScale, int rScale)
    {
        this.name = name;
        this.cScale = cScale;
        this.rScale = rScale;
        this.steps=0;
        this.money = new double[0];
        this.shares = new int[0];
        this.flatCost = empty;
        this.ratioCost = empty;
        this.shareCost = empty;
        this.minCost = empty;
        this.maxCost = empty;
    }

    PricingScale(PricingScale ps, double money, int shares, BigDecimal flatCost, BigDecimal ratioCost, BigDecimal shareCost, BigDecimal minCost, BigDecimal maxCost)
    {
        this.name = ps.name;
        this.cScale = ps.cScale;
        this.rScale = ps.rScale;
        this.steps = ps.steps+1;
        
        this.money = Arrays6.copyOf(ps.money, this.steps);
        this.money[ps.steps]= money;
        
        this.shares = Arrays6.copyOf(ps.shares, this.steps);
        this.shares[ps.steps] = shares;
        
        this.flatCost = Arrays6.copyOf(ps.flatCost, this.steps);
        this.flatCost[ps.steps] = flatCost;
        
        this.ratioCost = Arrays6.copyOf(ps.ratioCost, this.steps);
        this.ratioCost[ps.steps] = ratioCost;
        
        this.shareCost = Arrays6.copyOf(ps.shareCost, this.steps);
        this.shareCost[ps.steps] = shareCost;
        
        this.minCost = Arrays6.copyOf(ps.minCost, this.steps);
        this.minCost[ps.steps] = minCost;

        this.maxCost = Arrays6.copyOf(ps.maxCost, this.steps);
        this.maxCost[ps.steps] = maxCost;
    }

    
    public BigDecimal getCost(TransactionType tt, OrderType ot, double price, int shares)
    {
        final double total = price*shares;
        final BigDecimal value = BigDecimal.valueOf(total).setScale(cScale);
        
        BigDecimal cost = BigDecimal.ZERO;
        
        for(int i=this.steps-1;i>=0;i--)
        {
            if( total>=this.money[i] && shares>=this.shares[i])
            {
                BigDecimal flat = flatCost[i].setScale(cScale,HALF_UP);
                BigDecimal ratio = value.multiply(ratioCost[i]).setScale(cScale,HALF_UP);
                BigDecimal share = BigDecimal.valueOf(shares).multiply(shareCost[i]).setScale(cScale,HALF_UP);
                cost = flat.add(ratio).add(share).setScale(rScale,HALF_UP);
                return cost.max(minCost[i]).min(maxCost[i]).setScale(rScale,HALF_UP);
            }
        }
        return cost;
    }

    public PricingScale add(double money, int shares, BigDecimal flatCost, BigDecimal ratioCost, BigDecimal shareCost, BigDecimal minCost, BigDecimal maxCost)
    {
        return new PricingScale(this, money, shares, flatCost, ratioCost, shareCost, minCost, maxCost);
    }
    public PricingScale add(double money, BigDecimal flatCost, BigDecimal ratioCost, BigDecimal minCost, BigDecimal maxCost)
    {
        return new PricingScale(this, money, 0, flatCost, ratioCost, BigDecimal.ZERO, minCost, maxCost);
    }
    public PricingScale add(int shares, BigDecimal flatCost, BigDecimal shareCost, BigDecimal minCost, BigDecimal maxCost)
    {
        return new PricingScale(this, 0, shares, flatCost, BigDecimal.ZERO, shareCost, minCost, maxCost);
    }
}
