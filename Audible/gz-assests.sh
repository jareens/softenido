#!/bin/sh

if [ "$1" = "1" ]; then
	gzip -9 -r assets/help/eng
	gzip -9 -r assets/help/spa
elif [ "$1" = "0" ]; then
	gunzip -rd assets/help/eng/
	gunzip -rd assets/help/spa/
else
	echo "$0 0 gzip assets html files"
	echo "$0 1 gunzip assets html files"
fi
            


