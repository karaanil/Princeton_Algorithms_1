import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;


/**
 * Randomized queue enqueue items one by one and when we deque it simply gives a random element and removes it
 * To preserve order we normally shift removed item to end by one.
 * However, the randomized queue by nature can only dequeue a random item therefore putting size-1'th item into the queue is ok
 * 
 * @author anilk
 *
 * @param <Item>
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
    
    private class RandomizedQueueIterator implements Iterator<Item> {
        
        // Values copied into iterator to make iterator to work even dequeue called
        // from RandomizedQueue object during the iteraton.
        
        private int currentIndex = 0;
        private final Item[] iteratorList;
        
        public RandomizedQueueIterator() {
            iteratorList = (Item[]) new Object[size];
            System.arraycopy(queueElements, 0, iteratorList, 0, size);
            StdRandom.shuffle(iteratorList);
        }
        
        @Override
        public boolean hasNext() {
            return currentIndex < iteratorList.length;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("RandomizedQueueIterator next element does not exists");
            }
            return iteratorList[currentIndex++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("RandomizedQueueIterator does not support remove operation");
        }
        
        
    }
    
    private Item[] queueElements;
    private int size = 0;
    
    // construct an empty randomized queue
    public RandomizedQueue() {
        queueElements = (Item[]) new Object[2];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        assertIfIllegalArgument(item);
        resizeUpCheck();
        queueElements[size] = item;
        size++;
    }
    
    private void resizeUpCheck() {
        if (isFull()) {
            resize(2 * size());
        }
    }

    // remove and return a random item
    public Item dequeue() {
        assertIfEmpty();
        int randomIndex = StdRandom.uniform(size);
        
        Item result = queueElements[randomIndex];
        queueElements[randomIndex] = queueElements[size - 1];
        queueElements[size - 1] = null;
        size--;
        resizeDownCheck();
        return result;
    }
    
    private void resizeDownCheck() {
        if (!isEmpty() && size <= queueElements.length / 4) {
            resize(queueElements.length / 2);
        }
    }

    // return a random item (but do not remove it)
    public Item sample() {
        assertIfEmpty();
        int randomIndex = StdRandom.uniform(size);
        
        return queueElements[randomIndex];
    }

    // return an independent iterator over items in random order
    @Override
    public Iterator<Item> iterator() {
        return (new RandomizedQueueIterator());
    }
    
    private void assertIfIllegalArgument(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
    }
    
    private void assertIfEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException("Randomized queue is empty");
        }
    }
    
    private boolean isFull() {
        return (size == queueElements.length);
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        System.arraycopy(queueElements, 0, copy, 0, size);
        queueElements = copy;
    }
    
//-----------------------------End Of RandomizedQueue Implementation------------------------------------

    // Below code segments normally should be seperated as unit test!
    // unit testing (required)
    public static void main(String[] args) {
        In in = new In(new Scanner(standartInputTXT()));
        StdOut.println("Starts");
        while (!in.isEmpty()) {
            String key = in.readString();
            TestCommands testCommand = TestCommands.valueOf(key);
            StdOut.println(testCommand.toString());
            switch(testCommand) {
                case construct:
                    unitTestRandomizedQueue(new RandomizedQueue<Integer>(), in);
                    break;
                default:
                    break;
            }
        }
        
    }
    
    private enum TestCommands {
        construct,
        isEmpty,
        size,
        enqueue,
        dequeue,
        sample,
        iterCheck
    };
    
    private static void unitTestRandomizedQueue(RandomizedQueue<Integer> randomizedQueue, In in) {
        while (!in.isEmpty()) {
            String key = in.readString();
            TestCommands testCommand = TestCommands.valueOf(key);
            StdOut.print(testCommand.toString() + " :: ");
            switch(testCommand) {
                case isEmpty:
                    StdOut.print(randomizedQueue.isEmpty() + "[" + in.readBoolean() + "]");
                    break;
                case size:
                    StdOut.print(randomizedQueue.size() + "[" + in.readInt() + "]");
                    break;
                case enqueue:
                    int enqueueItem = in.readInt();
                    randomizedQueue.enqueue(enqueueItem);
                    StdOut.print(enqueueItem);
                    break;
                case dequeue:
                    StdOut.print(randomizedQueue.dequeue());
                    break;
                case sample:
                    StdOut.print(randomizedQueue.sample());
                    break;
                case iterCheck:
                    Iterator<Integer> iter = randomizedQueue.iterator();
                    while (iter.hasNext()) {
                        StdOut.print(iter.next() + ":");
                    }
                    break;
                default:
                    break;
            }
            StdOut.println();
        }
    }
    
    // This is for trial purposes normally input should be taken from StandartInput.
    private static String standartInputTXT() {
        return  "construct enqueue 3 enqueue 4 size 2 dequeue iterCheck size 1 isEmpty false "+
                "sample sample size 1 dequeue size 0 isEmpty true iterCheck enqueue 1 enqueue 2 "+
                "dequeue enqueue 3 enqueue 4 dequeue enqueue 5 enqueue 6 iterCheck";
    }
}