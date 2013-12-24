public class PointSET {
    private SET<Point2D> pSet;

    public PointSET() { pSet = new SET<Point2D>(); }

    public boolean isEmpty() { return pSet.isEmpty(); }

    public int size() { return pSet.size(); }

    public void insert(Point2D p) {
        if (!pSet.contains(p))
            pSet.add(p);
    }

    public boolean contains(Point2D p) { return pSet.contains(p); }

    public void draw() {
        for (Point2D p : pSet)
            p.draw();
    }

    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> n = new Queue<Point2D>();
        for (Point2D p : pSet)
            if (rect.contains(p))
                n.enqueue(p);
        return n;
    }

    public Point2D nearest(Point2D p) {
        if (pSet.isEmpty()) return null;
        double minDist = Double.POSITIVE_INFINITY;
        Point2D winner = p;
        for (Point2D point : pSet) {
            double currentDist = point.distanceSquaredTo(p);
            if (currentDist < minDist) {
                minDist = currentDist;
                winner = point;
            }
        }
        return winner;
    }
}