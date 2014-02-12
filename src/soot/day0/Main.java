/* Soot - a J*va Optimization Framework
 * Copyright (C) 1997-2013 Eric Bodden and others
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */
package soot.day0;

import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soot.PackManager;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootMethod;
import soot.Transform;
import soot.Unit;
import soot.jimple.toolkits.ide.JimpleIFDSSolver;
import soot.jimple.toolkits.ide.exampleproblems.IFDSPossibleTypes;
import soot.jimple.toolkits.ide.icfg.JimpleBasedInterproceduralCFG;
import soot.util.Chain;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		PackManager.v().getPack("wjtp").add(new Transform("wjtp.ifds", new SceneTransformer() {
			protected void internalTransform(String phaseName, @SuppressWarnings("rawtypes") Map options) {

				IFDSTabulationProblem<Unit,?,SootMethod,InterproceduralCFG<Unit,SootMethod>> problem = new IFDSPossibleTypes(new JimpleBasedInterproceduralCFG());
				
				@SuppressWarnings({ "rawtypes", "unchecked" })
				JimpleIFDSSolver<?,InterproceduralCFG<Unit,SootMethod>> solver = new JimpleIFDSSolver(problem);
				solver.solve();
				
				solver.dumpResults();
				
				solver.printStats();
				
				InterproceduralCFG<Unit,SootMethod> icfg = problem.interproceduralCFG();
				
				System.out.println(Scene.v().getApplicationClasses());
				Chain<SootClass> appClasses = Scene.v().getApplicationClasses();
				
				for (Iterator<SootClass> iter = appClasses.iterator(); iter.hasNext(); )
				{
					SootClass sc = iter.next();
					
					List<SootMethod> sms = sc.getMethods();
					
				}
			}
		}));
		
		soot.Main.main(args);
	}

}
