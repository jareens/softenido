/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.trading.pricing;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;



public class PricingBuilder
{
    static final BigDecimal _0_00 = BigDecimal.ZERO;
    final String name;
    final int cScale; 
    final int rScale;

    public PricingBuilder(String name, int cScale, int rScale)
    {
        this.name = name;
        this.cScale = cScale;
        this.rScale = rScale;
    }

    public PricingBuilder(String name, int scale)
    {
        this.name = name;
        this.cScale = scale;
        this.rScale = scale;
    }
    public PricingBuilder(String name)
    {
        this.name = name;
        this.cScale = 6;
        this.rScale = 2;
    }
    
    public NullPricing zeroCost()
    {
        return new NullPricing();
    }
    
    public PricingScale flatCost(double val)
    {
        return new PricingScale(name, cScale, rScale).add(0.0, BigDecimal.valueOf(val), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(Double.MAX_VALUE));
    }
    public PricingScale ratioCost(double[] money, double[] flat, double[] ratio, double min, double max)
    {
        PricingScale ps = new PricingScale(name, cScale, rScale);
        for(int i=0;i<money.length;i++)
        {
            ps = ps.add(money[i], BigDecimal.valueOf(flat[i]), BigDecimal.valueOf(ratio[i]), BigDecimal.valueOf(min), BigDecimal.valueOf(max) );
        }
        return ps;
    }
    public PricingScale shareCost(int[] shares, double[] flat, double[] price, double min, double max)
    {
        PricingScale ps = new PricingScale(name, cScale, rScale);
        for(int i=0;i<shares.length;i++)
        {
            ps = ps.add(shares[i], BigDecimal.valueOf(flat[i]), BigDecimal.valueOf(price[i]), BigDecimal.valueOf(min), BigDecimal.valueOf(max) );
        }
        return ps;
    }
    
    public PricingScale shareCost(double val, double min, double max)
    {
        return new PricingScale(name, cScale, rScale).add(0, BigDecimal.ZERO, BigDecimal.valueOf(val), BigDecimal.valueOf(min), BigDecimal.valueOf(max));
    }
    public PricingScale shareCost(double val)
    {
        return shareCost(val, 0.0, Double.MAX_VALUE);
    }
    public PricingGroup add(Pricing ... items)
    {
        return new PricingGroup(name, rScale, PricingGroup.Mode.ADD, items);
    }
    public PricingGroup min(Pricing ... items)
    {
        return new PricingGroup(name, rScale, PricingGroup.Mode.MIN, items);
    }
    public PricingGroup max(Pricing ... items)
    {
        return new PricingGroup(name, rScale, PricingGroup.Mode.MAX, items);
    }
    public PricingTransactionType transaction(Pricing buy, Pricing sell, Pricing sshort, Pricing cover)
    {
        return new PricingTransactionType(buy, sell, sshort, cover);
    }
    public PricingTransactionType transaction(Pricing buy, Pricing sell)
    {
        return new PricingTransactionType(buy, sell);
    }
}
