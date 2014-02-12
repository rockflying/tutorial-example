package iccta.instrument;

import iccta.analysis.InstrumentHelper;
import iccta.analysis.OS;

import java.util.Map;

import soot.Body;
import soot.PackManager;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.Transform;
import soot.options.Options;

public class CloneClassTransformer extends SceneTransformer 
{
	public static void main(String[] args)
	{
		Options.v().set_src_prec(Options.src_prec_apk);
		Options.v().set_output_format(Options.output_format_dex);
		
        PackManager.v().getPack("wjtp").add(new Transform("wjtp.CloneClassTransformer", new CloneClassTransformer()));
        
        String[] args2 = 
    		{
    			"-android-jars", "C:/Users/li.li/Documents/GitHub/android-platforms",
    			"-process-dir", "C:/Project/sharedfolder/epicc/apks/EpiccTest.apk",
    			"-ire", 
    			"-pp", 
    			"-allow-phantom-refs",
    			"-w",
    			"-p", "cg", "enabled:false"
    		};
        
        try
        {
        	OS.delete("sootOutput/EpiccTest.apk");
        	soot.Main.main(args2);
        }
        catch (Exception ex)
        {
        	System.out.println("[CloneClassTransformer:exception] : " + ex.getMessage());
        	ex.printStackTrace();
        }

	}
	
	
	
	
	@Override
	protected void internalTransform(String phaseName, Map<String, String> options) 
	{
		System.out.println("88888888888888" + phaseName);
		
		SootClass sc = Scene.v().getSootClass("com.example.epicctest.ToOtherActivityListener");
		
		SootClass newSootClass = InstrumentHelper.cloneSootClass(sc, "com.example.epicctest.ToOtherActivityListener224", phaseName);
		
		Scene.v().addClass(newSootClass);
		newSootClass.setApplicationClass();
	}




	protected void internalTransform(Body b, String phaseName, Map<String, String> options) 
	{
		
		if (b.getMethod().getDeclaringClass().toString().contains("ToThirdActivityListener"))
		{
			System.out.println(b);
			
			
		}

	}

}
