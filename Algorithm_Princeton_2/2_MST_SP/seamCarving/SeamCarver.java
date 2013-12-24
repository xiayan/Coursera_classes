import java.awt.Color;

public class SeamCarver {
    private static final int CORNERVAL = 195705;
    private Picture pic;
    private int width;
    private int height;
    private int[][] energies;

    public SeamCarver(Picture p) {
        pic = p;
        width = pic.width();
        height = pic.height();
        energies = new int[height][width];
        initializeEnergies();
    }

    public Picture picture() {
        // current picture
        Picture image = new Picture(width, height);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                image.set(i, j, pic.get(i, j));

        pic = image;
        return pic;
    }

    private void initializeEnergies() {
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                energies[i][j] = calEnergy(j, i);
    }

    public int width() {
        // width of current picture
        return pic.width();
    }

    public int height() {
        // height of current picture
        return pic.height();
    }

    public double energy(int x, int y) {
        if (x < 0 || x > width - 1 || y < 0 || y > height - 1) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        
        return energies[y][x];
    }

    private int getDelta(Color a, Color b) {
        int dRed = a.getRed() - b.getRed();
        int dGrn = a.getGreen() - b.getGreen();
        int dBlu = a.getBlue() - b.getBlue();
        return dRed * dRed + dGrn * dGrn + dBlu * dBlu;
    }

    private int calEnergy(int x, int y) {
        // energy of pixel at column x and row y
        //if (transposed) {
        //    int temp = x;
        //    x = y;
        //    y = temp;
        //}

        if (x == 0 || y == 0 || x == pic.width() - 1 || y == pic.height() - 1)
            return CORNERVAL;

        Color left  = pic.get(x - 1, y);
        Color right = pic.get(x + 1, y);
        Color up    = pic.get(x, y - 1);
        Color down  = pic.get(x, y + 1);

        int deltaX = getDelta(left, right);
        int deltaY = getDelta(up, down);

        return deltaX + deltaY;
    }

    public int[] findVerticalSeam() {
        SeamFinder sf = new SeamFinder(energies, height, width, false);
        return sf.verticalSeam();
    }
    
    public int[] findHorizontalSeam() {
        SeamFinder sf = new SeamFinder(energies, height, width, true);
        return sf.horizontalSeam();
    }
    
    private boolean inValidSeam(int[] a) {
        int diff;
        for (int i = 1; i < a.length; i++) {
            diff = a[i] - a[i - 1];
            if (diff < -1 || diff > 1) return true;
        }

        return false;
    }

    public void removeVerticalSeam(int[] a) {
        // remove vertical seam from picture
        
        if ((a.length != pic.height() && a.length != height) || width == 0)
            throw new java.lang.IllegalArgumentException();

        for (int i = 0; i < height; i++) {
            int s = a[i];
            for (int j = s; j < width - 1; j++) {
                pic.set(j, i, pic.get(width - 1, i));
            }
            // pic.set(width - 2, i, pic.get(width - 1, i));
        }

        width--;
        //for (int i = 0; i < height; i++) {
        //    int s = a[i] - 1;
        //    if (s < 0) s = 0;
        //    for (int j = s; j < width; j++) {
        //        energies[i][s] = calEnergy(s, i);
        //    }
        //}
        
        return;
    }

    public void removeHorizontalSeam(int[] a) {
        // remove horizontal seam from picture
        if ((a.length != pic.width() && a.length != width) || height == 0)
            throw new java.lang.IllegalArgumentException();

        for (int i = 0; i < width; i++) {
            int s = a[i];
            for (int j = s; j < height - 1; j++) {
                pic.set(i, j, pic.get(i, height - 1));
            }
            // pic.set(i, height - 2, pic.get(i, height - 1));
        }

        height--;
        //for (int i = 0; i < width; i++) {
        //    int s = a[i] - 1;
        //    if (s < 0) s = 0;
        //    for (int j = s; j < height; j++) {
        //        energies[s][i] = calEnergy(i, s);
        //    }
        //}

        return;
    }

    public static void main(String[] args) {
        SeamCarver seamCarver = new SeamCarver(new Picture(args[0]));
        for (int i = 0; i < seamCarver.height; i++) {
            for (int j = 0; j < seamCarver.width; j++) {
                Color c = seamCarver.pic.get(j, i);
                StdOut.printf("%d %d %d ", c.getRed(), c.getGreen(), c.getBlue());
            }
            StdOut.println();
        }
        StdOut.println();
        int[] seam = seamCarver.findVerticalSeam();
        seamCarver.removeVerticalSeam(seam);

        for (int i = 0; i < seamCarver.height; i++) {
            for (int j = 0; j < seamCarver.width; j++) {
                Color c = seamCarver.pic.get(j, i);
                StdOut.printf("%d %d %d ", c.getRed(), c.getGreen(), c.getBlue());
            }
            StdOut.println();
        }

    }
}

