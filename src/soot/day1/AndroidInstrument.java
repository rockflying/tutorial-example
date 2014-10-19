package soot.day1;

import soot.options.Options;


public class AndroidInstrument {
	
	public static void main(String[] args) {
		
		//prefer Android APK files// -src-prec apk
		Options.v().set_src_prec(Options.src_prec_apk);
		
		//output as APK, too//-f J
		Options.v().set_output_format(Options.output_format_jimple);
		
        // resolve the PrintStream and System soot-classes
		//Scene.v().addBasicClass("java.io.PrintStream",SootClass.SIGNATURES);
        //Scene.v().addBasicClass("java.lang.System",SootClass.SIGNATURES);
       
        
        /*PackManager.v().getPack("jtp").add(new Transform("jtp.myInstrumenter", new BodyTransformer() {

			@Override
			protected void internalTransform(final Body b, String phaseName, @SuppressWarnings("rawtypes") Map options) {
				//final PatchingChain<Unit> units = b.getUnits();

			}


		}));*/
		
		String[] args2 = 
		{
			"-android-jars", "C:/Project/sharedfolder/github/android-platforms",
			"-process-dir", "C:/Project/sharedfolder/apks/test-10activities-with-5join-source-sink_bak.apk",
			"-ire", 
			"-pp", 
			"-allow-phantom-refs"
		};
		
		soot.Main.main(args2);
	}
}