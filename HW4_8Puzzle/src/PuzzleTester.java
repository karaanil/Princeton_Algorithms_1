import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class PuzzleTester {

    public static void main(String[] args) {


    }
    
    
    public static Board readBoard(String[] args) {
        if (args.length == 0) {
            args = new String[] { "testInputs/puzzle06.txt" };
        }
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        
        return initial;
    }
}
