package soot.callgraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soot.Local;
import soot.PackManager;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootMethod;
import soot.Transform;
import soot.Type;
import soot.Unit;
import soot.jimple.IdentityStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.Stmt;
import soot.options.Options;

public class ToComponentWholeModel extends SceneTransformer 
{

	private String Signature = "android.app.Activity: void startActivity(android.content.Intent)";
	private String signature2 = "android.content.Context: void startActivity(android.content.Intent)";
	
	public static void main(String[] args) 
	{
		Options.v().set_src_prec(Options.src_prec_apk);
		
		Options.v().set_output_format(Options.output_format_dex);
		
        PackManager.v().getPack("wjtp").add(new Transform("wjtp.myInstrumenter", new ToComponentWholeModel()));

        soot.Main.main(args);
	}

	
	
	@Override
	protected void internalTransform(String phaseName, Map<String, String> options) {
		// TODO Auto-generated method stub

		
		
		//SootClass  c = Scene.v().getSootClass("com.example.epicctest.ToThirdActivityListener");
		
		
		
		SootClass sootClass = Scene.v().getSootClass("com.example.epicctest.ToOtherActivityListener");
		SootClass sc1 = new SootClass(sootClass.toString() + "1");
		sc1.setSuperclass(sootClass);
		
		
		
		
		
		for (SootMethod sm : sootClass.getMethods())
		{
			if (sm.getSignature().toString().contains("<init>"))
			{
				buildConstuctMethod(sc1, sootClass);
			}
		}
		
		Scene.v().addClass(sc1);
		sc1.setApplicationClass();
		
		
		
		//SootMethod sm = new SootMethod("<init>", types);
		
		/*Set<String> clsses = Scene.v().getBasicClasses();
        
		for (String s : clsses)
		{
			System.out.println("--------------------*****--->" + s);
		}*/
		
		/*Chain<SootClass> classes = Scene.v().getClasses();
		
		System.out.println(classes.size());
		
		SootClass sootClass = null;
		
        for (Iterator<SootClass> iter = classes.iterator(); iter.hasNext(); )
        {
        	SootClass sc = iter.next();
        	
        	//System.out.println("-->" + sc);
        	
        	if (sc.toString().equals("com.example.epicctest.ToOtherActivityListener"))
        	{
        		sootClass = sc;
        	}
        }
        
		
		
		
		System.out.println(sc1.isApplicationClass());
		
		
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		
		
		
		for (Iterator<SootClass> iter = Scene.v().getApplicationClasses().iterator(); iter.hasNext();)
    	{
			SootClass sc = iter.next();
    		if (sc.toString().contains("com.example.epicctest.ToOtherActivityListener"))
    			System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&" + sc);
    	}
		
		for (Iterator<SootClass> iter = Scene.v().getApplicationClasses().iterator(); iter.hasNext();)
    	{
			SootClass sc = iter.next();
    		if (sc.toString().contains("com.example.epicctest.ToOtherActivityListener1"))
    			System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%" + sc);
    	}*/
	}
	
	public List<String> primitives = new ArrayList<String>(Arrays.asList(new String[] 
	{
		"boolean", "byte", "char", "short", "int", "long", "float", "double"	
	}));
	
	
	//it is better to return a body since I don't know how to use SootMethod
	public SootMethod buildConstuctMethod(SootClass currentClass, SootClass supperClass)
	{
		SootMethod init = null;
		
		for (SootMethod sm : supperClass.getMethods())
		{
			if (sm.getSignature().toString().contains("<init>"))
			{
				List<Type> paramTypes = sm.getParameterTypes();
				init  = new SootMethod("<init>", sm.getParameterTypes(), sm.getReturnType());
				
				if (null != paramTypes && 0 != paramTypes.size())
				{
					JimpleBody body = Jimple.v().newBody(init);
					init.setActiveBody(body);

					Local _this = Jimple.v().newLocal(getLocalName(), RefType.v(currentClass.toString()));
					body.getLocals().add(_this);
					
					IdentityStmt thisStmt = Jimple.v().newIdentityStmt(_this, Jimple.v().newThisRef(RefType.v(currentClass.toString())));
					
					Local[] locals = new Local[paramTypes.size()];
					IdentityStmt[] istmts = new IdentityStmt[paramTypes.size()];
					
					for (int i = 0; i < paramTypes.size(); i++)
					{
						locals[i] = Jimple.v().newLocal(getLocalName(), paramTypes.get(i));
						body.getLocals().add(locals[i]);
						
						istmts[i] = Jimple.v().newIdentityStmt(locals[i], Jimple.v().newParameterRef(paramTypes.get(i), i));
					}
					
					
					PatchingChain<Unit> units = body.getUnits();
					
					
					
					units.add(thisStmt);
					for (int i = 0; i < istmts.length; i++)
					{
						units.add(istmts[i]);
					}
					
					InvokeExpr specialInvoke = Jimple.v().newSpecialInvokeExpr(_this, sm.makeRef(), Arrays.asList(locals));
					
					units.add(Jimple.v().newInvokeStmt(specialInvoke));
					units.add(Jimple.v().newReturnVoidStmt());
					
					body.validate();
					init.setActiveBody(body);
					
					System.out.println(body);
				}
			}
		}
		
		return init;
	}
	
	String localStartWith = "l";
	static int localCount = 0;
	public String getLocalName()
	{
		return localStartWith + (localCount++);
	}
}
