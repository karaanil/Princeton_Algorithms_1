import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    
    private class Node {
        private final Item item;
        private Node next;
        private Node previous;
        
        private Node(Item item) {
            this.item = item;
        }
        
        private void linkNext(Node nextNode) {
            if (nextNode == null) {
                throw new NullPointerException("Item " + item.toString() + " tried to link null next");
            }
            next = nextNode;
            nextNode.previous = this;
        }
        private void unlink() {
            next = null;
            previous = null;
        }
    }
    
    private class DequeIterator implements Iterator<Item> {
        private Node currentNode;
        
        private DequeIterator() {
            currentNode = first;
        }
        
        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("DequeIterator next element does not exists");
            }
            Item result = currentNode.item;
            currentNode = currentNode.next;
            return result;
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException("DequeIterator does not support remove operation");
        }
    }
    
    private int size;
    private Node first;
    private Node last;
    
    // construct an empty deque
    public Deque() {
    }

    // is the deque empty?
    public boolean isEmpty() {
        return (size() == 0);
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        validateAdd(item);
        Node oldFirst = first;
        first = new Node(item);
        
        if (isEmpty()) {
            last = first;
        }
        else {
            first.linkNext(oldFirst);
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        validateAdd(item);
        Node oldLast = last;
        last = new Node(item);
        
        if (isEmpty()) {
            first = last;
        }
        else {
            oldLast.linkNext(last);
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        validateRemove();
        size--;
        Node oldFirst = first;
        
        if (isEmpty()) {
           first = null;
           last = null;
        }
        else {
            first = first.next;
            first.previous = null;
        }
        oldFirst.unlink();
        return oldFirst.item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        validateRemove();
        size--;
        Node oldLast = last;
        
        if (isEmpty()) {
            first = null;
            last = null;
        }
        else {
            last = last.previous;
            last.next = null;
        }
        oldLast.unlink();
        return oldLast.item;
    }

    // return an iterator over items in order from front to back
    @Override
    public Iterator<Item> iterator() {
        return new DequeIterator();
        
    }
    
    private void validateRemove() {
        if (isEmpty()) {
            throw new NoSuchElementException("Remove operation on empty deque is not allowed");
        }
    }
    
    private void validateAdd(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Null input item into deque is not supported");
        }
    }
    
//-----------------------------End Of Deque Implementation------------------------------------
    
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
                    unitTestDeque(new Deque<Integer>(), in);
                    break;
                default:
                    break;
            }
        }
        StdOut.println("Ends");
    }
    
    private enum TestCommands {
        construct,
        isEmpty,
        size,
        addFirst,
        addLast,
        removeFirst,
        removeLast,
        iterCheck
    };
        
    private static void unitTestDeque(Deque<Integer> deque, In in) {
        while (!in.isEmpty()) {
            String key = in.readString();
            TestCommands testCommand = TestCommands.valueOf(key);
            StdOut.print(testCommand.toString() + " :: ");
            switch(testCommand) {
                case addFirst:
                    int firstItem = in.readInt();
                    deque.addFirst(firstItem);
                    StdOut.print(firstItem);
                    break;
                case addLast:
                    int lastItem = in.readInt();
                    deque.addLast(lastItem);
                    StdOut.print(lastItem);
                    break;
                case isEmpty:
                    StdOut.print(deque.isEmpty() + "[" + in.readBoolean() + "]");
                    break;
                case iterCheck:
                    String str = in.readString();
                    String[] expectedList = str.split(":");
                    Iterator<Integer> iter = deque.iterator();
                    int counter = 0;
                    while (iter.hasNext() && counter < deque.size()) {
                        int nextItem  = iter.next();
                        int expected = Integer.parseInt(expectedList[counter++]);
                        if (nextItem != expected) {
                            StdOut.print(" !");
                        }
                        else {
                            StdOut.print(" ");
                        }
                        StdOut.print(nextItem + "[" + expected + "]");
                    }
                    break;
                case removeFirst:
                    deque.removeFirst();
                    break;
                case removeLast:
                    deque.removeLast();
                    break;
                case size:
                    StdOut.print(deque.size() + "[" + in.readInt() + "]");
                    break;
                default:
                    break;
            }
            StdOut.println();
        }
    }
    
    // This is for trial purposes normally input should be taken from StandartInput.
    private static String standartInputTXT() {
        return "construct addFirst 3 addLast 4 size 2 removeFirst size 1 isEmpty false " +
               "removeLast size 0 isEmpty true addFirst 7 addFirst 8 addFirst 9 addFirst 10 " +
               "addLast 3 addLast 5 addFirst 2 iterCheck 2:10:9:8:7:3:5 size 7 isEmpty false " +
               "removeLast removeLast removeFirst iterCheck 10:9:8:7 addLast 4 addFirst 1 addLast 5 "+
               "addFirst 0 size 8 isEmpty false iterCheck 0:1:10:9:8:7:4:5"; 
    }
    

}
