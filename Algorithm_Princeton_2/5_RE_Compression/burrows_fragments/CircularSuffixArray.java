public class CircularSuffixArray {
    int[] indexes;
    
    public CircularSuffixArray(String s) {
        indexes = new int[s.length()];
        for (int i = 0; i < indexes.length; i++)
            indexes[i] = i;
        sortSuffixArray(s, 0, s.length()-1, 0);
    }

    private void sortSuffixArray(String s, int lo, int hi, int d) {
        if (hi <= lo || d >= s.length()) return;
        int lt = lo, gt = hi;
        char v = charAt(s, lo, d);
        int i = lo + 1;
        while (i <= gt) {
            char t = charAt(s, i, d);
            if      (t < v) exch(lt++, i++);
            else if (t > v) exch(i, gt--);
            else            i++;
        }

        sortSuffixArray(s, lo, lt-1, d);
        if (v >= 0) sortSuffixArray(s, lt, gt, d+1);
        sortSuffixArray(s, gt+1, hi, d);
    }

    private char charAt(String s, int i, int d) {
        return s.charAt((indexes[i] + d) % s.length());
    }

    private void exch(int i, int j) {
        int temp = indexes[i];
        indexes[i] = indexes[j];
        indexes[j] = temp;
    }

    public int length() {
        return indexes.length;
    }

    public int index(int i) {
        return indexes[i];
    }

    public static void main(String[] args) {
        CircularSuffixArray array = new CircularSuffixArray("abracadabra!");
        for (int i = 0; i < array.length(); i++)
            StdOut.println(array.index(i));
    }
}
