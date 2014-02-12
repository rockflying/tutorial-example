package iccta.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import soot.SootClass;

public class ProcessHelper {
	
	//Listener class -> ICC sources
	public static Map<String, List<String>> cls2sources = new HashMap<String, List<String>>();
	public static Set<String> sourceInvokedClasses = new HashSet<String>();
	
	//Listener class -> used component class. if more than 2 component classes have used one Listener class, that Listener class should been resolved. 
	public static Map<String, List<String>> cls2comps = new HashMap<String, List<String>>();
	
	public static Map<String, List<String>> cls2callers = new HashMap<String, List<String>>();
	
	//caller class -> origin class -> new artifice class
	public static List<Triple> triples = new ArrayList<Triple>();
	
	public static void put(Map<String, List<String>> map, String key, String value)
	{
		List<String> strs = map.get(key);
		if (null == strs)
		{
			strs = new ArrayList<String>();
		}
		
		strs.add(value);
		
		map.put(key, strs);
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
	
	public static boolean isSupperClass(SootClass sootClass, String supperClass)
	{
		boolean result = false;
		String className = sootClass.getName();
		
		if (className.equals(supperClass))
		{
			result = true;
		}
		
		while (sootClass.hasSuperclass())
		{
			sootClass = sootClass.getSuperclass();
			
			if (sootClass.getName().equals(supperClass))
			{
				result = true;
			}
		}
		
		return result;
	}
	
	public static Set<String> componentSuperclasses = new HashSet<String>();
	public static Set<String> iccMethods = new HashSet<String>();
    public static Set<String> ignoredPackagePrefix = new HashSet<String>();
	
    static {
        componentSuperclasses.addAll(
                Arrays.asList(
                        "android.content.Context",
                        "android.app.Activity",
                        "android.app.Service",
                        "android.content.BroadcastReceiver",
                        "android.content.ContentProvider"));
        
        iccMethods.addAll(
        		Arrays.asList("" +
        				": void startActivity(android.content.Intent)>",
        				": void startActivityForResult(android.content.Intent,int)>"));
        
        ignoredPackagePrefix.addAll(
        		Arrays.asList("" +
        				"android.support.v4"));
        
    }
}

