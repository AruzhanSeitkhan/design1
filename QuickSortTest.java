package algo.sort;

import algo.common.Metrics;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class QuickSortTest {

    @Test
    void smallArray() {
        int[] a = {3,1,4,1,5,9};
        Metrics m = new Metrics();
        QuickSort.sort(a, m);
        assertTrue(QuickSort.isSorted(a));
    }

    @Test
    void randomArrays() {
        Random rnd = new Random(123);
        for (int t = 0; t < 30; t++) {
            int n = 1000 + rnd.nextInt(2000);
            int[] a = rnd.ints(n, -5000, 5000).toArray();
            int[] b = Arrays.copyOf(a, a.length);
            QuickSort.sort(a, new Metrics());
            Arrays.sort(b);
            assertArrayEquals(b, a, "Mismatch trial " + t);
        }
    }

    @Test
    void adversarialDescending() {
        int n = 5000;
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = n - i;
        QuickSort.sort(a, new Metrics());
        assertTrue(QuickSort.isSorted(a));
    }
}
