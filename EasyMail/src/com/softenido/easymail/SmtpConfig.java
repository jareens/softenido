/*
 *  SmtpConfig.java
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
package com.softenido.easymail;

/**
 *
 * @author franci
 */
public class SmtpConfig
{
    private String host;
    private String user;
    private String password;
    private boolean ssl;
    private boolean auth;
    private boolean starttls;

    private SmtpConfig(String host, String user, String password, boolean auth, boolean ssl, boolean starttls)
    {
        this.host = host;
        this.user = user;
        this.password = password;
        this.ssl = ssl;
        this.auth = auth;
        this.starttls = starttls;
    }
//    public SmtpConfig(String host)
//    {
//        this(host, null, null, false, false, false);
//    }
//    public SmtpConfig(String host, String user, String password)
//    {
//        this(host, user, password, true, false, false);
//    }
    public SmtpConfig(String host, String user, String password, boolean ssl, boolean starttls)
    {
        this(host, user, password, true, ssl, starttls);
    }
//    public SmtpConfig()
//    {
//        this(null, null, null, false, false, false);
//    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }
    
    public boolean isAuth()
    {
        return auth;
    }

    public void setAuth(boolean auth)
    {
        this.auth = auth;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
   
    public boolean isSsl()
    {
        return ssl;
    }

    public void setSsl(boolean ssl)
    {
        this.ssl = ssl;
    }

    public boolean isStarttls()
    {
        return starttls;
    }

    public void setStarttls(boolean starttls)
    {
        this.starttls = starttls;
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