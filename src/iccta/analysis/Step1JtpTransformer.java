package iccta.analysis;

import java.util.Iterator;
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.PackManager;
import soot.PatchingChain;
import soot.SootClass;
import soot.Transform;
import soot.Unit;
import soot.jimple.Stmt;
import soot.options.Options;

public class Step1JtpTransformer extends BodyTransformer 
{
	public static void main(String[] args)
	{
		Options.v().set_src_prec(Options.src_prec_apk);
		//Options.v().set_output_format(Options.output_format_dex);
		
        PackManager.v().getPack("jtp").add(new Transform("jtp.step1", new Step1JtpTransformer()));
        
        try
        {
        	soot.Main.main(args);
        }
        catch (Exception ex)
        {
        	System.out.println("[step1:exception] : " + ex.getMessage());
        }

	}
	
	
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
		
		PatchingChain<Unit> units = b.getUnits();
		for (Iterator<Unit> iter = units.iterator(); iter.hasNext(); )
		{
			Stmt stmt = (Stmt) iter.next();
			
			for (String iccMethod : ProcessHelper.iccMethods)
			{	
				if (stmt.toString().contains(iccMethod))
				{
					SootClass sc = b.getMethod().getDeclaringClass();
					ProcessHelper.put(ProcessHelper.cls2sources, sc.toString(), stmt.toString());
					
					if (! ProcessHelper.isAndroidComponentClass(sc))
					{
						ProcessHelper.sourceInvokedClasses.add(sc.toString());
					}
					
					break;
				}
			}
		}
	}

}
