package soot.day0;
import java.util.Map;

import soot.Local;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.FlowUniverse;
import soot.util.Chain;


public class SimpleLiveLocalsAnalysis extends BackwardFlowAnalysis{

	FlowSet emptySet;
	Map unitToGenerateSet;
	Map unitToPreserveSet;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public SimpleLiveLocalsAnalysis(UnitGraph graph) {
		super(graph);
		
		Chain<Local> locals = graph.getBody().getLocals();
		
		
		
	}

	
	
	@Override
	protected void flowThrough(Object in, Object d, Object out) 
	{
		FlowSet inValue = (FlowSet)in;
		FlowSet outValue = (FlowSet)out;
		
		//perform kill
		inValue.intersection((FlowSet)unitToPreserveSet.get(d), outValue);
		
		//perform generation
		outValue.union((FlowSet)unitToGenerateSet.get(d), outValue);
	}

	@Override
	protected Object newInitialFlow() 
	{	
		return emptySet.clone();
	}

	@Override
	protected Object entryInitialFlow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void merge(Object in1, Object in2, Object out) 
	{
		FlowSet inSet1 = (FlowSet)in1;
		FlowSet inSet2 = (FlowSet)in2;
		
		FlowSet outSet = (FlowSet)out;
		
		inSet1.union(inSet2, outSet);
	}

	@Override
	protected void copy(Object source, Object dest) 
	{
		FlowSet sourceSet = (FlowSet)source;
		FlowSet destSet = (FlowSet)dest;
		
		sourceSet.copy(destSet);
	}
}
