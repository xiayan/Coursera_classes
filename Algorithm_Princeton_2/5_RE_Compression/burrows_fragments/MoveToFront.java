public class MoveToFront {
    private static final int R = 256;

    public static void encode() {
        char[] seq = new char[R];
        // initialize seq array
        for (int i = 0; i < R; i++)
            seq[i] = (char)i;

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            char t = seq[0];
            char temp;
            int i;
            for (i = 0; i < R - 1; i++) {
                if (t == c) break;
                temp = t;
                t = seq[i+1];
                seq[i+1] = temp;
            }
            seq[0] = t;
            BinaryStdOut.write((char)i);
        }
        BinaryStdOut.close();
    }

    public static void decode() {
        char[] seq = new char[R];
        for (int i = 0; i < R; i++)
            seq[i] = (char)i;
        
        while (!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readChar();
            char c    = seq[index];
            BinaryStdOut.write(seq[index]);

            for (int i = index; i > 0; i--)
                seq[i] = seq[i-1];
            seq[0] = c;
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
