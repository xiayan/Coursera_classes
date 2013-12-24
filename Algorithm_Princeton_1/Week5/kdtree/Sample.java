/*  Copyright (c) 1998 by Peter Szolovits and Massachusetts Institute of Technology.

This code was developed as part of the Maita project at the Clinical Decision
Making Group of the MIT Laboratory for Computer Science, under funding from
the HPKB (High Performance Knowledge Base) program of DARPA.

Permission is granted to use this code on the conditions that:
 1.  There is no warranty, implied or explicit, that this code is correct or
     does anything useful.  Therefore the user will not hold above copyright
     holders in any way responsible or liable for losses resulting from using
     this code or causing it to be used.
 2.  This copyright notice is retained on any copies made of this code.
 3.  Alterations may be made but must be identified as derived from this work.
 4.  No derivative software substantially incorporating this code may be sold
     for profit without explicit permission of the above copyright holders.
*/

import com.sun.java.util.collections.*;

/*
    Implementation of KD trees, approximately as described in Chapter 5 of
    deBerg, et al., Geometric Algorithms, Springer 1997.

    To create a KdTree from a Vector of points, call KdTree.make(v).
    To create a Vector of points that fall within a Region r, call findPts(r).
    */

public class Sample {
    double line;
    Object left;
    Object right;

    /*
    KdTree(double split) {
        this(split, null, null);
    }
    */

    KdTree(double split, Object l, Object r) {
        line = split;
        left = l;
        right = r;
    }

    public String toString() {
        return "<KD@"+line+">";
    }

    public void display() {
        display(0);
    }

    void display(int depth) {
        String prefx = "";
        for (int i = depth; i > 0; i--) prefx = prefx + "  ";
        System.out.println(prefx + line + ((depth%2 == 0) ? "|" : "-"));
        if (left instanceof KdTree) ((KdTree)left).display(depth+1);
        else System.out.println(prefx + " " + ((Pt)left).toString());
        if (right instanceof KdTree) ((KdTree)right).display(depth+1);
        else System.out.println(prefx + " " + ((Pt)right).toString());
    }

    /*  Split recursively splits lists of points (sorted by x and y) in
        alternating directions until it reaches singletons.  Even levels
        split by a vertical line (i.e., on either side of some x0), odd
        levels split by horizontal.  Arbitrarily, we call the points on
        which the split will take place the xs.
    */
    static Object split(int level, Cons xs, Cons ys) {
        if (xs.cdr() == null)   // last point
        return xs.car();        // return it

        // First, split in x direction
        Cons p1 = xs;
        Cons p2 = xs;
        while (p2.cdr() != null) {
            p2 = (Cons)p2.cdr();
            if (p2.cdr() == null) break;
            p2 = (Cons)p2.cdr();
            p1 = (Cons)p1.cdr();
        }
        // now, p1 is the Cons at middle of list
        Pt splitPt = (Pt)p1.car(); // the splitting point
        p2 = (Cons)p1.cdr();        // the second half of the list
        p1.setCdr(null);            // break link from end of first half
        p1 = xs;

        // Now separate the ys by which side of the splitPt they fall on.
        /* Original, simple implementation conses a lot.
        boolean vert = (level%2 == 0);
        Cons lows = null;
        Cons highs = null;
        for ( ; ys != null; ys = (Cons)ys.cdr()) {
            Pt p = (Pt)ys.car();
            if ((p.equals(splitPt))
                || (vert && p.PtLess(splitPt))
                || (!vert && p.PtLessY(splitPt)))
            lows = new Cons(p, lows);
            else highs = new Cons(p, highs);
        }
        lows = lows.nreverse();
        highs = highs.nreverse();
        */
        // Re-use the pieces of the ys list.
        boolean vert = (level%2 == 0);
        Cons lows = null;
        Cons highs = null;
        while (ys != null) {
            Cons current = ys;
            ys = (Cons)ys.cdr();
            Pt p = (Pt)current.car();
            if ((p.equals(splitPt)) || ((vert) ? p.PtLess(splitPt) : p.PtLessY(splitPt))) {
                current.setCdr(lows);
                lows = current;
            }
            else {
                current.setCdr(highs);
                highs = current;
            }
        }
        lows = lows.nreverse();
        highs = highs.nreverse();

        return new KdTree((vert) ? splitPt.ptx() : splitPt.pty(),
                          split(level+1, lows, p1),
                          split(level+1, highs, p2));
    }

    /*  findPts returns a Vector of Pts in the region, using the KdTree to
        make the operation efficient.  If a second argument answer is given,
        the answer is appended to that Vector.

        Because we represent a KdTree for a single point as a Pt, this code
        cannot findPts for singletons.  Therefore, we special-case it.  This
        will fail if the top level of the KdTree is itself a singleton Pt; to
        make that work, we would need to define a findPts method on Pt.
    */

    public Vector findPts(Region r) {
        return findPts(r, new Vector());
    }

    public Vector findPts(Region r, Vector answer) {
        search(r, answer, 0, new KdBounds());
        return answer;
    }

