import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    /**
     *  gridGraphVirtualTopBottom : will be used if perculation occurs without looping n bottom elements.
     *                              Be careful while using because bottom elements connected to each other.
     *                              Therefore can not be used for isFull check.
     *  gridGraphVirtualTop : will be used if grid (row, col) can be reached from top i.e. full. 
     */
    private final WeightedQuickUnionUF gridGraphVirtualTopBottom;
    private final WeightedQuickUnionUF gridGraphVirtualTop;
    private final int size;
    private final int gridNumber;
    private final int virtualTopGridIndex;
    private final int virtualBottomGridIndex;
    private boolean[][] gridState;
    private int openGridCount = 0;
    
    /** Constructs n by n percolation grid.
     * @param n :  width and height parameter for percolation.
     */
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Percolation grid size : " + n + " is <= 0");
        }

        size = n;
        gridNumber = size * size;
        gridGraphVirtualTopBottom = new WeightedQuickUnionUF(gridNumber + 2); // 0 to gridNumber+1
        gridGraphVirtualTop = new WeightedQuickUnionUF(gridNumber + 1); // 0 to gridNumber 
        virtualTopGridIndex = gridNumber;
        virtualBottomGridIndex = gridNumber + 1;
        gridState = new boolean[size][size];
    }
    
    /** opens grid in (row, col)
     * @param row : row of grid 1 to n
     * @param col : col of grid 1 to n
     * @throws IllegalArgumentException if input out of range
     */
    public void open(int row, int col) {
        validate(row, col);
        if (isOpen(row, col)) {
            return;
        }
        
        int inputIndex = convertRowColToIndex(row, col);

        // Top connection required
        if (row == 1) {
            gridGraphVirtualTop.union(inputIndex, virtualTopGridIndex);
            gridGraphVirtualTopBottom.union(inputIndex, virtualTopGridIndex);
            
        }
        
        // Bottom connection required
        if (row == size) {
            gridGraphVirtualTopBottom.union(inputIndex, virtualBottomGridIndex);
        }

        gridState[row - 1][col - 1] = true;
        openGridCount++;
        
        checkAndConnect(inputIndex, row - 1, col); // top
        checkAndConnect(inputIndex, row, col - 1); // left
        checkAndConnect(inputIndex, row + 1, col); // bottom
        checkAndConnect(inputIndex, row, col + 1); // right
    }
    
    /**
     * Connects grid represented by srcIndex with grid represented by (dstRow, dstCol)
     * @param srcIndex : index value of (srcRow, srcCol) pair which will union with (dstRow,dstCol) 
     * @param dstRow : row of destination 
     * @param dstCol : col of destination
     */
    private void checkAndConnect(int srcIndex, int dstRow, int dstCol) {
        if (isInGrid(dstRow, dstCol) && isOpen(dstRow, dstCol)) {
            final int dstIndex = convertRowColToIndex(dstRow, dstCol);
            gridGraphVirtualTop.union(srcIndex, dstIndex);
            gridGraphVirtualTopBottom.union(srcIndex, dstIndex);
        }
    }
    
    /**
     * Checks if grid (row, col) is open
     * @param row : row of grid 1 to n
     * @param col : col of grid 1 to n
     * @return
     * @throws IllegalArgumentException if input out of range
     */
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return gridState[row-1][col-1];
    }

    /**
     * Checks if grid (row, col) can be reached from top.
     * @param row : row of grid 1 to n.
     * @param col : col of grid 1 to n.
     * @return true if full false if not.
     * @throws IllegalArgumentException if input out of range.
     */
    public boolean isFull(int row, int col) {
        validate(row, col);
        return isOpen(row, col) &&
               (gridGraphVirtualTop.find(virtualTopGridIndex) == gridGraphVirtualTop.find(convertRowColToIndex(row, col)));
    }

    /**
     * @return numberOfOpenGrid
     */
    public int numberOfOpenSites() {
        return openGridCount;
    }

    /**
     * @return if the grid array percolates i.e. top connects to bottom.
     */
    public boolean percolates() {
        return gridGraphVirtualTopBottom.find(virtualTopGridIndex) == gridGraphVirtualTopBottom.find(virtualBottomGridIndex);
    }
    
    /**
     * converts row,col value to 1D index representation
     * @param row : row of grid 1 to n.
     * @param col : col of grid 1 to n.
     * @return 1D index representation.
     */
    private int convertRowColToIndex(int row, int col) {
        return (row - 1) * this.size + (col - 1);
    }
    
    /**
     * Validate if given (row, col) pair is a valid grid location
     * @param row : row of grid 1 to n.
     * @param col : col of grid 1 to n.
     * @throws IllegalArgumentException (row, col) is not in percolation grid.
     */
    private void validate(int row, int col) {
        if (!isInGrid(row, col)) {
            throw new IllegalArgumentException("Row : " + row + " Col : " + col + " is not in percolation(" + size + ")");
        }
    }
    
    /**
     * Checks if given (row, col) is in percolation grid.
     * @param row : row of grid 1 to n.
     * @param col : col of grid 1 to n.
     * @return true if it is in grid else false.
     */
    private boolean isInGrid(int row, int col) {
        return (row >= 1 && row <= size) && (col >= 1 && col <= size);
    }
    
    // test client (optional)
    public static void main(String[] args) {
        Percolation p = new Percolation(1);
        p.open(1, 1);
        System.out.println("Perculate : " + p.percolates());
        
        Percolation p2 = new Percolation(1);
        System.out.println("Perculate : " + p2.percolates());

    }
}