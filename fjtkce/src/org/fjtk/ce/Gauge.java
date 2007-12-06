/*
 *  Gauge.java
 *
 *  Copyright (C) 2007  Francisco Gómez Carrasco
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
package org.fjtk.ce;

public interface Gauge
{
    public void begin();
    public void begin(int max);
    public void end();
    public void setPrefix(String prefix);
    public String getPrefix();
    
    /**
     * 
     * @return 
     */

    public double getDone();
    /**
     * Gets the current value
     * <p>
     * Obtiene el valor actual
     * @return 
     */

    public int getVal();
    /**
     * Gets the minimun value
     * <p>
     * Obtiene el valor mínimo
     * @return 
     */


    public int getMax();
    /**
     * Sets the current value
     * <p>
     * Establece el valor actual
     * @param n 
     */

    public void setVal(int n);
    /**
     * Sets the minimun value
     * <p>
     * Establece el valor mínimo
     * @param n 
     */

    public void setMax(int n);
    public void step();
    public void step(int n);
    public void paint(double done,String msg);
}
    