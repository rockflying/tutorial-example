package soot.def;
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

public class CallGraphDumper extends SceneTransformer{

	public static void main(String[] args)
	{
		List<String> argsList = new ArrayList<String>(Arrays.asList(args));
		
		argsList.addAll(Arrays.asList(new String[] {"-w", "-main-class", "CallGraphDumper","CallGraphDumper","-v"}));
		
		CallGraphDumper cc = new CallGraphDumper();
		
		PackManager.v().getPack("wjtp").add(new Transform("wjtp.ifds", cc));
		
		args = argsList.toArray(new String[0]);
		
		soot.Main.main(args);
	}
	
	@Override
	protected void internalTransform(String phaseName, Map options) 
	{
		CHATransformer.v().transform();
		
		SootClass a = Scene.v().getSootClass("CallGraphDumper");
		CallGraph callGraph = Scene.v().getCallGraph();
		
		SootMethod src = Scene.v().getMainClass().getMethodByName("printMethod");
		
		
		Iterator<MethodOrMethodContext> targets = new Targets(callGraph.edgesOutOf(src));
		
		while (targets.hasNext())
		{
			SootMethod m = (SootMethod) targets.next();
			a.setResolvingLevel(1);
			
			
		}
	}
	
	public void printMethod()
	{
		System.out.println("haha");
	}

}