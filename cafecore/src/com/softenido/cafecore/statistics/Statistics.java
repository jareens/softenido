/*
 * Statistics.java
 *
 * Copyright (c) 2012  Francisco Gómez Carrasco
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
package com.softenido.cafecore.statistics;

/**
 *
 * @author franci
 */
public class Statistics
{
//    TAL VEZ SEA MEJOR IMPLEMENTAR COMO OOP, UN OBJETO CON PUT Y GET O QUIZAS UN NEXT
    double simpleMovingAverage(double previous, double data, int n)
    {
        return previous - (previous/n) + (data/n);
    }
    double [] simpleMovingAverage(double[] data, int n)
    {
  //      double[] sma = new int[data.lenth]
        return null;
    }
    
}
