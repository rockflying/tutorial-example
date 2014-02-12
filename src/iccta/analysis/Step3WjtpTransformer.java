package iccta.analysis;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import soot.PackManager;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootMethod;
import soot.Transform;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.options.Options;

public class Step3WjtpTransformer extends SceneTransformer 
{
	public static Set<String> newCls = new HashSet<String>();
	public static Set<String> callers = new HashSet<String>();
	public static Map<String, String> newCls2originCls = new HashMap<String, String>();
	
	public Step3WjtpTransformer()
	{
		for (Triple t : ProcessHelper.triples)
		{
			callers.add(t.first);
			newCls.add(t.third);
			newCls2originCls.put(t.third, t.second);
		}
	}
	
	
	@Override
	protected void internalTransform(String phaseName, Map<String, String> options) 
	{
		for (Map.Entry<String, String> entry : newCls2originCls.entrySet())
		{
			String key = entry.getKey();
			String value = entry.getValue();
			
			SootClass sootClass = Scene.v().getSootClass(value);
			SootClass newSootClass = InstrumentHelper.cloneSootClass(sootClass, key, phaseName);
			Scene.v().addClass(newSootClass);
			newSootClass.setApplicationClass();
		}
	}
}
