package athena.soot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soot.Body;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.Stmt;
import soot.tagkit.LineNumberTag;
import soot.tagkit.Tag;

public class SootHelper 
{
	/**
	 * Get all Unit from one soot class
	 * 
	 * @param sootClass
	 * @param methodSignature
	 * @return
	 */
	public static Map<String, List<Unit>> getSpecifiedMethodInvokeStmtsFromClass(SootClass sootClass, String methodSignature)
	{
		Map<String, List<Unit>> result = new HashMap<String, List<Unit>>();
		
		List<SootMethod> sootMethods = sootClass.getMethods();
		for (SootMethod sootMethod : sootMethods)
		{
			List<Unit> units = getSpecifiedMethodInvokeStmtsFromMethod(sootMethod, methodSignature);
			if (null != units && !units.isEmpty())
			{
				result.put(sootMethod.getSignature(), units);
			}
		}
		
		return result;
	}
	
	/**
	 * Get all Unit from one soot method, 
	 * one method may have multiple specified method call
	 * 
	 * @param sootMethod
	 * @param methodSignature
	 * @return
	 */
	public static List<Unit> getSpecifiedMethodInvokeStmtsFromMethod(SootMethod sootMethod, String methodSignature)
	{
		List<Unit> result = new ArrayList<Unit>();
		
		Body body = extractBody(sootMethod);
		for (Iterator<Unit> iter = body.getUnits().snapshotIterator(); iter.hasNext(); )
		{
			Stmt stmt = (Stmt) iter.next();
			
			if (stmt.containsInvokeExpr() && 
				stmt.toString().contains(methodSignature))
			{
				result.add(stmt);
			}
		}
		
		return result;
	}
	
	/**
	 * Get the line number of one Unit
	 * 
	 * @param unit
	 * @return
	 */
	public static int getLineNumber(Unit unit)
	{
		List<Tag> tags = unit.getTags();
		if (null == tags)
		{
			throw new RuntimeException("getTags return null from Unit");
		}
		
		int lineNumber = -1;
		
		for (Tag tag : tags)
		{
			if (tag instanceof LineNumberTag)
			{
				System.out.println(tag);
				lineNumber = Integer.parseInt(tag.toString());
				break;
			}
		}
		
		return lineNumber;
	}
	
	public static Body extractBody(SootMethod sootMethod)
	{
		Body body = null;
		
		try
		{
			//getActiveBody may throw RuntimeException
			body = sootMethod.getActiveBody();
		}
		catch (RuntimeException ex)
		{
			System.out.println(ex.getMessage());
		}
		
		
		if (null == body && sootMethod.isConcrete())
		{
			body = sootMethod.retrieveActiveBody();
		}
		
		System.out.println(body);
		
		return body;
	}
	
	public static List<Unit> getContainSpecifiedValueStmts(SootMethod sootMethod, Value value)
	{
		List<Unit> result = new ArrayList<Unit>();
		
		Body body = extractBody(sootMethod);
		
		
		
		for (Iterator<Unit> iter = body.getUnits().snapshotIterator(); iter.hasNext(); )
		{
			Stmt stmt = (Stmt) iter.next();
			
			List<ValueBox> valueBoxes = stmt.getUseAndDefBoxes();
			for (ValueBox valueBox : valueBoxes)
			{
				Value v = valueBox.getValue();
				if (v.equivTo(value))
				{
					result.add(stmt);
				}
			}
		}
		
		return result;
	}
	
	public static List<Unit> getContainSpecifiedValueStmts(SootMethod sootMethod, Value value, Value endStmt)
	{
		List<Unit> result = new ArrayList<Unit>();
		
		Body body = extractBody(sootMethod);
		
		for (Iterator<Unit> iter = body.getUnits().snapshotIterator(); iter.hasNext(); )
		{
			Stmt stmt = (Stmt) iter.next();
			
			if (stmt.equals(endStmt))
			{
				break;
			}
			
			List<ValueBox> valueBoxes = stmt.getUseAndDefBoxes();
			for (ValueBox valueBox : valueBoxes)
			{
				Value v = valueBox.getValue();
				if (v.equivTo(value))
				{
					result.add(stmt);
				}
			}
		}
		
		return result;
	}
}
