# design1
MergeSort

package algo.sort;

import algo.common.Metrics;

import java.util.Arrays;

public final class MergeSort {
    private static final int INSERTION_CUTOFF = 24;
    private MergeSort() {}

    public static void sort(int[] a, Metrics m) {
        if (a == null) throw new IllegalArgumentException("array is null");
        if (m == null) m = new Metrics();
        m.startTimer();
        if (a.length > 1) {
            int[] buf = new int[a.length];
            m.addAlloc(a.length);
            sortRec(a, 0, a.length, buf, m);
        }
        m.stopTimer();
    }

    private static void sortRec(int[] a, int lo, int hi, int[] buf, Metrics m) {
        m.onEnter();
        int n = hi - lo;
        if (n <= INSERTION_CUTOFF) {
            insertionSort(a, lo, hi, m);
            m.onExit();
            return;
        }
        int mid = lo + (n >> 1);
        sortRec(a, lo, mid, buf, m);
        sortRec(a, mid, hi, buf, m);


        m.comparisons++;
        if (a[mid - 1] <= a[mid]) {
            m.onExit();
            return;
        }
        merge(a, lo, mid, hi, buf, m);
        m.onExit();
    }

    private static void insertionSort(int[] a, int lo, int hi, Metrics m) {
        for (int i = lo + 1; i < hi; i++) {
            int x = a[i];
            int j = i - 1;
            while (j >= lo) {
                m.comparisons++;
                if (a[j] <= x) break;
                a[j + 1] = a[j];
                m.swaps++; 
                j--;
            }
            a[j + 1] = x;
        }
    }

    private static void merge(int[] a, int lo, int mid, int hi, int[] buf, Metrics m) {
        int i = lo, j = mid, k = lo;
        while (i < mid && j < hi) {
            m.comparisons++;
            if (a[i] <= a[j]) buf[k++] = a[i++];
            else               buf[k++] = a[j++];
        }
        while (i < mid) buf[k++] = a[i++];
        while (j < hi)  buf[k++] = a[j++];
        System.arraycopy(buf, lo, a, lo, hi - lo);
        m.swaps += (hi - lo);
    }

    public static boolean isSorted(int[] a) {
        for (int i = 1; i < a.length; i++) if (a[i-1] > a[i]) return false;
        return true;
    }


    public static void main(String[] args) {
        int[] demo = {5,4,3,2,1,7,6,9,8,0};
        Metrics m = new Metrics();
        sort(demo, m);
        System.out.println(Arrays.toString(demo));
        System.out.println(m);
    }
}

MergeSortTest

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

QuickSort

package algo.sort;

import algo.common.Metrics;

import java.util.Arrays;

import java.util.Random;

public final class QuickSort {
    private static final Random rnd = new Random();

    private QuickSort() {}

    public static void sort(int[] a, Metrics m) {
        if (a == null) throw new IllegalArgumentException("array is null");
        if (m == null) m = new Metrics();
        m.startTimer();
        quickSortIter(a, 0, a.length, m);
        m.stopTimer();
    }

    private static void quickSortIter(int[] a, int lo, int hi, Metrics m) {
        while (hi - lo > 1) {
            m.onEnter();
            int p = partition(a, lo, hi, m);
            int leftSize = p - lo;
            int rightSize = hi - (p + 1);

            // рекурсируем в меньшую часть
            if (leftSize < rightSize) {
                if (leftSize > 1) quickSortIter(a, lo, p, m);
                lo = p + 1; // хвост -> цикл
            } else {
                if (rightSize > 1) quickSortIter(a, p + 1, hi, m);
                hi = p; // хвост -> цикл
            }
            m.onExit();
        }
    }

    private static int partition(int[] a, int lo, int hi, Metrics m) {
        int pivotIndex = lo + rnd.nextInt(hi - lo);
        int pivot = a[pivotIndex];
        swap(a, pivotIndex, hi - 1, m);
        int i = lo;
        for (int j = lo; j < hi - 1; j++) {
            m.comparisons++;
            if (a[j] <= pivot) {
                swap(a, i, j, m);
                i++;
            }
        }
        swap(a, i, hi - 1, m);
        return i;
    }

