import edu.princeton.cs.algs4.StdDraw;

public class ColinearPointsVisualizer {
    
    private static final int DELAY = 500;
    
    public static void visualize(Point[] points, LineSegment[] lineSegments, boolean doAnimate) {

        initializeCanvas(points);
        
        // print and draw the line segments
        for (LineSegment segment : lineSegments) {
            segment.draw();
            if (doAnimate) {
                StdDraw.show();
                StdDraw.pause(DELAY);
            }
        }
        StdDraw.show();
        StdDraw.pause(2*DELAY);
        StdDraw.clear();
    }
    
    public static void initializeCanvas(Point[] points) {
        // draw the points
        StdDraw.enableDoubleBuffering();
        
        final int maxPosition = 32768;
        final int borderOffset = maxPosition/5;
        
        StdDraw.setXscale(0 - borderOffset, maxPosition + borderOffset);
        StdDraw.setYscale(0 - borderOffset, maxPosition + borderOffset);
        
        StdDraw.setPenRadius(0.01);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        StdDraw.setPenRadius();
    }

}
