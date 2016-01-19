**svnhg** is a commandline tool designed for migrating a repository from subversion to mercurial, without loosing your whole history.

This tool solves my "problem", any other tool I've tried was hard, complex or just doesn't work. Hey maybe you have one that works, but I haven't tried it.

The migration process is done by executing **svn** and **hg** comands in the following sequence

```
hg clone ...
svn checkout ...
svn update -r 1 ...
svn log -r 1 ...
hg add ...
hg commit ...
svn update -r 2 ...
svn log -r 2 ...
hg add ...
hg commit ...
...
```

I've just tried with my project softenido, so maybe it doesn't work with yours, just add an issue and it will be fixed in a near future.

# svnhg 0.0.1 help message #

```
svnhg version 0.0.1 alpha  (2010-04-01)
Copyright (C) 2010 by Francisco Gómez Carrasco
<http://www.softenido.com>

svnhg comes with ABSOLUTELY NO WARRANTY. This is free software, and you
are welcome to redistribute it under certain conditions. See the GNU
General Public Licence version 3 for details.

svnhg migrates a repository from subversion (svn) to mercurial (Hg)

usage: svnhg [options] path
       java -jar SvnHg.jar [options] path

Options:
 -v, --verbose               increase verbosity
 -L, --license               display software license
     --svn-url=url           subversion repository url
     --svn-user=username     subversion repository username
     --hg-url=url            mercurial repository url
     --hg-push               do mercurial push
     --skip=revisions        range of revisions to skip ( ie:1,3-5,7,13-17)
     --install               install a launcher
     --install-java[=path]   install a launcher using 'java' command
     --install-home[=path]   install a launcher using 'java.home' property
     --install-posix         posix flavor for install options when unknown
     --install-version       adds version to launcher name
     --version               print version number
     --examples              print some useful examples
     --debug                 debug mode for developers
(-h) --help                  show this help (-h works with no other options)
```