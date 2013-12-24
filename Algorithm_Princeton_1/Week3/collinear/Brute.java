import java.util.Arrays;

public class Brute {
    public static void main(String[] args) {
        In input = new In(args[0]);
        int number = Integer.parseInt(input.readLine().trim());

        Point[] points = new Point[number];
        int index = 0;
        String[] coordinates;

        // rescale coordinates and turn on animation mode
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.setPenRadius(0.02);
        StdDraw.show(0);

        while (!input.isEmpty()) {
            String content = input.readLine().trim();
            if (content.contentEquals("")) continue;
            coordinates = content.split("\\s+");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            Point p = new Point(x, y);
            p.draw();
            points[index++] = p;
            StdDraw.show(0);
        }

        if (number < 4) return;

        Arrays.sort(points);

        StdDraw.setPenRadius(0.005);
        StdDraw.setPenColor(StdDraw.RED);

        for (int p = 0; p < number; p++)
            for (int q = p + 1; q < number; q++)
                for (int r = q + 1; r < number; r++)
                    for (int s = r + 1; s < number; s++) {
                        double pq = points[p].slopeTo(points[q]);
                        double pr = points[p].slopeTo(points[r]);
                        double ps = points[p].slopeTo(points[s]);
                        if (pq == pr && pr == ps) {
                            points[p].drawTo(points[s]);
                            StdDraw.show(0);
                            StdOut.println(points[p]
                                           + " -> " + points[q] + " -> "
                                           + points[r] + " -> " + points[s]);
                        }
                    }
    }
}