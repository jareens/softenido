/*
 * StockManager.java
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.trading.stock;

import java.io.*;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author franci
 */
public class StockManager
{
    StockData loadYahoo(Stock stock, InputStream data)
    {
        return null;
    }
    StockData loadYahoo(Stock stock, File fd) throws FileNotFoundException, IOException
    {
        InputStream data = new FileInputStream(fd);
        if(fd.getName().toLowerCase().endsWith("gz"))
        {
            data = new GZIPInputStream(data);
        }
        return loadYahoo(stock, data);
    }
//    importar de ficheros comprimidos, en el test, luego
//    cargar StockData y luego IndicatorManager para generar los Indicadores.
}
