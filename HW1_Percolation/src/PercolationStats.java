import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONF_CONST = 1.96;
    private final double[] results;
    
    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Required n > 0 && trials > 0 but input n : "+ n + " trials :" + trials);
        }
        
        final int gridNumber = n * n;
        
        results = new double[trials];
        
        for (int i = 0; i < trials; i++) {
            Percolation experiment = new Percolation(n);
            while (!experiment.percolates()) {
                int rowRand = StdRandom.uniform(1, n + 1);
                int colRand = StdRandom.uniform(1, n + 1);
                experiment.open(rowRand, colRand);
            }
            
            results[i] = (double) experiment.numberOfOpenSites() / gridNumber;
        }
        
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(results);
        
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(results);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - CONF_CONST * stddev() / Math.sqrt(results.length);
        
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + CONF_CONST * stddev() / Math.sqrt(results.length);
        
    }

   // test client (see below)
   public static void main(String[] args) {
       int n;
       int trials;
       n = Integer.parseInt(args[0]);
       trials = Integer.parseInt(args[1]);

       PercolationStats stats = new PercolationStats(n, trials);

       StdOut.println("mean                    = " + stats.mean());
       StdOut.println("stddev                  = " + stats.stddev());
       StdOut.println("95% confidence interval = [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
   }

}