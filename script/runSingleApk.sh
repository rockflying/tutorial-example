#! /bin/sh

mysql -uli -pchangeme cc < schema

EPICC_WS=/Users/li.li/Project/epiccws

./makeInputAppsWithDexpler.sh -t $EPICC_WS/in -o $EPICC_WS/out -a $EPICC_WS/in/category1/$1
./runSingle.sh -t $EPICC_WS/out/category1/$1 -o output_epicc1 -a $EPICC_WS/in/category1/$1
