import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private static final int MIN_COLLINEAR_PTS = 4;
    private LineSegment[] lineSegments = new LineSegment[0];
    
    
    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        validateInputIsNull(points);
        
        Point[] inPoints = points.clone();
        validateRepatingElements(inPoints);
        
        if (points.length < MIN_COLLINEAR_PTS) {
            return;
        }
        
        calculateColinearLineSegments(inPoints);
    }
    // the number of line segments
    public int numberOfSegments() {
        return lineSegments == null ? 0 : lineSegments.length;
    }
    
    // the line segments
    public LineSegment[] segments() {
        return Arrays.copyOf(lineSegments, lineSegments.length);
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
    
    private void calculateColinearLineSegments(Point[] inPoints) {
        List<LineSegment> lineSegmentsList = new LinkedList<>();
        Point[] copyPoints = inPoints.clone();

        for (int i = 0; i < inPoints.length; i++) {
            Arrays.sort(copyPoints, inPoints[i].slopeOrder());            
            findCollinearSegments(copyPoints, lineSegmentsList);
        }
        
        lineSegments = lineSegmentsList.toArray(new LineSegment[lineSegmentsList.size()]);
    }
    
    private void findCollinearSegments(Point[] slopeSortedPoints, List<LineSegment> lineSegmentsList) {
        Point initialPoint = slopeSortedPoints[0];
        int segStart = 1;
        int segEnd = 1;
        double previousSlope = initialPoint.slopeTo(slopeSortedPoints[segEnd]);
        
        while (segEnd < slopeSortedPoints.length) {
            double currentSlope = initialPoint.slopeTo(slopeSortedPoints[segEnd]);
            if (previousSlope == currentSlope) {
                segEnd++;
            }
            else {
                handleCurrentSegment(initialPoint, slopeSortedPoints, segStart, segEnd, lineSegmentsList);
                segStart = segEnd;
                segEnd++;
            }
            previousSlope = currentSlope;
        }
        handleCurrentSegment(initialPoint, slopeSortedPoints, segStart, segEnd, lineSegmentsList);

    }

    private void handleCurrentSegment(Point initialPoint, Point[] slopeSortedPoints, int segStart, int segEnd,
            List<LineSegment> lineSegmentsListCandidates) {
        if (segEnd - segStart >= MIN_COLLINEAR_PTS - 1) {
            Arrays.sort(slopeSortedPoints, segStart, segEnd);
            if (isInitialPointSmallestY(initialPoint, slopeSortedPoints[segStart])) {
                lineSegmentsListCandidates.add(new LineSegment(initialPoint, slopeSortedPoints[segEnd - 1]));
            }
        }
    }
    
    private boolean isInitialPointSmallestY(Point initialPoint, Point smallestY) {
        if (initialPoint.compareTo(smallestY) < 0) {
            return true;
        }
        return false;
    }
    
//-----------------------------End Of FastCollinearPoints Implementation------------------------------------
    
    // Below code segments normally should be seperated as unit test!
    // unit testing (required)
    public static void main(String[] args) {
        if (args.length == 0) {
            args = new String[]{"testInputs/grid4x4.txt"};
        }
        
        In in = new In(args[0]);
        
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
                
        FastCollinearPoints fastCollinear = new FastCollinearPoints(points);
        LineSegment[] lineSegments = fastCollinear.segments();
        for (LineSegment segment : lineSegments) {
            StdOut.println(segment);
        }
        
        // CommentOut while submitting!
        ColinearPointsVisualizer.visualize(points, lineSegments, true);
    }
}