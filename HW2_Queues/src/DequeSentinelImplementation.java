import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Below deque implementation add head and tail sentinel nodes to make every add or remove to be in between of elements.
 * However this makes addFirst, addLast, removeFirst, removeLast to have 2 times more assignment i.e. slower.
 * This implementation might be useful if we have had below function
 *      public void insert(Iterator<Item> iterator, Item item) 
 *      {
 *          //insert after or before iterator
 *      }
 *      
 * Complexity is the same with Deque implementation but 2 times slower.
 * Inserting items in between head or tail calls same code i.e. understandable and easy to implement.
 * 
 * @author anilk
 * 
 * @param <Item> generic typed item
 */
public class DequeSentinelImplementation<Item> implements Iterable<Item> {
    
    private class Node {
        private Item item;
        private Node next;
        private Node previous;
        
        private Node(Item item) {
            this.item = item;
        }
        
        private void unlink() {
            next = null;
            previous = null;
        }
    }
    
    class DequeIterator implements Iterator<Item> {
        private Node currentNode;
        
        private DequeIterator(Node initialNode) {
            this.currentNode = initialNode;
        }
        
        @Override
        public boolean hasNext() {
            return currentNode != tailSentinel;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Deque Iterator is finished");
            }
            Item result = currentNode.item;
            currentNode = currentNode.next;
            return result;
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException("Deque Iterator does not support remove operation");
        }
    }
    
    private int size;
    private Node headSentinel;
    private Node tailSentinel;
    
    // construct an empty deque
    public DequeSentinelImplementation() {
        headSentinel = new Node(null);
        tailSentinel = new Node(null);
        linkNodeEdge(headSentinel, tailSentinel);
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
        Node firstItem = new Node(item);
        addAfterNode(headSentinel, firstItem);
    }

    // add the item to the back
    public void addLast(Item item) {
        validateAdd(item);
        Node lastItem = new Node(item);
        addAfterNode(tailSentinel.previous, lastItem);
    }

    // remove and return the item from the front
    public Item removeFirst() {
        validateRemove();
        return removeAfterNode(headSentinel).item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        validateRemove();
        return removeAfterNode(tailSentinel.previous.previous).item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator(headSentinel.next);
        
    }
    
    private void linkNodeEdge(Node previousNode, Node nextNode) {
        previousNode.next = nextNode;
        nextNode.previous = previousNode;
    }
    
    private void addAfterNode(Node currentNode, Node addNode) {
        linkNodeEdge(addNode, currentNode.next);
        linkNodeEdge(currentNode, addNode);
        size++;
    }
    
    private Node removeAfterNode(Node currentNode) {
        if (isEmpty()) {
            return null;
        }
        Node result = currentNode.next;
        linkNodeEdge(currentNode, result.next);
        result.unlink();
        size--;
        return result;
    }
    
    private void validateRemove() {
        if (isEmpty()) {
            throw new NoSuchElementException("Remove operation on empty deque is not allowed");
        }
    }
    
    private void validateAdd(Item item) {
        if (item == null) {
            throw new NullPointerException("Null input item into deque is not supported");
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        
    }
}