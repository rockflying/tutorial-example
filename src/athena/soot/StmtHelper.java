package athena.soot;

import java.util.List;

import soot.RefType;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.NewExpr;
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
	
	/**
	 * take <init> as real new expr
	 * 
	 * @param stmt
	 * @param newType
	 * @return
	 */
	public static boolean containNewExpr(Stmt stmt, RefType newType)
	{
		if (isAssignStmt(stmt))
		{
			if (getRightOpOfAssignStmt(stmt) instanceof NewExpr)
			{
				NewExpr newExpr = (NewExpr) getRightOpOfAssignStmt(stmt);
				if (newExpr.getBaseType().equals(newType))
				{
					return true;
				}
			}
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
