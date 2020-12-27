import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

public class PointSET {
    
    private final Set<Point2D> pointSet;
    
   // construct an empty set of points 
    public PointSET() {
        pointSet = new TreeSet<Point2D>();
    }
   
   // is the set empty?
    public boolean isEmpty() {
        return pointSet.isEmpty();
    }
   
    // number of points in the set
    public int size() {
        return pointSet.size();
    }
   
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        validateNull(p);
        pointSet.add(p);
    }
   
    // does the set contain point p? 
    public boolean contains(Point2D p) {
        validateNull(p);
        return pointSet.contains(p);
    }
   
   // draw all points to standard draw 
    public void draw() {
        for (Point2D point : pointSet) {
            point.draw();
        }
    }
   
   // all points that are inside the rectangle (or on the boundary) 
   public Iterable<Point2D> range(RectHV rect) {
       validateNull(rect);
       List<Point2D> insideList = new LinkedList<Point2D>();
       for (Point2D point : pointSet) {
           if (rect.contains(point)) {
               insideList.add(point);
           }
       }
       return insideList;
   }
   
   // a nearest neighbor in the set to point p; null if the set is empty
   public Point2D nearest(Point2D p) {
       validateNull(p);
       if (pointSet == null) { return null; }
       
       Point2D closesPt = null;
       for (Point2D pt : pointSet) {
           if (closesPt == null) {
               closesPt = pt;
           }
           else {
               double currentDist = pt.distanceSquaredTo(p);
               if (currentDist < closesPt.distanceSquaredTo(p)) {
                   closesPt = pt;
               }
           }
       }
       return closesPt;
   }
   
   private void validateNull(Object p) {
       if (p == null) { throw new IllegalArgumentException("Null input is not allowed"); }
   }

   // unit testing of the methods (optional)
   public static void main(String[] args) {
       StdOut.println("Unit Test");
   }
}