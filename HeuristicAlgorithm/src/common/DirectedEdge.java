/*************************************************************************
 *  Compilation:  javac DirectedEdge.java
 *  Execution:    java DirectedEdge
 *
 *  Immutable weighted directed edge.
 *
 *************************************************************************/

/**
 *  The <tt>DirectedEdge</tt> class represents a weighted edge in an directed graph.
 *  <p>
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/44sp">Section 4.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */

package common;
public class DirectedEdge { 
    private int v;
    private int w;
    private int energyCost;
    private int linkId;

   /**
     * Create a directed edge from v to w with given weight.
     */
    public DirectedEdge(int linkId, int firstId, int secondId, int energyCost) {
        this.v = firstId;
        this.w = secondId;
        this.linkId = linkId;
        this.energyCost = energyCost;
    }
    
    public int getLinkId() {
		return linkId;
	}

   /**
     * Return the vertex where this edge begins.
     */
    public int from() {
        return v;
    }

   /**
     * Return the vertex where this edge ends.
     */
    public int to() {
        return w;
    }

   /**
     * Return the weight of this edge.
     */
    public double weight() { return energyCost; }

   /**
     * Return a string representation of this edge.
     */
    public String toString() {
        return v + "->" + w + " " + String.format("%d", energyCost);
    }

}
