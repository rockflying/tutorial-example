#! /bin/sh

# $1 : category number

#SRC=../github/epiccws/category$1
#SRC=../github/epiccws/in/category$1

SRC=./combined-apks

ANDROID_PLATFORMS=../github/android-platforms/

OUTPUT=out

rm -rf $OUTPUT
mkdir $OUTPUT

result_file=infoflow_android_epicc_test_result.txt

for apk in `ls $SRC`
do
	#java -Xmx8192m -jar infoflow-epicc.jar $SRC/$apk $ANDROID_PLATFORMS --timeout 5 > $OUTPUT/$apk.txt 2>&1
	./execute_with_limit_time.sh java -Xmx8192m -jar infoflow-epicc.jar $SRC/$apk $ANDROID_PLATFORMS > $OUTPUT/$apk.txt 2>&1

	echo $SRC/$apk

	#python parseResult.py $OUTPUT $apk.txt

	echo $OUTPUT/$apk.txt >> $result_file        #file name
	echo ": " >> $result_file
	echo `grep "IpcSC" $OUTPUT/$apk.txt | grep "on Path" | wc -l` >> $result_file
	#echo `cat $OUTPUT/$apk.txt | grep "IpcSC" | wc -l` >> $result_file	
	echo "\n" >> $result_file
done
