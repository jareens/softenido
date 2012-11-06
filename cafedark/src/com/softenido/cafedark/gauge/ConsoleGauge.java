/*
 *  ConsoleGaugeProgress.java
 *
 *  Copyright (C) 2007-2012 Francisco Gómez Carrasco
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
package com.softenido.cafedark.gauge;

import com.softenido.cafecore.gauge.AbstractGaugeProgress;

/**
 *
 * @author franci
 */
public class ConsoleGauge extends AbstractGaugeProgress
{
    private final ConsoleGaugeView view;
    public ConsoleGauge()
    {
        super();
        view = new ConsoleGaugeView();
    }

    @Override
    public void paint(boolean started, int max, int val, String prefix, double done, String msg)
    {
        view.paint(started, max, val, prefix, done, msg);
    }
}
