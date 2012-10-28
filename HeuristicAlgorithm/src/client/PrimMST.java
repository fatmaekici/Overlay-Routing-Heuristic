

/******************************************************************************
 *  Compilation:  javac PrimMST.java
 *  Execution:    java PrimMST filename.txt
 *  Dependencies: EdgeWeightedGraph.java Edge.java Queue.java
 *                IndexMinPQ.java UF.java In.java StdOut.java
 *
 *  Compute a minimum spanning forest using Prim's algorithm.
 *
 ******************************************************************************/
package client;


import common.*;


public class PrimMST {
    private Link[] edgeTo;        // edgeTo[v] = shortest edge from tree vertex to non-tree vertex
    private double[] distTo;      // distTo[v] = weight of shortest such edge
    private boolean[] marked;     // marked[v] = true if v on tree, false otherwise
    private IndexMinPQ<Double> pq;
    private Graph inputGraph;

    public PrimMST(Graph G) {
    	inputGraph = G;
        edgeTo = new Link[G.V()];
        distTo = new double[G.V()];
        marked = new boolean[G.V()];
        pq = new IndexMinPQ<Double>(G.V());
        for (int v = 0; v < G.V(); v++) distTo[v] = Double.POSITIVE_INFINITY;

        for (int v = 0; v < G.V(); v++)      // run from each vertex to find
            if (!marked[v]) prim(G, v);      // minimum spanning forest

        // check optimality conditions
        assert check(G);
    }

    // run Prim's algorithm in graph G, starting from vertex s
    private void prim(Graph G, int s) {
        distTo[s] = 0.0;
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            scan(G, v);
        }
    }

    // scan vertex v
    private void scan(Graph G, int v) {
        marked[v] = true;
        for (Link e : G.adj(v)) {
            int w = e.other(v);
            if (marked[w]) continue;         // v-w is obsolete edge
            if (e.weight() < distTo[w]) {
                distTo[w] = e.weight();
                edgeTo[w] = e;
                if (pq.contains(w)) pq.change(w, distTo[w]);
                else                pq.insert(w, distTo[w]);
            }
        }
    }

    // return iterator of edges in MST
    public Iterable<Link> edges() {
        Queue<Link> mst = new Queue<Link>();
        for (int v = 0; v < edgeTo.length; v++) {
            Link e = edgeTo[v];
            if (e != null) {
                mst.enqueue(e);
            }
        }
        return mst;
    }


    // return weight of MST
    public double weight() {
        double weight = 0.0;
        for (Link e : edges())
            weight += e.weight();
        return weight;
    }


    // check optimality conditions (takes time proportional to E V lg* V)
    private boolean check(Graph G) {

        // check weight
        double totalWeight = 0.0;
        for (Link e : edges()) {
            totalWeight += e.weight();
        }
        double EPSILON = 1E-12;
        if (Math.abs(totalWeight - weight()) > EPSILON) {
            System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", totalWeight, weight());
            return false;
        }

        // check that it is acyclic
        UF uf = new UF(G.V());
        for (Link e : edges()) {
            int v = e.either(), w = e.other(v);
            if (uf.connected(v, w)) {
                System.err.println("Not a forest");
                return false;
            }
            uf.union(v, w);
        }

        // check that it is a spanning forest
        for (Link e : edges()) {
            int v = e.either(), w = e.other(v);
            if (!uf.connected(v, w)) {
                System.err.println("Not a spanning forest");
                return false;
            }
        }

        // check that it is a minimal spanning forest (cut optimality conditions)
        for (Link e : edges()) {
            int v = e.either(), w = e.other(v);

            // all edges in MST except e
            uf = new UF(G.V());
            for (Link f : edges()) {
                int x = f.either(), y = f.other(x);
                if (f != e) uf.union(x, y);
            }

            // check that e is min weight edge in crossing cut
            for (Link f : G.edges()) {
                int x = f.either(), y = f.other(x);
                if (!uf.connected(x, y)) {
                    if (f.weight() < e.weight()) {
                        System.err.println("Edge " + f + " violates cut optimality conditions");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public Graph getMSTgraph(){
    	Graph mstGraph = new Graph(inputGraph.V());
    	mstGraph.setVertices(inputGraph.getVertices());
    	for (Link e : this.edges()) {
    		mstGraph.addEdge(e);
        }
    	return mstGraph;
    }

}

