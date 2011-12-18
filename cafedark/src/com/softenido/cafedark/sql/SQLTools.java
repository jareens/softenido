/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.cafedark.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author franci
 */
public class SQLTools
{
    public boolean tableExists(Connection con, String tableName) throws SQLException
    {
        DatabaseMetaData meta = con.getMetaData();
        ResultSet tables = meta.getTables(null, null, tableName, null);
        try
        {
            return !tables.next();
        }
        finally
        {
            tables.close();
        }    
    }
}
