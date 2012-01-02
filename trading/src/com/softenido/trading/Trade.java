/*
 * Trade.java
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

package com.softenido.trading;

/**
 *
 * @author franci
 */
public class Trade
{
    PositionType type;
    final String ticket;
    final double limit;
    final double stop;
    final double target;
    final double slippage;
    final double ratio;
    final int shares;
    final double risk;

    public Trade(String ticket, double limit, double stop, double target, double slippage)
    {
        this.ticket = ticket;
        this.limit = limit;
        this.stop = stop;
        this.target = target;
        this.slippage = slippage;
        this.type = PositionType.get(limit, stop, target);
        this.ratio = 0;
        this.shares = 0;
        this.risk = 0;
    }

    Trade(String ticket, double limit, double stop, double target, double slippage, double ratio, int shares, double risk)
    {
        this.ticket = ticket;
        this.limit = limit;
        this.stop = stop;
        this.target = target;
        this.slippage = slippage;
        this.type = PositionType.get(limit, stop, target);
        this.ratio = ratio;
        this.shares = shares;
        this.risk   = risk;
    }
    Trade(Trade trade, double ratio, int shares, double risk)
    {
        this.ticket = trade.ticket;
        this.limit = trade.limit;
        this.stop = trade.stop;
        this.target = trade.target;
        this.slippage = trade.slippage;
        this.type = trade.type;
        this.ratio = ratio;
        this.shares = shares;
        this.risk   = risk;
    }

    public double getLimit()
    {
        return limit;
    }

    public double getSlippage()
    {
        return slippage;
    }

    public double getStop()
    {
        return stop;
    }

    public double getTarget()
    {
        return target;
    }

    public PositionType getType()
    {
        return type;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        final Trade other = (Trade) obj;
        if(this.type != other.type)
            return false;
        if((this.ticket == null) ? (other.ticket != null) : !this.ticket.equals(other.ticket))
            return false;
        if(Double.doubleToLongBits(this.limit) != Double.doubleToLongBits(other.limit))
            return false;
        if(Double.doubleToLongBits(this.stop) != Double.doubleToLongBits(other.stop))
            return false;
        if(Double.doubleToLongBits(this.target) != Double.doubleToLongBits(other.target))
            return false;
        if(Double.doubleToLongBits(this.slippage) != Double.doubleToLongBits(other.slippage))
            return false;
        if(Double.doubleToLongBits(this.ratio) != Double.doubleToLongBits(other.ratio))
            return false;
        if(this.shares != other.shares)
            return false;
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 17 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 17 * hash + (this.ticket != null ? this.ticket.hashCode() : 0);
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.limit) ^ (Double.doubleToLongBits(this.limit) >>> 32));
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.stop) ^ (Double.doubleToLongBits(this.stop) >>> 32));
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.target) ^ (Double.doubleToLongBits(this.target) >>> 32));
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.slippage) ^ (Double.doubleToLongBits(this.slippage) >>> 32));
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.ratio) ^ (Double.doubleToLongBits(this.ratio) >>> 32));
        hash = 17 * hash + this.shares;
        return hash;
    }
    
}
