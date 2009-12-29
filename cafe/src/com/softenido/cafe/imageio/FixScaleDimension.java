/*
 *  FixScaleDimension.java
 *
 *  Copyright (C) 2009  Francisco Gómez Carrasco
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
package com.softenido.cafe.imageio;

import java.awt.Dimension;

/**
 *
 * @author franci
 */
public class FixScaleDimension implements ScaleDimension
{
    final int max;
    final int min;
    final double[] ratios = { 1.0, 0.9, 0.8, 0.75, 2.0/3.0, 0.6, 0.5, 0.4, 1.0/3.0, 0.25, 0.1 };

    public FixScaleDimension(int max, int min)
    {
        this.max = max;
        this.min = min;
    }  
    
    public Dimension convert(Dimension size)
    {
        for( double r : ratios)
        {
            Dimension dim = convert(size, r);
            if(dim!=null)
                return dim;
        }
        return size;
    }
    public Dimension convert(Dimension size,double ratio)
    {
        final int w= (int)Math.round(size.width*ratio);
        final int h= (int)Math.round(size.height*ratio);

        if(min>0)
        {
            if(w<min || h<min)
            {
                return null;
            }
        }
        if(max>0)
        {
            if(w>max || h>max)
            {
                return null;
            }
        }
        if(ratio==1.0)
            return size;

        return new Dimension(w,h);
    }

}
