/*
 *  EchoServer.java
 *
 *  Copyright (C) 2010-2011 Francisco Gómez Carrasco
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
package com.softenido.echoserver;


import com.softenido.cafecore.util.ArrayUtils;
import com.softenido.cafedark.util.launcher.LauncherParser;
import com.softenido.cafedark.util.options.BooleanOption;
import com.softenido.cafedark.util.options.InvalidOptionException;
import com.softenido.cafedark.util.options.NumberOption;
import com.softenido.cafedark.util.options.OptionException;
import com.softenido.cafedark.util.options.OptionParser;
import com.softenido.cafedark.util.options.SizeOption;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author franci
 */
public class EchoServer implements Runnable
{
    private static final int DEFAULT_TASKS= 1024;
    private static final int DEFAULT_QUEUE= 64;
    private static final int DEFAULT_SIZE = 64*1024;

    private static final String ECHOSERVER = "echoserver";
    private static final String VER = "0.0.1";
    private static final String VERSION =
            ECHOSERVER+"  version "+VER+" beta (2010-01-09)\n"
            + "Copyright (C) 2010 by Francisco Gómez Carrasco\n"
            + "<http://www.softenido.com>\n";
    private static final String REPORT_BUGS =
            "Report bugs to <flikxxi@gmail.com>\n"
            + "            or <http://code.google.com/p/softenido/issues>\n";
    private static final String LICENSE =
            VERSION
            + "\n"
            + "This program is free software: you can redistribute it and/or modify\n"
            + "it under the terms of the GNU General Public License as published by\n"
            + "the Free Software Foundation, either version 3 of the License, or\n"
            + "(at your option) any later version.\n"
            + "\n"
            + "This program is distributed in the hope that it will be useful,\n"
            + "but WITHOUT ANY WARRANTY; without even the implied warranty of\n"
            + "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n"
            + "GNU General Public License for more details.\n"
            + "\n"
            + "You should have received a copy of the GNU General Public License\n"
            + "along with this program.  If not, see <http://www.gnu.org/licenses/>.\n"
            + "\n"
            + REPORT_BUGS;
    private static final String HELP =
            VERSION
            + "\n"
            + ECHOSERVER+" comes with ABSOLUTELY NO WARRANTY. This is free software, and you\n"
            + "are welcome to redistribute it under certain conditions. See the GNU\n"
            + "General Public Licence version 3 for details.\n"
            + "\n"
            + ECHOSERVER+" is a TCP/IP echo server\n"
            + "\n"
            + "usage: "+ECHOSERVER+" [option]... port [port]...\n"
            + "       java -jar EchoServer.jar [option]... port [port]...\n"
            + "\n"
            + "Options:\n"
            + " -v, --verbose               increase verbosity\n"
            + " -L, --license               display software license\n"
            + " -c, --clients=N             maximum number N of clients connections\n"
            + " -q, --queue=N               size of the queue\n"
            + " -d, --deadline=N            timeout of N seconds before unconditional exit\n"
            + " -t, --client-timeout=N      close inactive connections after N seconds\n"
            + " -T, --global-timeout=N      exit after N seconds without conections\n"
            + " -s, --size=size[bkmgt]      size[bkmgt] of buffers (1k-1m)\n"
            + " -n, --noexit                disable disconection when receiving EXIT\n"
            + "     --install               install a launcher\n"
            + "     --install-java[=path]   install a launcher using 'java' command\n"
            + "     --install-home[=path]   install a launcher using 'java.home' property\n"
            + "     --install-posix         posix flavor for install options when unknown\n"
            + "     --install-version       adds version to launcher name\n"
            + "     --version               print version number\n"
            + "     --examples              print some useful examples\n"
            + "(-h) --help                  show this help (-h works with no other options)\n"
            +"\n"
            + "size units:\n"
            + " 1=1b, 1k=1024b, 1m=1024k, 1g=1024m, 1t=1024g\n"
            + "\n"
            + REPORT_BUGS;
    private static final String EXAMPLES =
            VERSION
            + "\n"
            + "examples of echoserver usage:\n"
            + "\n"
            + " gij -jar EchoServer.jar --install-java gij\n"
            + " java -jar EchoServer.jar --install\n"
            + " sudo java -jar EchoServer.jar --install\n"
            + " sudo /opt/jdk1.6/bin/java -jar EchoServer.jar --install-home\n"
            + " sudo /opt/jdk1.6/bin/java -jar EchoServer.jar --install-posix\n"
            + " echoserver 7777\n"
            + " echoserver 1111 3333 5555 7777 -c16\n"
            + " echoserver 7777 -nc16 \n"
            + " echoserver 7777 -d3600 -t60 -T300\n"
            + " echoserver 7777 -s64k\n"
            + "\n"
            + " send me yours to: <flikxxi@gmail.com>\n";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException, IOException
    {
        if (args.length < 1)
        {
            System.out.println(HELP);
            return;
        }
        int verboseLevel = 0;
        
        OptionParser options = new OptionParser();

        BooleanOption optVerbose = options.add(new BooleanOption('v', "verbose"));
        BooleanOption optLicense = options.add(new BooleanOption('L', "license"));
        NumberOption  optTasks   = options.add(new NumberOption('c',"clients"));
        NumberOption  optQueue   = options.add(new NumberOption('q',"queue"));
        NumberOption  optDeadline= options.add(new NumberOption('d', "deadline"));
        NumberOption  optClientTimeout = options.add(new NumberOption('t', "client-timeout"));
        NumberOption  optGlobalTimeout = options.add(new NumberOption('T', "global-timeout"));
        SizeOption    optSize = options.add(new SizeOption('s', "size"));
        BooleanOption optNoExit = options.add(new BooleanOption('n',"noexit"));
        BooleanOption version = options.add(new BooleanOption("version"));
        BooleanOption help = options.add(new BooleanOption('h', "help"));
        BooleanOption examples = options.add(new BooleanOption("examples"));

        String[] ports;
        try
        {
            args = LauncherParser.parseInstall(ECHOSERVER, null, VER, args);
            if(args==null)
            {
                return;
            }
            ports = options.parse(args);
        }
        catch (InvalidOptionException ex)
        {
            System.err.println(ex);
            System.err.println(ECHOSERVER+": try --help for more information");
            return;
        }

        if (help.isUsed())
        {
            System.out.println(HELP);
            return;
        }
        if (optLicense.isUsed())
        {
            System.out.println(LICENSE);
            return;
        }
        if (version.isUsed())
        {
            System.out.println(VERSION);
            return;
        }
        if (examples.isUsed())
        {
            System.out.println(EXAMPLES);
            return;
        }
        if (optVerbose.isUsed())
        {
            verboseLevel = optVerbose.getCount();
        }
        final int tasks;
        final int queue;
        final int deadline;
        final int clientTimeout;
        final int globalTimeout;
        final int size;
        try
        {
            tasks   = optTasks.intValue(DEFAULT_TASKS);
            queue   = optQueue.intValue(DEFAULT_QUEUE);
            deadline= optDeadline.intValue(0)*1000;
            clientTimeout = optClientTimeout.intValue(0)*1000;
            globalTimeout = optGlobalTimeout.intValue(0)*1000;
            size    = (int) Math.max(Math.min(1024, optSize.longValue(DEFAULT_SIZE)),1024*1024);
        }
        catch (OptionException ex)
        {
            System.err.println(ECHOSERVER+": "+ex.getMessage());
            return;
        }
        final boolean noexit = optNoExit.isUsed();
        if (ports.length == 0)
        {
            System.err.println(ECHOSERVER+": missing port");
            return;
        }
        Arrays.sort(ports);
        ports = ArrayUtils.uniqueCopyOf(ports);

        EchoServer[] echo = new EchoServer[ports.length];
        Thread[] th = new Thread[ports.length];

        for(int i=0;i<ports.length;i++)
        {
            int port;
            try
            {
                port = Integer.parseInt(ports[i]);
            }
            catch (NumberFormatException ex)
            {
                System.err.println(ECHOSERVER+": '"+ports[i]+"' not a valid port number");
                continue;
            }
            echo[i] = new EchoServer(port,queue,tasks,clientTimeout, size,!noexit);
            th[i] = new Thread(echo[i],"EchoServer-"+port);
            th[i].setDaemon(true);
            th[i].start();
            System.out.println(ECHOSERVER+": listening on port "+port);
        }

        final Object lock = new Object();

        if(deadline>0)
        {
            new Thread("DeadLine")
            {
                @Override
                public void run()
                {
                    synchronized(lock)
                    {
                        try
                        {
                            lock.wait(deadline);
                        }
                        catch (InterruptedException ex)
                        {
                            Logger.getLogger(EchoServer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        System.out.println(ECHOSERVER+": deadline expired");
                        System.exit(0);
                    }
                }
            }.start();
        }

        if(globalTimeout>0)
        {
            new Thread("GlobalTimeout")
            {
                @Override
                public void run()
                {
                    synchronized(lock)
                    {
                        long curCount=0;
                        long oldCount=-1;

                        while( curCount != oldCount)
                        try
                        {
                            lock.wait(globalTimeout);
                            oldCount = curCount;
                            curCount = EchoSocket.getCount();
                        }
                        catch (InterruptedException ex)
                        {
                            Logger.getLogger(EchoServer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        System.out.println(ECHOSERVER+": global timeout expired");
                        System.exit(0);
                    }
                }
            }.start();
        }

        for(int i=0;i<th.length;i++)
        {
            if(th[i]!=null)
            {
                th[i].join(deadline);
            }
        }
        System.exit(0);
    }

    private final int port;
    private final int queue;
    private final int tasks;
    private final int timeout;
    private final int size;
    private final boolean exit;

    private EchoServer(int port, int queue, int tasks, int timeout, int size, boolean exit)
    {
        this.port = port;
        this.queue = queue;
        this.tasks = tasks;
        this.timeout = timeout;
        this.size = size;
        this.exit = exit;
    }

    public void run()
    {
        boolean keep = true;
        try
        {
            ServerSocket server = new ServerSocket(port, queue);
            EchoSocket.setPermits(tasks);
            while(keep)
            {
                Socket sc = server.accept();
                EchoSocket echoTask = EchoSocket.getEchoSocket(sc, size, timeout, exit);
                new Thread(echoTask).start();
            }
            server.close();
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(EchoServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(EchoServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
