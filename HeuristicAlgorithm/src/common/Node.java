/**
 * 
 */
package common;

/**
 * @author fatma
 *
 */
public class Node {
	
	private int nodeId;
	private int relayCost;
	private int energyCost;
	private boolean isSleepable;
	
	public Node(int nodeId, int relayCost, int energyCost, boolean isSleepable){
		this.setNodeId(nodeId);
		this.setRelayCost(relayCost);
		this.setEnergyCost(energyCost);
		this.setSleepable(isSleepable);
	}
	
	public String toString() {
        return String.format("Node Id %d Cost %d", nodeId, energyCost);
    }

	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public int getRelayCost() {
		return relayCost;
	}

	public void setRelayCost(int relayCost) {
		this.relayCost = relayCost;
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
}
