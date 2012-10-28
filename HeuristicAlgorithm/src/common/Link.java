	/**
 * 
 */
package common;
import java.util.List;
import java.util.ArrayList;

/**
 * @author fatma
 *
 */
public class Link implements Comparable<Link> {
	
	private int firstId;
	private int secondId;
	private int linkId;
	private int energyCost;
	private boolean isSleepable;
	private boolean isOnUnderlay;
	public boolean isOnOverlay;
	List<Integer> pathIdList;
	public Path firstPath;
	
	public Link(int linkId, int firstId, int secondId, int energyCost, boolean isSleepable, boolean isOnUnderlay){
		this.setFirstId(firstId);
		this.setSecondId(secondId);
		this.setEnergyCost(energyCost);
		this.setSleepable(isSleepable);
		this.setOnUnderlay(isOnUnderlay);
		this.setLinkId(linkId);
		isOnOverlay = false;
		pathIdList = new ArrayList<Integer>();
	}
	
	public void addPathId(int pathId){
		pathIdList.add(pathId);
	}
	
	 /**
     * Return the weight of this link.
     */
    public double weight() {
        return energyCost;
    }

   /**
     * Return either endpoint of this edge.
     */
    public int either() {
        return firstId;
    }

   /**
     * Return the endpoint of this edge that is different from the given vertex
     * (unless a self-loop).
     */
    public int other(int vertex) {
        if      (vertex == firstId) return secondId;
        else if (vertex == secondId) return firstId;
        else throw new RuntimeException("Illegal endpoint");
    }

   /**
     * Compare edges by weight.
     */
    public int compareTo(Link that) {
        if      (this.weight() < that.weight()) return -1;
        else if (this.weight() > that.weight()) return +1;
        else                                    return  0;
    }


   /**
     * Return a string representation of this edge.
     */
    public String toString() {
        return String.format("%d %d %d", firstId, secondId, energyCost);
    }


	public int getFirstId() {
		return firstId;
	}

	public void setFirstId(int firstId) {
		this.firstId = firstId;
	}

	public int getSecondId() {
		return secondId;
	}

	public void setSecondId(int secondId) {
		this.secondId = secondId;
	}

	public int getEnergyCost() {
		return energyCost;
	}

	public void setEnergyCost(int energyCost) {
		this.energyCost = energyCost;
	}

	public boolean isSleepable() {
		return isSleepable;
	}

	public void setSleepable(boolean isSleepable) {
		this.isSleepable = isSleepable;
	}

	public boolean isOnUnderlay() {
		return isOnUnderlay;
	}

	public void setOnUnderlay(boolean isOnUnderlay) {
		this.isOnUnderlay = isOnUnderlay;
	}

	public int getLinkId() {
		return linkId;
	}

	public void setLinkId(int linkId) {
		this.linkId = linkId;
	}
	
	 /**
     * Test client.
     */
    /*public static void main(String[] args) {
        Edge e = new Edge(12, 23, 3.14);
        StdOut.println(e);
    }*/
}
