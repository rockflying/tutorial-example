package iccta.analysis;

import graph.Graph;
import graph.Node;
import graph.NodeUtil;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;


public class RebuildGraph extends Graph
{
	public Node buildGraph()
	{
		Node root = getRoot();
		
		
		for (Map.Entry<String, List<String>> entry : ProcessHelper.cls2callers.entrySet())
		{
			String key = entry.getKey();
			List<String> callers = entry.getValue();
			
			Node node = NodeUtil.getNode(root, key);
			if (null == node)
			{
				node = new Node(key);
			}
			
			for (String callerName : callers)
			{
				Node callerNode = NodeUtil.getNode(root, callerName);
				if (null == callerNode)
				{
					callerNode = new Node(callerName);
					root.getChildren().add(callerNode);
				}
				
				node.getChildren().add(callerNode);
				callerNode.setParent(node);
			}
		
			root.getChildren().add(node);
		}
		
		return root;
	}
	
	public Set<Node> getEntryPoints()
	{
		Set<Node> entryPoints = new HashSet<Node>();
		
		Node root = getRoot();
		for (Node node : root.getChildren())
		{
			if (null == node.getParent())
			{
				entryPoints.add(node);
			}
		}
		
		return entryPoints;
	}
	
	public void resolve()
	{
		Queue<Node> queue = new LinkedList<Node>();
		
		Set<Node> entryPoints = getEntryPoints();
		queue.addAll(entryPoints);
		
		while (! queue.isEmpty())
		{
			Node node = queue.poll();
			
			int childCount = node.getChildren().size();
			if (0 == childCount)
			{
				continue;
			}
			
			Node[] children = node.getChildren().toArray(new Node[0]);
			Node[] newNodes = new Node[children.length];
			newNodes[0] = node;
			
			for (int i = 1; i < children.length; i++)
			{
				newNodes[i] = node.clone(node.getRootElement() + i);
				getRoot().getChildren().add(newNodes[i]);
			}
			
			for (int i = 0; i < children.length; i++)
			{
				children[i].setParent(newNodes[i]);
				children[i].getParent().getChildren().clear();
				children[i].getParent().getChildren().add(children[i]);
				
				queue.add(children[i]);
				
				if (0 != i)
				{
					/*Pair p = new Pair();
					p.src = children[i].getName();
					p.dest = newNodes[i].getName();
					AnalysisHelper.pairs.add(p);*/
					
					Triple t = new Triple();
					t.first = children[i].getRootElement();
					t.second = node.getRootElement();
					t.third = newNodes[i].getRootElement();
					ProcessHelper.triples.add(t);
				}
			}
		}
	}
}
