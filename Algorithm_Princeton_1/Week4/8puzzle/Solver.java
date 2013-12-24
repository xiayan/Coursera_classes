public class Solver {
    private MinPQ<SearchNode> minQ = new MinPQ<SearchNode>();
    private MinPQ<SearchNode> twinQ = new MinPQ<SearchNode>();
    private Board test;
    private SearchNode result;
    private boolean solvable;
    private int steps;

    // Data type to put on MinPQ
    private class SearchNode implements Comparable<SearchNode> {
        private final Board bd;
        private final int moves;
        private final int totalMan;
        private final SearchNode previous;

        public SearchNode(Board t, int m, SearchNode p) {
            bd = t;
            moves = m;
            previous = p;
            totalMan = m + bd.manhattan();
        }

        public Iterable<SearchNode> neigborNodes() {
            Iterable<Board> neigborBoards = bd.neighbors();
            Queue<SearchNode> results = new Queue<SearchNode>();
            for (Board b : neigborBoards) {
                if (previous == null || !b.equals(previous.bd)) {
                    SearchNode newNode = new SearchNode(b, moves + 1, this);
                    results.enqueue(newNode);
                }
            }
            return results;
        }

        public boolean isGoal() { return bd.isGoal(); }

        public int compareTo(SearchNode that) {
            if (this.totalMan < that.totalMan) return -1;
            else if (this.totalMan == that.totalMan) return 0;
            else return 1;
        }
    }

    public Solver(Board initial) {
        SearchNode initNode = new SearchNode(initial, 0, null);
        SearchNode twinNode = new SearchNode(initial.twin(), 0, null);
        minQ.insert(initNode);
        twinQ.insert(twinNode);
        int counter = 0;

        while (true) {
            // take care of size issue
            SearchNode minNode = minQ.delMin();
            if (minNode.isGoal()) {
                result = minNode;
                solvable = true;
                steps = minNode.moves;
                break;
            }
            Iterable<SearchNode> nodes = minNode.neigborNodes();
            for (SearchNode s : nodes)
                minQ.insert(s);

            SearchNode minTwin = twinQ.delMin();
            if (minTwin.isGoal()) {
                solvable = false;
                steps = -1;
                break;
            }
            Iterable<SearchNode> twins = minTwin.neigborNodes();
            for (SearchNode t : twins)
                twinQ.insert(t);
        }

    }

    public boolean isSolvable() { return solvable; }

    public int moves() { return steps; }

    public Iterable<Board> solution() {
        if (!solvable) return null;

        SearchNode current = result;
        Stack<Board> sol = new Stack<Board>();
        do {
            sol.push(current.bd);
            current = current.previous;
        } while (current != null);
        return sol;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = "
                           + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}