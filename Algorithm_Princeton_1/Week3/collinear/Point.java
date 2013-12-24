/*************************************************************************
 * Name:
 * Email:
 *
 * Compilation:  javac Point.java
 * Execution:
 * Dependencies: StdDraw.java
 *
 * Description: An immutable data type for points in the plane.
 *
 *************************************************************************/

import java.util.Comparator;

public class Point implements Comparable<Point> {

    // compare points by slope
    public final Comparator<Point> SLOPE_ORDER = new BySlope();

    private final int x;                              // x coordinate
    private final int y;                              // y coordinate

    // create the point (x, y)
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    // plot this point to standard drawing
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    // draw line between this point and that point to standard drawing
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // slope between this point and that point
    public double slopeTo(Point that) {
        /* YOUR CODE HERE */
        if (this.x == that.x) {
            if (this.y == that.y) return Double.NEGATIVE_INFINITY;
            else return Double.POSITIVE_INFINITY;
        } else return (that.y - this.y + 0.0) / (that.x - this.x + 0.0);
    }

    // is this point lexicographically smaller than that one?
    // comparing y-coordinates and breaking ties by x-coordinates
    public int compareTo(Point that) {
        /* YOUR CODE HERE */
        if (this.y != that.y) {
            if (this.y > that.y) return 1;
            else return -1;
        }
        else {
            if (this.x > that.x) return 1;
            else if (this.x < that.x) return -1;
            else return 0;
        }
    }

    // return string representation of this point
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    private class BySlope implements Comparator<Point> {
        public int compare(Point a, Point b) {
            Point s = new Point(x, y);
            double slopeA = s.slopeTo(a);
            double slopeB = s.slopeTo(b);
            if (slopeA < slopeB) return -1;
            else if (slopeA == slopeB) return 0;
            else return 1;
        }
    }

    // unit test
    public static void main(String[] args) {
        /* YOUR CODE HERE */
        Point A = new Point(1, 2);
        Point B = new Point(2, 1);
        Point C = new Point(1, 2);
        Point D = new Point(2, 2);

        StdOut.println(A.compareTo(B));
        StdOut.println(A.compareTo(C));
        StdOut.println(B.compareTo(C));

        Point[] aSet = new Point[]{A, B, C, D};
        java.util.Arrays.sort(aSet, A.SLOPE_ORDER);

        for (Point x : aSet)
            StdOut.println(x);

        Point E = new Point(8192, 25088);
        Point F = new Point(8192, 26112);

        StdOut.println(E.slopeTo(F));
    }
}