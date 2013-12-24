public class Percolation {
    private int n;
    private int size;
    private WeightedQuickUnionUF tree;
    private WeightedQuickUnionUF check;
    private boolean started;
    private boolean[] openSites;

    public Percolation(int N) {
        // create N-by-N grid, with all sites blocked
        n = N;
        size = N*N + 2;
        tree = new WeightedQuickUnionUF(size);
        check = new WeightedQuickUnionUF(size);
        started = false;
        openSites = new boolean[size];

        for (int i = 0; i < size; i++) {
            openSites[i] = false;
        }

        for (int i = 1; i < N+1; i++) {
            tree.union(0, i);
            tree.union(size-1, N*(N-1)+i);
            check.union(0, i);
        }
    }

    public void open(int i, int j) {
        // open site (row i, column j) if it is not already
        started = true;
        int rank = n*(i-1) + j;
        if (i < 1 || i > n || j < 1 || j > n)
            throw new
                IndexOutOfBoundsException("row index i out of bounds");

        if (openSites[rank])
            return;

        openSites[rank] = true;
        int up = rank - n;
        if (up < 0)
            up = -1;

        int down = rank + n;
        if (down > size - 2)
            down = -1;

        int left = rank - 1;
        if (rank % n == 1)
            left = -1;

        int right = rank + 1;
        if (rank % n == 0)
            right = -1;

        int[] directions = new int[]{up, down, left, right};

        for (int k = 0; k < 4; k++) {
            int x = directions[k];
            if (x > 0 && openSites[x]) {
                tree.union(rank, x);
                check.union(rank, x);
            }
        }
    }

    public boolean isOpen(int i, int j) {
        // is site (row i, column j) open?
        int rank = n*(i-1) + j;
        if (i < 1 || i > n || j < 1 || j > n) {
            throw new
                IndexOutOfBoundsException("row index i out of bounds");
        }
        return openSites[rank];
    }

    public boolean isFull(int i, int j) {
        // is site (row i, column j) full?
        int rank = n*(i-1) + j;
        if (i < 1 || i > n || j < 1 || j > n) {
            throw new
                IndexOutOfBoundsException("row index i out of bounds");
        }
        if (!this.isOpen(i, j))
            return false;

        return check.connected(0, rank);
    }

    public boolean percolates() {
        // does the system percolate?
        if (!started)
            return false;

        return tree.connected(0, size-1);
    }
}