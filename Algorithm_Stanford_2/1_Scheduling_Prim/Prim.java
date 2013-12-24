// Answer is -3612829
/*
 * Prim algorithm for MST of a weighted graph.
 * Greedily choose the minimum weighted node that in the visited-unvisited interface.
 * Use min-heap to speed up the computation
 */

public class Prim {
    private EdgeWeightedGraph graph;    // a weighted graph
    private int totalScore;             // total mst score
    private IndexMinPQ<Integer> minPQ;  // to score local winner node for each round
    private boolean[] visitedNodes;     // keep track of which nodes are visited
    private int numOfEdges;
    private int numOfNodes;

    public Prim(In input) {
        this.initializeGraph(input);
        this.initializeMinPQ();
        this.solveBST();
    }

    public int getTotalScore() { return totalScore; }

    private void initializeGraph(In input) {
        String[] items;
        
        // parse number of nodes and number of edges from the first line
        if (input.hasNextLine()) {
            items = input.readLine().split(" ");
            if (items.length != 2) { System.out.println("Illegal Format"); return; }
            numOfNodes = Integer.parseInt(items[0]);
            numOfEdges = Integer.parseInt(items[1]);
        }
        
        // Initiate graph, visitedNodes and minPQ
        graph = new EdgeWeightedGraph(numOfNodes);
        visitedNodes = new boolean[numOfNodes];
        minPQ = new IndexMinPQ<Integer>(numOfNodes);

        // mark the first node as visited
        visitedNodes[0] = true;
        
        // construct the graph
        while (input.hasNextLine()) {
            items = input.readLine().split(" ");
            if (items.length < 3) continue;
            // parse information from each line
            int node1 = Integer.parseInt(items[0]) - 1;
            int node2 = Integer.parseInt(items[1]) - 1;
            int weigh = Integer.parseInt(items[2]);
            Edge newEdge = new Edge(node1, node2, weigh);
            graph.addEdge(newEdge);
        }
    }

    private void initializeMinPQ() {
        // choose node 0 to be the start node
        for (Edge e : graph.adj(0)) {
            // iterate through all the neibors of node 0;
            int head = e.other(0);
            int w = (int)e.weight();
            // add them to the minPQ
            minPQ.insert(head, w);
        }
    }

    private void solveBST() {
        // every iteration, the heap present a new(unvisited node), so after numofNodes rounds, a MST is generated.
        // This process iterate exactly number of nodes - 1 rounds
        for (int i = 1; i < numOfNodes; i++) {
            totalScore += minPQ.minKey();
            int winner = minPQ.delMin();
            visitedNodes[winner] = true;
            
            // update the neigbors of local winner
            for (Edge e : graph.adj(winner)) {
                int head = e.other(winner);
                // if both ends are visited, continue
                if (visitedNodes[head] == true) continue;
                if (!minPQ.contains(head)) minPQ.insert(head, (int)e.weight());
                else {
                    int newScore = Math.min(minPQ.keyOf(head), (int)e.weight());
                    minPQ.changeKey(head, newScore);
                }
            }
        }
        // assert that all the nodes are indeed visited
        assert allVisited();
    }
    
    private boolean allVisited() {
        for (int i = 0; i < numOfNodes; i++) {
            if (visitedNodes[i] == false) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        In input = new In("edges.txt");
        Prim newGraph = new Prim(input);
        System.out.println(newGraph.getTotalScore());
    }
}
