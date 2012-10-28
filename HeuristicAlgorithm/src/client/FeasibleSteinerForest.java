package client;

import common.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class FeasibleSteinerForest {
	private Graph inputGraph; // original underlay graph
	private Set<Pair<Node, Node>> sourceDestPairs;
	private BreadthFirstPaths pathCalculator;
	private Graph mstGraph;
	private Set<Pair<Integer, Integer>> bfsEdges;
	private Set<Integer> bfsNodes;
	
	// We take the underlay graph and list of source destination pairs as input
	public FeasibleSteinerForest(Graph inGraph, Set<Pair<Node, Node>> sourceDestPairs){
		this.inputGraph = inGraph;
		this.sourceDestPairs = sourceDestPairs;
		bfsEdges = new HashSet<Pair<Integer, Integer>>();
		bfsNodes = new HashSet<Integer>();
	}
	
	public Graph findSteinerForest() throws NoSuchElementException{
		PrimMST mst = new PrimMST(inputGraph);
		mstGraph = mst.getMSTgraph();
		
		StdOut.println("***************************************");
        StdOut.println("MST graph is as below:");
        StdOut.println(mstGraph);
		pathCalculator = new BreadthFirstPaths(mstGraph);
		for(Pair<Node, Node> sdPair : sourceDestPairs){
			pathCalculator.bfs(sdPair.first.getNodeId(), sdPair.second.getNodeId());
			Iterable<Integer> path = pathCalculator.pathTo();
			Integer previous = -1;
			Integer next = -1;
			for(Integer node : path){
				bfsNodes.add(node);
				if(previous == -1){
					previous = node;
					continue;
				}
				next = node;
				Pair<Integer, Integer> edge = new Pair<Integer, Integer>(previous, next);
				bfsEdges.add(edge);
				previous = next;
			}
		}
		// Because of the adjacency lists which are indexed according to node id, we need the size of original graph
		Graph steinerForest = new Graph(inputGraph.V());
		Iterator nodeIterator = bfsNodes.iterator();
		while(nodeIterator.hasNext()){
			Integer nodeId = (Integer)nodeIterator.next();
			Node newNode = inputGraph.findNode(nodeId);
			if(newNode == null){
				throw new NoSuchElementException("No such node exists!");
			}
			else{
				steinerForest.addNode(newNode);
			}
		}
		Iterator edgeIterator = bfsEdges.iterator();
		while(edgeIterator.hasNext()){
			Pair<Integer, Integer> edge = (Pair<Integer, Integer>)edgeIterator.next();
			Link newEdge = inputGraph.findLink(edge.first, edge.second);
			if(newEdge == null){
				throw new NoSuchElementException("No such link exists!");
			}
			else{
				steinerForest.addEdge(newEdge);
			}
		}
		return steinerForest;
	}
}
