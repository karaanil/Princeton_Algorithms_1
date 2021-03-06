import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

// This is memory inefficent implementation
// { double from, double to } can be hold instead of RectHV region
// For the sake of simplicity it is implemented with RectHV, which can pass memory tests.
// Grade : 100/100
public class KdTree {
    
    private class Node implements Comparable<Point2D> {
        private final Point2D pt;
        private Node left;
        private Node right;
        private final boolean isVertical;
        private RectHV region;
        
        private Node(Point2D pt, Node left, Node right, boolean isVertical, RectHV region) {
            this.pt = pt;
            this.left = left;
            this.right = right;
            this.isVertical = isVertical;
            this.region = region;
        }
        
        // o is smaller than current point or not?
        @Override
        public int compareTo(Point2D o) {
            return compareTo(o.x(), o.y());
        }
        
        private int compareTo(double x, double y) {
            return isVertical ? compareTo(pt.x(), x, pt.y(), y) : compareTo(pt.y(), y, pt.x(), x);
        }
        
        private int compareTo(double ptPrior, double inPrior, double ptTieBreak, double inTieBreak) {
            if (inPrior < ptPrior)          return -1;
            if (inPrior > ptPrior)          return 1;
            if (inTieBreak < ptTieBreak)    return -1;
            if (inTieBreak > ptTieBreak)    return 1;
            return 0;
        }
        
        public double minimumDistanceSquaredTo(Point2D that) {
            return region.distanceSquaredTo(that);
        }

        public void draw() {
            StdDraw.setPenColor(Color.BLACK);
            StdDraw.setPenRadius(0.03);
            pt.draw();
            StdDraw.setPenRadius();
            if (isVertical) {
                StdDraw.setPenColor(Color.RED);
                StdDraw.line(pt.x(), region.ymin(), pt.x(), region.ymax());
            }
            else { 
                StdDraw.setPenColor(Color.BLUE);
                StdDraw.line(region.xmin(), pt.y(), region.xmax(), pt.y());
            }
            
        }
    }
    
    private static final RectHV INPUT_RANGE = new RectHV(0, 0, 1, 1);
    private Node root;
    private int nodeCount;

    private Point2D nearestSolution = null;
    private double nearestDistanceSquared = Double.POSITIVE_INFINITY;
    
   // construct an empty set of points 
    public KdTree() {
        nodeCount = 0;
    }
   
   // is the set empty?
    public boolean isEmpty() {
        return (root == null);
    }
   
    // number of points in the set
    public int size() {
        return nodeCount;
    }
   
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        validateNull(p);
        validateRange(p);
        root = insert(root, p, true, INPUT_RANGE.xmin(), INPUT_RANGE.ymin(), INPUT_RANGE.xmax(), INPUT_RANGE.ymax());
    }
   
    private Node insert(Node node, Point2D p, boolean isVertical, double minX, double minY, double maxX, double maxY) {
        if (node == null) { 
            nodeCount++;
            return new Node(new Point2D(p.x(), p.y()), null, null, isVertical, new RectHV(minX, minY, maxX, maxY)); 
        }
        
        if (node.isVertical)    { insertVertical(node, p, isVertical, minX, minY, maxX, maxY); }
        else                    { insertHorizontal(node, p, isVertical, minX, minY, maxX, maxY); }
        
        return node;
    }

    private void insertVertical(Node node, Point2D p, boolean isVertical, double minX, double minY,
                                                                          double maxX, double maxY) {
        int compared = node.compareTo(p);
        if (compared < 0)       { node.left = insert(node.left, p, !isVertical, minX, minY, node.pt.x(), maxY); }
        else if (compared > 0)  { node.right = insert(node.right, p, !isVertical, node.pt.x(), minY, maxX, maxY); }
        else                    { return; }
    }

    private void insertHorizontal(Node node, Point2D p, boolean isVertical, double minX, double minY,
                                                                            double maxX, double maxY) {
        int compared = node.compareTo(p);
        if (compared < 0)       { node.left = insert(node.left, p, !isVertical, minX, minY, maxX, node.pt.y()); }
        else if (compared > 0)  { node.right = insert(node.right, p, !isVertical, minX, node.pt.y(), maxX, maxY); }
        else                    { return; }
    }
    
    // does the set contain point p? 
    public boolean contains(Point2D p) {
        validateNull(p);
        if (!INPUT_RANGE.contains(p)) { return false; }
        return get(root, p) != null;
    }
    
    private Node get(Node node, Point2D p) {
        if (node == null) { return null; }

        int compared = node.compareTo(p);
        if (compared < 0)       { return get(node.left, p); }
        else if (compared > 0)  { return get(node.right, p); }
        else                    { return node; } 
    }
   
    // draw all points to standard draw 
    public void draw() {
        draw(root);
    }

    private void draw(Node node) {
        if (node == null) { return; }
        node.draw();
        draw(node.left);
        draw(node.right);
    }

    // all points that are inside the rectangle (or on the boundary) 
    public Iterable<Point2D> range(RectHV rect) {
        validateNull(rect);
        List<Point2D> insideList = new LinkedList<Point2D>();
        range(root, rect, insideList);
        return insideList;
    }

    private void range(Node node, RectHV rect, List<Point2D> insideList) {
        if (node == null) { return; }
        
        if (rect.contains(node.pt)) {
            insideList.add(node.pt); // Equality case of compMin compMax handled already!
        }
        
        if (node.left != null) {
            if (rect.intersects(node.left.region)) {
                range(node.left, rect, insideList);
            }
        }
        
        if (node.right != null) {
            if (rect.intersects(node.right.region)) {
                range(node.right, rect, insideList);
            }
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        validateNull(p);
        if (isEmpty()) { return null; }
        nearestSolution = null;
        nearestDistanceSquared = Double.POSITIVE_INFINITY;
        nearest(root, p);
        return nearestSolution;
    }
    
    private void nearest(Node node, Point2D p) {
        if (node == null) { return; }
        
        double candidateDist = node.pt.distanceSquaredTo(p);
        if (candidateDist <= nearestDistanceSquared) {
            nearestSolution = node.pt;
            nearestDistanceSquared = candidateDist;
        }
        
        int compared = node.compareTo(p);
        if (compared < 0) { 
            nearest(node.left, p);
            if (node.right != null &&
                node.right.minimumDistanceSquaredTo(p) <= nearestDistanceSquared) {
                nearest(node.right, p);
            }
        }
        else if (compared > 0) {
            nearest(node.right, p);
            if (node.left != null &&
                node.left.minimumDistanceSquaredTo(p) <= nearestDistanceSquared) {
                nearest(node.left, p);
            }
        }
        else { 
            nearestDistanceSquared = -0.0;
            nearestSolution = node.pt;
        }
        return;
    }

    private void validateNull(Object p) {
        if (p == null) { throw new IllegalArgumentException("Null input is not allowed"); }
    }
    
    private void validateRange(Point2D p) {
        if (!INPUT_RANGE.contains(p)) {
            throw new IllegalArgumentException("Input should be in x[0,1] y[0,1]; Input is" + p.toString());
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
           StdOut.println("Unit test");
    }
}