import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a = (Item[]) new Object[1];
    private int size = 0;

    public RandomizedQueue() { }

    public boolean isEmpty() { return size == 0; }

    public int size() { return size; }

    private void resize(int max) {
        Item[] temp = (Item[]) new Object[max];
        for (int i = 0; i < size; i++) {
            temp[i] = a[i];
        }
        a = temp;
    }

    private void exch(int i, int j) {
        Item temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public void enqueue(Item item) {
        if (item == null) throw new
            java.lang.NullPointerException("Null Item");

        if (size == a.length) resize(2*a.length);
        a[size++] = item;
    }

    public Item dequeue() {
        if (size == 0) throw new
            java.util.NoSuchElementException("Empty Queue");

        int random = StdRandom.uniform(size);
        if (random != size - 1) exch(random, size-1);

        Item item = a[--size];
        a[size] = null; // avoid loitering
        if (size > 0 && size == a.length / 4) resize(a.length/2);
        return item;
    }

    public Item sample() {
        if (size == 0) throw new
            java.util.NoSuchElementException("Empty Queue");

        int random = StdRandom.uniform(size);
        return a[random];
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator(a, size);
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int i;
        private Item[] b;

        public RandomizedQueueIterator(Item[] a, int size) {
            i = size;
            b = (Item[]) new Object[i];
            for (int j = 0; j < i; j++)
                b[j] = a[j];

            StdRandom.shuffle(b);
        }

        public boolean hasNext() { return i > 0; }

        public Item next() {
            if (!hasNext())
                throw new java.util.NoSuchElementException("Deque is empty\n");
            return b[--i]; }

        public void remove() {
            throw new
            java.lang.UnsupportedOperationException("Not Supported");
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> test = new RandomizedQueue<Integer>();
        for (int i = 0; i < 10; i++)
            test.enqueue(i);

        for (int i : test)
            StdOut.print(i);

        StdOut.println();

        for (int i = 0; i < 10; i++)
            StdOut.print(test.sample());

        StdOut.println();

        for (int i = 0; i < 10; i++)
            StdOut.print(test.dequeue());

        StdOut.println();
    }
}