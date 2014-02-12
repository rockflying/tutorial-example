package iccta.analysis;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import soot.Body;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.jimple.IdentityStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.util.Chain;

public class InstrumentHelper 
{
	public static SootClass cloneSootClass(SootClass sootClass, String newClassName, String phaseName)
	{
		SootClass newSootClass = new SootClass(newClassName);
		newSootClass.setSuperclass(sootClass.getSuperclass());
		
		Chain<SootClass> interfaces = sootClass.getInterfaces();
		for (Iterator<SootClass> iter = interfaces.iterator(); iter.hasNext(); )
		{
			try
			{
				newSootClass.addInterface(iter.next());
			}
			catch (Exception ex)
			{
				//maybe: already declared RuntimeException. extends from supper class
			}
			
		}
		
		Chain<SootField> fields = sootClass.getFields();
		for (Iterator<SootField> iter = fields.iterator(); iter.hasNext(); )
		{
			try
			{
				newSootClass.addField(iter.next());
			}
			catch (Exception ex)
			{
				//maybe: already declared RuntimeException. extends from supper class
			}
		}
		
		List<SootMethod> methods = sootClass.getMethods();
		for (SootMethod method : methods)
		{
			List<Type> paramTypes = method.getParameterTypes();
			SootMethod sm = new SootMethod(method.getName(), paramTypes, method.getReturnType());
			newSootClass.addMethod(sm);
			sm.setActiveBody(method.getSource().getBody(method, phaseName));
		}
		
		return newSootClass;
		
	}
	
	public static void copyMethod(SootClass current, SootClass parent)
	{
		for (SootMethod method : parent.getMethods())
		{
			if (! method.getName().contains("<init>"))
			{
				List<Type> paramTypes = method.getParameterTypes();
				SootMethod sm = new SootMethod(method.getName(), paramTypes, method.getReturnType());
				
				current.addMethod(sm);
				
				JimpleBody body = Jimple.v().newBody(sm);
				sm.setActiveBody(body);
				PatchingChain<Unit> units = body.getUnits();
				
				Body parentBody = method.getActiveBody();
				PatchingChain<Unit> parentUnits = parentBody.getUnits();
				
				for (Iterator<Unit> iter = parentUnits.iterator(); iter.hasNext();)
				{
					units.add(iter.next());
				}
				
				body.validate();
				sm.setActiveBody(body);
				System.out.println(body);
			}
		}
	}
	
	public static void addMethod(SootClass current, SootClass parent)
	{
		for (SootMethod method : parent.getMethods())
		{
			List<Type> paramTypes = method.getParameterTypes();
			SootMethod sm = new SootMethod(method.getName(), paramTypes, method.getReturnType());
			
			current.addMethod(sm);
			
			JimpleBody body = Jimple.v().newBody(sm);
			sm.setActiveBody(body);
			PatchingChain<Unit> units = body.getUnits();
			
			Local _this = Jimple.v().newLocal(getLocalName(), RefType.v(current.toString()));
			body.getLocals().add(_this);
			IdentityStmt thisStmt = Jimple.v().newIdentityStmt(_this, Jimple.v().newThisRef(RefType.v(current.toString())));
			units.add(thisStmt);
			
			Local[] locals = null;
			if (null != paramTypes && 0 != paramTypes.size())
			{
				locals = new Local[paramTypes.size()];
				IdentityStmt[] istmts = new IdentityStmt[paramTypes.size()];
				
				for (int i = 0; i < paramTypes.size(); i++)
				{
					locals[i] = Jimple.v().newLocal(getLocalName(), paramTypes.get(i));
					body.getLocals().add(locals[i]);
					
					istmts[i] = Jimple.v().newIdentityStmt(locals[i], Jimple.v().newParameterRef(paramTypes.get(i), i));
					
					units.add(istmts[i]);
				}
				
			}
			
			InvokeExpr specialInvoke = Jimple.v().newVirtualInvokeExpr(_this, sm.makeRef(), Arrays.asList(locals));
			
			units.add(Jimple.v().newInvokeStmt(specialInvoke));
			units.add(Jimple.v().newReturnVoidStmt());
			
			body.validate();
			sm.setActiveBody(body);
			
			System.out.println(body);

			//current.addMethod(sm);
		}
	}
	
	static int localCount = 0;
	public static String getLocalName()
	{
		return "l" + (localCount++);
	}
}


/*

public void buildConstuctMethod(SootClass currentClass, SootClass supperClass)
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
		
		currentClass.addMethod(init);
	}

*/