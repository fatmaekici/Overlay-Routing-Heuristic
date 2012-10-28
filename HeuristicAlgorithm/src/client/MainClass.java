package client;

import java.util.List;
import java.util.ArrayList;

import common.*;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;


public class MainClass {
	
	public static void main(String[] args) {
        Graph G = new Graph(20, 20);
        StdOut.println("Input graph's adjacency list is as below:");
        StdOut.println(G);
        StdOut.println("***************************************");
        
        
        
        // START OF PART 1
        PrimMST mst = new PrimMST(G);
        StdOut.println("Minimum spanning tree is as below");
        for (Link e : mst.edges()) {
            StdOut.println(e);
        }
        StdOut.printf("%.5f\n", mst.weight());
        Set<Pair<Node, Node>> sourceDestPairs = new HashSet<Pair<Node, Node>>();
        Random randomGen = new Random();
        // Suppose that there are 3 source dest pairs
        StdOut.println("***************************************");
        StdOut.println("Source Destination pairs:");
        while(sourceDestPairs.size() < 3){
        	int v = randomGen.nextInt(G.V());
            int w = randomGen.nextInt(G.V());
            if(v == w){
            	continue;
            }
            Node source = G.findNode(v);
            Node dest = G.findNode(w);
            Pair<Node, Node> newPair = new Pair<Node, Node>(source, dest);
            sourceDestPairs.add(newPair);
            StdOut.printf("%d %d\n", v, w);
        }
        FeasibleSteinerForest forestGenerator = new FeasibleSteinerForest(G, sourceDestPairs);
        Graph steinerForest = forestGenerator.findSteinerForest();
        StdOut.println("***************************************");
        StdOut.println("Feasible steiner forest is as below:");
        StdOut.println(steinerForest);
        
        // Add the unsleepable edges after finding the feasible steiner forest
        Map<Integer, Link> links = G.getLinks();
        for(Map.Entry<Integer, Link> entry : links.entrySet()){
        	Link value = entry.getValue();
        	if(!value.isSleepable()){
        		steinerForest.addNode(G.findNode(value.getFirstId()));
        		steinerForest.addNode(G.findNode(value.getSecondId()));
        		steinerForest.addEdge(value);
        	}
        }
        
        StdOut.println("***************************************");
        StdOut.println("Generic graph after adding unsleepable edges:");
        StdOut.println(steinerForest);
        // END OF PART 1
        
        
        
        // START OF PART 2
        // Find shortest paths between source and destination pairs
        StdOut.println("***************************************");
        StdOut.println("Shortest paths between pairs:");
        GraphConverter converter = new GraphConverter(steinerForest);
        EdgeWeightedDigraph directedGraph = converter.toDirectedGraph();
        DijkstraSP shortestPathFinder = new DijkstraSP(directedGraph);
        
        // List of shortest path after running dijkstra for all source dest pairs
        List<Path> shortestPaths = new ArrayList<Path>();
        int pathId = 0;
        for(Pair<Node, Node> sdPair : sourceDestPairs){
        	int firstId = sdPair.first.getNodeId();
        	int secondId = sdPair.second.getNodeId();
        	shortestPathFinder.findShortestPath(firstId, secondId);
        	StdOut.printf("From %d to %d\n", firstId, secondId);
        	Path shortPath = new Path(firstId, secondId, 10, pathId);
        	Stack<DirectedEdge> djkstraPath = (Stack<DirectedEdge>)shortestPathFinder.pathTo(secondId);
        	
            while(!djkstraPath.isEmpty()) {
            	DirectedEdge e = djkstraPath.pop();
                StdOut.print(e + "   ");
                Link link = G.findLink(e.getLinkId());
                shortPath.addLinkToPath(link);
                link.addPathId(pathId);
                if(link.isOnOverlay){
                	shortPath.addSharedEdgeId(e.getLinkId());
                	// First path that used this edge, did not know that this edge would be shared
                	// So add this to first path's shared edge list
                	link.firstPath.addSharedEdgeId(e.getLinkId());
                }
                else{
                	link.isOnOverlay = true;
                	link.firstPath = shortPath;
                }
            }
            StdOut.println();
            
            shortestPaths.add(shortPath);
            pathId++;
        }
        
        for(Path p : shortestPaths){
        	StdOut.print("Number of shared edges " + p.getNumOfSharedEdges());
        	StdOut.println();
        }
     // END OF PART 2
        
        
    }
}
