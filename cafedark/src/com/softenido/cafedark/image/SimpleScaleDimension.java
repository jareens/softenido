/*
 *  SimpleScaleDimension.java
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
package com.softenido.cafedark.image;

import java.awt.Dimension;

/**
 *
 * @author franci
 */
public class SimpleScaleDimension implements ScaleDimension
{
    final int width;
    final int height;

    public SimpleScaleDimension(int width, int height)
    {
        this.width = width;
        this.height = height;
    }
    public SimpleScaleDimension(int size)
    {
        this.width  = size;
        this.height = size;
    }
    
    public Dimension convert(Dimension size)
    {
        double wRatio = (double)width / (double)size.width;
        double hRatio = (double)height / (double)size.height;

        double ratio  = 1.0;
        if(wRatio<ratio)
            ratio = wRatio;
        if(hRatio<ratio)
            ratio = hRatio;

        if(ratio==1.0)
            return size;

        final int w= (int)Math.round(size.width*ratio);
        final int h= (int)Math.round(size.height*ratio);
        return new Dimension(w,h);
    }

}
