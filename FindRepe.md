**findrepe** is a command-line tool designed for an eficient search of repeated files. Basically the method consists in an initial comparison of file sizes, followed by an MD5 and SHA1 signature comparison (both must be satified to avoid MD5 or SHA1 collisions). There are some options to filter and an option to ask for deletion of repeated files. Zip and Jar files can be scanned recursively as regular files in read-only mode.

There are some known performance inefficiencies:

  * There is no MD5 and SHA1 signature cache for previously unchanged files (those with the same file size and modification time).

  * Reading from multiple devices could be parallelized to improve performance.

findrepe is been developed using Java and [NetBeans](http://www.netbeans.org) mostly in GNU/Linux (ubuntu 9.10 at this moment), and tested on Windows XP, Windows 7 and OpenSolaris.

There are some partial-implemented issues:
  * symlinks
  * cyclic loops
  * skip by default /dev and /proc /tmp and some others
  * comparator optimized for big files
  * "Actor Model" to achieve an scalable algorithm
  * concurrent optimizations
  * filter by filename
  * non-unicode filenames
  * scanning into zip, jar

There are some known non-implemented issues:

  * scanning into gzip, tgz and rar files
  * option for passing specific options to JVM, like -Xmx250m
  * hardlinks
  * make hardlinks or softlinks from duplicated files
  * changing files
  * reading file list from stdin or file
  * i18n (internationalization)
  * comparing images, audios and videos like humans
  * javadoc should be added
  * source code should be tested with findbugs, pdm, ...
  * ultrafast comparator assuming few KB are enough
  * GUI version
  * Web-GUI version (with HttpServer class) maybe option "--web 80"
  * exclude some control files of netbeans java projects
  * cache for files using javadb (derby) or smalSQL. canonicalpath+size+modification\_time
    * modes: off, aggressive, conservative
    * clean cache
    * countdown for cached hashes
    * autoclean cache
  * android edition


**Help wanted**

If you want to help for free (under the GPLv3 or any later version or any compatible license) you are welcome. The list of people I need:

  * A debian sponsor.
  * Translators for intenationalization.
  * Programmers
  * Beta-testers
  * Sponsors
  * A girlfriend
  * Groupies }:->

# findrepe 0.10.0 help message #

```
findrepe  version 0.10.0 alpha (2010-08-11)
Copyright (C) 2009-2010 by Francisco Gómez Carrasco
<http://www.softenido.com>

findrepe comes with ABSOLUTELY NO WARRANTY. This is free software, and you
are welcome to redistribute it under certain conditions. See the GNU
General Public Licence version 3 for details.

findrepe searches the given path for repeated files by content (not name). Such
files are found by comparing file sizes and MD5+SHA1 signatures.

usage: findrepe [options] [directories]
       java -jar FindRepe.jar [options] [directories]

Options:
 -v, --verbose               increase verbosity
     --verbose-logger        format messages as a logger
 -L, --license               display software license
 -d, --delete                prompt user for files to delete
     --delete-auto=path      smart auto-selection of files for deletion
 -n, --noempty               exclude zero-length files
 -s, --symlinks              follow symlinks
 -m, --min-size=size         exclude files shorter than size[bkmgt]
 -M, --max-size=size         exclude files larger than size[bkmgt]
 -w  --min-wasted=size       minimum wasted size[bkmgt] copies, size*(n-1)
 -S  --size                  show size[bkmgt] of files
 -H  --nohide                ignore hide entries
 -A  --absolute-path         show absolute path of files
     --install               install a launcher
     --install-java[=path]   install a launcher using 'java' command
     --install-home[=path]   install a launcher using 'java.home' property
     --install-posix         posix flavor for install options when unknown
     --install-version       adds version to launcher name
     --unique                list only unique files (--count=1)
     --count=N               list files repeated N times  
 -c  --min-count=N           files repeated at least N times
 -C  --max-count=N           files repeated no more than N times
     --noautoexclude         don't autoexclude some paths (/dev, /proc, ...)
     --exclude=path          don't follow path
     --exclude-dir=pattern   don't follow directories matching pattern
     --exclude-file=pattern  ignore files matching pattern
     --exclude-rc            ignore revision control directories
     --exclude-svn           ignore subversion (.svn)
     --exclude-cvs           ignore cvs (CVS)
     --exclude-hg            ignore mercurial (.hg and .hgignore)
 -f  --focus=path            focus on files in path
     --focus-dir=pattern     focus on directories matching pattern
     --focus-file=pattern    focus on files matching pattern
     --dir=pattern           only directories matching pattern
     --file=pattern          only files matching pattern
 -z  --zip                   recurse into zip files (zip, jar, ...)(ALPHA)
 -Z  --zip-only              exclude files not added by option --zip
 -e  --regex                 uses java regular expresions
     --wildcard              uses wildcards '*', '?' and '[]' (default)
 -j  --jobs=N                limits thread use to N (0-1024, developers only)
     --bug                   show filenames with bugs
     --bug-fix               try to fix filenames with bugs
     --version               print version number
     --examples              print some useful examples
(-h) --help                  show this help (-h works with no other options)

size units:
 1=1b, 1k=1024b, 1m=1024k, 1g=1024m, 1t=1024g
 separator character is :
```
Note: the separator character is Opereting System dependant, so it may change.