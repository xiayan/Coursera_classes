import java.util.Arrays;

public class FastOld {
    public static void main(String[] args) {
        Stopwatch a = new Stopwatch();
        In input = new In(args[0]);
        int number = Integer.parseInt(input.readLine().trim());
        int i1 = 0;
        int i2 = 0;

        Point[] points = new Point[number];
        Point[] ref = new Point[number];
        String[] coordinates;

        // rescale coordinates and turn on animation mode
        StdDraw.setXscale(0, 10);
        StdDraw.setYscale(0, 10);
        StdDraw.setPenRadius(0.001);
        StdDraw.show(0);

        String content;

        while (!input.isEmpty()) {
            content = input.readLine().trim();
            if (content.contentEquals("")) continue;
            coordinates = content.split("\\s+");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            Point p = new Point(x, y);
            points[i1++] = p;
            ref[i2++] = p;
            p.draw();
        }
        StdDraw.show(0);

        if (number < 4) return;

        StdDraw.setPenRadius(0.0025);
        StdDraw.setPenColor(StdDraw.RED);

        double[] slopes = new double[number];
        SET<String> pairs = new SET<String>();

        Arrays.sort(ref);

        for (int i = 0; i < number; i++) {
            Point origin = ref[i];
            Arrays.sort(points, origin.SLOPE_ORDER);
            for (int j = 0; j < number; j++)
                slopes[j] = origin.slopeTo(points[j]);

            double slope = slopes[1];
            int lo = 1;
            int high = 1;
            int total = 1;
            for (int k = 2; k < number; k++) {
                if (slopes[k] == slope) {
                    high++;
                    total++;
                } else {
                    if (total >= 3) {
                        Point[] sequence = new Point[total + 1];
                        sequence[0] = origin;
                        int rangeLo = lo;
                        for (int m = 1; m <= total; m++)
                            sequence[m] = points[rangeLo++];
                        Arrays.sort(sequence);
                        String sig = sequence[0].toString();
                        for (int m = 1; m <= total; m++)
                            sig += (" -> " + sequence[m]);
                        sig += "\n";

                        if (!pairs.contains(sig)) {
                            StdOut.printf(sig);
                            pairs.add(sig);
                            sequence[0].drawTo(sequence[total]);
                        }
                    }
                    slope = slopes[k];
                    lo = k;
                    high = k;
                    total = 1;
                }
            }
            // Handel the last one
            if (total >= 3 && high == number - 1) {
                Point[] sequence = new Point[total + 1];
                sequence[0] = origin;
                int rangeLo = lo;
                for (int m = 1; m <= total; m++)
                    sequence[m] = points[rangeLo++];
                Arrays.sort(sequence);
                String sig = sequence[0].toString();
                for (int m = 1; m <= total; m++)
                    sig += (" -> " + sequence[m]);
                sig += "\n";

                if (!pairs.contains(sig)) {
                    StdOut.printf(sig);
                    pairs.add(sig);
                    sequence[0].drawTo(sequence[total]);
                }
            }
        }
        StdDraw.show(0);
        StdOut.println(a.elapsedTime());
    }
}