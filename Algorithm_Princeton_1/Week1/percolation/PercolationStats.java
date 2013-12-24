public class PercolationStats
{
    private double[] results;

    public PercolationStats(int N, int T) {
        if (N < 1 || T < 1) {
            throw new
                IllegalArgumentException("N or T");
        }
        results = new double[T];

        for (int i = 0; i < T; i++) {
            Percolation sample = new Percolation(N);
            while (!sample.percolates()) {
                int m = StdRandom.uniform(1, N+1);
                int n = StdRandom.uniform(1, N+1);
                sample.open(m, n);
            }

            int counter = 0;
            for (int k = 1; k <= N; k++) {
                for (int j = 1; j <= N; j++) {
                    if (sample.isOpen(k, j)) { counter += 1; }
                }
            }
            results[i] = (counter + 0.0) / (N * N);
        }
    }

    public double mean() {
        double sum = 0.0;
        for (int i = 0; i < results.length; i++) {
            sum += results[i];
        }

        return sum/(results.length + 0.0);
    }

    public double stddev() {
        double sum = 0.0;
        double mean = this.mean();
        for (int i = 0; i < results.length; i++) {
            sum += (results[i] - mean) * (results[i] - mean);
        }
        return Math.sqrt(sum/(results.length-1));
    }

    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);

        PercolationStats tests = new PercolationStats(N, T);
        double mean = tests.mean();
        double stddev = tests.stddev();
        double ciUpper = mean + 1.96 * stddev / Math.sqrt(T);
        double ciLower = mean - 1.96 * stddev / Math.sqrt(T);

        System.out.printf("%s = %f\n", "mean", tests.mean());
        System.out.printf("%s = %f\n", "stddev", tests.stddev());
        System.out.printf("%s = %f, %f\n", "95% confidence interval",
                          ciUpper, ciLower);
    }
}