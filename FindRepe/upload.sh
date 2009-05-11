#!/bin/sh

if [ $# -lt 4 ] ; then
  echo upload.sh version summary user password
  exit
fi

filename=findrepe-"$1"
filenamezip="$filename".zip

if [ -d $filename ] ; then
  rm -r $filename
fi

if [ -f $filenamezip ] ; then
  rm $filenamezip
fi

cp -R dist $filename/
cp GPLv3.txt $filename/GPLv3.txt

zip $filenamezip \
    $filename/FindRepe.jar \
    $filename/lib/cafe.jar \
    $filename/GPLv3.txt
    
rm -r $filename

./googlecode_upload.py \
	-s "$2" \
	-p softenido \
	-l Featured,Type-Archive,OpSys-All \
	$filenamezip
