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
