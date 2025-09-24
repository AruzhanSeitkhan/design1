package algo.main;

import algo.common.Metrics;
import algo.sort.MergeSort;
import algo.sort.QuickSort;
import algo.select.DeterministicSelect;
import algo.pair.ClosestPair;
import algo.pair.Point2D;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Usage: java -jar dac.jar algo n");
            System.out.println("algo = mergesort | quicksort | select | closest");
            return;
        }
        String algo = args[0];
        int n = Integer.parseInt(args[1]);

        Metrics m = new Metrics();
        Random rnd = new Random(42);

        switch (algo) {
            case "mergesort" -> {
                int[] a = rnd.ints(n, -1_000_000, 1_000_000).toArray();
                MergeSort.sort(a, m);
            }
            case "quicksort" -> {
                int[] a = rnd.ints(n, -1_000_000, 1_000_000).toArray();
                QuickSort.sort(a, m);
            }
            case "select" -> {
                int[] a = rnd.ints(n, -1_000_000, 1_000_000).toArray();
                int k = n/2;
                DeterministicSelect.select(a, k, m);
            }
            case "closest" -> {
                Point2D[] pts = new Point2D[n];
                for (int i = 0; i < n; i++) {
                    pts[i] = new Point2D(rnd.nextDouble()*1000, rnd.nextDouble()*1000);
                }
                ClosestPair.closest(pts, m);
            }
            default -> throw new IllegalArgumentException("Unknown algo: " + algo);
        }


        System.out.println("Algo: " + algo + ", n=" + n);
        System.out.println(m);


        try (FileWriter fw = new FileWriter("metrics.csv", true)) {
            fw.write(algo + "," + n + "," + m.comparisons + "," + m.swaps + "," +
                    m.allocations + "," + m.maxRecursionDepth + "," + m.elapsedNanos + "\n");
        }
    }
}
