public class PowerLaw {
    public static void main(String[] args) {
        int seed = 158046;
        int upper = (int)Math.pow(2, 13);
        for (int i = 2; i <= upper; i *= 2) {
            Stopwatch stopwatch = new Stopwatch();
            Timing.trial(i, seed);
            double time = stopwatch.elapsedTime();
            System.out.format("%d \t %f \n", i, time);
        }
    }
}