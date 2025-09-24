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

