package soot.callgraph;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.PackManager;
import soot.PatchingChain;
import soot.Transform;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.Stmt;
import soot.tagkit.Tag;
import soot.util.Chain;


/*
 * 
 * -cp .;C:/Project/workspace/soottest1/for_soot_process_dir -src-prec class  -process-dir  C:/Project/workspace/soottest1/for_soot_process_dir -allow-phantom-refs
 * 
 */
public class VariableDumper extends BodyTransformer {
    public static void main(String[] args) 
    {
		PackManager.v().getPack("jtp").add(
		    new Transform("jtp.dumpcg", new VariableDumper()));
	
		soot.Main.main(args);
		
		/*Options.v().parse(args);
		SootClass c = Scene.v().forceResolve("MyTag", SootClass.BODIES);
		Scene.v().loadNecessaryClasses();
		c.setApplicationClass();
		List<SootMethod> entryPoints = new ArrayList<SootMethod>();
		entryPoints.add(c.getMethodByName("main"));
		Scene.v().setEntryPoints(entryPoints);
		PackManager.v().runPacks();*/
    }

    
    
    @Override
	protected void internalTransform(Body b, String phaseName,
			Map<String, String> options) {
		System.out.println(b);
		
		
		for (Type t : b.getMethod().getParameterTypes())
		{
			System.out.println(t + ":" + t.getNumber() + ":" + t.getArrayType() + ":" + t.getClass().getName());
		}
		
		
		List<ValueBox> vbx = b.getDefBoxes();
		for (ValueBox vb : vbx)
		{
			System.out.println(vb.getValue());
		}
		
		List<Tag> tags = b.getTags();
		for (Tag tag : tags)
		{
			System.out.println(tag.getName() + ":" + tag.getValue());
		}
		
		Chain<Local> list = b.getLocals();
		/*for (Iterator<Local> iter = list.iterator(); iter.hasNext(); )
		{
			Local local = iter.next();
			
			System.out.println(local.getName() + ":" + local.getType() + ":" + local.getNumber() + ":" + local.getUseBoxes());
		}*/
		
		PatchingChain<Unit> units = b.getUnits();
		for (Iterator iter = units.iterator(); iter.hasNext(); )
		{
			Stmt stmt = (Stmt) iter.next();
			
			if (stmt.toString().startsWith("specialinvoke") && stmt.toString().contains("MyTag$Intent: void <init>"))
        	{
				System.out.println("class is ---->" + b.getMethod().getDeclaringClass());
				
				
        		List<ValueBox> vbs = stmt.getUseBoxes();
        		
        		Value v = vbs.get(0).getValue();
        		
        		System.out.println();
        		
        		for (Iterator<Local> iter2 = list.iterator(); iter2.hasNext(); )
        		{
        			Local local = iter2.next();
        			
        			System.out.println(local.getName() + ":" + v);
        			
        			if (local.getName().toString().equals(v.toString()))
        			{
        				System.out.println(local.getName() + ":" + local.getType() + ":" + local.getNumber() + ":" + local.getUseBoxes());
        			}
        			
        			
        		}
        	}
		}
	}


    /*
	protected void internalTransform(String phaseName, Map options)
    {
        CallGraph cg = Scene.v().getCallGraph();

        
        
        
        
        Iterator it = cg.listener();
        while( it.hasNext() ) 
        {
        	soot.jimple.toolkits.callgraph.Edge e = (soot.jimple.toolkits.callgraph.Edge) it.next();
        	
        	if (e.srcStmt().toString().startsWith("virtualinvoke") && e.srcStmt().toString().contains("MyTag: void startActivity(MyTag$Intent)"))
        	{
        		Stmt stmt = e.srcStmt();
        		
        		List<ValueBox> vbs = stmt.getUseBoxes();
        		
        		System.out.println("***************************> " + vbs.size());
        		for (ValueBox vb : vbs)
        		{
        			System.out.println("***************************> " + vb.getValue());
        		}
        	}
        	
            StringBuilder sb = new StringBuilder();
            sb.append("src: " + e.src() + "\n");
            sb.append("srcStmt: " + e.srcStmt() + "\n");
            sb.append("kind: " + e.kind() + "\n");
            sb.append("tgt: " + e.tgt() + "\n");
            sb.append("\n");
            
            System.out.println(sb.toString());
        }
    }*/
}
