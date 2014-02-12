package iccta.analysis;

import java.util.HashMap;
import java.util.Map;

public class Result 
{
	private Map<String, String> cls2comps = null;
	
	public Result()
	{
		cls2comps = new HashMap<String, String>();
	}
	
	public void put(String iccCallerClass, String compClass)
	{
		cls2comps.put(iccCallerClass, compClass);
	}

	public Map<String, String> getCls2comps() {
		return cls2comps;
	}
}
