**picbulk** is a command-line tool designed to make easy a bulk upload of pics to picasaweb and alternatively send them by email.

There are multiple known bugs due it's under develompent, it has been a private program for 2 years (known as uploadpicasa) with no code for exceptions or errors, so it should be added. Now I've decided to release it to help those GNU/Linux users that hate the tedious task of uploading images to picasa.

# picbulk 0.0.1 help message #

```
picbulk version 0.0.1 alpha  (2009-12-29)
Copyright (C) 2007-2009 by Francisco Gómez Carrasco
<http://www.softenido.com>

picbulk comes with ABSOLUTELY NO WARRANTY. This is free software, and you
are welcome to redistribute it under certain conditions. See the GNU
General Public Licence version 3 for details.

picbulk sends images to picasa or to email addresses specified.

usage: picbulk [options] title directory
usage: picbulk [options] title directory --to=friend@example.com
       java -jar PicBulk.jar [options] title directory

Options:
 -v, --verbose               increase verbosity
 -L, --license               display software license
     --user=username         username for identification
     --pass=password         password for identification
     --to=address            send by email to address
     --cc=address            send by email to address
     --bcc=address           send by email to address
     --smtp=host             smtp host
     --from=address          from email address
     --ssl=[true|false]      use ssl for smtp
     --starttls=[true|false] use starttls for smtp
     --albumsize=N           max number of images by album or email
     --shrink=SIZE           shrink image to fit into SIZExSIZE
     --skip=N                skip first N images
     --loadconf              load configuration (user, ... from .picbulk
     --saveconf              save configuration (user, ... from .picbulk
     --passconf              to use pass in configuration file (unsafe)
     --install               install a launcher
     --install-java[=path]   install a launcher using 'java' command
     --install-home[=path]   install a launcher using 'java.home' property
     --install-posix         posix flavor for install options when unknown
     --version               print version number
     --examples              print some useful examples
     --debug                 debug mode for developers
(-h) --help                  show this help (-h works with no other options)
```