package soot.test.second;

import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.PackManager;
import soot.Transform;
import soot.toolkits.graph.ExceptionalUnitGraph;

public class MyMain {

	public static void main(String[] args) {
		Transform t = new Transform("jtp.myTransform", new BodyTransformer() {

			@Override
			protected void internalTransform(Body body, String arg1, Map arg2) {
				new MyForwardFlowAnalysis(new ExceptionalUnitGraph(body));
				
			}
			
		});
		
		PackManager.v().getPack("jtp").add(t);
		
		soot.Main.main(args);
	}

}
