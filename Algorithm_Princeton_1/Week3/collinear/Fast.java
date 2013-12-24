import java.util.Arrays;
import java.util.Hashtable;

public class Fast {
    private static void addToCheckAndDraw(Point[] points,
                                          Hashtable<String, SET<Double>> check,
                                          Point origin, double oldSlope,
                                          int oldLo, int oldHi) {

        String originName = origin.toString();
        if (check.containsKey(originName)
            && check.get(originName).contains(oldSlope))
            return;

        else if (!check.containsKey(originName)) {
            SET<Double> newSet = new SET<Double>();
            newSet.add(oldSlope);
            check.put(originName, newSet);
        }
        else {
            SET<Double> preSet = check.get(originName);
            preSet.add(oldSlope);
        }

        Arrays.sort(points, oldLo, oldHi + 1);
        origin.drawTo(points[oldHi]);
        String result = origin.toString();
        for (int m = oldLo; m <= oldHi; m++) {
            Point middle = points[m];
            String middleName = middle.toString();

            if (!check.containsKey(middleName)) {
                SET<Double> newSet1 = new SET<Double>();
                newSet1.add(oldSlope);
                check.put(middleName, newSet1);
            } else {
                SET<Double> preSet1 = check.get(middleName);
                preSet1.add(oldSlope);
            }
            result += (" -> " + middle);
        }
        StdOut.println(result);
    }

    public static void main(String[] args) {
        In input = new In(args[0]);
        int number = Integer.parseInt(input.readLine().trim());
        int i1 = 0;
        int i2 = 0;

        Point[] points = new Point[number];
        Point[] ref = new Point[number];
        String[] coordinates;

        // rescale coordinates and turn on animation mode
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.setPenRadius(0.008);
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

        Hashtable<String, SET<Double>> check =
        new Hashtable<String, SET<Double>>();

        Arrays.sort(ref);

        for (int i = 0; i < number; i++) {
            Point origin = ref[i];
            Arrays.sort(points, origin.SLOPE_ORDER);

            int index = 2;
            int lo = 1;
            int high = 1;
            int total = 1;
            double slope = origin.slopeTo(points[1]);

            do {
                double tempSlope = origin.slopeTo(points[index++]);
                if (tempSlope == slope) {
                    high++;
                    total++;
                    if (index == number && total >= 3)
                        addToCheckAndDraw(points, check, origin, slope, lo, high);

                } else {
                    int oldLo = lo;
                    int oldHi = high;
                    int oldTol = total;
                    double oldSlope = slope;
                    lo = index - 1;
                    high = index - 1;
                    slope = tempSlope;
                    total = 1;

                    if (oldTol < 3) continue;
                    else
                        addToCheckAndDraw(points, check,
                                          origin, oldSlope, oldLo, oldHi);
                }
            } while (index < number);
        }
        StdDraw.show(0);
    }
}