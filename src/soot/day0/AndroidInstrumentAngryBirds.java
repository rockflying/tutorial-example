package soot.day0;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.LongType;
import soot.Modifier;
import soot.PackManager;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Transform;
import soot.Unit;
import soot.jimple.AddExpr;
import soot.jimple.Jimple;
import soot.jimple.LongConstant;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.options.Options;


public class AndroidInstrumentAngryBirds 
{

	public static void main(String[] args) {
		Options.v().set_src_prec(Options.src_prec_apk);
		Options.v().set_output_format(Options.output_format_dex);
		
		Scene.v().addBasicClass("java.io.PrintStream",SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.System",SootClass.SIGNATURES);
        Scene.v().addBasicClass("android.util.Log",SootClass.SIGNATURES);

        
        PackManager.v().getPack("jtp").add(new Transform("jtp.myInstrumenter", new AngryBirdsBodyTransformer()));
        
        soot.Main.main(args);
	}
}


class AngryBirdsBodyTransformer extends BodyTransformer
{
	boolean addedFieldToAppClass = false;
	String appClassName = "com.rovio.ka3d.App";
	String methodSignature = "<com.flurry.android.Flog: int a(java.lang.String,java.lang.String)>";
	String fieldSignature = "<com.rovio.ka3d.App: long counter>";
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void internalTransform(Body b, String phaseName, Map options) 
	{
		
		if (! addedFieldToAppClass)
		{
			SootClass sClass = Scene.v().getSootClass(appClassName);
			SootField field = new SootField("counter", LongType.v(), Modifier.PUBLIC | Modifier.STATIC);
			sClass.addField(field);
			addedFieldToAppClass = true;
		}
		
		
		final SootMethod method = b.getMethod();
		
		if (methodSignature.equals(method.getSignature()))
		{
		
			final PatchingChain<Unit> units = b.getUnits();
			for(Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext();) 
			{
				Stmt stmt = (Stmt)iter.next();
				
				if (stmt instanceof ReturnStmt || stmt instanceof ReturnVoidStmt)
				{
					Local tmpStr1 = Jimple.v().newLocal("tmpStr1", RefType.v("java.lang.String")); 
			        b.getLocals().add(tmpStr1);
			        
			        Local tmpStr2 = Jimple.v().newLocal("tmpStr2", RefType.v("java.lang.String")); 
			        b.getLocals().add(tmpStr2);
			        
			        Local tmpRef = Jimple.v().newLocal("tmpRef", LongType.v());
			        b.getLocals().add(tmpRef);
			        
			        //tmpRef = com.rovio.ka3d.App.counter;
			        units.insertBefore(Jimple.v().newAssignStmt(tmpRef, 
			        		Jimple.v().newStaticFieldRef(Scene.v().getField(fieldSignature).makeRef())), stmt);
			        
			        //tmpRef = tmpRef + 1;
			        AddExpr addExpr = Jimple.v().newAddExpr(tmpRef, LongConstant.v(1));
					units.insertBefore(Jimple.v().newAssignStmt(tmpRef, addExpr), stmt);
					
					//com.rovio.ka3d.App.counter = tmpRef;
					units.insertBefore(Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(
							Scene.v().getField(fieldSignature).makeRef()), tmpRef), stmt);
					
					//tmpStr1 = "JOEY"
					units.insertBefore(Jimple.v().newAssignStmt(tmpStr1, StringConstant.v("JOEY")), stmt);
					
					//tmpStr2 = java.lang.String.valueOf(tmpRef);
					SootMethod valueOf = Scene.v().getSootClass("java.lang.String").getMethod("java.lang.String valueOf(long)");
			        StaticInvokeExpr staticInvokeExpr = Jimple.v().newStaticInvokeExpr(valueOf.makeRef(), tmpRef);
			        units.insertBefore(Jimple.v().newAssignStmt(tmpStr2, staticInvokeExpr), stmt);
			        
			        //Log.i(tmpStr1, tmpStr2);
			        SootMethod toCall = Scene.v().getSootClass("android.util.Log").getMethod("int i(java.lang.String,java.lang.String)");
			        units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(
			        				toCall.makeRef(), tmpStr1, tmpStr2)), stmt);
			        
			        b.validate();
			        System.out.println(b);
				}
			}
		}
	}
	
}


