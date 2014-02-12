#! /bin/sh

mysql -uli -pchangeme cc < schema

EPICC_WS_IN=/Users/li.li/Project/epiccws/in/category1

for apk in `ls $EPICC_WS_IN`
do
	echo "run epicc to parse $apk"
	./runSingleApk.sh $apk
done
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
