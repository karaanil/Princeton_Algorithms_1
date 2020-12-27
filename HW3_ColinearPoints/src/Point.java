/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *  
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import java.util.Arrays;
import java.util.Comparator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class Point implements Comparable<Point> {
    
    private final int x;
    private final int y;

    // Integer overflow ignored!
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw() {
        StdDraw.point(x, y);
    }

    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param  that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        int yDiff = that.y - this.y;
        int xDiff = that.x - this.x;
        
        if (yDiff == 0 && xDiff == 0)   {
            return Double.NEGATIVE_INFINITY; 
        }
        if (xDiff == 0) {
            return Double.POSITIVE_INFINITY;
        }
        if (yDiff == 0) {
            return +0.0;
        }
        return (double) yDiff / xDiff;
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param  that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     *         point (x0 = x1 and y0 = y1);
     *         a negative integer if this point is less than the argument
     *         point; and a positive integer if this point is greater than the
     *         argument point
     */
    @Override
    public int compareTo(Point that) {
        if (this.y == that.y) {
            return this.x - that.x;
        }

        return this.y - that.y;
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
//        return new SlopeOrder();
        return new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                double slopeO1 = Point.this.slopeTo(o1);
                double slopeO2 = Point.this.slopeTo(o2);
                
                if (slopeO1 < slopeO2) {
                    return -1;
                }
                if (slopeO1 > slopeO2) {
                    return 1;
                }
                
                return 0;
            }
        };
    }
    
    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        /* YOUR CODE HERE */
        if (args.length == 0) {
            args = new String[]{"testInputs/grid5x5.txt"};
        }
        
        In in = new In(args[0]);
        
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        StdOut.println("SlopeOrder WRT : " + points[12]);
        Point slopeElem = points[12];
        Arrays.sort(points, slopeElem.slopeOrder());
        for (Point pt : points) {
            StdOut.println(pt + " :: " + String.format("%.20f", slopeElem.slopeTo(pt)));
        }
    }
}
