import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private Node first = null;
    private Node last = null;
    private int size = 0;

    private class Node {
        private Item item;
        private Node previous;
        private Node next;

        public Node(Item newItem) { item = newItem; }
    }

    public Deque() { };

    public boolean isEmpty() { return first == null; }

    public int size() { return size; }

    public void addFirst(Item item) {
        if (item == null) throw new
                java.lang.NullPointerException("Null Item\n");

        Node oldfirst = first;
        first = new Node(item);

        first.previous = null;
        first.next = oldfirst;
        if (oldfirst == null) last = first;
        else oldfirst.previous = first;
        size++;
    }

    public void addLast(Item item) {
        if (item == null) throw new
                java.lang.NullPointerException("Null Item\n");

        Node oldlast = last;
        last = new Node(item);
        last.next = null;
        last.previous = oldlast;
        if (oldlast == null) first = last;
        else oldlast.next = last;

        size++;
    }

    public Item removeFirst() {
        if (isEmpty()) throw new
            java.util.NoSuchElementException("Deque is empty\n");

        Item item = first.item;
        Node oldfirst = first;
        first = first.next;
        if (first != null) first.previous = null;
        else last = first;
        oldfirst.next = null;
        size--;
        return item;
    }

    public Item removeLast() {
        if (isEmpty()) throw new
            java.util.NoSuchElementException("Deque is empty\n");

        Item item = last.item;
        Node oldlast = last;
        last = last.previous;
        if (last != null) last.next = null;
        else first = last;
        oldlast.previous = null;
        size--;
        return item;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() { return current != null; }

        public Item next() {
            if (!hasNext())
                throw new java.util.NoSuchElementException("Deque is empty\n");

            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new
            java.lang.UnsupportedOperationException("remmove() is not supported\n");
        }
    }

    public static void main(String[] args) {
        Deque<String> deck = new Deque<String>();
        for (int i = 1; i <= 9; i += 2)
            deck.addFirst(String.valueOf('A'));

        for (String i : deck)
            StdOut.print(i);

        StdOut.println('\n');

        for (int i = 2; i <= 10; i += 2)
            deck.addLast(String.valueOf('B'));

        for (String i : deck) {
            StdOut.println(i);
            for (String j : deck) {
                StdOut.print(j);
            }
            StdOut.println();
        }

        StdOut.println('\n');

        String result = null;
        for (int test = 10; test >= 1; test--) {
            if (test % 2 == 0)  result = deck.removeLast();
            else result = deck.removeFirst();
            StdOut.print(result);
        }

        StdOut.println();
    }
}