import java.util.Arrays;

public class BurrowsWheeler {
    private static final int step = 1024;

    public static void encode() {
        String s = BinaryStdIn.readString();

        int start = 0;
        while (start <= s.length()) {
            int end = start + step;
            if (end >= s.length())
                end = s.length();
            String sub = s.substring(start, end);
            CircularSuffixArray cArray = new CircularSuffixArray(sub);
            int i;
            for (i = 0; i < sub.length(); i++) {
                if (cArray.index(i) == 0)
                    break;
            }

            BinaryStdOut.write(i);

            for (int j = 0; j < sub.length(); j++) {
                int k = cArray.index(j) - 1;
                if (k < 0) k += sub.length();
                BinaryStdOut.write(sub.charAt(k));
            }

            start += step;
        }

        BinaryStdOut.close();
    }

    public static void decode() {
        while (!BinaryStdIn.isEmpty()) {
            int first = BinaryStdIn.readInt();
            StringBuilder sb = new StringBuilder();
            for (int m = 0; m < step && !BinaryStdIn.isEmpty(); m++)
                sb.append(BinaryStdIn.readChar());

            String sub = sb.toString();
            char[] chars = sub.toCharArray();
        
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
                for (; j < sub.length(); j++) {
                    if (sub.charAt(j) == c && !marked[j]) {
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
        }

        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
