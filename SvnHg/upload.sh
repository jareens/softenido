#!/bin/sh

if [ $# -lt 2 ] ; then
  echo upload.sh version summary
  exit
fi

filename=svnhg-"$1"
filenamezip="$filename".zip
mode=$1

if [ -d $filename ] ; then
  rm -r $filename
fi

if [ -f $filenamezip ] ; then
  rm $filenamezip
fi

cp GPLv3.txt $filename/

cp -R dist $filename/
cp -R GPLv3.txt $filename/
zip $filenamezip \
    $filename/SvnHg.jar \
    $filename/lib/cafe.jar \
    $filename/GPLv3.txt

rm -r $filename

./googlecode_upload.py \
	-s "$2" \
	-p softenido \
	-l Featured,Type-Archive,OpSys-All \
	$filenamezip
