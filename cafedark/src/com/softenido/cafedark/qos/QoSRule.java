/*
 *  QoSRule.java
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
public class QoSRule
{
    private final int bpsDown;
    private final int bpsUp;
    private final int delayDown;
    private final int delayUp;

    public QoSRule(int bpsDown, int bpsUp, int delayDown, int delayUp)
    {
        this.bpsDown = bpsDown;
        this.bpsUp = bpsUp;
        this.delayDown = delayDown;
        this.delayUp = delayUp;
    }

    public QoSRule(int bps, int delay)
    {
        this.bpsDown = bps;
        this.bpsUp = bps;
        this.delayDown = delay;
        this.delayUp = delay;
    }

    public int getBpsDown()
    {
        return bpsDown;
    }

    public int getBpsUp()
    {
        return bpsUp;
    }

    public int getDelayDown()
    {
        return delayDown;
    }

    public int getDelayUp()
    {
        return delayUp;
    }
}
