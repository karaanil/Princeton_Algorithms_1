import java.util.Comparator;
import java.util.LinkedList;
import java.util.function.ToIntFunction;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    
    private boolean solvable = true;
    private LinkedList<Board> solution = new LinkedList<Board>();
    
    private static class SolverNode {
        private static ToIntFunction<SolverNode> heuristicFunction;
        private final Board board;
        private final SolverNode parent;
        private final int move;
        private int heuristic = -1;
        
        private SolverNode(Board board, SolverNode parent) {
            this.board = board;
            this.parent = parent;
            this.move = parent == null ? 0 : (parent.move + 1);
            this.heuristic = heuristicFunction.applyAsInt(this);
        }
    }
    
    private class HeuristicOrder implements Comparator<SolverNode> {
        @Override
        public int compare(SolverNode n1, SolverNode n2) {
            // If we force Node to store heuristic then we no longer need to calculate it here
            int heuristicN1 = n1.heuristic;
            int heuristicN2 = n2.heuristic;
            int costN1 = n1.move + heuristicN1;
            int costN2 = n2.move + heuristicN2;
            
            if (costN1 > costN2)            { return  1; }
            if (costN1 < costN2)            { return -1; }
            if (heuristicN1 > heuristicN2)  { return  1; }
            if (heuristicN1 < heuristicN2)  { return -1; }

            return 0;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) { throw new IllegalArgumentException("Null argument is not allowed"); }
        
        SolverNode result = solve(initial);
        while (result != null) {
            solution.addFirst(result.board);
            result = result.parent;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
        
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return solvable ? solution.size() - 1 : -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solvable ? solution : null;
    }
    
    private SolverNode solve(Board initial) {

        // ToIntFunction in lambda form. 
        SolverNode.heuristicFunction = (SolverNode value) -> { return value.board.manhattan(); };
        
        MinPQ<SolverNode> solverPQ = new MinPQ<SolverNode>(new HeuristicOrder());
        MinPQ<SolverNode> twinPQ = new MinPQ<SolverNode>(new HeuristicOrder());
        solverPQ.insert(new SolverNode(initial, null));
        twinPQ.insert(new SolverNode(initial.twin(), null));
        
        MinPQ<SolverNode> activePQ = solverPQ;
        while (!activePQ.isEmpty()) {
            SolverNode currentNode = activePQ.delMin();
            
            if (currentNode.board.isGoal()) {
                solvable = activePQ == solverPQ;
                return currentNode;
            }
            
            for (Board neighbor : currentNode.board.neighbors()) {
                if (currentNode.parent == null ||
                    !neighbor.equals(currentNode.parent.board)) {
                    activePQ.insert(new SolverNode(neighbor, currentNode));
                }
            }
            activePQ = (activePQ == solverPQ) ? twinPQ : solverPQ;
        }
        solvable = false;
        return null;
    }

    // test client (see below) 
    public static void main(String[] args) {
        if (args.length == 0) {
            args = new String[] { "testInputs/puzzle20.txt" };
        }
        Board initial = readBoard(args);

        Solver solver = new Solver(initial);
        
        if (solver.isSolvable()) {
            StdOut.println("Minimum number of moves : " + solver.moves());
//            StdOut.println("Is Solvable : " + solver.isSolvable());

            for (Board eachMove : solver.solution()) {
                StdOut.println(eachMove.toString());
            }
        }
    }
    
    // Replicate of PuzzleTester function should stay,
    // because PuzzleTester is not submission file i.e. we cant use it.
    private static Board readBoard(String[] args) {
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