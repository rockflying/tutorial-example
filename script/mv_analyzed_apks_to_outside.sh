#! /bin/sh

# $1 out directory
OUT=out
SRC_DIR=/home/li/github/epiccws/in/category4
DEST_DIR=/home/li/github/epiccws/category4_analyzed
BACKUP=backup


for file in `ls $OUT`
do
	APK=`basename $file .txt`
	mv $SRC_DIR/$APK $DEST_DIR
done

mv $OUT/*  $BACKUP/

