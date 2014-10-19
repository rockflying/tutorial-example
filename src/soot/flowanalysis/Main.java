package soot.flowanalysis;

import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.PackManager;
import soot.Transform;
import soot.toolkits.graph.ExceptionalUnitGraph;

public class Main {

	public static void main(String[] args) {
		if(args.length == 0)
        {
            System.out.println("Syntax: java "+
                "soot.examples.instrumentclass.Main --app mainClass "+
                "[soot options]");
            System.exit(0);
        }            
        
        PackManager.v().getPack("jtp").add(
             new Transform("jtp.flowanalysisinstrumenter", new FlowAnalysisBodyTransformer()));
        soot.Main.main(args);

	}

}

class FlowAnalysisBodyTransformer extends BodyTransformer
{
	public FlowAnalysisBodyTransformer() {}
	
	@Override
	protected void internalTransform(Body b, String phaseName, Map<String, String> options) 
	{
		
		new GuaranteedDefs(new ExceptionalUnitGraph(b));
	}
	
}


/*System.out.println("------------------------------>" + phaseName);

for (Map.Entry<String, String> entry : options.entrySet())
{
	String key = entry.getKey();
	String value = entry.getValue();
	
	System.out.println("------------------------------>" + key + ": " + value);
}

ExceptionalUnitGraph eug = new ExceptionalUnitGraph(b);

SimpleLiveLocals sl = new SimpleLiveLocals(eug);*/
/*Map m = sl.getUnitToLocalsAfter();
Set<Object> keys = m.keySet();
for (Object o : keys)
{
	Object value = m.get(o);
	System.out.println(o + ": " + value);
}*/