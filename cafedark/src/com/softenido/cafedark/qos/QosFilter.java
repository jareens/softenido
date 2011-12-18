/*
 *  QosFilter.java
 *
 *  Copyright (C) 2008-2010  Francisco Gómez Carrasco
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  Report bugs or new features to: flikxxi@gmail.com
 *
 */

package com.softenido.cafedark.qos;

class SavePoint
{
    long time;
    long count;
    long dif;
    long avg;
    int update(long curTime, long num)
    {
        count += num;
        dif    = Math.max(1, curTime-time);
        avg    = count/dif;
        long bpms = 100;
        return (int)( (count / bpms)- dif);
    }
}

/**
 *
 * @author franci
 */
public class QosFilter
{
    private static final long MAX_TIMES[] = {100,200,400,800,1600,3200,6400,12800};
    private final QosFilter parent;
    private final QoSRule   rule;

    
    private final Object lock = new Object();
    private final SavePoint[] history = new SavePoint[MAX_TIMES.length];

    public QosFilter(QoSRule rule, QosFilter parent)
    {
        this.parent = parent;
        this.rule   = rule;
    }

    public QosFilter(QoSRule rule)
    {
        this(rule,null);
    }

    public void count(int num, boolean delay)
    {
//        try
//        {
//            if(rule.getDelay()>0)
//            {
//                //Thread.sleep(rule.getDelay());
//            }
            if (num <= 0)
            {
                return;
            }
//            long tini = System.currentTimeMillis();
//            synchronized(lock)
//            {
//                long bps = rule.getBps();
//                long tcur = System.currentTimeMillis();
//                // se incrementa el contador
//                for(int i =0;i<history.length;i++)
//                {
//                    int left = history[i].update(tcur, num);
//                    if(left>0)
//                    {
//                        Thread.sleep(left);
//                    }
//                }
//                // se desplazan los contadores caducados
//                parent.count(num,false);
//            }
//        }
//        catch (InterruptedException ex)
//        {
//            Logger.getLogger(QosFilter.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}
