import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Board {
    private static String stringFormat;
    private static int dimension;
    
    private final int[] squares; // 1D is a bit efficent than storing 2d array, which is array of arrays
    private int zeroIndex = -1;
    private int hamming = -1;
    private int manhattan = -1;
    
    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    // Called Once!
    public Board(int[][] tiles) {
        validateInput(tiles);
        this.squares = convert2DInto1D(tiles);
        dimension = tiles.length;
        stringFormat = "%" + (int) (Math.log10(squares.length - 1) + 2) + "d";
        
        calculateHamming();
        calculateManhattan();
        calculateZeroIndex();
    }
    
    private Board(int[] tiles, int hamming, int manhattan, int zeroIndex) {
        this.squares = tiles;
        this.hamming = hamming;
        this.manhattan = manhattan;
        this.zeroIndex = zeroIndex;
    }
    
    public String toString() {
        StringBuilder boardString = new StringBuilder();
        boardString.append(dimension());
        for (int i = 0; i < squares.length; i++) {
            if (i % dimension == 0) {
                boardString.append("\n");
            }
            boardString.append(String.format(stringFormat, squares[i]));
        }
        return boardString.toString();
    }

    // board dimension n
    public int dimension() {
        return dimension;
    }
    
    // number of tiles out of place 
    public int hamming() {
        return hamming;
        
    }
    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < squares.length - 1; i++) {
            if (squares[i] != i + 1) {
                return false;
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        // Normally we should use super.equals at first but course forbids to use
        if (y == this) { return true; }
        if (y == null) { return false; }
        if (this.getClass() != y.getClass()) { return false; } 
        
        final Board input = (Board) y;
        if (input.squares.length != this.squares.length) { return false; }
        
        for (int i = 0; i < squares.length; i++) {
            if (input.squares[i] != this.squares[i]) {
                return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> neighbors = new LinkedList<Board>();
        
        createChildBoardTop(neighbors);
        createChildBoardBottom(neighbors);
        createChildBoardLeft(neighbors);
        createChildBoardRight(neighbors);
        
        return neighbors;
    }
    
    private static void validateInput(int[][] tiles) {
        if (tiles == null) { throw new NullPointerException("Board tiles is null"); }
        if (tiles.length != tiles[0].length) { throw new IllegalArgumentException("Board tiles should be N by N"); }
    }
    
    private static int[] convert2DInto1D(int[][] tiles) {
        final int dim = tiles.length; 
        int[] result = new int[dim * dim];
        for (int i = 0, id = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++, id++) {
                result[id] = tiles[i][j];
            }
        }
        return result;
    }
    
    private void calculateHamming() {
        hamming = 0;
        for (int i = 0; i < squares.length; i++) {
            if (checkHamming(i, squares[i])) {
                hamming++;
            }
        }
        
    }
    
    private void calculateManhattan() {
        manhattan = 0;
        for (int i = 0; i < squares.length; i++) {
            manhattan += calculateManhattan(i, squares[i]);
        }
    }
    
    private boolean checkHamming(int index, int value) {
        if (value != 0 && value != index + 1) {
            return true;
        }
        return false;
    }
    
    private int calculateManhattan(int index, int value) {
        if (checkHamming(index, value)) {
            final int manhattanDist =   (Math.abs(index / dimension - (value - 1) / dimension) +
                                        Math.abs(index % dimension - (value - 1) % dimension));
            return manhattanDist;
        }
        return 0;
    }
    
    private void calculateZeroIndex() {
        for (int i = 0; i < squares.length; i++) {
            if (squares[i] == 0) {
                zeroIndex = i;
                return;
            }
        }
    }
    
    private void createChildBoardTop(List<Board> neighbours) {
        final int topIndex = zeroIndex - dimension;
        if (topIndex < 0) { return; }
        neighbours.add(createBoardBySwappingElements(topIndex, zeroIndex));
    }
    
    private void createChildBoardBottom(List<Board> neighbours) {
        final int bottomIndex = zeroIndex + dimension;
        if (bottomIndex >= squares.length) { return; }
        neighbours.add(createBoardBySwappingElements(bottomIndex, zeroIndex));
    }
    
    private void createChildBoardLeft(List<Board> neighbours) {
        final int leftIndex = zeroIndex - 1;
        if (leftIndex < 0 ||
            leftIndex / dimension != zeroIndex / dimension) { return; }
        neighbours.add(createBoardBySwappingElements(leftIndex, zeroIndex));
    }
    
    private void createChildBoardRight(List<Board> neighbours) {
        final int rightIndex = zeroIndex + 1;
        if (rightIndex >= squares.length ||
            rightIndex / dimension != zeroIndex / dimension) { return; }
        neighbours.add(createBoardBySwappingElements(rightIndex, zeroIndex));
    }
    
    // swap firstIndex and secondIndex elements 
    private Board createBoardBySwappingElements(int firstIndex, int secondIndex) {
        // Delta Manhattan
        int currentManhattan =  calculateManhattan(firstIndex, squares[firstIndex]) +
                                calculateManhattan(secondIndex, squares[secondIndex]);
        
        int swappedManhattan =  calculateManhattan(firstIndex, squares[secondIndex]) +
                                calculateManhattan(secondIndex, squares[firstIndex]);
        
        int deltaManhattan = swappedManhattan - currentManhattan;
        
        // Delta Hamming
        int currentHamming =    (checkHamming(firstIndex, squares[firstIndex]) ? 1 : 0) +
                                (checkHamming(secondIndex, squares[secondIndex]) ? 1 : 0);
        
        int swappedHamming =    (checkHamming(firstIndex, squares[secondIndex]) ? 1 : 0) +
                                (checkHamming(secondIndex, squares[firstIndex]) ? 1 : 0);
        
        int deltaHamming = swappedHamming - currentHamming;
        
        int newZeroIndex = zeroIndex;
        if (firstIndex == zeroIndex) { newZeroIndex = secondIndex; } // Because secondIndex exchanged with firstIndex!
        if (secondIndex == zeroIndex) { newZeroIndex = firstIndex; } // Because firstIndex exchanged with secondIndex!
        
        int[] currentSquares = Arrays.copyOf(squares, squares.length);
        int temp = currentSquares[firstIndex];
        currentSquares[firstIndex] = currentSquares[secondIndex];
        currentSquares[secondIndex] = temp;
        
        return new Board(currentSquares, hamming + deltaHamming, manhattan + deltaManhattan, newZeroIndex);
    }

    // a board that is obtained by exchanging any pair of tiles (0 is not a tile)
    public Board twin() {
        int[] swapItems = new int[2];
        for (int i = 0, j = 0; i < squares.length; i++) {
            if (squares[i] != 0) {
                swapItems[j++] = i;
                if (j == 2) {
                    break;
                }
            }
        }
        return createBoardBySwappingElements(swapItems[0], swapItems[1]);
    }
    
    // unit testing (not graded)
    
    public static void main(String[] args) {
        // Comment out before submitting!
        
//        if (args.length == 0) {
//            args = new String[] { "testInputs/puzzle00.txt" };
//        }
//        Board initial = PuzzleTester.readBoard(args);
//        printBoardStatus(initial);
//        Board twin = initial.twin();
//        printBoardStatus(twin);
//        for (Board child : initial.neighbors()) {
//            printBoardStatus(child);
//        }        
    }
//
//    private static void printBoardStatus(Board board) {
//        StdOut.println(board.toString());
//        StdOut.println("Zero :" + board.zeroIndex + " Ham:" + board.hamming +
//                       " Man:" + board.manhattan + " Goal:" + board.isGoal());
//    }

}
