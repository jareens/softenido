/*
ServerSocketBouncer.java

Copyright (C) 2008  Francisco Gómez Carrasco

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
package com.softenido.cafedark.bouncer;

import com.softenido.cafedark.qos.QoSRule;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author franci
 */
public class ServerSocketBouncer extends Thread
{
    final private ServerSocket server;
    final private String remoteHost;
    final private int remotePort;
    private boolean cancel;
    
    private final QoSRule commonLocalRule;
    private final QoSRule commonRemoteRule;
    private final QoSRule socketLocalRule;
    private final QoSRule socketRemoteRule;

//    private final QoSAgent commonLocalAgent;
//    private final QoSAgent commonRemoteAgent;
    
    
    public ServerSocketBouncer(final int localPort, String remoteHost, final int remotePort) throws IOException
    {
        this(localPort, remoteHost, remotePort, null, null, null, null);
    }

    public ServerSocketBouncer(final int localPort, String remoteHost, final int remotePort, QoSRule clr, QoSRule crr, QoSRule slr, QoSRule srr) throws IOException
    {
        super();
        this.server = new ServerSocket(localPort);
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
        
        this.commonLocalRule = clr;
        this.commonRemoteRule = crr;
        this.socketLocalRule = slr;
        this.socketRemoteRule = srr;
        
//        commonLocalAgent = new QoSAgent(commonLocalRule);
//        commonRemoteAgent= new QoSAgent(commonRemoteRule);
    }

    @Override
    public void run()
    {
        while (!cancel)
        {
            try
            {
                Socket org = server.accept();
                Socket dst = new Socket(remoteHost, remotePort);

//                new Thread(new SocketBouncer(org, dst, getLocalQoS(), getRemoteQoS())).start();
            }
            catch (IOException ex)
            {
                Logger.getLogger(ServerSocketBouncer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try
        {
            server.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(ServerSocketBouncer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isCancel()
    {
        return cancel;
    }

    public void setCancel(boolean cancel)
    {
        this.cancel = cancel;
    }
    
//    private QoS getLocalQoS()
//    {
//        return new QoSGlue(new QoSAgent(socketLocalRule),commonLocalAgent);
//    }
//    private QoS getRemoteQoS()
//    {
//        return new QoSGlue(new QoSAgent(socketRemoteRule),commonRemoteAgent);
//    }
}
