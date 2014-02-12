package soot.flowanalysis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import soot.Local;
import soot.Unit;
import soot.ValueBox;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArrayFlowUniverse;
import soot.toolkits.scalar.ArrayPackedSet;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.BoundedFlowSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.FlowUniverse;
import soot.util.Chain;

public class SimpleLiveLocalsAnalysis extends BackwardFlowAnalysis
{
    FlowSet emptySet;
    Map unitToGenerateSet;
    Map unitToPreserveSet;

    protected Object newInitialFlow()
    {
        return emptySet.clone();
    }
    
    @Override
    protected void flowThrough(Object inValue, Object unit, Object outValue)
    {
        FlowSet in = (FlowSet) inValue, out = (FlowSet) outValue;

        in.intersection(null, null);
        
        // Perform kill
            in.intersection((FlowSet) unitToPreserveSet.get(unit), out);

        // Perform generation
            out.union((FlowSet) unitToGenerateSet.get(unit), out);
    }

    protected void merge(Object in1, Object in2, Object out)
    {
        FlowSet inSet1 = (FlowSet) in1,
            inSet2 = (FlowSet) in2;

        FlowSet outSet = (FlowSet) out;

        inSet1.union(inSet2, outSet);
    }
    
    protected void copy(Object source, Object dest)
    {
        FlowSet sourceSet = (FlowSet) source,
            destSet = (FlowSet) dest;
            
        sourceSet.copy(destSet);
    }

    SimpleLiveLocalsAnalysis(UnitGraph g)
    {
        super(g);

        // Generate list of locals and empty set
        {
            Chain locals = g.getBody().getLocals();
            FlowUniverse localUniverse = new ArrayFlowUniverse(locals.toArray());

            emptySet = new ArrayPackedSet(localUniverse);            
        }

        // Create preserve sets.
        {
            unitToPreserveSet = new HashMap(g.size() * 2 + 1, 0.7f);
            Iterator unitIt = g.iterator();

            while(unitIt.hasNext())
            {
                Unit s = (Unit) unitIt.next();

                BoundedFlowSet killSet = (BoundedFlowSet) emptySet.clone();
                Iterator boxIt = s.getDefBoxes().iterator();

                while(boxIt.hasNext())
                {
                    ValueBox box = (ValueBox) boxIt.next();

                    if(box.getValue() instanceof Local)
                        killSet.add(box.getValue(), killSet);
                }

                // Store complement
                    killSet.complement(killSet);
                    unitToPreserveSet.put(s, killSet);
            }
        }

        // Create generate sets
        {
            unitToGenerateSet = new HashMap(g.size() * 2 + 1, 0.7f);
            Iterator unitIt = g.iterator();

            while(unitIt.hasNext())
            {
                Unit s = (Unit) unitIt.next();

                FlowSet genSet = (FlowSet) emptySet.clone();
                Iterator boxIt = s.getUseBoxes().iterator();

                while(boxIt.hasNext())
                {
                    ValueBox box = (ValueBox) boxIt.next();

                    if(box.getValue() instanceof Local)
                        genSet.add(box.getValue(), genSet);
                }

                unitToGenerateSet.put(s, genSet);
            }
        }

        doAnalysis();
    }


	

	@Override
	protected Object entryInitialFlow() {
		// TODO Auto-generated method stub
		return null;
	}
}
