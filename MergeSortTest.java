package algo.sort;

import algo.common.Metrics;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class MergeSortTest {

    @Test
    void smallSorted() {
        int[] a = {1,2,3,4,5};
        Metrics m = new Metrics();
        MergeSort.sort(a, m);
        assertTrue(MergeSort.isSorted(a));
        assertTrue(m.maxRecursionDepth >= 0);
    }

    @Test
    void randomArrays() {
        Random rnd = new Random(123);
        for (int t = 0; t < 30; t++) {
            int n = 800 + rnd.nextInt(1200);
            int[] a = rnd.ints(n, -1_000_000, 1_000_000).toArray();
            int[] b = Arrays.copyOf(a, a.length);
            Metrics m = new Metrics();
            MergeSort.sort(a, m);
            Arrays.sort(b);
            assertArrayEquals(b, a, "mismatch on trial " + t);
            int bound = 1 + (int)Math.ceil(Math.log(n) / Math.log(2)) + 4;
            assertTrue(m.maxRecursionDepth <= bound, "depth too large: " + m.maxRecursionDepth + " > " + bound);
        }
    }

    @Test
    void adversarialDescending() {
        int n = 3000;
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = n - i;
        Metrics m = new Metrics();
        MergeSort.sort(a, m);
        assertTrue(MergeSort.isSorted(a));
    }
}