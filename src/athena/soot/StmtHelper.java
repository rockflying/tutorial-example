package athena.soot;

import java.util.List;

import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.Stmt;

public class StmtHelper 
{
	public static boolean contain(Stmt stmt, Value value)
	{
		List<ValueBox> valueBoxes = stmt.getUseAndDefBoxes();
		for (ValueBox valueBox : valueBoxes)
		{
			Value v = valueBox.getValue();
			if (v.equivTo(value))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isAssignStmt(Stmt stmt)
	{
		if (stmt instanceof AssignStmt)
		{
			return true;
		}
		
		return false;
	}
	
	public static Value getLeftOpOfAssignStmt(Stmt stmt)
	{
		if (! isAssignStmt(stmt))
		{
			throw new RuntimeException(stmt + " is not an AssignStmt");
		}
		
		return ((AssignStmt) stmt).getLeftOp();
	}
	
	public static Value getRightOpOfAssignStmt(Stmt stmt)
	{
		if (! isAssignStmt(stmt))
		{
			throw new RuntimeException(stmt + " is not an AssignStmt");
		}
		
		return ((AssignStmt) stmt).getRightOp();
	}
	
	
}
