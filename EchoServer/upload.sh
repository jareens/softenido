#!/bin/sh

if [ $# -lt 3 ] ; then
  echo upload.sh mode version summary
  echo mode 0=normal 1=proguard
  exit
fi

filename=echoserver-"$2"
filenamezip=../"$filename".zip
mode=$1

if [ -d $filename ] ; then
  rm -r $filename
fi

if [ -f $filenamezip ] ; then
  rm $filenamezip
fi

cp GPLv3.txt $filename/

if [ $1 -eq 1] ; then
  proguard @echoserver.proguard
  cp -R EchoServer.jar $filename/
  cp -R GPLv3.txt $filename/
  zip $filenamezip $filename/EchoServer.jar $filename/GPLv3.txt
  rm EchoServer.jar
else
  cp -R dist $filename/
  cp -R GPLv3.txt $filename/
  zip $filenamezip \
      $filename/EchoServer.jar \
      $filename/lib/cafe.jar \
      $filename/GPLv3.txt
fi


rm -r $filename

./googlecode_upload.py \
	-s "$3" \
	-p softenido \
	-l Featured,Type-Archive,OpSys-All \
	$filenamezip
