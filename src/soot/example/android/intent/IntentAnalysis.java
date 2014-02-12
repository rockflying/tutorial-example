package soot.example.android.intent;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import athena.soot.StmtHelper;

import soot.Body;
import soot.G;
import soot.PackManager;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootMethod;
import soot.Transform;
import soot.Unit;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.options.Options;
import soot.util.Chain;

public class IntentAnalysis extends SceneTransformer {

	public static void main(String[] args)
	{
		IntentAnalysis intentAnalysis = new IntentAnalysis();
		intentAnalysis.initSoot();
		
	}
	
	public void initSoot()
	{
		G.reset();
		
		Options.v().set_src_prec(Options.src_prec_apk);
		Options.v().set_output_format(Options.output_format_none);
		
        PackManager.v().getPack("wjtp").add(new Transform("wjtp.IntentAnalysis", new IntentAnalysis()));
        
        String[] args = 
		{
			"-android-jars", "/Users/li.li/Project/github/android-platforms",
			"-process-dir", "/Users/li.li/Project/apks/lu.wort.main-1.apk",
			"-ire", 
			"-pp", 
			"-allow-phantom-refs",
			"-w",
			"-p", "cg", "enabled:true"
		};
        
        //PackManager.v().getPack("wjpp").apply();
        //PackManager.v().getPack("cg").apply();
        //PackManager.v().getPack("wjtp").apply();
        
    	soot.Main.main(args);
	}

	@Override
	protected void internalTransform(String phaseName, Map<String, String> options) 
	{
		CallGraph cg = Scene.v().getCallGraph();
		
		RefType intentType = RefType.v("android.content.Intent");
		
		Chain<SootClass> scs = Scene.v().getClasses();
		for (Iterator<SootClass> iter = scs.snapshotIterator(); iter.hasNext(); )
		{
			SootClass sc = iter.next();
			System.out.println(sc);
			List<SootMethod> sms = sc.getMethods();
			for (SootMethod sm : sms)
			{
				System.out.println(sm);
				Body body = sm.retrieveActiveBody();
				
				PatchingChain<Unit> units = body.getUnits();
				for (Iterator<Unit> unitIter = units.snapshotIterator(); unitIter.hasNext(); )
				{
					Stmt stmt = (Stmt) unitIter.next();
					if (StmtHelper.containNewExpr(stmt, intentType))
					{
						System.out.println("****" + stmt);
					}
				}
			}
		}
		
		/*
		List<Type> parameters = new ArrayList<Type>();
        parameters.add();
        Type returnType = VoidType.v();
        int modifiers = Modifier.PUBLIC;
        
		SootMethod sootMethod = new SootMethod("startActivity", parameters, returnType, modifiers);
		
		Iterator<Edge> edges = cg.edgesInto(sootMethod);
		while (edges.hasNext())
		{
			Edge edge = edges.next();
			System.out.println("-->" + edge.getSrc());
			System.out.println("-->" + edge.getTgt());
		}
		*/
		
		System.out.println("********************************");
	}

	
}
