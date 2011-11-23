/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.trading;

/**
 *
 * @author franci
 */
public class RiskManager
{
    final Account account;

    public RiskManager(Account account)
    {
        this.account = account;
    }

    public Trade buildRisk(Trade trade, double allowedRisk)
    {
        double expectedGain;
        double expectedLoss;
        TransactionType entryType;
        TransactionType exitType;
        double entryPrice;
        double exitPrice;
        
        switch(trade.type)
        {
            case LONG:
                expectedGain = (trade.target-trade.limit);
                expectedLoss   = (trade.limit -trade.stop);
                entryType  = TransactionType.BUY;
                exitType   = TransactionType.SELL;
                entryPrice = trade.limit;
                exitPrice  = trade.stop-trade.slippage;
                break;
            case SHORT:
                expectedGain = (trade.limit - trade.target);
                expectedLoss   = (trade.stop  - trade.limit);
                entryType  = TransactionType.SHORT;
                exitType   = TransactionType.COVER;
                entryPrice = trade.limit;
                exitPrice  = trade.stop+trade.slippage;
                break;
            default:
                expectedGain = 0;
                expectedLoss = 0;
                entryType  = null;
                exitType   = null;
                entryPrice = 0;
                exitPrice  = 0;
        }
        double shareLoss = expectedLoss+trade.slippage;
        double expectedRatio = (expectedGain/expectedLoss);
        double currentRisk = 0;
        
        // se reduce el riesto al máximo permitido
        allowedRisk = Math.min(allowedRisk,account.tradeRisk);

        // gross max shares
        int shares = (int)(account.buyingPower/entryPrice);
        int qty = (int) (allowedRisk/shareLoss);
        shares = Math.min(shares,qty);
        
        // reducing by buying power
        for(;shares>0;shares--)
        {
            double sharesCost= shares * entryPrice;
            double entryCost = account.pricing.getCost(entryType, OrderType.LIMIT, entryPrice, shares);
            double exitCost  = account.pricing.getCost(exitType, OrderType.STOP,  exitPrice,  shares);
            double totalCost = sharesCost + entryCost + exitCost;
            if(totalCost<account.buyingPower)
                break;
        }
        
        // reducing by risk
        for(;shares>0;shares--)
        {
            double sharesRisk= shares * (expectedLoss+trade.slippage);
            double entryCost = account.pricing.getCost(entryType, OrderType.LIMIT, entryPrice, shares);
            double exitCost  = account.pricing.getCost(exitType, OrderType.STOP,  exitPrice,  shares);
            currentRisk = sharesRisk + entryCost + exitCost;
            if(currentRisk<allowedRisk)
                break;
        }
        return new Trade(trade,expectedRatio,shares, currentRisk);
    }
    
    
}
