package iccta.analysis;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.ValueBox;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInvokeStmt;
import soot.jimple.internal.JimpleLocalBox;

public class Step4JtpTransformer extends BodyTransformer 
{
	@Override
	protected void internalTransform(Body b, String phaseName, Map<String, String> options) 
	{
		for (String prefix : ProcessHelper.ignoredPackagePrefix)
		{
			if (b.getMethod().getDeclaringClass().toString().startsWith(prefix))
			{
				return;
			}
		}
		
		for (Triple t : ProcessHelper.triples)
		{
			if (b.getMethod().getDeclaringClass().toString().equals(t.first))
			{
				PatchingChain<Unit> units = b.getUnits();
				
				for (Iterator<Unit> iter = units.iterator(); iter.hasNext(); )
				{
					Stmt stmt = (Stmt) iter.next();
					if (stmt.toString().contains(t.second))
					{
						if (stmt instanceof JAssignStmt)
						{
							((JAssignStmt) stmt).getRightOpBox().setValue(Jimple.v().newNewExpr(RefType.v(t.third)));
						}
						else if (stmt instanceof JInvokeStmt)
						{
							InvokeExpr ie = stmt.getInvokeExpr();
							
							String newMethodSignature = ie.getMethod().getSignature().toString().replace(t.second, t.third);
							SootMethod toCall = Scene.v().getMethod(newMethodSignature);
							
							List<ValueBox> vbs = ie.getUseBoxes();
							Local base = null;
							
							for (ValueBox vb : vbs)
							{
								if (vb instanceof JimpleLocalBox)
								{
									base = (Local) vb.getValue();
								}
							}

							stmt.getInvokeExprBox().setValue(Jimple.v().newSpecialInvokeExpr(base, toCall.makeRef(), ie.getArgs()));
						}
					}
				}
				
			}
		}
	}

}
