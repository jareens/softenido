/*
 *  QoSAgent.java
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

/**
 *
 * @author franci
 */
public class QoSAgent
{
    private volatile QoSAgent parent;
    private QoSRule rule;
    private final QoSCounter countDown;
    private final QoSCounter countUp;

    public QoSAgent(QoSAgent parent, QoSRule rule)
    {
        this.parent = parent;
        this.rule = rule;
        this.countDown = new QoSCounter(rule.getBpsDown(), rule.getDelayDown());
        this.countUp   = new QoSCounter(rule.getBpsUp(),   rule.getDelayUp());
    }

    public QoSCounter getCountDown()
    {
        return countDown;
    }

    public QoSCounter getCountUp()
    {
        return countUp;
    }

    public QoSAgent getParent()
    {
        return parent;
    }

    public QoSRule getRule()
    {
        return rule;
    }

    public void setParent(QoSAgent parent)
    {
        this.parent = parent;
    }

    public void setRule(QoSRule rule)
    {
        this.rule = rule;
    }
   
}
