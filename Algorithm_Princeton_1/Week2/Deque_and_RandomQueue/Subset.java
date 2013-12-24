public class Subset {
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);

        RandomizedQueue<String> queue = new RandomizedQueue<String>();

        while (!StdIn.isEmpty())
            queue.enqueue(StdIn.readString());

        for (int i = 0; i < N; i++)
            StdOut.println(queue.dequeue());
    }
}