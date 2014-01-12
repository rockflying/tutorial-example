package soot.example.paramref;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soot.MethodOrMethodContext;
import soot.PackManager;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootMethod;
import soot.Transform;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.NewExpr;
import soot.jimple.ReturnStmt;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.options.Options;
import athena.soot.SootHelper;
import athena.soot.SootMethodHelper;
import athena.soot.StmtHelper;


/**
 * may have two ways to compute such path from a parameter of a method to its construct statement
 * 
 * this problem can convert to varible living problem.
 * 
 * 1) first check the parameter object's construct statement.
 * 2) then to find all the path from the point to ICC method.
 * 
 * 
 * from Intent's <init> method to ICC method
 * 
 * @author joey
 *
 */
public class ParaRefChainTest 
{
	public static void main(String[] args) 
	{
		String[] args2 = 
		{
			"-process-dir", "test-workspace",
			"-ire", 
			"-pp", 
			"-allow-phantom-refs",
			"-keep-line-number", 
			"-W",
			//"-p", "cg.spark", "enabled:true"
		};
			
		Options.v().set_src_prec(Options.src_prec_java);
		Options.v().set_output_format(Options.output_format_jimple);
		
		Transform t = new Transform("wjtp.ParaRefChainTest", new ObjChainSceneTransformer());
		PackManager.v().getPack("wjtp").add(t);
		
		soot.Main.main(args2);
	}

}

class ObjChainSceneTransformer extends SceneTransformer
{

	
	public void resolve(List<Stmt> stmts, SootMethod sootMethod, Stmt stmt, Value value)
	{
		List<Stmt> previousStmts = SootMethodHelper.getPreviousReverseStmts(sootMethod, stmt);

		for (int i = 0; i < previousStmts.size(); i++)
		{
			Stmt s = previousStmts.get(i);
			
			if (StmtHelper.contain(s, value))
			{
				//finish condition
				if (0 == i)
				{
					if (s instanceof AssignStmt)
					{
						Value rightOp = ((AssignStmt) s).getRightOp();
						
						if (rightOp instanceof NewExpr)
						{
							stmts.add(s);
							return;
						}
					}
				}

				if (s instanceof AssignStmt)
				{
					Value rightOp = StmtHelper.getRightOpOfAssignStmt(s);
					
					if (rightOp instanceof InvokeExpr)
					{
						SootMethod sm = ((InvokeExpr) rightOp).getMethod();
						List<Stmt> ss = SootMethodHelper.getPreviousReverseStmts(sm, null);
						
						Value v = null;
						
						Stmt returnStmt = ss.get(0);
						if (returnStmt instanceof ReturnStmt)
						{
							v = ((ReturnStmt) returnStmt).getOp();
						}
						
						resolve(stmts, sm, returnStmt, v);
					}
					//else if (CastStmt)...
					else
					{
						//normal assignment
						
						resolve(stmts, sootMethod, s, rightOp);
					}
					
					
				}
				else if (s instanceof IdentityStmt)
				{
					System.out.println(s);
					IdentityStmt is = (IdentityStmt) s;
					System.out.println(is.getRightOp() + ": " + is.getRightOp().getType());
					
					String stmtStr = is.toString();
					int paramIndex = Integer.parseInt(stmtStr.substring("@parameter".length(), stmtStr.indexOf(':')).trim());
					
					//Scene.v().getCallGraph().
					
					Iterator<Edge> entries = Scene.v().getCallGraph().edgesInto(sootMethod);
					
					while (entries.hasNext())
					{
						Edge edge = entries.next();
						
						MethodOrMethodContext ctx = edge.getSrc();
						
						
						System.out.println(edge);
					}
					
					System.out.println(paramIndex);
					
					//is.get
				}
				else
				{
					stmts.add(s);
				}
			}
			
			
			/*else
			{
				if (StmtHelper.contain(s, value))
				{
					stmts.add(s);
				}
			}*/
			
		}

	}
	
	@Override
	protected void internalTransform(String phaseName, Map<String, String> options) 
	{
		CallGraph cg = Scene.v().getCallGraph();
		
		String methodSignature = "<Love: void startActivity(java.lang.Object)>";
		
		SootClass love = Scene.v().getSootClass("Love");
		
		SootMethod loveMethod = love.getMethodByName("love");
		
		Map<String, List<Unit>> map = SootHelper.getSpecifiedMethodInvokeStmtsFromClass(love, methodSignature);
		
		System.out.println(map);
		
		Stmt stmt = (Stmt)map.get("<Love: void love(Love,Love)>").get(0);
		
		Value arg0 = stmt.getInvokeExpr().getArg(0);
		
		List<Stmt> stmts = new ArrayList<Stmt>();
		
		resolve(stmts, loveMethod, stmt, arg0);
		
		System.out.println(stmts);
		
		//List<Unit> units = SootHelper.getContainSpecifiedValueStmts(loveMethod, arg0);
		
		
		
		//System.out.println(units);
		
		/*
		List<SootMethod> sms = love.getMethods();
		for (SootMethod sm : sms)
		{
			System.out.println(sm);
			if (sm.isConcrete())
			{
				System.out.println(sm.retrieveActiveBody());
				
				Body body = sm.retrieveActiveBody();
				for (Iterator<Unit> iter = body.getUnits().snapshotIterator(); iter.hasNext(); )
				{
					Stmt stmt = (Stmt) iter.next();
					
					System.out.println("line number: " + SootHelper.getLineNumber(stmt));
					
					if (stmt.containsInvokeExpr())
					{
						if (stmt.getInvokeExpr().getMethod().getName().equals("startActivity"))
						{
							List<Tag> tags = stmt.getTags();
							
							for (Tag t : tags)
							{
								System.out.println(t + "-->" + t.getName() + ":" + t.getValue());
								
								
							}
							
							LineNumberTag tag = (LineNumberTag) stmt.getTag("LineNumberTag");
							System.out.println(tag);
							
							System.out.println(stmt);
						}
					}
				}
			}
		}
		*/
		//SootMethod sm = Scene.v().getMethod("<Love: void startActivity(java.lang.Object)>");
		
		//System.out.println(sm);
	}
	
	//public void resolve(List<Unit> units, )
}