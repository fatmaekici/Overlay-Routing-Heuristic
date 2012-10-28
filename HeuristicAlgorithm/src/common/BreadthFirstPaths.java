package common;

import java.awt.DisplayMode;
import java.util.Arrays;
import java.util.Stack;

public class BreadthFirstPaths {
    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] marked;  // marked[v] = is there an s-v path
    private int[] edgeTo;      // edgeTo[v] = previous edge on shortest s-v path
    private int[] distTo;      // distTo[v] = number of edges shortest s-v path
    private int source;
    private int dest;
    private Graph inputGraph;

    // single source
    public BreadthFirstPaths(Graph G) {
        marked = new boolean[G.V()];
        distTo = new int[G.V()];
        edgeTo = new int[G.V()];
        inputGraph = G;
    }
    
    public void clear(){
    	Arrays.fill(distTo, INFINITY);
    	Arrays.fill(marked, false);
    	Arrays.fill(edgeTo, -1);
    }


    // BFS from single source to single destination
    public void bfs(int s, int d){
    	Graph G = inputGraph;
        Queue<Integer> q = new Queue<Integer>();
        clear();
        source = s;
        dest = d;
        //for (int v = 0; v < G.V(); v++) distTo[v] = INFINITY;
        distTo[s] = 0;
        marked[s] = true;
        q.enqueue(s);
        int v = q.dequeue();
        while (v != d) {
            for (Node w : G.adjVertices(v)) {
                if (!marked[w.getNodeId()]) {
                    edgeTo[w.getNodeId()] = v;
                    distTo[w.getNodeId()] = distTo[v] + 1;
                    marked[w.getNodeId()] = true;
                    q.enqueue(w.getNodeId());
                }
            }
            v = q.dequeue();
        }
        assert check(G, s);
    }

    // is there a path between s (or sources) and v?
    public boolean hasPathTo(int v) {
        return marked[v];
    }

    // length of shortest path between s (or sources) and v
    public int distTo(int v) {
        return distTo[v];
    }

    // shortest path bewteen s (or sources) and v; null if no such path
    public Iterable<Integer> pathTo() {
        if (!hasPathTo(dest)) return null;
        Stack<Integer> path = new Stack<Integer>();
        int x;
        for (x = dest; distTo[x] != 0; x = edgeTo[x])
            path.push(x);
        path.push(x);
        return path;
    }


    // check optimality conditions for single source
    private boolean check(Graph G, int s) {

        // check that the distance of s = 0
        if (distTo[s] != 0) {
            StdOut.println("distance of source " + s + " to itself = " + distTo[s]);
            return false;
        }

        // check that for each edge v-w dist[w] <= dist[v] + 1
        // provided v is reachable from s
        for (int v = 0; v < G.V(); v++) {
            for (Node w : G.adjVertices(v)) {
                if (hasPathTo(v) != hasPathTo(w.getNodeId())) {
                    StdOut.println("edge " + v + "-" + w);
                    StdOut.println("hasPathTo(" + v + ") = " + hasPathTo(v));
                    StdOut.println("hasPathTo(" + w + ") = " + hasPathTo(w.getNodeId()));
                    return false;
                }
                if (hasPathTo(v) && (distTo[w.getNodeId()] > distTo[v] + 1)) {
                    StdOut.println("edge " + v + "-" + w);
                    StdOut.println("distTo[" + v + "] = " + distTo[v]);
                    StdOut.println("distTo[" + w + "] = " + distTo[w.getNodeId()]);
                    return false;
                }
            }
        }

        // check that v = edgeTo[w] satisfies distTo[w] + distTo[v] + 1
        // provided v is reachable from s
        for (int w = 0; w < G.V(); w++) {
            if (!hasPathTo(w) || w == s) continue;
            int v = edgeTo[w];
            if (distTo[w] != distTo[v] + 1) {
                StdOut.println("shortest path edge " + v + "-" + w);
                StdOut.println("distTo[" + v + "] = " + distTo[v]);
                StdOut.println("distTo[" + w + "] = " + distTo[w]);
                return false;
            }
        }

        return true;
    }


    // test client
 /*   public static void main(String[] args) {
        In in = new In(args[0]);
        Graph G = new Graph(in);
        // StdOut.println(G);

        int s = Integer.parseInt(args[1]);
        BreadthFirstPaths bfs = new BreadthFirstPaths(G, s);

        for (int v = 0; v < G.V(); v++) {
            if (bfs.hasPathTo(v)) {
                StdOut.printf("%d to %d (%d):  ", s, v, bfs.distTo(v));
                for (int x : bfs.pathTo(v)) {
                    if (x == s) StdOut.print(x);
                    else        StdOut.print("-" + x);
                }
                StdOut.println();
            }

            else {
                StdOut.printf("%d to %d (-):  not connected\n", s, v);
            }

        }
    }

*/
}