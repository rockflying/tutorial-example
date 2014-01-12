package athena.soot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import soot.Body;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Stmt;

public class SootMethodHelper 
{
	public static List<Stmt> getPreviousReverseStmts(SootMethod sootMethod, Stmt stmt)
	{
		List<Stmt> result = new ArrayList<Stmt>();
		
		Body body = extractBody(sootMethod);
		
		for (Iterator<Unit> iter = body.getUnits().snapshotIterator(); iter.hasNext(); )
		{
			Stmt s = (Stmt) iter.next();
			
			//contain current stmt
			result.add(s);
			if (stmt.equals(s))
			{
				break;
			}
		}

		Collections.reverse(result);
		return result;
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
}