    private static void swap(int[] a, int i, int j, Metrics m) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
        m.swaps++;
    }

    public static boolean isSorted(int[] a) {
        for (int i = 1; i < a.length; i++) if (a[i-1] > a[i]) return false;
        return true;
    }

    public static void main(String[] args) {
        int[] demo = {5,4,3,2,1,7,6,9,8,0};
        Metrics m = new Metrics();
        sort(demo, m);
        System.out.println(Arrays.toString(demo));
        System.out.println(m);
    }
}

QuickSortTest

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


            }
        }
        return new int[]{lt - 1, gt + 1};
    }

    private static void insertionSort(int[] a, int lo, int hi, Metrics m) {
        for (int i = lo + 1; i < hi; i++) {
            int x = a[i];
            int j = i - 1;
            while (j >= lo) {
                m.comparisons++;
                if (a[j] <= x) break;
                a[j + 1] = a[j];
                m.swaps++;
                j--;
            }
            a[j + 1] = x;
        }
    }

    private static void swap(int[] a, int i, int j, Metrics m) {
        if (i == j) return;
        int t = a[i]; a[i] = a[j]; a[j] = t;
        if (m != null) m.swaps++;
    }


DeterministicSelect

package algo.select;

import algo.common.Metrics;

public final class DeterministicSelect {
    private static final int GROUP = 5;
    private static final int CUTOFF = 32;

    private DeterministicSelect() {}


    public static int selectInPlace(int[] a, int k, Metrics m) {
        if (a == null || a.length == 0) throw new IllegalArgumentException("empty array");
        if (k < 0 || k >= a.length) throw new IllegalArgumentException("k out of range");
        if (m == null) m = new Metrics();
        m.startTimer();
        int ans = selectRec(a, 0, a.length, k, m);
        m.stopTimer();
        return ans;
    }


    public static int select(int[] a, int k, Metrics m) {
        int[] copy = java.util.Arrays.copyOf(a, a.length);
        if (m != null) m.addAlloc(copy.length);
        return selectInPlace(copy, k, m);
    }

    private static int selectRec(int[] a, int lo, int hi, int k, Metrics m) {
        m.onEnter();
        int n = hi - lo;
        if (n <= CUTOFF) {
            insertionSort(a, lo, hi, m);
            int res = a[lo + k];
            m.onExit();
            return res;
        }


        int groups = (n + GROUP - 1) / GROUP;
        for (int g = 0; g < groups; g++) {
            int gs = lo + g * GROUP;
            int ge = Math.min(gs + GROUP, hi);
            insertionSort(a, gs, ge, m);
            int medianIdx = gs + ((ge - gs) >> 1);
            swap(a, lo + g, medianIdx, m); 
        }


        int medianOfMedians = selectRec(a, lo, lo + groups, groups / 2, m);


        int[] bounds = partition3(a, lo, hi, medianOfMedians, m);
        int ltEnd = bounds[0];
        int gtBeg = bounds[1];

        int leftSize = ltEnd - lo + 1;
        int midSize  = gtBeg - (ltEnd + 1);

        int res;
        if (k < leftSize) {
            res = selectRec(a, lo, ltEnd + 1, k, m);
        } else if (k < leftSize + midSize) {

            res = medianOfMedians;
        } else {
            res = selectRec(a, gtBeg, hi, k - leftSize - midSize, m);
        }
        m.onExit();
        return res;
    }

    private static int[] partition3(int[] a, int lo, int hi, int pivot, Metrics m) {
        int lt = lo, i = lo, gt = hi - 1;
        while (i <= gt) {
            m.comparisons++;
            if (a[i] < pivot) {
                swap(a, lt++, i++, m);
            } else {
                m.comparisons++;
                if (a[i] > pivot) {
                    swap(a, i, gt--, m);
                } else {
                    i++;
                }
            }
        }
        return new int[]{lt - 1, gt + 1};
    }

