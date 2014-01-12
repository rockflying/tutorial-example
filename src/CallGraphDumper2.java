import java.util.ArrayList;
import java.util.Arrays;
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
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Targets;

public class CallGraphDumper2 extends SceneTransformer{

	public static void main(String[] args)
	{
		List<String> argsList = new ArrayList<String>(Arrays.asList(args));
		
		argsList.addAll(Arrays.asList(new String[] {"-w", 
				"-src-prec", "java", 
				"-main-class", "CallGraphDumper2", "CallGraphDumper2"}));
		
		CallGraphDumper2 cc = new CallGraphDumper2();
		
		PackManager.v().getPack("wjtp").add(new Transform("wjtp.ifds", cc));
		
		args = argsList.toArray(new String[0]);
		
		cc.printMethod();
		
		soot.Main.main(args);
	}
	
	@Override
	protected void internalTransform(String phaseName, Map options) 
	{
		//CHATransformer.v().transform();
		
		SootClass a = Scene.v().getSootClass("CallGraphDumper2");
		CallGraph callGraph = Scene.v().getCallGraph();
		
		SootMethod src = a.getMethodByName("printMethod");
		
		
		Iterator<MethodOrMethodContext> targets = new Targets(callGraph.edgesOutOf(src));
		
		while (targets.hasNext())
		{
			SootMethod m = (SootMethod) targets.next();
			a.setResolvingLevel(1);
			
			System.out.println(m);
		}
	}
	
	public void printMethod()
	{
		System.out.println("haha");
	}

}