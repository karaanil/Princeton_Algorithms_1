import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;

public class SimpleElementsDrawTest {

    public static void main(String[] args) {
        point2DDraw(args);
    }

    private static void point2DDraw(String[] args) {
        if (args.length == 0) {
            args = new String[] { "testInputs/circle10.txt" };
        }
        
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenRadius(0.01);
        
        String filename = args[0];
        In in = new In(filename);
        
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
//            kdtree.insert(p);
            p.draw();
            StdDraw.show();
        }
    }
}
