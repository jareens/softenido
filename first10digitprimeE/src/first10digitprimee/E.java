/*
   E.java realiza operaciones relativas al número E

   Copyright (C) 2007  Francisco Gómez Carrasco

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package first10digitprimee;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 *
 * @author franci
 */
public class E
{
    public static BigDecimal buildE(int setPrecision)
    {
        BigDecimal f = BigDecimal.ONE;
        BigDecimal e = new BigDecimal(2);
        MathContext mc = new MathContext(setPrecision);
        for(int i=2;i<setPrecision+3;i++)
        {
            f = f.multiply(new BigDecimal(i));
            BigDecimal e2 = e.add(BigDecimal.ONE.divide(f,mc),mc);
            if(e2.equals(e))
                break;
            e = e2;
        }
        return e;
    }
}
