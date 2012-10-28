package common;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class Path {
	
	private int sourceID;
    private int destID;
    private int pathId;
    private int maxAllowedOverlap;
    private List<Link> pathSequence;
    private Set<Integer> setOfSharedEdges;
    
    public Path(int sourceId, int destId, int maxOverlap, int pathId){
    	this.sourceID = sourceId;
    	this.destID = destId;
    	this.maxAllowedOverlap = maxOverlap;
    	this.pathId = pathId;
    	pathSequence = new ArrayList<Link>();
    	setOfSharedEdges = new HashSet<Integer>();
    }
    
    public int getNumOfSharedEdges(){
    	return setOfSharedEdges.size();
    }
    
    public boolean hasViolation(){
    	int numOfShared = getNumOfSharedEdges();
    	return (numOfShared > maxAllowedOverlap);
    }
    
    public boolean addLinkToPath(Link newLink){
    	return pathSequence.add(newLink);
    }
    
    public void addSharedEdgeId(int id){
    	setOfSharedEdges.add(id);
    }
    
	public int getSourceID() {
		return sourceID;
	}

	public void setSourceID(int sourceID) {
		this.sourceID = sourceID;
	}

	public int getDestID() {
		return destID;
	}

	public void setDestID(int destID) {
		this.destID = destID;
	}

	public int getMaxAllowedOverlap() {
		return maxAllowedOverlap;
	}

	public void setMaxAllowedOverlap(int maxAllowedOverlap) {
		this.maxAllowedOverlap = maxAllowedOverlap;
	}
    
}
