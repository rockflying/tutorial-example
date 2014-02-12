package soot.day0;
import java.util.Iterator;
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
import soot.Type;
import soot.Unit;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.AddExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.LongConstant;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.StringConstant;
import soot.options.Options;
import soot.util.Chain;


public class AndroidInstrument4TestLog {
	
	static long count = 0;
	
	public static void main(String[] args) {
		
		Options.v().set_src_prec(Options.src_prec_apk);
		Options.v().set_output_format(Options.output_format_dex);
		
        
		Scene.v().addBasicClass("java.io.PrintStream",SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.System",SootClass.SIGNATURES);
        Scene.v().addBasicClass("android.util.Log",SootClass.SIGNATURES);
        
        
        
        
        
        
        PackManager.v().getPack("jtp").add(new Transform("jtp.myInstrumenter", new BodyTransformer() {

			@Override
			protected void internalTransform(final Body b, String phaseName, @SuppressWarnings("rawtypes") Map options) {
			
				SootClass sClass = Scene.v().getSootClass("purainli.first.FullscreenActivity");
				
				boolean existField = false;
				Chain<SootField> fields = sClass.getFields();
				for (Iterator<SootField> iter = fields.snapshotIterator(); iter.hasNext();)
				{
					SootField field = iter.next();
					if (field.getSignature().equals("<purainli.first.FullscreenActivity: long counter>"))
					{
						existField = true;
						//System.out.println("================================================================");
					}
				}
				
				if (false == existField)
				{
					Type longType = LongType.v();
					SootField field = new SootField("counter", longType, Modifier.PUBLIC | Modifier.STATIC);
					sClass.addField(field);
				}
				
				final PatchingChain<Unit> units = b.getUnits();
				for(Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext();) {
					final Unit u = iter.next();
					
					u.apply(new AbstractStmtSwitch() {
						
						public void caseInvokeStmt(InvokeStmt stmt) {
							InvokeExpr invokeExpr = stmt.getInvokeExpr();
							if(invokeExpr.getMethod().getName().equals("test")) {
								
								System.out.println("before if");
								
								Local tmpString = addTmpString(b);
								Local tmpString2 = addTmpString(b);
											
								Local tmpRef = Jimple.v().newLocal("tmpRef"+(++count), LongType.v());
						        b.getLocals().add(tmpRef);
								

								units.insertBefore(Jimple.v().newAssignStmt(tmpRef, Jimple.v().newStaticFieldRef(
										Scene.v().getField("<purainli.first.FullscreenActivity: long counter>").makeRef())), u);
								
								AddExpr addExpr = Jimple.v().newAddExpr(tmpRef, LongConstant.v(1));
								units.insertBefore(Jimple.v().newAssignStmt(tmpRef, addExpr), u);

								units.insertBefore(Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(
										Scene.v().getField("<purainli.first.FullscreenActivity: long counter>").makeRef()), tmpRef), u);
								
						        units.insertBefore(Jimple.v().newAssignStmt(tmpString, StringConstant.v("JOEY")), u);
						        
						        SootMethod valueOf = Scene.v().getSootClass("java.lang.String").getMethod("java.lang.String valueOf(long)");
						        StaticInvokeExpr staticInvokeExpr = Jimple.v().newStaticInvokeExpr(valueOf.makeRef(), tmpRef);
						        
						        units.insertBefore(Jimple.v().newAssignStmt(tmpString2, staticInvokeExpr), u);
						        
						        SootMethod toCall = Scene.v().getSootClass("android.util.Log").getMethod("int i(java.lang.String,java.lang.String)");

						        System.out.println(toCall);
						        
						        units.insertBefore(Jimple.v().newInvokeStmt(
						        		Jimple.v().newStaticInvokeExpr(
						        				toCall.makeRef(), tmpString, tmpString2)), u);
						        
						        b.validate();
						        
						        System.out.println(b);
							}
						}
					});
				}
			}


		}));
		
		soot.Main.main(args);
	}
    
    private static Local addTmpString(Body body)
    {
        Local tmpString = Jimple.v().newLocal("tmpString"+(++count), RefType.v("java.lang.String")); 
        body.getLocals().add(tmpString);
        return tmpString;
    }
}