/*************************************************************************
 *  Compilation:  javac DeluxeBST.java
 *  Execution:    java DeluxeBST V E
 *  Dependencies: Digraph.java Queue.java Stack.java
 *
 *  Run breadth first search on a digraph.
 *  Runs in O(E + V) time.
 *
 *  % java DeluxeBST tinyDG.txt 3
 *  3 to 0 (2):  3->2->0
 *  3 to 1 (3):  3->2->0->1
 *  3 to 2 (1):  3->2
 *  3 to 3 (0):  3
 *  3 to 4 (2):  3->5->4
 *  3 to 5 (1):  3->5
 *  3 to 6 (-):  not connected
 *  3 to 7 (-):  not connected
 *  3 to 8 (-):  not connected
 *  3 to 9 (-):  not connected
 *  3 to 10 (-):  not connected
 *  3 to 11 (-):  not connected
 *  3 to 12 (-):  not connected
 *
 *************************************************************************/
public class DeluxeBST {
    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] marked;  // marked[v] = is there an s->v path?
    private int[] distTo;      // distTo[v] = length of shortest s->v path
    private Queue<Integer> touched;

    public DeluxeBST(int s) {
        touched = new Queue<Integer>();
        marked = new boolean[s];
        distTo = new int[s];
        for (int v = 0; v < s; v++)
            distTo[v] = INFINITY;
    }

    // // single source
    // public DeluxeBST(Digraph G, int s) {
    //     marked = new boolean[G.V()];
    //     distTo = new int[G.V()];
    //     for (int v = 0; v < G.V(); v++) distTo[v] = INFINITY;
    //     bfs(G, s);
    // }

    // // multiple sources
    // public DeluxeBST(Digraph G, Iterable<Integer> sources) {
    //     marked = new boolean[G.V()];
    //     distTo = new int[G.V()];
    //     for (int v = 0; v < G.V(); v++) distTo[v] = INFINITY;
    //     bfs(G, sources);
    // }

    // BFS from single source
    public void bfs(Digraph G, int s) {
        Queue<Integer> q = new Queue<Integer>();
        marked[s] = true;
        distTo[s] = 0;
        q.enqueue(s);
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    q.enqueue(w);
                }
            }
        }
    }

    // BFS from multiple sources
    public void bfs(Digraph G, Iterable<Integer> sources) {
        Queue<Integer> q = new Queue<Integer>();
        touched = new Queue<Integer>();
        for (int s : sources) {
            marked[s] = true;
            distTo[s] = 0;
            touched.enqueue(s);
            q.enqueue(s);
        }
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    touched.enqueue(w);
                    q.enqueue(w);
                }
            }
        }
    }

    // length of shortest path from s (or sources) to v
    public int distTo(int v) {
        return distTo[v];
    }

    // is there a directed path from s (or sources) to v?
    public boolean hasPathTo(int v) {
        return marked[v];
    }

    public Iterable<Integer> covering() {
        return touched;
    }

    public void reset() {
        for (int v : touched) {
            marked[v] = false;
            distTo[v] = INFINITY;
        }
    }

    public static void main(String[] args) {
        DeluxeBST bfs = new DeluxeBST(2);
    }


}
