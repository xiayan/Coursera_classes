public class Outcast {
    private WordNet wn;

    public Outcast(WordNet wordnet) {
        wn = wordnet;
    }

    public String outcast(String[] nouns) {
        Queue<String> rotation = new Queue<String>();
        for (int i = 1; i < nouns.length; i++)
            rotation.enqueue(nouns[i]);

        String first = nouns[0];
        String current = first;
        String worst = first;
        int sum = -1;

        do {
            int currentSum = 0;
            for (String x : rotation) {
                currentSum += wn.distance(current, x);
            }
            
            if (currentSum > sum) {
                sum = currentSum;
                worst = current;
            }

            rotation.enqueue(current);
            current = rotation.dequeue();
        } while (!current.equals(first));
        
        return worst;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);

        for (int t = 2; t < args.length; t++) {
            String[] nouns = In.readStrings(args[t]);
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}

