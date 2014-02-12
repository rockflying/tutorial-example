package iccta.analysis;

import soot.G;
import soot.PackManager;
import soot.Transform;
import soot.options.Options;

public class Process {

	public static void main(String[] args) 
	{
		Process p = new Process();
		
		//p.step1();
		
		//System.out.println("********************" + ProcessHelper.sourceInvokedClasses);
		
		/*p.step2();
		
		RebuildGraph g = new RebuildGraph();
		g.buildGraph();
		g.resolve();
		
		System.out.println(ProcessHelper.cls2sources);
		System.out.println(ProcessHelper.cls2callers);
		System.out.println(ProcessHelper.cls2comps);
		System.out.println(ProcessHelper.sourceInvokedClasses);
		
		System.out.println(ProcessHelper.triples);
		*/
		p.step3();
	}

	public void step1()
	{
		String[] args = 
		{
			"-android-jars", "C:/Users/li.li/Documents/GitHub/android-platforms",
			"-process-dir", "C:/Project/sharedfolder/epicc/apks/EpiccTest.apk",
			"-ire", 
			"-pp", 
			"-allow-phantom-refs"
		};
		
		G.reset();
		Step1JtpTransformer.main(args);
	}
	
	public void step2()
	{
		String[] args = 
		{
			//"-android-jars", "C:/Users/li.li/Documents/GitHub/android-platforms",
			"-process-dir", "C:/Project/sharedfolder/epicc/apks/EpiccTest.apk",
			"-ire", 
			"-pp", 
			"-allow-phantom-refs"
		};
		
		Step2JtpTransformer.main(args);
	}
	
	public void step3()
	{
		G.reset();
		
		Options.v().set_src_prec(Options.src_prec_apk);
		Options.v().set_output_format(Options.output_format_dex);
		
        PackManager.v().getPack("wjtp").add(new Transform("wjtp.step3", new Step3WjtpTransformer()));
        PackManager.v().getPack("jtp").add(new Transform("jtp.step4", new Step4JtpTransformer()));
        
        String[] args2 = 
		{
			"-android-jars", "C:/Users/li.li/Documents/GitHub/android-platforms",
			"-process-dir", "C:/Project/sharedfolder/epicc/apks/EpiccTest.apk",
			"-ire", 
			"-pp", 
			"-allow-phantom-refs",
			"-w",
			//"-p", "cg", "enabled:false"
		};
        
        
        OS.delete("sootOutput/EpiccTest.apk");
    	soot.Main.main(args2);
	}
}
