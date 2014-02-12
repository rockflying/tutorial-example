package iccta.analysis;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import soot.PackManager;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootMethod;
import soot.Transform;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.options.Options;
import apkpler.ApkUtil;
import athena.soot.SootHelper;

public class WjtpTestTransformer extends SceneTransformer 
{
	private Set<String> cls;
	private Map<String, Set<String>> cls2comp;
	
	public WjtpTestTransformer(Set<String> cls)
	{
		this.cls = cls;
		cls2comp = new HashMap<String, Set<String>>();
	}
	
	private void put(String cls, String comp)
	{
		Set<String> comps = cls2comp.get(cls);
		if (null == comps)
		{
			comps = new HashSet<String>();
		}
		
		comps.add(comp);
		cls2comp.put(cls, comps);
	}
	
	/*
	 * 1. get cls's all <init> methods
	 * 2. get <init> method's all caller through CallGraph
	 */
	public Set<String> cls2comp(CallGraph cg, Set<String> cls)
	{
		System.out.println("cls2comp***********************************************************");
		
		Set<String> nonCompCls = new HashSet<String>();
		
		for (String className : cls)
		{
			SootClass sootClass = Scene.v().getSootClass(className);
			
			if (ApkUtil.isAndroidComponent(sootClass))
			{	
				put(className, className);
			}
			else
			{
				Set<SootMethod> sms = SootHelper.getMethodByName(sootClass, "<init>");
				
				for (SootMethod sm : sms)
				{
					for (Iterator<Edge> iter = cg.edgesInto(sm); iter.hasNext(); )
					{
						SootMethod srcSM = (SootMethod) iter.next().getSrc();
						if (ApkUtil.isAndroidComponent(srcSM.getDeclaringClass()))
						{
							put(className, srcSM.getDeclaringClass().getName());
						}
						else if (srcSM.getDeclaringClass().getName().equals("java.lang.Object"))
						{
							//ignore this kind of cls
						}
						else
						{
							nonCompCls.add(srcSM.getDeclaringClass().getName());
						}
					}
				}
			}
			
		}
		
		return nonCompCls;
	}
	
	
	@Override
	protected void internalTransform(String phaseName, Map<String, String> options) 
	{
		CallGraph cg = Scene.v().getCallGraph();
		
		Set<String> nonCompCls = cls2comp(cg, cls);
		while (0 != nonCompCls.size())
		{
			nonCompCls = cls2comp(cg, cls);
		}
	}
	
	/**
	 * 
	 * @param cls, all classes include ICC methods includes components and non-components (like OnClickListener)
	 * @return cls2comp mapping, mapping from class to components (maybe it is component to component, nothing changed)
	 */
	public static Map<String, Set<String>> execute()
	{
        String[] args2 = 
		{
			"-android-jars", "C:/Users/li.li/Documents/GitHub/android-platforms",
			"-process-dir", "C:/Project/sharedfolder/epicc/apks/EpiccTest.apk",
			"-ire", 
			"-pp", 
			"-allow-phantom-refs",
			"-w",
			//"-p", "cg", "enabled:false"
		};
        
        Options.v().set_src_prec(Options.src_prec_apk);
		Set<String> cls = new HashSet<String>();
		cls.add("<com.example.epicctest.ToOtherActivityListener: void <init>(android.content.Context)>");
		
		WjtpTestTransformer transfer = new WjtpTestTransformer(cls);
        PackManager.v().getPack("wjtp").add(new Transform("wjtp.WjtpTestTransformer", transfer));
    	soot.Main.main(args2);
    	
    	return transfer.cls2comp;
	}
}
