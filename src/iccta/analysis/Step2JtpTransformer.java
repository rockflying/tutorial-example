package iccta.analysis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import soot.Body;
import soot.BodyTransformer;
import soot.PackManager;
import soot.SootClass;
import soot.Transform;
import soot.options.Options;

/*
 * Get all called classes of the one class's init method
 */
public class Step2JtpTransformer extends BodyTransformer 
{
	public static void main(String[] args)
	{
		Options.v().set_src_prec(Options.src_prec_apk);
		Options.v().set_output_format(Options.output_format_dex);
		
        PackManager.v().getPack("jtp").add(new Transform("jtp.step2", new Step2JtpTransformer()));
        
        while (0 != nonResolvedInvokedClasses.size())
        {
        	sourceInvokedClasses.clear();
        	
        	for (String cls : nonResolvedInvokedClasses)
    		{
        		if (cls.contains(":"))
        		{
        			String[] strs = cls.split(":");
        			String currentCls = strs[strs.length-1];
        			
        			dest2src.put(currentCls, cls);
        			sourceInvokedClasses.add(currentCls + suffix);    //for parse
        		}
        		else
        		{
        			sourceInvokedClasses.add(cls + suffix);    //for parse
        		}
    		}

        	nonResolvedInvokedClasses.clear();
        	
        	try
            {
            	soot.Main.main(args);
            }
            catch (Exception ex)
            {
            	System.out.println("[step2:exception] : " + ex.getMessage());
            	//ex.printStackTrace();
            }
        	
        }
        
	}
	
	private static final String sperator = ":";
	private static final String suffix = ": void <init>";
	
	private static Set<String> sourceInvokedClasses = new HashSet<String>();
	private static Set<String> nonResolvedInvokedClasses = new HashSet<String>();
	private static Map<String, String> dest2src = new HashMap<String, String>();  //to keep call path
	
	public Step2JtpTransformer()
	{
		for (String cls : ProcessHelper.sourceInvokedClasses)
		{
			nonResolvedInvokedClasses.add(cls);        //for quit
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
		
		for (String sourceInvokedClass : sourceInvokedClasses)
		{
			String srcClass = sourceInvokedClass.replace(suffix, "");
			
			if (b.toString().contains(sourceInvokedClass))
			{
				SootClass sc = b.getMethod().getDeclaringClass();
				
				if (ProcessHelper.isAndroidComponentClass(sc))
				{
					if (dest2src.containsKey(srcClass))
					{
						String clss = dest2src.get(srcClass);
						ProcessHelper.put(ProcessHelper.cls2comps, clss.split(sperator)[0], sc.toString());
					}
					else
					{
						ProcessHelper.put(ProcessHelper.cls2comps, srcClass, sc.toString());
					}
					
					ProcessHelper.put(ProcessHelper.cls2callers, srcClass, sc.toString());
				}
				else if (ProcessHelper.isSupperClass(sc, srcClass))
				{
					//ignore the own <init> method.
				}
				else
				{
					if (dest2src.containsKey(srcClass))
					{
						nonResolvedInvokedClasses.add(dest2src.get(srcClass) + sperator + sc.toString());
					}
					else
					{
						nonResolvedInvokedClasses.add(srcClass + sperator + sc.toString());
					}
					
					ProcessHelper.put(ProcessHelper.cls2callers, srcClass, sc.toString());
				}
			}
		}
	}

}
