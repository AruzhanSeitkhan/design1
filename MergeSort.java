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