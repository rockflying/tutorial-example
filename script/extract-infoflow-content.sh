#! /bin/sh

list='es.skyneth.latigo-apkpler-com.agiletech.VerticalPlayer.apk.txt 
es.skyneth.latigo-apkpler-com.dci.fruitbloxlite.apk.txt
es.skyneth.latigo-apkpler-com.electricpocket.bugme.lite.apk.txt
es.skyneth.latigo-apkpler-com.phantomefx.leprecoin_x.apk.txt
es.skyneth.latigo-apkpler-com.simsoftrd.android_pauker.apk.txt
es.skyneth.latigo-apkpler-com.tumblr.shino.shotquote.apk.txt
es.skyneth.latigo-apkpler-edu.rit.netip.cloud.raytracer.apk.txt
es.skyneth.latigo-apkpler-net.inkcode.qute.apk.txt
es.skyneth.latigo-apkpler-nl.hj200.imageview3d.apk.txt
es.skyneth.latigo-apkpler-org.leetzone.android.yatsewidgetqueueplugin.apk.txt
trafficinfo.paq-apkpler-com.agiletech.VerticalPlayer.apk.txt
trafficinfo.paq-apkpler-com.dci.fruitbloxlite.apk.txt
trafficinfo.paq-apkpler-com.electricpocket.bugme.lite.apk.txt
trafficinfo.paq-apkpler-com.phantomefx.leprecoin_x.apk.txt
trafficinfo.paq-apkpler-com.simsoftrd.android_pauker.apk.txt
trafficinfo.paq-apkpler-com.tumblr.shino.shotquote.apk.txt
trafficinfo.paq-apkpler-edu.rit.netip.cloud.raytracer.apk.txt
trafficinfo.paq-apkpler-net.inkcode.qute.apk.txt
trafficinfo.paq-apkpler-nl.hj200.imageview3d.apk.txt
trafficinfo.paq-apkpler-org.leetzone.android.yatsewidgetqueueplugin.apk.txt
'

for i in $list; do

	echo $i
	grep "soot.jimple.infoflow.Infoflow" $i | sed 's@\[main\]\ INFO\ soot\.jimple\.infoflow\.Infoflow\ \-@@' >> ../$i
done

