#!/bin/sh

if [ $# -lt 3 ] ; then
  echo upload.sh mode version summary
  echo mode 0=normal 1=proguard
  exit
fi

filename=findrepe-"$2"
filenamezip="$filename".zip
mode=$1

if [ -d $filename ] ; then
  rm -r $filename
fi

if [ -f $filenamezip ] ; then
  rm $filenamezip
fi

cp GPLv3.txt $filename/GPLv3.txt

if [ $1 -eq 1]
  proguard @findrepe.proguard
  cp -R FindRepe.jar $filename/
  zip $filenamezip \
      $filename/FindRepe.jar \
      $filename/GPLv3.txt
else
  cp -R dist $filename/
  zip $filenamezip \
      $filename/FindRepe.jar \
      $filename/lib/cafe.jar \
      $filename/GPLv3.txt
fi

rm -r $filename

./googlecode_upload.py \
	-s "$3" \
	-p softenido \
	-l Featured,Type-Archive,OpSys-All \
	$filenamezip