    private static void insertionSort(int[] a, int lo, int hi, Metrics m) {
        for (int i = lo + 1; i < hi; i++) {
            int x = a[i];
            int j = i - 1;
            while (j >= lo) {
                m.comparisons++;
                if (a[j] <= x) break;
                a[j + 1] = a[j];
                m.swaps++;
                j--;
            }
            a[j + 1] = x;
        }
    }

    private static void swap(int[] a, int i, int j, Metrics m) {
        if (i == j) return;
        int t = a[i]; a[i] = a[j]; a[j] = t;
        if (m != null) m.swaps++;
    }


    public static void main(String[] args) {
        int[] a = {7,1,9,3,3,5,8,2,6,4};
        Metrics m = new Metrics();
        int k = 4; 
        int v = select(a, k, m);
        System.out.println("k=" + k + " -> " + v);
        System.out.println(m);
    }
}

DeterministicSelectTest

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

ClosestPair

package algo.pair;

import algo.common.Metrics;

import java.util.Arrays;

import java.util.Comparator;

public final class ClosestPair {

    public static final class Result {
        public final Point2D a, b;
        public final double dist;
        public Result(Point2D a, Point2D b, double dist) { this.a = a; this.b = b; this.dist = dist; }
        @Override public String toString() { return a + " - " + b + " : " + dist; }
    }

    private static final int CUTOFF = 32;

    private ClosestPair() {}


    public static Result closest(Point2D[] pts, Metrics m) {
        if (pts == null || pts.length < 2) throw new IllegalArgumentException("need >= 2 points");
        if (m == null) m = new Metrics();
        m.startTimer();

        Point2D[] px = Arrays.copyOf(pts, pts.length);
        Arrays.sort(px, Comparator.comparingDouble(Point2D::x));
        Point2D[] py = Arrays.copyOf(px, px.length);
        Arrays.sort(py, Comparator.comparingDouble(Point2D::y));
        m.addAlloc(2L * pts.length);

        Result res = solve(px, py, 0, px.length, m);

        m.stopTimer();
        return res;
    }


    private static Result solve(Point2D[] px, Point2D[] py, int lo, int hi, Metrics m) {
        m.onEnter();
        int n = hi - lo;
        if (n <= CUTOFF) {
            Result r = brute(px, lo, hi, m);
            m.onExit();
            return r;
        }

        int mid = lo + n / 2;
        double midX = px[mid].x();


        Point2D[] pyl = new Point2D[n];
        Point2D[] pyr = new Point2D[n];
        int li = 0, ri = 0;
        for (int i = 0; i < py.length; i++) {
            Point2D p = py[i];

            if (p.x() < px[lo].x() || p.x() > px[hi-1].x()) continue;
            if (p.x() < midX || (p.x() == midX && indexOf(px, lo, mid, p) >= 0)) {
                pyl[li++] = p;
            } else {
                pyr[ri++] = p;
            }
        }
        m.addAlloc(li + ri);

        Result left  = solve(px, Arrays.copyOf(pyl, li), lo, mid, m);
        Result right = solve(px, Arrays.copyOf(pyr, ri), mid, hi, m);
        Result best = left.dist <= right.dist ? left : right;


        double delta = best.dist;
        Point2D[] strip = new Point2D[py.length];
        int si = 0;
        for (Point2D p : py) {
            if (p == null) continue;
            m.comparisons++;
            if (Math.abs(p.x() - midX) < delta) strip[si++] = p;
        }
        m.addAlloc(si);

        for (int i = 0; i < si; i++) {

            for (int j = i + 1; j < si && (strip[j].y() - strip[i].y()) < delta; j++) {
                double d = dist(strip[i], strip[j], m);
                if (d < best.dist) best = new Result(strip[i], strip[j], d);
            }
        }

        m.onExit();
        return best;
    }


    private static int indexOf(Point2D[] px, int lo, int hi, Point2D p) {
        for (int i = lo; i < hi; i++) if (px[i] == p) return i;
        return -1;
    }


    private static Result brute(Point2D[] px, int lo, int hi, Metrics m) {
        Result best = new Result(px[lo], px[lo+1], dist(px[lo], px[lo+1], m));
        for (int i = lo; i < hi; i++) {
            for (int j = i + 1; j < hi; j++) {
                double d = dist(px[i], px[j], m);
                if (d < best.dist) best = new Result(px[i], px[j], d);
            }
        }
        return best;
    }

