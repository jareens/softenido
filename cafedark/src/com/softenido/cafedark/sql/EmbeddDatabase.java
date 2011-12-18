/*
 *  EmbeddDatabase.java
 *
 *  Copyright (C) 2007-2011  Francisco Gómez Carrasco
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
package com.softenido.cafedark.sql;
/*
 * EmbeddDatabase.java
 *
 * Created on 7 de junio de 2007, 8:39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author franci
 */
public interface EmbeddDatabase
{
    public boolean loadDriver();
    
    public boolean shutdown();
    
    void backup(String path);
    void restore(String path);
    
    public String getURL();
    public Connection getConnection() throws SQLException;
    
}
