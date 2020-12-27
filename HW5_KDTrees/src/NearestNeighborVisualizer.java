/******************************************************************************
 *  Compilation:  javac NearestNeighborVisualizer.java
 *  Execution:    java NearestNeighborVisualizer input.txt
 *  Dependencies: PointSET.java KdTree.java
 *
 *  Read points from a file (specified as a command-line argument) and
 *  draw to standard draw. Highlight the closest point to the mouse.
 *
 *  The nearest neighbor according to the brute-force algorithm is drawn
 *  in red; the nearest neighbor using the kd-tree algorithm is drawn in blue.
 *
 ******************************************************************************/

import java.awt.Color;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class NearestNeighborVisualizer {

    public static void main(String[] args) {

        // initialize the two data structures with point from file
        if (args.length == 0) {
            args = new String[] { "testInputs/aTraversalFailCase.txt" };
        }

        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            brute.insert(p);
        }

        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        kdtree.draw();
        
        // process nearest neighbor queries
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenRadius(0.01);
        while (true) {

            // the location (x, y) of the mouse
            if (StdDraw.isMousePressed()) {
                
                double x = 0.4375; 
                double y = 0.5625;
                Point2D query = new Point2D(x, y);
                
                // draw all of the points
                StdDraw.clear();
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setPenRadius(0.01);
                kdtree.draw();
                
                StdDraw.setPenColor(Color.MAGENTA);
                StdDraw.setPenRadius(0.03);
                query.draw();
    
                // draw in red the nearest neighbor (using brute-force algorithm)
                StdDraw.setPenRadius(0.03);
                StdDraw.setPenColor(StdDraw.RED);
                Point2D resBrute = brute.nearest(query);
                resBrute.draw();
                StdDraw.setPenRadius(0.02);
    
                // draw in blue the nearest neighbor (using kd-tree algorithm)
                StdDraw.setPenColor(StdDraw.BLUE);
                Point2D resKD = kdtree.nearest(query);
                resKD.draw();
                
                if (0 != resKD.compareTo(resKD)) {
                    StdOut.println("UNEXPECTED CASE");
                }
                
                StdDraw.show();
                StdDraw.pause(40);
            }
        }
    }
}
