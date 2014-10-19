package soot.callgraph;

import java.util.Iterator;
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.PackManager;
import soot.PatchingChain;
import soot.Scene;
import soot.SootClass;
import soot.Transform;
import soot.Unit;
import soot.jimple.Stmt;
import soot.options.Options;

/*
 * ICC Graph => Inter-Component Communication Graph
 * 

Step1: Check ICC system method's class is component of not, if it is, then finished. if not, to Setp2.
Step2: Check again to identify where the found class has been initialized. if the class has been initialized in multi-component, then go to Step3.
Step3: Split the found class to multi-class (based on the size of components).
	In this stituation, we need do the next 2 things:
	1) to make it simple, we just copy the original class to multiple pieces by setting as different name.
	2) to modify the initialized class name as the correct one which created from 1).
  

Modified APK -> epicc -> information -> build ICC Graph --->marked ICC Graph->parse to results 
			 -> Flowdroid ----------------> taint paths--|
			 
			 
APK -> epicc -> infomation -> Modify APK to a big component -> Flowdroid -> results 

-process-dir C:/Project/sharedfolder/epicc/apks/EpiccTest.apk -android-jars C:/Users/li.li/Documents/GitHub/android-platforms -cp C:/Project/sharedfolder/epicc/apks/EpiccTest.apk -ire -pp -allow-phantom-refs

*/
public class ToComponent extends BodyTransformer 
{

	private String Signature = "android.app.Activity: void startActivity(android.content.Intent)";
	private String signature2 = "android.content.Context: void startActivity(android.content.Intent)";
	
	public static void main(String[] args) 
	{
		Options.v().set_src_prec(Options.src_prec_apk);
		
		Options.v().set_output_format(Options.output_format_dex);
		
        PackManager.v().getPack("jtp").add(new Transform("jtp.myInstrumenter", new ToComponent()));
        
        try
        {
        	soot.Main.main(args);
        }
        catch (Exception ex)
        {
        	System.out.println("*************************************************" + ex.getMessage());
        }
        
        
        /*PackManager.v().getPack("jtp").add(new Transform("jtp.myInstrumenter2", new CheckInit("com.example.epicctest.ToOtherActivityListener")));
	
        soot.Main.main(args);*/
	}

	@Override
	protected void internalTransform(Body b, String phaseName, Map<String, String> options) 
	{
		if (b.toString().contains("ToOtherActivityListener"))
		{
			System.out.println(b);
		}
		
		PatchingChain<Unit> units = b.getUnits();
		for (Iterator<Unit> iter = units.iterator(); iter.hasNext(); )
		{
			Stmt stmt = (Stmt) iter.next();
			
			if (stmt.toString().contains("android.content.Context: void startActivity(android.content.Intent)"))
			{
				System.out.println("----->" + b.getMethod().getDeclaringClass());
				
				SootClass sootClass = b.getMethod().getDeclaringClass();
				
				SootClass sc = new SootClass(b.getMethod().getDeclaringClass().toString() + "1");
				sc.setSuperclass(sootClass);
				Scene.v().addClass(sc);
				
				
				System.out.println("*****>" + sootClass.getSuperclass().toString());
				
				break;
			}
			
			//System.out.println(stmt.getInvokeExpr());
			
			
		}
		
		
		
		/*if (b.toString().contains("ToOtherActivityListener"))
		{
			System.out.println(b);
		}*/
		
	}

	
}

/*class CheckInit extends BodyTransformer 
{
	private String clsName;
	
	public CheckInit(String clsName)
	{
		this.clsName = clsName;
	}
	
	@Override
	protected void internalTransform(Body b, String phaseName, Map<String, String> options) 
	{
		System.out.println(b);
		
		PatchingChain<Unit> units = b.getUnits();
		for (Iterator<Unit> iter = units.iterator(); iter.hasNext(); )
		{
			Stmt stmt = (Stmt) iter.next();
			if (stmt.toString().contains(clsName + ": void <init>"))
			{
				System.out.println("----->" + b.getMethod().getDeclaringClass());
				
				SootClass sootClass = b.getMethod().getDeclaringClass();
				
				System.out.println("*****>" + sootClass.getSuperclass().toString());
			}
			
		}
		
	}
}*/
