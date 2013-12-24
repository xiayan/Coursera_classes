public class SeamFinder {
    private static final int INFINITY = Integer.MAX_VALUE;
    private static final int CORNERVAL = 195705;    
    int[][] energies;
    int[][] dist;
    boolean transposed;
    int height;
    int width;
    int mID;

    public SeamFinder(int[][] e, int h, int w, boolean t) {
        energies = e;
        height = h;
        width = w;
        transposed = t;

        dist = new int[height][width];
        relax();
        
        if (transposed)
            mID = findHorizontalID();
        else
            mID = findVerticalID();
    }

    public int[] verticalSeam() {
        return getVerticalSeam(mID);
    }

    public int[] horizontalSeam() {
        return getHorizontalSeam(mID);
    }
   
    private void initializeDist() {
        if (!transposed) {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (i == 0) dist[i][j] = CORNERVAL;
                    else        dist[i][j] = INFINITY;
                }
            }
        } else {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (i == 0) dist[j][i] = CORNERVAL;
                    else        dist[j][i] = INFINITY;
                }
            }
        }
    }    

    private void relax() {
        initializeDist();

        if (!transposed) {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    checkLowerLeft(i, j);
                    checkLowerDown(i, j);
                    checkLowerRight(i, j);
                }
            }
        } else {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    checkRightUp(j, i);
                    checkRightMid(j, i);
                    checkRightDown(j, i);
                }
            }
        }     
    }

    private int findVerticalID() {
        double minVal = dist[height - 1][0];
        int    minID  = 0;

        for (int i = 1; i < width; i++) {
            if (dist[height - 1][i] < minVal) {
                minVal = dist[height - 1][i];
                minID  = i;
            }
        }
        return minID;
    }

    private int findHorizontalID() {
        double minVal = dist[0][width - 1];
        int     minID = 0;

        for (int i = 1; i < height; i++) {
            if (dist[i][width - 1] < minVal) {
                minVal = dist[i][width - 1];
                minID = i;
            }
        }
        return minID;        
    }

    private void checkLowerLeft(int i, int j) {
        int k = i + 1;
        int l = j - 1;
        if (l < 0 || k >= height) return;

        if (dist[i][j] + energies[k][l] < dist[k][l])
            dist[k][l] = dist[i][j] + energies[k][l];
    }

    private void checkLowerDown(int i, int j) {
        int k = i + 1;
        int l = j;
        if (k >= height) return;

        if (dist[i][j] + energies[k][l] < dist[k][l])
            dist[k][l] = dist[i][j] + energies[k][l];
    }

    private void checkLowerRight(int i, int j) {
        int k = i + 1;
        int l = j + 1;
        if (l >= width || k >= height) return;

        if (dist[i][j] + energies[k][l] < dist[k][l])
            dist[k][l] = dist[i][j] + energies[k][l];
    }

    private void checkRightUp(int j, int i) {
        int k = j - 1;
        int l = i + 1;
        if (k < 0 || l >= width) return;

        if (dist[j][i] + energies[k][l] < dist[k][l])
            dist[k][l] = dist[j][i] + energies[k][l];
    }

    private void checkRightMid(int j, int i) {
        int k = j;
        int l = i + 1;
        if (l >= width) return;

        if (dist[j][i] + energies[k][l] < dist[k][l])
            dist[k][l] = dist[j][i] + energies[k][l];
    }
    
    private void checkRightDown(int j, int i) {
        int k = j + 1;
        int l = i + 1;
        if (k >= height || l >= width) return;

        if (dist[j][i] + energies[k][l] < dist[k][l])
            dist[k][l] = dist[j][i] + energies[k][l];
    }
    
    private int[] getVerticalSeam(int m) {
        int[] seam = new int[height];
        int mID = m;
        seam[height - 1] = mID;
        int parent = mID;
        int child = mID;

        for (int i = height - 2; i >= 0; i--) {
            double childDist = dist[i + 1][mID];
            double childEnry = energies[i + 1][mID];

            if (mID - 1 >= 0 
                    && childDist == childEnry + dist[i][mID - 1]) {
                parent = mID - 1;
            } else if (childDist == childEnry + dist[i][mID]) {
                parent = mID;
            } else if (mID + 1 < width
                    && childDist == childEnry + dist[i][mID + 1]) {
                parent = mID + 1;
            } else {
                StdOut.println("Can't find parent!");
            }

            seam[i] = parent;
            mID = parent;
        }

        return seam;
    }
    
    private int[] getHorizontalSeam(int m) {
        //for (double[] x : energies) {
        //    for (double y : x)
        //        StdOut.printf("%f ", y);
        //    StdOut.println();
        //}

        int[] seam = new int[width];
        int mID = m;
        seam[width - 1] = mID;
        int parent = mID;
        int child = mID;

        for (int i = width - 2; i >= 0; i--) {
            double childDist = dist[mID][i + 1];
            double childEnry = energies[mID][i + 1];

            if (mID - 1 >= 0 
                    && childDist == childEnry + dist[mID - 1][i]) {
                parent = mID - 1;
            } else if (childDist == childEnry + dist[mID][i]) {
                parent = mID;
            } else if (mID + 1 < height
                    && childDist == childEnry + dist[mID + 1][i]) {
                parent = mID + 1;
            } else {
                StdOut.println("Can't find parent!");
            }

            seam[i] = parent;
            mID = parent;
        }

        return seam;
    }
}
