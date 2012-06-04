/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.trading.pricing;

import com.softenido.trading.OrderType;
import com.softenido.trading.TransactionType;
import java.math.BigDecimal;
import java.math.MathContext;

/**
 *
 * @author franci
 */
public class PricingGroup implements Pricing
{
    static enum Mode{ADD, MIN, MAX};
    final String name;
    final int rScale;
    final Pricing[] pricing;
    final PricingGroup.Mode mode;

    public PricingGroup(String name, int rScale, PricingGroup.Mode mode, Pricing ... pricing)
    {
        this.name = name;
        this.rScale = rScale;
        this.pricing = pricing;
        this.mode = mode;
    }
    
    public PricingGroup(String name, int rScale, Pricing ... pricing)
    {
        this.name = name;
        this.pricing = pricing;
        this.rScale = rScale;
        this.mode = PricingGroup.Mode.ADD;
    }
    
    public BigDecimal getCost(TransactionType tt, OrderType ot, double price, int shares)
    {
        BigDecimal cost = BigDecimal.ZERO;
        
        for(Pricing p : pricing)
        {
            switch(mode)
            {
                case ADD: 
                    cost = cost.add(p.getCost(tt, ot, price, shares));
                    break;
                case MIN: 
                    cost = cost.min(p.getCost(tt, ot, price, shares));
                    break;
                case MAX: 
                    cost = cost.max(p.getCost(tt, ot, price, shares));
                    break;
            }
        }
        return cost.setScale(rScale);
    }

    public PricingGroup add(Pricing item)
    {
        return new PricingGroup(name,rScale, PricingGroup.Mode.ADD, this, item);
    }
    public PricingGroup min(Pricing item)
    {
        return new PricingGroup(name,rScale, PricingGroup.Mode.MIN, this, item);
    }
    public PricingGroup max(Pricing item)
    {
        return new PricingGroup(name,rScale, PricingGroup.Mode.MAX, this, item);
    }
}
