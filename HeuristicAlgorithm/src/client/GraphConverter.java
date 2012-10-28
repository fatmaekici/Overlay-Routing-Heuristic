package client;
import common.*;

public class GraphConverter {
	
	private Graph originalGraph;
	
	public GraphConverter(Graph original){
		this.originalGraph = original;
	}
	
	public EdgeWeightedDigraph toDirectedGraph(){
		EdgeWeightedDigraph newGraph = new EdgeWeightedDigraph(originalGraph.V());
		for (Link f : originalGraph.edges()) {
			addLinkAsDirectedEdge(f, newGraph);
		}
		return newGraph;
	}
	
	private void addLinkAsDirectedEdge(Link link, EdgeWeightedDigraph g){
		int firstId = link.getFirstId();
		int secondId = link.getSecondId();
		int linkId = link.getLinkId();
		int energyCost = link.getEnergyCost();
		int energyCost1 = energyCost + (originalGraph.findNode(firstId).getEnergyCost());
		int energyCost2 = energyCost + (originalGraph.findNode(secondId).getEnergyCost());
		DirectedEdge newEdge1 = new DirectedEdge(linkId, firstId, secondId, energyCost1);
		DirectedEdge newEdge2 = new DirectedEdge(linkId, secondId, firstId, energyCost2);
		g.addEdge(newEdge1);
		g.addEdge(newEdge2);
	}
}
