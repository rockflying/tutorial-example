package soot.day2.graph;

import graph.Marker;

/*
 * type 1
 * 	1) isComponent
 */
public class CompMarker extends Marker
{
	boolean isComponent = false;
	
	public CompMarker(int type) 
	{
		super(type);
	}

	public boolean isComponent() {
		return isComponent;
	}

	public void setComponent(boolean isComponent) {
		this.isComponent = isComponent;
	}
	
}
