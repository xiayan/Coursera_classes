public class Board {
    private int[][] entries;
    private final int dimension;
    private int blankRow;
    private int blankCol;

    public Board(int[][] blocks) {
        dimension = blocks.length;
        entries = new int[dimension][dimension];

        for (int i = 0; i < dimension; i++)
            for (int j = 0; j < dimension; j++) {
                int number = blocks[i][j];
                entries[i][j] = number;
                if (number == 0) {
                    blankRow = i;
                    blankCol = j;
                }
            }
    }

    public int dimension() { return dimension; }

    public int hamming() {
       int outOfPlace = 0;
       for (int i = 0; i < dimension; i++)
            for (int j = 0; j < dimension; j++) {
                int currentNumber = entries[i][j];
                if (currentNumber == 0) continue;
                if (currentNumber != i * dimension + j + 1)
                    outOfPlace++;
            }
        return outOfPlace;
    }

    public int manhattan() {
        int totalManhattna = 0;

        for (int i = 0; i < dimension; i++)
            for (int j = 0; j < dimension; j++) {
                int number = entries[i][j];
                if (number == 0) continue;
                int row = (number - 1) / dimension;
                int col = (number - 1) % dimension;
                totalManhattna += (Math.abs(i - row) + Math.abs(j - col));
            }
        return totalManhattna;
    }

    public boolean isGoal() {
        return this.hamming() == 0;
    }

    public Board twin() {
        Board twin = new Board(entries);

        int row = 0;
        if (blankRow == 0) row = 1;
        else row = 0;

        twin.exchange(row, 0, row, 1);
        return twin;
    }

    public boolean equals(Object y) {
        if (y == this) return true;

        if (y == null) return false;

        if (y.getClass() != this.getClass())
            return false;

        Board that = (Board) y;
        if (this.dimension != that.dimension) return false;
        int dim = this.dimension;
        for (int i = 0; i < dim; i++)
            for (int j = 0; j < dim; j++) {
                if (this.entries[i][j] != that.entries[i][j])
                    return false;
            }
        return true;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(dimension + "\n");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                result.append(String.format("%2d ", entries[i][j]));
            }
            result.append("\n");
        }
        return result.toString();
    }

    public Iterable<Board> neighbors() {
        Queue<Board> n = new Queue<Board>();
        if (blankRow > 0) {
            Board upNeigbor = new Board(entries);
            upNeigbor.exchange(blankRow, blankCol,
                               blankRow - 1, blankCol);
            n.enqueue(upNeigbor);
        }

        if (blankRow < dimension - 1) {
            Board downNeigbor = new Board(entries);
            downNeigbor.exchange(blankRow, blankCol,
                               blankRow + 1, blankCol);
            n.enqueue(downNeigbor);
        }

        if (blankCol != 0) {
            Board leftNeigbor = new Board(entries);
            leftNeigbor.exchange(blankRow, blankCol,
                               blankRow, blankCol - 1);
            n.enqueue(leftNeigbor);
        }

        if (blankCol < dimension - 1) {
            Board rightNeigbor = new Board(entries);
            rightNeigbor.exchange(blankRow, blankCol,
                               blankRow, blankCol + 1);
            n.enqueue(rightNeigbor);
        }
        return n;
    }

    private void exchange(int i, int j, int m, int n) {
        int temp = entries[i][j];
        entries[i][j] = entries[m][n];
        entries[m][n] = temp;
        if (i == blankRow && j == blankCol) {
            blankRow = m;
            blankCol = n;
            return;
        }
        if (m == blankRow && n == blankCol) {
            blankRow = i;
            blankCol = j;
            return;
        }
    }

    public static void main(String[] args) {
        int[][] test = { {1, 2, 3}, {4, 6, 5}, {7, 0, 8} };
        Board board = new Board(test);
        StdOut.print(board.toString());
        StdOut.printf("BlankRow:%d, BlankCol:%d\n", board.blankRow, 
                                                    board.blankCol);
        StdOut.printf("Hamming: %d\n", board.hamming());
        StdOut.printf("Manhattan: %d\n", board.manhattan());
        StdOut.printf("isGoal: %b\n", board.isGoal());

        Board twin = board.twin();
        StdOut.println("board");
        StdOut.print(board.toString());
        StdOut.println("twin");
        StdOut.print(twin.toString());

        StdOut.printf("Equals: %b\n", board.equals(twin));
        StdOut.println();
        StdOut.println();

        Iterable<Board> neighbors = board.neighbors();
        for (Board x : neighbors) {
            StdOut.println(x.toString());
            StdOut.println("neighbors:");
            for (Board y : x.neighbors())
                StdOut.println(y.toString());
        }
    }
}