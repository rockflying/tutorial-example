package soot.callgraph;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soot.PackManager;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.Transform;
import soot.ValueBox;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.util.Chain;


/*
 * 
 * -cp .;C:/Project/workspace/soottest1/for_soot_process_dir -src-prec class  -process-dir  C:/Project/workspace/soottest1/for_soot_process_dir -allow-phantom-refs -w
 * 
 * 
 */
public class CallGraphDumper extends SceneTransformer {
    public static void main(String[] args) 
    {
		PackManager.v().getPack("wjtp").add(
		    new Transform("wjtp.dumpcg", new CallGraphDumper()));
	
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

    protected void internalTransform(String phaseName, Map options)
    {
        CallGraph cg = Scene.v().getCallGraph();

        
        Chain<SootClass> clsses = Scene.v().getClasses();
        
        for (Iterator<SootClass> iter = clsses.iterator(); iter.hasNext(); )
        {
        	SootClass sc = iter.next();
        	
        	if (sc.toString().equals("com.example.epicctest.ToOtherActivityListener"))
        	{
        		SootClass sc1 = new SootClass(sc.toString() + "1");
				sc.setSuperclass(sc);
				Scene.v().addClass(sc1);
				System.out.println("-----------------------------------<<<<<<<<<<<>>>>>>>>>>>>>>>");
        	}
        }
        
        //cg.findEdge(u, callee);
        
        
        
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
    }
}
