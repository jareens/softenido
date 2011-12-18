/*
 *  EmbeddDatabaseDerby.java
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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author franci
 */
public class EmbeddDatabaseDerby implements EmbeddDatabase
{

    private static boolean driverloaded = false;
//  jdbc:derby:;databaseName=databaseName
//  jdbc:derby:jar:(pathToArchive)databasePathWithinArchive
//  jdbc:derby:/databasePathWithinArchive
//  jdbc:derby:;shutdown=true
//
//  jdbc:derby:databaseName;create=true
//  jdbc:derby:sample;user=jill;password=toFetchAPail
//  jdbc:derby:encryptedDB;create=true;dataEncryption=true;bootPassword=DBpassword
//  jdbc:derby:encryptedDB;bootPassword=DBpassword
    private static final String DERBY_DRIVER_CLASS = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String JDBC_DERBY = "jdbc:derby";
//  String bootPassword=null;         //    * bootPassword=key
    private boolean create = false;    // * create=true
    final private String databaseName;     // * databaseName=nameofDatabase
//  boolean dataEncryption=true;      //    * dataEncryption=true
//  String encryptionProvider=null;   //    * encryptionProvider=providerName
//  String encryptionAlgorithm=null;  //    * encryptionAlgorithm=algorithm
//  String territory=null;            //    * territory=ll_CC
//  String logDevice=null;            //    * logDevice=logDirectoryPath
//  String createFrom=null;           //    * createFrom=BackupPath
//  String restoreFrom=null;          //    * restoreFrom=BackupPath
//  String rollForwardrecoveryFrom=null;//    * rollForwardrecoveryFrom=BackupPath
    private String password = null;    // * password=userPassword
    private String user = null;    // * user=userName
//  shutdown=true
    private String url = null;

    public EmbeddDatabaseDerby(String databaseName)
    {
        this.databaseName = databaseName;
    }


    /**
     * get de URL to create a connection, it returns the same String each time it is
     * called, until a new String is needed
     * @return
     */
    public synchronized String getURL()
    {
        if (url == null)
        {
            url = JDBC_DERBY;

            if (databaseName != null)
            {
                url += ":" + databaseName;
            }

            if (password != null)
            {
                url += ";password=" + password;
            }

            if (user != null)
            {
                url += ";user=" + user;
            }

            if (create)
            {
                url += ";create=true";
            }
        }

        return url;
    }

    public synchronized boolean loadDriver()
    {
        try
        {
            Class.forName(DERBY_DRIVER_CLASS);
            driverloaded = true;
        } 
        catch (ClassNotFoundException ex)
        {
            driverloaded = false;
            ex.printStackTrace();
        }
        return driverloaded;
    }

    private boolean loadDriverIfNeeded()
    {
        // if driver is loaded there is nothing to do so we don't need to synchronyze
        if (driverloaded)
        {
            return driverloaded;
        }

        // if driver isn't loaded it's time to synchronize to avoid unneeded concurrent calls to loadDriver
        synchronized (this)
        {
            if (driverloaded)
            {
                return driverloaded;
            }
            return loadDriver();
        }
    }

    /**
     *
     * @return
     */
    public boolean shutdown()
    {
        try
        {
            loadDriverIfNeeded();
            DriverManager.getConnection(getURL() + ";shutdown=true");
            return true;
        } 
        catch (SQLException ex)
        {
            Logger.getLogger(EmbeddDatabaseDerby.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
    }

    /**
     *
     * @param path
     */
    @Override
    public void backup(String path)
    {
        loadDriverIfNeeded();
        try
        {
            Connection con = getConnection();
            CallableStatement cs = con.prepareCall("CALL SYSCS_UTIL.SYSCS_BACKUP_DATABASE(?)"); 
            cs.setString(1, path);
            cs.execute(); 
            cs.close();
        } 
        catch (SQLException ex)
        {
            Logger.getLogger(EmbeddDatabaseDerby.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param path
     */
    public void restore(String path)
    {
        loadDriverIfNeeded();
    }

    /**
     *
     * @throws java.sql.SQLException
     * @return
     */
    public Connection getConnection() throws SQLException
    {
        loadDriverIfNeeded();
        return DriverManager.getConnection(getURL());
    }

    /**
     *
     * @param create
     */
    public void setCreate(boolean create)
    {
        this.create = create;
    }

    /**
     *
     * @return
     */
    public String getDatabaseName()
    {
        return databaseName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }
}