    void search(Region r, Vector answer, int d, KdBounds b) {
        boolean vert = (d%2 == 0);
        // Should cons less by re-using bounds rather than making new ones!
        KdBounds bottom = new KdBounds(b);
        KdBounds top    = new KdBounds(b);
        if (vert) {
            bottom.setKdxh(line);
            top.setKdxl(line);
        }
        else {
            bottom.setKdyh(line);
            top.setKdyl(line);
        }
        if (bottom.regionIn(r)) ReportSubtree(left, answer);
        else if (bottom.regionIntersects(r)) {
            if (left instanceof KdTree) ((KdTree)left).search(r, answer, d+1, bottom);
            else if (r.contains((Pt)left)) {
                answer.addElement(left);
                //System.out.println("Adding " + ((Pt)left).toString());
            }
        }

        if (top.regionIn(r)) ReportSubtree(right, answer);
        else if (top.regionIntersects(r)) {
            if (right instanceof KdTree) ((KdTree)right).search(r, answer, d+1, top);
            else if (r.contains((Pt)right)) {
                answer.addElement(right);
                //System.out.println("Adding " + ((Pt)right).toString());
            }
        }
    }

    // Following is static so we can pass node explicitly even if it is a Pt.
    static void ReportSubtree(Object node, Vector answer) {
        if (node instanceof KdTree) {
            ReportSubtree(((KdTree)node).left, answer);
            ReportSubtree(((KdTree)node).right, answer);
        }
        else {
            answer.addElement(node);
            //System.out.println("Adding (subtree) " + ((Pt)node).toString());
        }
    }

    // Factory:

    /*  Make takes a set of points, represented in some way,
        and returns a KdTree for those points.
        */
    public static Object make(Vector v) {
        Cons xs = (Cons.ListNonNull(v)).sort();
        Cons ys = (Cons.ListNonNull(v)).sort(new PtComparatorY());
        return split(0, xs, ys);
    }

    private class KdBounds {
        //  Uses Double rather than double, so it can be null, which
        //  corresponds to unbounded.
        Double xl;
        Double xh;
        Double yl;
        Double yh;

        KdBounds() {
            this(null, null, null, null);
        }

        KdBounds(KdBounds b) {
            xl = b.xl;
            xh = b.xh;
            yl = b.yl;
            yh = b.yh;
        }

        KdBounds(Double xli, Double xhi, Double yli, Double yhi) {
            xl = xli;
            xh = xhi;
            yl = yli;
            yh = yhi;
        }

        KdBounds(double xli, double xhi, double yli, double yhi) {
            this(new Double(xli), new Double(xhi), new Double(yli), new Double(yhi));
        }

        public String toString() {
            return "{"
                 + ((xl == null) ? "-inf" : xl.toString())
                 + ", "
                 + ((xh == null) ? "+inf" : xh.toString())
                 + "}x{"
                 + ((yl == null) ? "-inf" : yl.toString())
                 + ", "
                 + ((yh == null) ? "+inf" : yh.toString())
                 + "}";
        }

        void setKdBounds(Double xli, Double xhi, Double yli, Double yhi) {
            xl = xli;
            xh = xhi;
            yl = yli;
            yh = yhi;
        }

        void setKdxl(Double xli) {
            xl = xli;
        }
        void setKdxh(Double xhi) {
            xh = xhi;
        }
        void setKdyl(Double yli) {
            yl = yli;
        }
        void setKdyh(Double yhi) {
            yh = yhi;
        }

        void setKdxl(double xli) {
            xl = new Double(xli);
        }
        void setKdxh(double xhi) {
            xh = new Double(xhi);
        }
        void setKdyl(double yli) {
            yl = new Double(yli);
        }
        void setKdyh(double yhi) {
            yh = new Double(yhi);
        }

        /*  Determines if the KdBounds falls inside the region r.  Note that
            it any of the bounds are indeterminate, then the answer is false.
            */
        boolean regionIn(Region r) {
            boolean
            ans =  (xl != null && xh != null && yl != null & yh != null
                    && r.xl             <= xl.doubleValue()
                    && xl.doubleValue() <= xh.doubleValue()
                    && xh.doubleValue() <= r.xh
                    && r.yl             <= yl.doubleValue()
                    && yl.doubleValue() <= yh.doubleValue()
                    && yh.doubleValue() <= r.yh);
            //System.out.println(toString() + " inside " + r.toString() + "->" + ans);
            return ans;
        }

        /*  KdBounds fails to intersect r only if in any dimension one of its
            extremes is beyond the region.
        */
        boolean regionIntersects(Region r) {
            boolean
            ans =  !(   (xh != null && xh.doubleValue() < r.xl)
                     || (xl != null && xl.doubleValue() > r.xh)
                     || (yh != null && yh.doubleValue() < r.yl)
                     || (yl != null && yl.doubleValue() > r.yh));
            //System.out.println(toString() + " intersects " + r.toString() + "->" + ans);
            return ans;
        }
    }
}