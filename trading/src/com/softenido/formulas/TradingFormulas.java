/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.formulas;

/**
 *
 * @author franci
 */
public class TradingFormulas
{

    static public int NULL = 0;
    static public int BUY = 1;
    static public int SELL = 2;
    static public int SHORT = 3;
    static public int COVER = 4;

    static public double tradeRatio(double target, double entry, double stop)
    {
        assert (target >= entry && entry >= stop) || (target <= entry && entry <= stop);
        double r = (target - entry) / (entry - stop);
        assert (r >= 0);
        return r;
    }

    static public double tradeLimit(double target, double stopLoss, double ratio)
    {
        assert (ratio > 0);
        return (target + ratio * stopLoss) / (ratio + 1);
    }

    static public int tradeSize(double buyingPower, double tradeRisk, double entryLimit, double stopLoss, double tradeFee, double shareSlippage)
    {
        int size = tradeSize(tradeRisk, Math.abs(entryLimit - stopLoss), tradeFee, shareSlippage);
        return (int)Math.min(size, (buyingPower-tradeFee)/entryLimit);
        
    }

    static public int tradeSize(double tradeRisk, double shareRisk, double tradeFee, double shareSlippage)
    {
        assert (tradeRisk > 0 && shareRisk > 0);
        return (int) ((tradeRisk - tradeFee) / (shareRisk + shareSlippage));
    }

    static public int tradeType(double target, double entry, double stop)
    {
        if (target > entry && entry > stop)
        {
            return BUY;
        }
        if (target < entry && entry < stop)
        {
            return SHORT;
        }
        return NULL;
    }

    static public double tradeRisk(double target, double entry, double stop)
    {
        double gain = Math.abs(target - entry);
        double loss = Math.abs(entry - stop);
        if (loss == 0)
        {
            return -1;
        }
        return gain / loss;
    }
}
