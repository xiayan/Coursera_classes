public class KdTree {
    private static boolean HORIZONTAL = true;
    private static boolean VERTICAL = false;

    private Node root;
    private int size = 0;
    private Point2D winner;
    private double score;

    private static class Node {
        private Point2D p;
        private RectHV rect;
        private Node left, right;
        private int level;
        private boolean split;

        public Node(Point2D p, RectHV rect, int level) {
            this.p = p;
            this.rect = rect;
            this.level = level;
            if (level % 2 == 0) this.split = VERTICAL;
            else this.split = HORIZONTAL;
        }
    }

    public KdTree() { }

    // is the symbol table empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return number of key-value pairs in BST
    public int size() {
        return size;
    }

    /* Contains() related methods
    */
    public boolean contains(Point2D p) {
        return (get(root, p) != null);
    }

    private Point2D get(Node x, Point2D p) {
        if (x == null) return null;
        int cmp = compareNP(p, x);
        if      (cmp < 0) return get(x.left, p);
        else if (cmp > 0) return get(x.right, p);
        else              return x.p;
    }

    private int compareNP(Point2D a, Node n) {
        // if points are the same, return 0;
        // if two points have the same x or y value, always go right
        if (a.equals(n.p)) return 0;
        if (n.split == HORIZONTAL) {
            int diff = (int) Math.signum(a.y() - n.p.y());
            if (diff == 0) return 1;
            else return diff;
        }
        else {
            int diff = (int) Math.signum(a.x() - n.p.x());
            if (diff == 0) return 1;
            else return diff;
        }
    }

    /* insert() related methods
    */
    public void insert(Point2D p) {
        if (p == null) return;
        root = put(root, p);
    }

    private Node put(Node x, Point2D p) {
        if (size == 0) {
            size++;
            RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
            return new Node(p, rect, 0);
        }
        return put(x, root, p, true);
    }

    private Node put(Node x, Node parent, Point2D p, boolean left) {
        if (x == null) {
            size++;
            RectHV rect = null;
            RectHV pRect = parent.rect;
            int level = parent.level + 1;

            if (parent.split == HORIZONTAL && left)
                rect = new RectHV(pRect.xmin(), pRect.ymin(),
                                  pRect.xmax(), parent.p.y());
            else if (parent.split == HORIZONTAL && !left)
                rect = new RectHV(pRect.xmin(), parent.p.y(),
                                  pRect.xmax(), pRect.ymax());
            else if (parent.split == VERTICAL && left)
                rect = new RectHV(pRect.xmin(), pRect.ymin(),
                                  parent.p.x(), pRect.ymax());
            else
                rect = new RectHV(parent.p.x(), pRect.ymin(),
                                  pRect.xmax(), pRect.ymax());

            return new Node(p, rect, level);
        }

        int cmp = compareNP(p, x);
        if      (cmp < 0) x.left  = put(x.left, x, p, true);
        else if (cmp > 0) x.right = put(x.right, x, p, false);
        return x;
    }

    /* draw() related methods
    */

    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        root.rect.draw();
        draw(root);
    }

    private void draw(Node x) {
        if (x == null) return;
        // Draw the point
        double x0, y0, x1, y1;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        x.p.draw();
        // Draw the line according to the rect, the split and the point
        StdDraw.setPenRadius(.0025);
        if (x.split == VERTICAL) {
            StdDraw.setPenColor(StdDraw.RED);
            x0 = x.p.x();
            y0 = x.rect.ymin();
            x1 = x0;
            y1 = x.rect.ymax();
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            x0 = x.rect.xmin();
            y0 = x.p.y();
            x1 = x.rect.xmax();
            y1 = y0;
        }
        StdDraw.line(x0, y0, x1, y1);
        // Recursively call the left child and right child
        draw(x.left);
        draw(x.right);
    }

    /* range() related methods
    */

    public Iterable<Point2D> range(RectHV rect) {
    // Create a queue, and create a new recursive method that takes
    // the queue as one argument and keep adding points into the queue
        Queue<Point2D> queue = new Queue<Point2D>();
        range(rect, root, queue);
        return queue;
    }

    private void range(RectHV rect, Node x, Queue<Point2D> q) {
        if (x == null) return;
        if (!x.rect.intersects(rect)) return;
        if (rect.contains(x.p)) q.enqueue(x.p);
        range(rect, x.left, q);
        range(rect, x.right, q);
    }

    /* nearest() related methods
    */

    public Point2D nearest(Point2D p) {
    // always go to the child node that contains the query point first.
    // Then use the pruning rule to further eliminate substrees.
        score = Double.POSITIVE_INFINITY;
        nearest(p, root);
        return this.winner;
    }

    private void nearest(Point2D point, Node x) {
        // Pruning rule
        if (x.rect.distanceSquaredTo(point) > score) return;

        double dist = x.p.distanceSquaredTo(point);
        if (dist < score) {
            this.winner = x.p;
            score = dist;
        }

        if (x.left == null && x.right == null) return;
        else if (x.left == null && x.right != null)
            nearest(point, x.right);
        else if (x.left != null && x.right == null)
            nearest(point, x.left);
        else {
            if (x.left.rect.contains(point)) {
                nearest(point, x.left);
                nearest(point, x.right);
            } else {
                nearest(point, x.right);
                nearest(point, x.left);
            }
        }
    }

    public static void main(String[] args) {
        KdTree tree = new KdTree();
        StdOut.printf("tree is empty: %b, size is: %d\n",
                      tree.isEmpty(), tree.size());
        tree.insert(new Point2D(0.7, 0.2));
        StdOut.printf("tree is empty: %b, size is: %d\n",
                      tree.isEmpty(), tree.size());
        tree.insert(new Point2D(0.5, 0.4));
        StdOut.printf("tree is empty: %b, size is: %d\n",
                      tree.isEmpty(), tree.size());
        tree.insert(new Point2D(0.2, 0.3));
        tree.insert(new Point2D(0.4, 0.7));
        tree.insert(new Point2D(0.9, 0.6));
        tree.draw();
        StdOut.printf("tree is empty: %b, size is: %d\n",
                      tree.isEmpty(), tree.size());
        StdOut.printf("tree contains (0.2, 0.3): %b\n",
                      tree.contains(new Point2D(0.2, 0.3)));
        StdOut.printf("tree contains (0.3, 0.3): %b\n",
                      tree.contains(new Point2D(0.3, 0.3)));
        StdOut.printf("tree contains (0.9, 0.6): %b\n",
                      tree.contains(new Point2D(0.9, 0.6)));
    }
}
