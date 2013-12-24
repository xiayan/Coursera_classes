// import java.util.*;
// import java.lang.IllegalArgumentException;
import java.util.Iterator;

public class WordNet {
    private ST<String, Bag<Integer>> symbolTable;
    private ST<Integer, String> idTable;
    private Digraph G;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        symbolTable = new ST<String, Bag<Integer>>();
        idTable     = new ST<Integer, String>();
        int nodes = buildSymbolTable(synsets);
        G = new Digraph(nodes);
        buildGraph(hypernyms);
        if (!checkDAG()) {
            throw new IllegalArgumentException();
        }
        sap = new SAP(G);
    }

    private int buildSymbolTable(String synsets) {
        In synFile = new In(synsets);
        String line = null;
        String[] tokens = null;
        Bag<Integer> ids = null;

        int id = -1;

        while (synFile.hasNextLine()) {
            line = synFile.readLine();
            tokens = line.split(",");
            if (tokens.length < 2) continue;
            id = Integer.parseInt(tokens[0]);
            idTable.put(id, tokens[1]);
            for (String s : tokens[1].split(" ")) {
                ids = symbolTable.get(s);
                if (ids == null) {
                    ids = new Bag<Integer>();
                    ids.add(id);
                    symbolTable.put(s, ids);
                } else {
                    ids.add(id);
                }
            }
        }
        return id + 1;
    }

    private void buildGraph(String hypernyms) {
        In hyperIn = new In(hypernyms);
        String[] tokens = null;
        int s, d;

        while (hyperIn.hasNextLine()) {
            tokens = hyperIn.readLine().split(",");
            if (tokens.length < 2) continue;
            s = Integer.parseInt(tokens[0]);
            for (int i = 1; i < tokens.length; i++)
                G.addEdge(s, Integer.parseInt(tokens[i]));
        }
    }

    private boolean checkDAG() {
        // check rooted condition
        int numOfRoots = 0;
        for (int i = 0; i < G.V(); i++) {
            if (getSize(G.adj(i)) == 0)
                numOfRoots++;
            if (numOfRoots > 1) {
                StdOut.println("not rooted!");
                return false;
            }
        }

        DirectedCycle cycle = new DirectedCycle(G);
        if (cycle.hasCycle()) {
            StdOut.println("not DAG!");
            for (int v : cycle.cycle()) {
                StdOut.print(v + " ");
            }
            StdOut.println();
            return false;
        }
        
        return true;
    }

    private int getSize(Iterable<Integer> iterable) {
        int counter = 0;
        Iterator<Integer> iterator = iterable.iterator();

        while (iterator.hasNext()) {
            iterator.next();
            counter++;
        }
        return counter;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return symbolTable.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return symbolTable.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (isNoun(nounA) && isNoun(nounB)) {
            Bag<Integer> idA = symbolTable.get(nounA);
            Bag<Integer> idB = symbolTable.get(nounB);
            return sap.length(idA, idB);
        } else {
            throw new IllegalArgumentException();
        }
    }

    // a synset (second field of synsets.txt) that is 
    // the common ancestor of nounA and nounB in a 
    // shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (isNoun(nounA) && isNoun(nounB)) {
            Bag<Integer> idA = symbolTable.get(nounA);
            Bag<Integer> idB = symbolTable.get(nounB);
            int aID = sap.ancestor(idA, idB);
            StdOut.println(aID);
            return idTable.get(aID);
        } else { 
            throw new IllegalArgumentException();
        }
    }

    // for unit testing of this class
    public static void main(String[] args) {
        String synsets = args[0];
        String hypernyms = args[1];
        String word1 = args[2];
        String word2 = args[3];
        
        WordNet wn = new WordNet(synsets, hypernyms);
        
        StdOut.println(wn.distance(word1, word2));
        StdOut.println(wn.sap(word1, word2));
    }
}
