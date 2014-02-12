package soot.day0;
import soot.PackManager;
import soot.Transform;
import soot.options.Options;


public class AndroidInstrument4CountMethodCall {
	
	public static void main(String[] args) {
		
		for (String arg : args)
		{
			System.out.println(arg);
		}
		
		//prefer Android APK files// -src-prec apk
		Options.v().set_src_prec(Options.src_prec_apk);
		//output as APK, too//-f J
		Options.v().set_output_format(Options.output_format_dex);
		
        
        PackManager.v().getPack("jtp").add(new Transform("jtp.myInstrumenter", new Instrumenter()));
        
		soot.Main.main(args);
	}
	
}