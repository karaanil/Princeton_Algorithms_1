import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private static final int MIN_COLLINEAR_PTS = 4;
    private LineSegment[] lineSegments = new LineSegment[0];
    
    // will not supply any input to BruteCollinearPoints that has 5 or more collinear points.
    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        validateInputIsNull(points);
        
        Point[] inPoints = points.clone();
        validateRepatingElements(inPoints);

        if (points.length < MIN_COLLINEAR_PTS) {
            return;
        }
        
        calculateColinearLineSegments(inPoints);
    }
    
    // the line segments
    public LineSegment[] segments() {
        return Arrays.copyOf(lineSegments, lineSegments.length);
    }
    
    // the number of line segments
    public int numberOfSegments() {
        return lineSegments == null ? 0 : lineSegments.length;
    }
    
    // Validates if point[] or any included point is null 
    private void validateInputIsNull(Point[] input) {
        if (input == null) {
            throw new IllegalArgumentException("Points[] array is null");
        }
        for (Point point : input) {
            if (point == null) {
                throw new IllegalArgumentException("Points[] array have null element inside");
            }
        }
    }
    
    // Validates sorted array for repeating elements
    private void validateRepatingElements(Point[] input) {
        Arrays.sort(input);
        for (int i = 1; i < input.length; i++) {
            if (input[i-1].compareTo(input[i]) == 0) {
                throw new IllegalArgumentException("Points[] array have equal elements inside");
            }
        }
    }
    
    private void calculateColinearLineSegments(Point[] points) {
        List<LineSegment> lineSegmentsList = new LinkedList<>();
        final int pointsLength = points.length;
        for (int p = 0; p < pointsLength; p++) {
            for (int q = p + 1; q < pointsLength; q++) {
                for (int r = q + 1; r < pointsLength; r++) {
                    for (int s = r + 1; s < pointsLength; s++) {
                        if (points[p].slopeTo(points[s]) == points[p].slopeTo(points[q]) &&
                            points[p].slopeTo(points[s]) == points[p].slopeTo(points[r])) {
                            
                            // Since already sorted by y the further elements will be the edges.
                            lineSegmentsList.add(new LineSegment(points[p], points[s]));
                        }
                    }
                }
            }
        }
        
        this.lineSegments = lineSegmentsList.toArray(new LineSegment[lineSegmentsList.size()]);
    }
    
//-----------------------------End Of BruteCollinearPoints Implementation------------------------------------
    
    // Below code segments normally should be seperated as unit test!
    // unit testing (required)
    public static void main(String[] args) {
        if (args.length == 0) {
            args = new String[]{"testInputs/input10.txt"};
        }
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        
        BruteCollinearPoints bruteCollinear = new BruteCollinearPoints(points);
        LineSegment[] lineSegments = bruteCollinear.segments();
        for (LineSegment segment : lineSegments) {
            StdOut.println(segment);
        }
        
        // CommentOut while submitting!
//        ColinearPointsVisualizer.visualize(points, lineSegments, true);
    }

}