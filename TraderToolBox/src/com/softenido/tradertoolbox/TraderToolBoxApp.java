/*
 * TraderToolBoxApplication.java
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

package com.softenido.tradertoolbox;

import android.app.Application;
import android.content.Context;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: franci
 * Date: 02/06/12
 * Time: 11:44
 * To change this template use File | Settings | File Templates.
 */
public class TraderToolBoxApp extends Application
{
    static final String CONFIG = "traderToolBox.properties";
    static Properties config = new Properties();
    @Override
    public void onCreate()
    {
        super.onCreate();

        try
        {
            config.load(this.openFileInput(CONFIG));
        }
        catch(IOException ex)
        {
            Logger.getLogger(TraderToolBoxApp.class.getName()).log(Level.INFO,"traderToolBox.properties don't exists");
        }
    }
    static Properties getConfiguration()
    {
        return config;
    }
    static void saveConfiguration(Context ctx)
    {
        try
        {
            config.store(ctx.openFileOutput(CONFIG, MODE_PRIVATE), null);
        }
        catch(FileNotFoundException ex)
        {
            Logger.getLogger(TraderToolBoxApp.class.getName()).log(Level.SEVERE, "error saving file " + CONFIG, ex);
        }
        catch(IOException ex)
        {
            Logger.getLogger(TraderToolBoxApp.class.getName()).log(Level.SEVERE, "error saving file " + CONFIG, ex);
        }
    }

}
