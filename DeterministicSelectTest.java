package algo.select;

import algo.common.Metrics;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DeterministicSelectTest {

    @Test
    void tinyArrays() {
        int[] a = {5};
        assertEquals(5, DeterministicSelect.select(a, 0, new Metrics()));

        int[] b = {2,1};
        assertEquals(1, DeterministicSelect.select(b, 0, new Metrics()));
        assertEquals(2, DeterministicSelect.select(b, 1, new Metrics()));
    }

    @Test
    void randomTrials() {
        Random rnd = new Random(42);
        for (int t = 0; t < 100; t++) {
            int n = 50 + rnd.nextInt(450);
            int[] a = rnd.ints(n, -10000, 10000).toArray();
            int[] b = Arrays.copyOf(a, a.length);
            Arrays.sort(b);
            int k = rnd.nextInt(n);
            int got = DeterministicSelect.select(a, k, new Metrics());
            assertEquals(b[k], got, "trial " + t);
        }
    }

    @Test
    void withDuplicates() {
        int[] a = {4,4,4,4,4,4,4,3,5,4,4,4,6,2,4};
        int[] b = Arrays.copyOf(a, a.length);
        Arrays.sort(b);
        for (int k = 0; k < a.length; k++) {
            assertEquals(b[k], DeterministicSelect.select(a, k, new Metrics()));
        }
    }

    @Test
    void monotoneAdversarial() {
        int n = 5000;
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = i;
        int mid = n / 2;
        int val = DeterministicSelect.select(a, mid, new Metrics());
        assertEquals(mid, val);
    }
}