    private static double dist(Point2D a, Point2D b, Metrics m) {
        m.comparisons++;
        double dx = a.x() - b.x(), dy = a.y() - b.y();
        return Math.hypot(dx, dy);
    }


    public static void main(String[] args) {
        Point2D[] pts = {
                new Point2D(0,0), new Point2D(5,5), new Point2D(1,1),
                new Point2D(9,9), new Point2D(2,2), new Point2D(8,3)
        };
        Metrics m = new Metrics();
        Result r = closest(pts, m);
        System.out.println(r);
        System.out.println(m);
    }
}


ClosestPairTest

package algo.pair;

import algo.common.Metrics;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class ClosestPairTest {

    @Test
    void tiny() {
        Point2D[] pts = {
                new Point2D(0,0),
                new Point2D(3,4),
                new Point2D(1,1)
        };
        Metrics m = new Metrics();
        ClosestPair.Result r = ClosestPair.closest(pts, m);
        assertEquals(Math.hypot(1,1), r.dist, 1e-9);
    }

    @Test
    void randomVsBruteSmall() {
        Random rnd = new Random(123);
        for (int n = 2; n <= 200; n += 7) {
            Point2D[] pts = new Point2D[n];
            for (int i = 0; i < n; i++) {
                pts[i] = new Point2D(rnd.nextDouble()*1000.0, rnd.nextDouble()*1000.0);
            }
            Metrics fastM = new Metrics();
            ClosestPair.Result fast = ClosestPair.closest(pts, fastM);

        
            double brute = Double.POSITIVE_INFINITY;
            for (int i = 0; i < n; i++) {
                for (int j = i+1; j < n; j++) {
                    double d = Math.hypot(pts[i].x()-pts[j].x(), pts[i].y()-pts[j].y());
                    if (d < brute) brute = d;
                }
            }
            assertEquals(brute, fast.dist, 1e-9, "n=" + n);
        }
    }
}

Point2D

package algo.pair;

public record Point2D(double x, double y) {
    @Override public String toString() { return "(" + x + "," + y + ")"; }
}

Main

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
 Metrics
 
 package algo.common;

public final class Metrics {
    public long comparisons;
    public long swaps;
    public long allocations;
    private int recursionDepth;
    public int maxRecursionDepth;
    private long startNanos;
    public long elapsedNanos;

    public void startTimer() { startNanos = System.nanoTime(); }
    public void stopTimer()  { elapsedNanos = System.nanoTime() - startNanos; }

    public void onEnter() {
        recursionDepth++;
        if (recursionDepth > maxRecursionDepth) maxRecursionDepth = recursionDepth;
    }
    public void onExit()  { recursionDepth--; }

    public void addAlloc(long units) { allocations += units; }

    @Override public String toString() {
        return "Metrics{comparisons=" + comparisons +
                ", swaps=" + swaps +
                ", allocations=" + allocations +
                ", maxRecursionDepth=" + maxRecursionDepth +
                ", elapsedNanos=" + elapsedNanos + "}";
    }
}

metrics.csv

algo,n,comparisons,swaps,allocations,maxDepth,elapsedNanos
mergesort,1000,10425,9700,1000,7,414700
quicksort,1000,11521,6993,0,7,949600
quicksort,5000,69541,38425,0,9,3213500
mergesort,5000,66581,62733,5000,9,1584900
select,3000,28875,20422,3000,8,929900
closest,4000,88665,0,37264,8,37630500
closest,78900,1677369,0,1257086,13,385359500
select,10000,98168,69192,10000,10,3208100
select,5000,48150,34048,5000,9,2868200
mergesort,10000,143422,135712,10000,10,2408500
mergesort,10000,143422,135712,10000,10,4174500
mergesort,10000,143422,135712,10000,10,2910900
mergesort,10000,143422,135712,10000,10,2848800
mergesort,10000,143422,135712,10000,10,2903700
mergesort,10000,143422,135712,10000,10,3192500
