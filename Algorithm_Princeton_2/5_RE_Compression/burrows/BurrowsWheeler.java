import java.util.Arrays;

public class BurrowsWheeler {
    public static void encode() {
        String s = BinaryStdIn.readString();

        CircularSuffixArray cArray = new CircularSuffixArray(s);
        int i;
        for (i = 0; i < s.length(); i++) {
            if (cArray.index(i) == 0)
                break;
        }
        BinaryStdOut.write(i);

        for (int j = 0; j < s.length(); j++) {
            int k = cArray.index(j) - 1;
            if (k < 0) k += s.length();
            BinaryStdOut.write(s.charAt(k));
        }

        BinaryStdOut.close();
    }

    public static void decode() {
        int first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
        char[] chars = s.toCharArray();
        
        Arrays.sort(chars);
        int next[] = new int[chars.length];
        boolean marked[] = new boolean[chars.length];
        
        char lastChar = chars[0];
        int  lastID   = 0;
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            int j = 0;
            if (c == lastChar) j = lastID;
            else               lastChar = c;
            for (; j < s.length(); j++) {
                if (!marked[j] && s.charAt(j) == c) {
                    next[i] = j;
                    marked[j] = true;
                    lastID = j + 1;
                    break;
                }
            }
        }

        for (int k = 0; k < next.length; k++) {
            BinaryStdOut.write(chars[first]);
            first = next[first];
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
