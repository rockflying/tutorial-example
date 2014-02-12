package soot.listenercount;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import soot.G;
import soot.PackManager;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.Transform;
import soot.options.Options;
import soot.util.Chain;


public class ListenerCountTransformer extends SceneTransformer 
{
	static BufferedWriter bw = null;
	
	static String[] args2 = 
		{
			"-android-jars", "C:/Users/li.li/Documents/GitHub/android-platforms",
			"-process-dir", "C:/Project/sharedfolder/epicc/apks/EpiccTest.apk",
			"-ire", 
			"-pp", 
			"-allow-phantom-refs",
			"-w", 
			"-p", "cg", "enabled:false"
		};
    static String[] args3 = 
		{
			"-process-dir", "C:/Project/sharedfolder/epicc/apks/EpiccTest.apk",
			"-ire", 
			"-pp", 
			"-allow-phantom-refs",
			"-w", 
			"-p", "cg", "enabled:false"
		};
	
    private static String appName = "";
	private static final String cls = "android.view.View$OnClickListener";
	private static Pair pair = new Pair();
    
	public static void main(String[] args) throws IOException
	{
		bw = new BufferedWriter(new FileWriter(new File("output.txt")));
		
		File dir = new File("apks");
        File[] files = dir.listFiles();
		for (File file : files)
		{
        	appName = file.getName();
        	//appName = "EpiccTest.apk";
        	pair = new Pair();
			G.reset();
			Options.v().set_src_prec(Options.src_prec_apk);
			Options.v().set_output_format(Options.output_format_dex);
			
	        PackManager.v().getPack("wjtp").add(new Transform("wjtp.ListenerCountTransformer", new ListenerCountTransformer()));
			
	        boolean first = true;
	        
	        if (first)
        	{
        		first = false;
        		
    			args2[3] = file.getAbsolutePath();
        		try { soot.Main.main(args2); } catch (Exception ex) {}
        	}
	        else
	        {
    			args3[1] = file.getAbsolutePath();
    			try { soot.Main.main(args3); } catch (Exception ex) {}
	        }
	        
	        System.out.println(appName + " " + pair.inner + " " + pair.nonInner + " " + pair.nonInnerIsComponent);
	        
	        bw.write(appName + "\t\tinner=" + pair.inner + "\t\tnonInner=" + pair.nonInner + "\t\tnonInnerIsComponent=" + pair.nonInnerIsComponent + "\n");
			bw.flush();
		}

		bw.close();
    }
	
	@Override
	protected void internalTransform(String phaseName, Map<String, String> options) 
	{
		Chain<SootClass> scs = Scene.v().getApplicationClasses();
		for (Iterator<SootClass> iter = scs.iterator(); iter.hasNext(); )
		{
			SootClass sc = iter.next();
			
			if (sc.toString().startsWith("android.support"))
			{
				continue;
			}
			
			Chain<SootClass> sootClasses = sc.getInterfaces();
			
			for (Iterator<SootClass> iter2 = sootClasses.iterator(); iter2.hasNext(); )
			{
				SootClass sootClass = iter2.next();
				
				if (sootClass.toString().equals(cls))
				{
					if (sc.toString().contains("$"))
					{
						pair.inner++;
					}
					else
					{
						pair.nonInner++;
						
						if (isAndroidComponentClass(sc))
						{
							pair.nonInnerIsComponent++;
						}
					}
					
					break;
				}
			}
		}
	}
	
	static class Pair
	{
		int inner = 0;
		int nonInner = 0;
		int nonInnerIsComponent = 0;
	}
	
	public static boolean isAndroidComponentClass(SootClass sootClass)
	{
		boolean result = false;
		
		String className = sootClass.getName();
        
        if (componentSuperclasses.contains(className)) 
        {
            result = true;
        }
        
        while (sootClass.hasSuperclass()) 
        {
            sootClass = sootClass.getSuperclass();
            
            if (componentSuperclasses.contains(sootClass.getName())) 
            {
                result = true;
            }
        }
        
		return result;
	}
	
	
	public static Set<String> componentSuperclasses = new HashSet<String>();
	
	static 
	{
		componentSuperclasses.addAll(
            Arrays.asList(
                    "android.content.Context",
                    "android.app.Activity",
                    "android.app.Service",
                    "android.content.BroadcastReceiver",
                    "android.content.ContentProvider"));
	}
}

