echo $1;
echo $2;
echo $3;
echo $4;

count=0;

for apk in `find $1 -type f -exec echo "{}" \;`
do
	size=`ls -l $apk | awk '{ print $6 }'`

	if (( size <= $3 )); then
		echo $size
		if (( size > $2 )); then
			count=$(( count+1 ));
			cp $apk $4/$count.apk
			echo "$size:$apk"

		fi
	fi
done
