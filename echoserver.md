**echoserver** is an TCP/IP echo server. It's just a developer tool, do not use it in production environmets for long time.

# echoserver 0.0.1 help message #

```
echoserver  version 0.0.1 beta (2010-01-09)
Copyright (C) 2010 by Francisco Gómez Carrasco
<http://www.softenido.com>

echoserver comes with ABSOLUTELY NO WARRANTY. This is free software, and you
are welcome to redistribute it under certain conditions. See the GNU
General Public Licence version 3 for details.

echoserver is a TCP/IP echo server

usage: echoserver [option]... port [port]...
       java -jar EchoServer.jar [option]... port [port]...

Options:
 -v, --verbose               increase verbosity
 -L, --license               display software license
 -c, --clients=N             maximum number N of clients connections
 -q, --queue=N               size of the queue
 -d, --deadline=N            timeout of N seconds before unconditional exit
 -t, --client-timeout=N      close inactive connections after N seconds
 -T, --global-timeout=N      exit after N seconds without conections
 -s, --size=size[bkmgt]      size[bkmgt] of buffers (1k-1m)
 -n, --noexit                disable disconection when receiving EXIT
     --install               install a launcher
     --install-java[=path]   install a launcher using 'java' command
     --install-home[=path]   install a launcher using 'java.home' property
     --install-posix         posix flavor for install options when unknown
     --install-version       adds version to launcher name
     --version               print version number
     --examples              print some useful examples
(-h) --help                  show this help (-h works with no other options)

size units:
 1=1b, 1k=1024b, 1m=1024k, 1g=1024m, 1t=1024g
```

# examples of echoserver usage #
```
 gij -jar EchoServer.jar --install-java gij
 java -jar EchoServer.jar --install
 sudo java -jar EchoServer.jar --install
 sudo /opt/jdk1.6/bin/java -jar EchoServer.jar --install-home
 sudo /opt/jdk1.6/bin/java -jar EchoServer.jar --install-posix
 echoserver 7777
 echoserver 1111 3333 5555 7777 -c16
 echoserver 7777 -nc16 
 echoserver 7777 -d3600 -t60 -T300
 echoserver 7777 -s64k
```