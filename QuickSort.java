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