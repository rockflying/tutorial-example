package soot.day0;
import soot.*;
import soot.jimple.*;
import soot.util.*;
import java.util.*;

public class Instrumenter extends BodyTransformer
{
	
	protected void internalTransform(Body body, String phase, Map options) {
		
		SootMethod method = body.getMethod();
		
		System.out.println("instrumenting method : " + method.getSignature());
		
		Chain units = body.getUnits();
		Iterator stmtIt = units.snapshotIterator();
		
		while (stmtIt.hasNext()) {
			Stmt stmt = (Stmt)stmtIt.next();
			if (!stmt.containsInvokeExpr()){
				continue;
			}
			InvokeExpr expr = (InvokeExpr)stmt.getInvokeExpr();
			
			if (expr.getMethod().getSignature().equals("void test()"))
			{
				Scene.v().addBasicClass("Account");
				SootMethod counter = Scene.v().getSootClass("Account").getMethod("void count()");
				
				InvokeExpr reportExpr = Jimple.v().newStaticInvokeExpr(counter.makeRef());
				Stmt countStmt = Jimple.v().newInvokeStmt(reportExpr);
				units.insertBefore(countStmt, stmt);
			}
		}
	}
}