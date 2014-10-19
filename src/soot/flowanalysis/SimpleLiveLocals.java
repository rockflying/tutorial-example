package soot.flowanalysis;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soot.Unit;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.LiveLocals;

public class SimpleLiveLocals implements LiveLocals{

	Map unitToLocalsAfter;
    Map unitToLocalsBefore;

    public SimpleLiveLocals(ExceptionalUnitGraph graph)
    {                        
        SimpleLiveLocalsAnalysis analysis = new SimpleLiveLocalsAnalysis(graph);

        // Build unitToLocals map
        {
            unitToLocalsAfter = new HashMap(graph.size() * 2 + 1, 0.7f);
            unitToLocalsBefore = new HashMap(graph.size() * 2 + 1, 0.7f);

            Iterator unitIt = graph.iterator();

            while(unitIt.hasNext())
            {
                Unit s = (Unit) unitIt.next();
 
                FlowSet set = (FlowSet) analysis.getFlowBefore(s);
                unitToLocalsBefore.put(s, 
                                   Collections.unmodifiableList(set.toList()));
                
                set = (FlowSet) analysis.getFlowAfter(s);
                unitToLocalsAfter.put(s, 
                                   Collections.unmodifiableList(set.toList()));
            }            
        }
    }

    public List getLiveLocalsAfter(Unit s)
    {
        return (List) unitToLocalsAfter.get(s);
    }
    
    public List getLiveLocalsBefore(Unit s)
    {
        return (List) unitToLocalsBefore.get(s);
    }

    public Map getUnitToLocalsAfter()
    {
    	return unitToLocalsAfter;
    }
    
    public Map getUnitToLocalsBefore()
    {
    	return unitToLocalsBefore;
    }
}
