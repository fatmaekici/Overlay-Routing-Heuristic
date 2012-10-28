/*************************************************************************
* User should provide either the names of link and node files or number of nodes and links
* For the second one, a random graph is generated
* Adjacency list of each vertex is stored in Bag structure
*  0: 6-0 0.58000  0-2 0.26000  0-4 0.38000  0-7 0.16000  
*  1: 1-3 0.29000  1-2 0.36000  1-7 0.19000  1-5 0.32000  
*  2: 6-2 0.40000  2-7 0.34000  1-2 0.36000  0-2 0.26000  2-3 0.17000  
*  3: 3-6 0.52000  1-3 0.29000  2-3 0.17000  
*  4: 6-4 0.93000  0-4 0.38000  4-7 0.37000  4-5 0.35000  
*  5: 1-5 0.32000  5-7 0.28000  4-5 0.35000   *  6: 6-4 0.93000  6-0 0.58000  3-6 0.52000  6-2 0.40000
*  7: 2-7 0.34000  1-7 0.19000  0-7 0.16000  5-7 0.28000  4-7 0.37000
*
*************************************************************************/

package common;

import java.util.Map;
import java.util.HashMap;
import java.util.Stack;
import java.io.IOException;
import java.lang.*;
import java.util.Random;

public class Graph {
	private Map<Integer, Link> listOfLinks;
	private Map<Integer, Node> listOfNodes;
	private int V; // num of vertices
    private int E; // num of edges
    private Bag<Link>[] adj; // adjacency list of vertices
    private Bag<Node>[] adjVertices;
    
 // Return the id of the link regardless of the node order
 	public Link findLink(int firstNode, int secondNode){
 		for(Link edge: this.edges()){
 			int node1 = edge.either();
 			int node2 = edge.other(node1);
 			if((node1 == firstNode && node2 == secondNode) || (node2 == firstNode && node1 == secondNode)){
 				return edge;
 			}
 		}
 		return null;
 	}
 	
 	public Link findLink(int linkId){
 		if(listOfLinks.containsKey(linkId)){
 			return listOfLinks.get(linkId);
 		}
 		return null;
 	}
 	
 	public final Map<Integer, Link> getLinks(){
 		return listOfLinks;
 	}
 	
 	public Node findNode(int nodeId){
 		if(listOfNodes.containsKey(nodeId)){
 			return listOfNodes.get(nodeId);
 		}
 		return null;
 	}
	
	public Graph(){
		listOfLinks= new HashMap<Integer, Link>();
		listOfNodes = new HashMap<Integer, Node>();
	}
	
	/**
     * Create an empty graph with V vertices.
     */
    public Graph(int V) {
    	this();
        if (V < 0) throw new RuntimeException("Number of vertices must be nonnegative");
        this.V = V;
        this.E = 0;
        adj = (Bag<Link>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<Link>();
        }
        adjVertices = (Bag<Node>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
        	adjVertices[v] = new Bag<Node>();
        }
    }
    
    /**
     * Copy constructor.
     */
    public Graph(Graph G) {
        this(G.V());
        this.E = G.E();
        for (int v = 0; v < G.V(); v++) {
            // reverse so that adjacency list is in same order as original
            Stack<Link> reverse = new Stack<Link>();
            for (Link e : G.adj[v]) {
                reverse.push(e);
            }
            for (Link e : reverse) {
                adj[v].add(e);
            }
            Stack<Node> reverse2 = new Stack<Node>();
            for (Node e : G.adjVertices[v]) {
                reverse2.push(e);
            }
            for (Node e : reverse2) {
                adjVertices[v].add(e);
            }
        }
        this.listOfLinks = G.listOfLinks;
        this.listOfNodes = G.listOfNodes;
    }


   /**
     * Create a random graph with V vertices and E edges.
     * The expected running time is proportional to V + E.
     */
    public Graph(int V, int E) {
        this(V);
        Random randomGen = new Random();
        if (E < 0) throw new RuntimeException("Number of edges must be nonnegative");
        int counter = 0;
        while(listOfLinks.size() < E){
        	int v = randomGen.nextInt(V);
            int w = randomGen.nextInt(V);
            if(findLink(v, w) != null){
            	continue;
            }
            int energyCost = randomGen.nextInt(100);
            boolean isSleepable = true;
            boolean isOnUnderlay = true;
            if(Math.random() < 0.5){
            	isSleepable = false;
            }
            if(Math.random() < 0.5){
            	isOnUnderlay = false;
            }
            Link newLink = new Link(counter, v, w, energyCost, isSleepable, isOnUnderlay);
            listOfLinks.put(counter, newLink);
            addEdge(newLink);
            counter++;
        }
        this.E = E;
        for (int i = 0; i < V; i++) {
        	 int energyCost = randomGen.nextInt(100);
        	 int relayCost = randomGen.nextInt(100);
             boolean isSleepable = true;
             if(Math.random() < 0.5){
             	isSleepable = false;
             }
             Node newNode = new Node(i, relayCost, energyCost, isSleepable);
             listOfNodes.put(i, newNode);
        }
    }
	
	// Generate graph from files
	public Graph(String nodeFileName, String linkFileName){
		this();
		try {
			generateNodesFromFile(nodeFileName);
			generateLinksFromFile(linkFileName);
		} catch (IOException e)
	    {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }

		V = listOfNodes.size();
		E = listOfLinks.size();
		adj = (Bag<Link>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<Link>();
        }
        adjVertices = (Bag<Node>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
        	adjVertices[v] = new Bag<Node>();
        }
		for(Map.Entry<Integer, Link> entry : listOfLinks.entrySet()){
			Link value = entry.getValue();
			this.addEdge(value);
		}
	}
	
