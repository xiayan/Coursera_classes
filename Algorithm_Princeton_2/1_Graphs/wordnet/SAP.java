import java.util.Set;
import java.util.TreeSet;

public class SAP {
    private static final int INFINITY = Integer.MAX_VALUE;
    private Digraph G;
    private DeluxeBST bfsA;
    private DeluxeBST bfsB;
    
    private Set<Integer> currentV;  // cache the v value from last calculation
    private Set<Integer> currentS;  // cache the s value from last calculation
    private int ancestor = -1;  // cache the ancestor from last calculation
    private int shortest = INFINITY;  
    // cache the shortest length from last calculation

    public SAP(Digraph g) {
        // Make a new copy of G to make the datatype immutable
        G = new Digraph(g);
        currentV = new TreeSet<Integer>();
        currentS = new TreeSet<Integer>();
        bfsA     = new DeluxeBST(G.V());
        bfsB     = new DeluxeBST(G.V());
    }

    private boolean tested(Iterable<Integer> a, Iterable<Integer> b) {
        Set<Integer> fromA = new TreeSet<Integer>();
        Set<Integer> fromB = new TreeSet<Integer>();

        for (int x : a) {
            if (x < 0 || x > G.V() - 1)
                throw new IndexOutOfBoundsException();
            fromA.add(x);
        }
        
        for (int y : b) {
            if (y < 0 || y > G.V() - 1)
                throw new IndexOutOfBoundsException();
            fromB.add(y);
        }

        return ((currentV.equals(fromA) && currentS.equals(fromB))
                || (currentV.equals(fromB) && currentS.equals(fromA)));
    }

    private boolean tested(int a, int b) {
        Stack<Integer> q1 = new Stack<Integer>();
        Stack<Integer> q2 = new Stack<Integer>();
        q1.push(a);
        q2.push(b);
        
        return tested(q1, q2);
    }

    private void initialize(Iterable<Integer> a, Iterable<Integer> b) {
        currentV.clear();
        currentS.clear();

        for (int x : a)
            currentV.add(x);
        for (int y : b)
            currentS.add(y);

        shortest = INFINITY;
        ancestor = -1;

        bfsA.reset();
        bfsB.reset();
        bfsA.bfs(G, a);
        bfsB.bfs(G, b);
    }

    private void initialize(int a, int b) {
        Stack<Integer> q1 = new Stack<Integer>();
        Stack<Integer> q2 = new Stack<Integer>();
        q1.push(a);
        q2.push(b);

        initialize(q1, q2);
    }
    
    private void identifyAncestor(Iterable<Integer> a, Iterable<Integer> b) {
        Iterable<Integer> covered = bfsA.covering();
        int distA, distB, sum;
        
        for (int c : covered) {
            if (!bfsB.hasPathTo(c)) continue;

            distA = bfsA.distTo(c);
            distB = bfsB.distTo(c);
            if (distA > shortest) return;

            sum = distA + distB;
            if (sum < shortest) {
                shortest = sum;
                ancestor = c;
            }
        }
    }

    private void identifyAncestor(int a, int b) {
        Stack<Integer> q1 = new Stack<Integer>();
        Stack<Integer> q2 = new Stack<Integer>();
        q1.push(a);
        q2.push(b);
        
        identifyAncestor(q1, q2);
    }

    public int length(int a, int b) {
        if (!tested(a, b)) {
            initialize(a, b);
            identifyAncestor(a, b);
        }

        if (shortest == INFINITY) return -1;
        else                      return shortest;
    }

    public int ancestor(int a, int b) {
        if (!tested(a, b)) {
            initialize(a, b);
            identifyAncestor(a, b);
        }
        return ancestor;
    }

    public int length(Iterable<Integer> a, Iterable<Integer> b) {
        if (!tested(a, b)) {
            initialize(a, b);
            identifyAncestor(a, b);
        }

        if (shortest == INFINITY) return -1;
        else                      return shortest;
    }

    public int ancestor(Iterable<Integer> a, Iterable<Integer> b) {
        if (!tested(a, b)) {
            initialize(a, b);
            identifyAncestor(a, b);
        }
        return ancestor;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            // int x = StdIn.readInt();
            // int y = StdIn.readInt();
            // Queue<Integer> q1 = new Queue<Integer>();
            // Queue<Integer> q2 = new Queue<Integer>();
            // q1.enqueue(v);
            // q2.enqueue(w);
            // q2.enqueue(x);
            // q2.enqueue(y);
            // int l = sap.length(q1,q2);
            // int a = sap.ancestor(q1, q2);
            // StdOut.printf("length = %d, ancester = %d\n", l, a);
            int l = sap.length(v, w);
            int a = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancester = %d\n", l, a);
        }
    }
}

