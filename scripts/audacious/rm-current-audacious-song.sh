#!/bin/sh
#set the following command in audacious "Song Change" plugin 
#  echo "%f" > audacious-playing.txt

cat audacious-playing.txt | sed s/file:[/][/]// |sed 's/%20/ /g' |sed 's/%5B/\[/g' |sed 's/%5D/\]/g' |sed 's/%28/\(/g' |sed 's/%29/\)/g'  |sed "s/%27/\'/g"   >tmp.txt
rm -i "`cat tmp.txt`"
rm tmp.txt

