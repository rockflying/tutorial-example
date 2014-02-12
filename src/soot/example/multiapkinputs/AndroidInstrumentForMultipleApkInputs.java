package soot.example.multiapkinputs;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import soot.Body;
import soot.PackManager;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.Transform;
import soot.options.Options;
import soot.util.Chain;


public class AndroidInstrumentForMultipleApkInputs {
	
	public static void main(String[] args) {
		
		//prefer Android APK files// -src-prec apk
		Options.v().set_src_prec(Options.src_prec_apk);
		
		//output as APK, too//-f J
		Options.v().set_output_format(Options.output_format_none);
        
        PackManager.v().getPack("wjtp").add(new Transform("wjtp.myInstrumenter", new SceneTransformer() {

			
			@Override
			protected void internalTransform(String phaseName,
					Map<String, String> options) {
				Chain<SootClass> scs = Scene.v().getApplicationClasses();
				for (SootClass sc : scs)
				{
					System.out.println("here" + sc);
				}
			}

		}));
		/*
        String[] args2 = 
		{
    		"-android-jars", "/Users/li.li/Project/github/android-platforms",
			"-process-dir", "test-workspace/apks/inter-app_sourcer.apk",
			"-process-dir", "test-workspace/apks/inter-app_sinker.apk",
			"-ire", 
			"-pp", 
			"-allow-phantom-refs",
			"-w",
			"-p", "cg", "enabled:false"
		};
        */
        
        List<String> processDirs = new ArrayList<String>();
        processDirs.add("test-workspace/apks/inter-app_sourcer.apk");
        processDirs.add("test-workspace/apks/inter-app_sinker.apk");
        
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_android_jars("/Users/li.li/Project/github/android-platforms");
        Options.v().set_soot_classpath("test-workspace/apks/inter-app_sourcer.apk:test-workspace/apks/inter-app_sinker.apk:/Users/li.li/Project/github/android-platforms/android-18/android.jar");
        Options.v().set_process_dir(processDirs);
        Options.v().set_whole_program(true);
        Options.v().setPhaseOption("cg.spark", "on");
        
        soot.Main.main(new String[] {"-ire"});
        
        //PackManager.v().getPack("wjpp").apply();
        //PackManager.v().getPack("cg").apply();
        //PackManager.v().getPack("wjtp").apply();
        //soot.Main.v().run(args);
		//soot.Main.main(args);
	}
}