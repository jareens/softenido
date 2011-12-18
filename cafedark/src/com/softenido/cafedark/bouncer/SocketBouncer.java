/*
 *  SocketBouncer.java
 *
 *  Copyright (C) 2008-2010  Francisco Gómez Carrasco
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
package com.softenido.cafedark.bouncer;

import com.softenido.cafedark.qos.QosFilter;
import com.softenido.cafedark.qos.QosInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author franci
 */
public class SocketBouncer implements Runnable
{
    Socket org;
    Socket dst;
    private QosFilter orgLimit;
    private QosFilter dstLimit;

    public SocketBouncer(Socket org, Socket dst, QosFilter orgLimit, QosFilter dstLimit)
    {
        this.org = org;
        this.dst = dst;
        this.orgLimit = orgLimit;
        this.dstLimit = dstLimit;
    }
    
    public SocketBouncer(Socket org, Socket dst) throws IOException
    {
        this(org,dst,null,null);
    }

    public void run()
    {
        Thread taskOrg = null;
        Thread taskDst = null;
        try
        {
            InputStream  orgIn  = (orgLimit==null)? org.getInputStream() : new QosInputStream(org.getInputStream(),orgLimit);
            //InputStream  orgIn  = org.getInputStream();
            OutputStream orgOut = org.getOutputStream();
            InputStream  dstIn  = (dstLimit==null)? dst.getInputStream() : new QosInputStream(dst.getInputStream(),orgLimit);
            //InputStream  dstIn  = dst.getInputStream();
            OutputStream dstOut = dst.getOutputStream();
            
            taskOrg = new Thread(new BouncerStream(orgIn, dstOut,"Org"),"Org");
            taskDst = new Thread(new BouncerStream(dstIn, orgOut,"Dst"),"Dst");
            
            taskOrg.start();
            taskDst.start();

            taskOrg.join();
            taskDst.join();
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(SocketBouncer.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(SocketBouncer.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                org.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(SocketBouncer.class.getName()).log(Level.SEVERE, null, ex);
            }
            try
            {
                dst.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(SocketBouncer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
