/*
   Main.java calcula el primer primo de 10 digitos en digitos consecutivos de E

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
import java.math.BigInteger;

/**
 *
 * @author franci
 */
public class Main
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        BigDecimal e = E.buildE(1000);

        BigDecimal base = BigDecimal.ONE.scaleByPowerOfTen(10);

        for (int i = 9; i < 1000; i++)
        {
            BigDecimal p = e.scaleByPowerOfTen(i);
            BigDecimal r = p.remainder(base);
            BigInteger r2 = r.toBigInteger();
            if (r2.isProbablePrime(Integer.MAX_VALUE))
            {
                System.out.printf("pos=%d\np=%s\n", i, r2.toString());
                break;
            }
        }

    }
}