	public void setVertices(Map<Integer, Node> nodes){
		this.listOfNodes = nodes;
	}
	
	public Map<Integer, Node> getVertices(){
		return new HashMap<Integer, Node>(this.listOfNodes);
	}
	
	/**
     * Add the undirected edge e to adjacency list.
     */
	
	public void addNode(Node n){
		if(!listOfNodes.containsKey(n.getNodeId())){
    		listOfNodes.put(n.getNodeId(), n);
    	}
	}
	
    public void addEdge(Link e) {
    	if(!listOfLinks.containsKey(e.getLinkId())){
    		listOfLinks.put(e.getLinkId(), e);
    		E++;
    	}
        int v = e.either();
        int w = e.other(v);
        boolean exists = false;
        for(Link link : adj[v]){
        	if(link.getLinkId() == e.getLinkId()){
        		exists = true;
        		break;
        	}
        }
        if(!exists){
        	adj[v].add(e); 
        }
        exists = false;
        for(Link link : adj[w]){
        	if(link.getLinkId() == e.getLinkId()){
        		exists = true;
        		break;
        	}
        }
        if(!exists){
        	adj[w].add(e); 
        }
        exists = false;
        if(adjVertices[v].size() != 0){
			for(Node node : adjVertices[v]){
				if(listOfNodes.get(w) != null){
					if(node.getNodeId() == listOfNodes.get(w).getNodeId()){
				      	exists = true;
				      	break;
					}
				}
			}
        }
       
        if(!exists){
	        adjVertices[v].add(listOfNodes.get(w));
        }
        exists = false;
        if(adjVertices[w].size() != 0){
			for(Node node : adjVertices[v]){
				if(listOfNodes.get(v) != null){
					if(node.getNodeId() == listOfNodes.get(v).getNodeId()){
				      	exists = true;
				      	break;
					}
				}
			}
        }
       
        if(!exists){
	        adjVertices[w].add(listOfNodes.get(v));
        }
    }


   /**
     * Return the edges incident to vertex v as an Iterable.
     * To iterate over the edges incident to vertex v, use foreach notation:
     * <tt>for (Edge e : graph.adj(v))</tt>.
     */
    public Iterable<Link> adj(int v) {
        return adj[v];
    }
    
    /**
     * Return the vertices adjacent to vertex v as an Iterable.
     * To iterate over the vertices adjacent to vertex v, use foreach notation:
     * <tt>for (Edge e : graph.adj(v))</tt>.
     */
    public Iterable<Node> adjVertices(int v) {
        return adjVertices[v];
    }
    
    /**
     * Return the number of vertices in this graph.
     */
    public int V() {
        return V;
    }

   /**
     * Return the number of edges in this graph.
     */
    public int E() {
        return E;
    }
	
	
	// nodeId relayCost energyCost isSleepable
	public void generateNodesFromFile (String nodeFileName) throws IOException {
		String[] lines = FileArrayProvider.readLines(nodeFileName);
		for (String line : lines) {
			String [] nodeVals = line.split(" ");
			int nodeId = Integer.parseInt(nodeVals[0]);
			int relayCost = Integer.parseInt(nodeVals[1]);
			int energyCost = Integer.parseInt(nodeVals[2]);
			boolean isSleepable = Boolean.parseBoolean(nodeVals[3]);
			Node newNode = new Node(nodeId, relayCost, energyCost, isSleepable);
			listOfNodes.put(nodeId, newNode);
		}
	}
	
	// linkId firstNodeId secondNodeId energyCost isSleepable isOnUnderlay
	public void generateLinksFromFile (String linkFileName) throws IOException {
		String[] lines = FileArrayProvider.readLines(linkFileName);
		for (String line : lines) {
			String [] linkVals = line.split(" ");
			int linkId = Integer.parseInt(linkVals[0]);
			int firstNodeId = Integer.parseInt(linkVals[1]);
			int secondNodeId = Integer.parseInt(linkVals[2]);
			int energyCost = Integer.parseInt(linkVals[3]);
			boolean isSleepable = Boolean.parseBoolean(linkVals[4]);
			boolean isOnUnderlay = Boolean.parseBoolean(linkVals[5]);
			Link newLink = new Link(linkId, firstNodeId, secondNodeId, energyCost, isSleepable, isOnUnderlay);
			listOfLinks.put(linkId, newLink);
		}
	}
	
	 /**
     * Return all edges in this graph as an Iterable.
     * To iterate over the edges in the graph, use foreach notation:
     * <tt>for (Edge e : G.edges())</tt>.
     */
    public Iterable<Link> edges() {
        Bag<Link> list = new Bag<Link>();
        for (int v = 0; v < V; v++) {
            int selfLoops = 0;
            for (Link e : adj(v)) {
                if (e.other(v) > v) {
                    list.add(e);
                }
                // only add one copy of each self loop
                else if (e.other(v) == v) {
                    if (selfLoops % 2 == 0) list.add(e);
                    selfLoops++;
                }
            }
        }
        return list;
    }
    


   /**
     * Return a string representation of this graph.
     */
    public String toString() {
        String NEWLINE = System.getProperty("line.separator");
        StringBuilder s = new StringBuilder();
        s.append(V + " " + E + NEWLINE);
        for (int v = 0; v < V; v++) {
        	if(listOfNodes.containsKey(v) && listOfNodes.get(v) != null){
	            s.append(listOfNodes.get(v).toString() + ": ");
	            for (Link e : adj[v]) {
	                s.append(e + "  ");
	            }
	            s.append(NEWLINE);
        	}
        }
        return s.toString();
    }

   /**
     * Test client.

    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraph G = new EdgeWeightedGraph(in);
        StdOut.println(G);
    }      */

}
