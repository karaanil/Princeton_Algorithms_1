import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {

    // If you are using eclipse hit CTRL-Z to send EOF to console so that it prints result!
    public static void main(String[] args) {
        
        RandomizedQueue<String> randQueue = new RandomizedQueue<String>();        
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            randQueue.enqueue(item);
        }
        final int numberOfRandomizedDequeue = Integer.parseInt(args[0]);
        for (int i = 0; i < numberOfRandomizedDequeue; i++) {
            StdOut.println(randQueue.dequeue());
        }
    }

}
